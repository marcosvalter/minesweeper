/*
 * This module is part of a small programming project just for fun
 * You may freely use and modify this code, NO WARRANTY, blah blah blah,
 * as long as you give proper credit to the original and subsequent authors.
 *
 * Date:        Author:
 * 2013-06-27   Marcos Valter Vidal Russo Alegre <m.valter@ua.pt>
 *

 */
package minesweeper;

/**
 * A simple minesweeper engine.
 * <p>
 * This game works on a state machine represented by a matrix MxN of cells, each
 * one with a nMines and status:
 * <p>
 * nMines:
 * <p>
 * -1 -> mine
 * <p>
 * 0-8 -> mines arround the cell
 * <p>
 * Status:
 * <p>
 * 0 -> hidden cell
 * <p>
 * 1 -> open cell
 * <p>
 * 2 -> flag
 * <p>
 * 3 -> incorrect flag
 * <p>
 * 4 -> correct flag
 * <p>
 * 5 -> explosion
 * <p>
 * The field is started with all cells nMines and status setted to zero and is
 * generated on the first play to avoid death on the first try. Then all you
 * need is play(x_pos, y_pos, flag) and get the cell and game status.
 * 
 * @author Marcos Valter <m.valter@ua.pt>
 */
public class Mines {

    private final int colNum;
    private final int lineNum;
    private final int minesNum;
    private int usedFlags = 0;
    private int cellsRem = 0;
    private final Cell[][] matrix;
    private boolean gameStarted = false;
    private boolean gameEnd = false;

    /**
     * default constructor
     *
     * @param colNum int for matrix width
     * @param lineNum int for matrix height
     * @param minesNum int for mines amount
     * @see Cell
     */
    public Mines(int colNum, int lineNum, int minesNum) {
        assert colNum > 0;
        assert lineNum > 0;
        assert minesNum >= 0 & minesNum < colNum * lineNum;

        this.colNum = colNum;
        this.lineNum = lineNum;
        this.minesNum = minesNum;
        matrix = new Cell[colNum + 2][lineNum + 2];
        cellsRem = lineNum * colNum - minesNum;
        fillField();
    }

    // Populates the matrix with cells(status=0; nMines=0)
    private void fillField() {
        for (int i = 0; i < colNum + 2; i++) {
            for (int j = 0; j < lineNum + 2; j++) {
                matrix[i][j] = new Cell();
            }
        }
    }

    // Populates the matrix's cells with values. This is a one time event
    // the cell identified by exp_x and exp_y stays with no mine
    private void genField(int exp_x, int exp_y) {
        assert exp_x >= 0 & exp_x < lineNum : "exp_x must be [0, lineNum[\n";
        assert exp_y >= 0 & exp_y < lineNum : "exp_y must be [0, colNum[\n";

        int temp_x, temp_y;
        int count = 0;

        // sets the exception cell to -1(mine)
        matrix[exp_x][exp_y].setnMines(-1);

        // random mines populating
        while (count < minesNum) {
            temp_x = (int) ((Math.random() * (colNum)) + 1);
            temp_y = (int) ((Math.random() * (lineNum)) + 1);
            if (matrix[temp_x][temp_y].getnMines() != -1) {
                matrix[temp_x][temp_y].setnMines(-1);
                count++;
            }
        }

        // sets the exception cell nMines to zero again, calculates
        // the other cells and set the gameStarted to true.
        matrix[exp_x][exp_y].setnMines(0);
        calcField();
        gameStarted = true;
    }

    // calculates how many mines are arround each cell
    private void calcField() {

        for (int i = 1; i <= colNum; i++) {
            for (int j = 1; j <= lineNum; j++) {
                if (matrix[i][j].getnMines() != -1) {
                    int count = 0;
                    for (int l = -1; l < 2; l++) {
                        for (int m = -1; m < 2; m++) {
                            if (matrix[i + l][j + m].getnMines() == -1) {
                                count++;
                            }
                        }
                    }
                    matrix[i][j].setnMines(count);
                }
            }
        }
    }

