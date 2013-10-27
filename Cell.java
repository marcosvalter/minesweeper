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
 * 
 * @author m.valter@ua.pt
 */
public class Cell {

    private int nMines;  /* possible values:
     -1 -> mine
     0 -> zero mines arround the cell
     1 -> one mines arround the cell
     2 -> two mines arround the cell
     3 -> three mines arround the cell
     4 -> four mines arround the cell
     5 -> five mines arround the cell
     6 -> six mines arround the cell
     7 -> seven mines arround the cell
     8 -> eigth mines arround the cell
     */


    private int status; /* possible values
     0 -> hidden cell
     1 -> open cell
     2 -> flag
     3 -> incorrect flag
     4 -> correct flag
     5 -> explosion
     */


    /**
     * Default constructor sets nMines and status to zero
     */
    public Cell() {
        nMines = 0;
        status = 0;
    }

    /**
     * two args constructor
     *
     * @param nMines int for cell nMines
     * @param status int for status nMines
     */
    public Cell(int nMines, int status) {
        assert nMines >= -1 && nMines <= 8 : "nMines must be [-1,8]\n";
        assert status >= 0 && status <= 5 : "Status must be [0,5]\n";

        this.nMines = nMines;
        this.status = status;
    }

    /**
     * gets the cell nMines
     *
     * @return int for cell nMines
     */
    public int getnMines() {
        return nMines;
    }

    /**
     * gets the cell status
     *
     * @return int for status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the nMines of cell
     * 
     * @param nMines int for nMines
     */
    public void setnMines(int nMines) {
        assert nMines >= -1 && nMines <= 8 : "Value must be [-1,8]\n";
        this.nMines = nMines;
    }

    /**
     * Sets the status of cell
     * 
     * @param status int for status
     */
    public void setStatus(int status) {
        assert status >= 0 && status <= 5 : "Status must be [0,5]\n";
        this.status = status;
    }

    /**
     * calculates an hashcode for an object
     *
     * @return int for hashcode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.nMines;
        hash = 43 * hash + this.status;
        return hash;
    }

    /**
     * checks if two objets are equal uses the nMines and status to check
     *
     * @param obj cell to compare
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        
        return this.status == other.status && this.nMines == other.nMines;
    }

    /**
     * Compares one cell to other uses nMines for comparsion
     *
     * @param toCompare cell to compare
     * @return -1 if less the, 0 if equal, 1 if greater
     */
    public int compareTo(Cell toCompare) {
        assert toCompare != null;

        if (this.nMines < toCompare.getnMines()) {
            return -1;
        }

        if (this.nMines > toCompare.getnMines()) {
            return 1;
        }

        return 0;
    }

    /**
     * the object summary
     *
     * @return String for object sumary
     */
    @Override
    public String toString() {
        return "Cell{" + "value=" + nMines + ", status=" + status + '}';
    }
}
