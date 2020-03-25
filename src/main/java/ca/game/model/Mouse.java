package ca.game.model;

import java.util.ArrayList;

import static ca.game.model.InputTokens.getToken;
import static ca.game.model.Maze.*;

/**
 * Handles all mouse activity.
 */
public class Mouse {
    private ArrayList<Cell> emptySpaces = new ArrayList<>();

    private Mouse() { }

    public static Mouse makeMouseObject() {
        return new Mouse();
    }

    private boolean doesCellEqualTo(Cell cell, char typeOfWall, char[][] maze){
        return maze[cell.getRow()][cell.getColumn()] == typeOfWall;
    }

    // Finds all possible moves of the mouse
    private void findEmptySpaces(Cell mouseCoord, char[][] maze) {
        emptySpaces.clear();

        Cell upCell = mouseCoord.getUp(mouseCoord);
        if(doesCellEqualTo(upCell, EMPTY_SPACE, maze) || doesCellEqualTo(upCell, CAT, maze) ||
                doesCellEqualTo(upCell, CHEESE, maze)){
            emptySpaces.add(upCell);
        }

        Cell downCell = mouseCoord.getDown(mouseCoord);
        if (doesCellEqualTo(downCell, EMPTY_SPACE, maze) || doesCellEqualTo(downCell, CAT, maze) ||
                doesCellEqualTo(downCell, CHEESE, maze)){
            emptySpaces.add(downCell);
        }

        Cell leftCell = mouseCoord.getLeft(mouseCoord);
        if(doesCellEqualTo(leftCell, EMPTY_SPACE, maze) || doesCellEqualTo(leftCell, CAT, maze) ||
                doesCellEqualTo(leftCell, CHEESE, maze)){
            emptySpaces.add(leftCell);
        }

        Cell rightCell = mouseCoord.getRight(mouseCoord);
        if(doesCellEqualTo(rightCell, EMPTY_SPACE, maze) || doesCellEqualTo(rightCell, CAT, maze) ||
                doesCellEqualTo(rightCell, CHEESE, maze)){
            emptySpaces.add(rightCell);
        }
    }

    public Cell findMousePosition(char[][] maze){
        return getToken(maze, MOUSE);
    }

    public boolean isValidMove(Cell userInput, char[][] maze) {
        Cell currentMouseCoord = findMousePosition(maze);
        findEmptySpaces(currentMouseCoord, maze);
        for (Cell emptySpace : emptySpaces) {
            if (userInput.equals(emptySpace)) {
                return true;
            }
        }
        return false;
    }
}
