package ca.game.model;

import static ca.game.model.Maze.*;

/**
 * Reveals the maze as the mouse is moved.
 */
public class MazeRevealer {
    private char[][] hiddenMaze = new char[ROW][COLUMN];

    private MazeRevealer() { }

    public static MazeRevealer makeMazeRevealerObject() {
        return new MazeRevealer();
    }

    public void updateHiddenMaze(Cell mouseCoord, char[][] maze, char[][] hiddenMaze){
        Cell upCell = mouseCoord.getUp(mouseCoord);
        if(!isPerimeter(upCell, maze)){
            hiddenMaze[upCell.getRow()][upCell.getColumn()] = EXPLORED_SPACES;
        }

        Cell downCell = mouseCoord.getDown(mouseCoord);
        if(!isPerimeter(downCell, maze)){
            hiddenMaze[downCell.getRow()][downCell.getColumn()] = EXPLORED_SPACES;
        }

        Cell leftCell = mouseCoord.getLeft(mouseCoord);
        if(!isPerimeter(leftCell, maze)){
            hiddenMaze[leftCell.getRow()][leftCell.getColumn()] = EXPLORED_SPACES;
        }

        Cell rightCell = mouseCoord.getRight(mouseCoord);
        if(!isPerimeter(rightCell, maze)){
            hiddenMaze[rightCell.getRow()][rightCell.getColumn()] = EXPLORED_SPACES;
        }

        Cell upLeftDiagonal = mouseCoord.getUpLeftDiagonal(mouseCoord);
        if(!isPerimeter(upLeftDiagonal, maze)){
            hiddenMaze[upLeftDiagonal.getRow()][upLeftDiagonal.getColumn()] = EXPLORED_SPACES;
        }

        Cell downLeftDiagonal = mouseCoord.getDownLeftDiagonal(mouseCoord);
        if(!isPerimeter(downLeftDiagonal, maze)){
            hiddenMaze[downLeftDiagonal.getRow()][downLeftDiagonal.getColumn()] = EXPLORED_SPACES;
        }

        Cell downRightDiagonal = mouseCoord.getDownRightDiagonal(mouseCoord);
        if(!isPerimeter(downRightDiagonal, maze)){
            hiddenMaze[downRightDiagonal.getRow()][downRightDiagonal.getColumn()] = EXPLORED_SPACES;
        }

        Cell upRightDiagonal = mouseCoord.getUpRightDiagonal(mouseCoord);
        if(!isPerimeter(upRightDiagonal, maze)){
            hiddenMaze[upRightDiagonal.getRow()][upRightDiagonal.getColumn()] = EXPLORED_SPACES;
        }
    }

    public boolean isPerimeter(Cell cell, char[][] maze) {
        return maze[cell.getRow()][cell.getColumn()] == PERIMETER_WALL;
    }

    public void setInitialHiddenMaze(){
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                hiddenMaze[i][j] = UNEXPLORED_SPACES;
            }
        }
        for (int i = 0; i < COLUMN; i++) {
            hiddenMaze[0][i] = WALL;
            hiddenMaze[ROW - 1][i] = WALL;
        }
        for (int i = 0; i < ROW; i++) {
            hiddenMaze[i][0] = WALL;
            hiddenMaze[i][COLUMN - 1] = WALL;
        }
    }

    public char[][] getHiddenMaze() {
        return hiddenMaze;
    }
}
