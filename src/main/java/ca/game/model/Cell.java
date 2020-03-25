package ca.game.model;

/**
 * Represents each cell on the maze.
 */
public class Cell {
    private int row;
    private int column;

    private Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static Cell makeCellObject(int row, int column) {
        return new Cell(row, column);
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                column == cell.column;
    }

    public boolean hasUpCell(Cell currentCell, char typeOfWall, char[][] maze){
        Cell upCell = getUp(currentCell);
        return maze[upCell.row][upCell.column] == typeOfWall;
    }

    public boolean hasDownCell(Cell currentCell, char typeOfWall, char[][] maze){
        Cell downCell = getDown(currentCell);
        return maze[downCell.row][downCell.column] == typeOfWall;
    }

    public boolean hasLeftCell(Cell currentCell, char typeOfWall, char[][] maze){
        Cell leftCell = getLeft(currentCell);
        return maze[leftCell.row][leftCell.column] == typeOfWall;
    }

    public boolean hasRightCell(Cell currentCell, char typeOfWall, char[][] maze){
        Cell rightCell = getRight(currentCell);
        return maze[rightCell.row][rightCell.column] == typeOfWall;
    }

    public Cell getUp(Cell currentCell) {
        return new Cell(currentCell.row - 1, currentCell.column);
    }

    public Cell getDown(Cell currentCell) {
        return new Cell(currentCell.row + 1, currentCell.column);
    }

    public Cell getLeft(Cell currentCell) {
        return new Cell(currentCell.row, currentCell.column - 1);
    }

    public Cell getRight(Cell currentCell) {
        return new Cell(currentCell.row, currentCell.column + 1);
    }

    public Cell getUpRightDiagonal(Cell currentCell){
        return new Cell(currentCell.row - 1, currentCell.column + 1);
    }

    public Cell getUpLeftDiagonal(Cell currentCell){
        return new Cell(currentCell.row - 1, currentCell.column - 1);
    }

    public Cell getDownRightDiagonal(Cell currentCell){
        return new Cell(currentCell.row + 1, currentCell.column + 1);
    }

    public Cell getDownLeftDiagonal(Cell currentCell){
        return new Cell(currentCell.row + 1, currentCell.column - 1);
    }

    @Override
    public String toString() {
        return "Cell [" +
                "row = " + row +
                ", column = " + column +
                ']';
    }
}