    /**
     * Opens or flag/unflag a cell
     * 
     * @param xPos int for x positon of cell
     * @param yPos int for y position of cell
     * @param flag boolean for flag
     */
    public void play(int xPos, int yPos, boolean flag) {
        assert xPos >= 0 && xPos < lineNum;
        assert yPos >= 0 && yPos < colNum;

        xPos++;
        yPos++;

        if (!gameStarted) {
            genField(xPos, yPos);
        }

        if (flag) {
            if (matrix[xPos][yPos].getStatus() == 2) {
                matrix[xPos][yPos].setStatus(0);
                usedFlags--;
            } else if (matrix[xPos][yPos].getStatus() == 0) {
                matrix[xPos][yPos].setStatus(2);
                usedFlags++;
            }
        } else {
            if (matrix[xPos][yPos].getnMines() == -1 && matrix[xPos][yPos].getStatus() != 2) {
                matrix[xPos][yPos].setStatus(5);
                gameEnd = true;
                showField();
            } else if (matrix[xPos][yPos].getStatus() != 2) {
                openCell(xPos, yPos);
                if (cellsRem == 0) {
                    gameEnd = true;
                    showField();
                }
            }
        }
    }

    // used when game is ended.
    // also show correct/incorrect flags and the explosion cell if is case
    private void showField() {
        for (int i = 0; i < colNum + 2; i++) {
            for (int j = 0; j < lineNum + 2; j++) {
                if (matrix[i][j].getnMines() == -1 && matrix[i][j].getStatus() == 2) {
                    matrix[i][j].setStatus(4);  // correct flag
                } else if (matrix[i][j].getnMines() != -1 && matrix[i][j].getStatus() == 2) {
                    matrix[i][j].setStatus(3);  // incorrect flag
                } else if (matrix[i][j].getStatus() != 5) {
                    matrix[i][j].setStatus(1);  // open cell
                }
            }
        }
    }

    // recursive method to open all the free space.
    // rule: if the nMines is equal to zero, then all cells arround are openned.
    private void openCell(int xPos, int yPos) {
        if (matrix[xPos][yPos].getStatus() == 0) {
            matrix[xPos][yPos].setStatus(1);
            cellsRem--;
        }
        if (matrix[xPos][yPos].getnMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (matrix[xPos + i][yPos + j].getnMines() != -1 && xPos + i > 0
                            && xPos + i <= colNum && yPos + j > 0 && yPos + j <= lineNum
                            && xPos != 0 && yPos != 0 && matrix[xPos + i][yPos + j].getStatus() == 0) {
                        openCell(xPos + i, yPos + j);
                    }

                }
            }
        }
    }

    /**
     * returns the nMines value of a specified cell
     * @param xPos int for cell x position
     * @param yPos int for cell y position
     * @return int for cell nMines value
     */
    public int getMatrixCellnMines(int xPos, int yPos) {
        assert xPos >= 0 && yPos >= 0;

        return matrix[++xPos][++yPos].getnMines();
    }

    /**
     * returns the status value of a specified cell
     * @param xPos int for cell x position
     * @param yPos int for cell y position
     * @return int for cell status value
     */
    public int getMatrixCellStatus(int xPos, int yPos) {
        assert xPos >= 0 && yPos >= 0;

        return matrix[++xPos][++yPos].getStatus();
    }

    /**
     * returns the number of mines in the field
     * @return int for number of mines in the field
     */
    public int getMinesNum() {
        return minesNum;
    }

    /**
     * returns the number of used flags
     * @return int for number of used flags
     */
    public int getUsedFlags() {
        return usedFlags;
    }

    /**
     * returns the state of the game
     * @return true if is ended, false if not
     */
    public boolean isGameEnd() {
        return gameEnd;
    }

    /**
     * returns the number of columns 
     * @return int for number of columns
     */
    public int getColNum() {
        return colNum;
    }

    /**
     * returns the number of lines
     * @return int for number of lines
     */
    public int getLineNum() {
        return lineNum;
    }
}
