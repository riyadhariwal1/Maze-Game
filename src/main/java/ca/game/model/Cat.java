package ca.game.model;

import java.util.ArrayList;
import java.util.Random;

import static ca.game.model.Cell.makeCellObject;
import static ca.game.model.Maze.*;

/**
 * Handles all cat activity.
 */

public class Cat {
    private ArrayList<Cell> validMoves = new ArrayList<>();
    private ArrayList<Cell> newCatPositions = new ArrayList<>();
    private ArrayList<Cell> catPositions = new ArrayList<>();
    private ArrayList<Cell> oldCatPositions = new ArrayList<>();

    private Cat() {
    }

    public static Cat makeCatObject() {
        return new Cat();
    }

    public void updateOldCatPositions(ArrayList<Cell> catPositions){
        oldCatPositions.clear();
        oldCatPositions.addAll(catPositions);
    }

    public ArrayList<Cell> getCatPositions(char[][] maze){
        catPositions.clear();
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (maze[i][j] == CAT){
                    catPositions.add(makeCellObject(i, j));
                }
            }
        }
        return catPositions;
    }

    public ArrayList<Cell> getNewCatPositions(char[][] maze, ArrayList<Cell> catPositions){
        newCatPositions.clear();

        for (Cell catPosition : catPositions) {
            findValidMoves(catPosition, maze);
            if (validMoves.size() != 1){
                for (Cell oldCatPosition : oldCatPositions) {
                    validMoves.remove(oldCatPosition);
                }
            }
            Random random = new Random();
            newCatPositions.add(validMoves.get(random.nextInt(validMoves.size())));
        }
        updateOldCatPositions(catPositions);

        return newCatPositions;
    }

    private boolean doesCellEqualTo(Cell cell, char typeOfWall, char[][] maze){
        return maze[cell.getRow()][cell.getColumn()] == typeOfWall;
    }

    public boolean isCellInValidMoves(Cell cell){
        for (Cell moves : newCatPositions) {
            if (cell.equals(moves)){
                return true;
            }
        }
        return false;
    }

    private void findValidMoves(Cell catCoord, char[][] maze) {
        validMoves.clear();

        Cell upCell = catCoord.getUp(catCoord);
        if(doesCellEqualTo(upCell, EMPTY_SPACE, maze) || doesCellEqualTo(upCell, MOUSE, maze) ||
                doesCellEqualTo(upCell, CHEESE, maze) || doesCellEqualTo(upCell, CAT, maze)){
            validMoves.add(upCell);
        }

        Cell downCell = catCoord.getDown(catCoord);
        if (doesCellEqualTo(downCell, EMPTY_SPACE, maze) || doesCellEqualTo(downCell, MOUSE, maze) ||
                doesCellEqualTo(downCell, CHEESE, maze) || doesCellEqualTo(downCell, CAT, maze)){
            validMoves.add(downCell);
        }

        Cell leftCell = catCoord.getLeft(catCoord);
        if(doesCellEqualTo(leftCell, EMPTY_SPACE, maze) || doesCellEqualTo(leftCell, MOUSE, maze) ||
                doesCellEqualTo(leftCell, CHEESE, maze) || doesCellEqualTo(leftCell, CAT, maze)){
            validMoves.add(leftCell);
        }

        Cell rightCell = catCoord.getRight(catCoord);
        if(doesCellEqualTo(rightCell, EMPTY_SPACE, maze) || doesCellEqualTo(rightCell, MOUSE, maze) ||
                doesCellEqualTo(rightCell, CHEESE, maze) || doesCellEqualTo(rightCell, CAT, maze)){
            validMoves.add(rightCell);
        }
    }
}
