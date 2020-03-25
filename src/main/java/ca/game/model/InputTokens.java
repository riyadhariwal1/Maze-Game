package ca.game.model;

import java.util.ArrayList;
import java.util.Random;

import static ca.game.model.Cat.makeCatObject;
import static ca.game.model.Cell.makeCellObject;
import static ca.game.model.Maze.*;
import static ca.game.model.Mouse.makeMouseObject;

/**
 * Places all the tokens in the maze.
 */
public class InputTokens {

    private Cat cat = makeCatObject();
    private Cell mousePosition = makeCellObject(1,1);
    private ArrayList<Cell> emptyPositions = new ArrayList<>();

    private InputTokens() { }

    public static InputTokens makeInputTokensObject() {
        return new InputTokens();
    }

    public void setInitialTokens(char[][] maze){
        maze[ROW - 2][1] = CAT;
        maze[ROW - 2][COLUMN - 2] = CAT;
        maze[1][COLUMN - 2] = CAT;
        maze[1][1] = MOUSE;

        Cell cheeseCell = putCheese(maze);
        maze[cheeseCell.getRow()][cheeseCell.getColumn()] = CHEESE;
    }

    public Cell getCheesePosition(char[][] maze) {
        return getToken(maze, CHEESE);
    }

    public static Cell getToken(char[][] maze, char token) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (maze[i][j] == token){
                    return makeCellObject(i, j);
                }
            }
        }
        return null;
    }

    public Cell putCheese(char[][] maze) {
        findSpaces(EMPTY_SPACE, maze, emptyPositions);
        Random random = new Random();
        return emptyPositions.get(random.nextInt(emptyPositions.size()));
    }

    public void updateMouseAndMaze(Cell userInput, char[][] maze) {
        Mouse mouse = makeMouseObject();
        Cell oldMousePosition = mouse.findMousePosition(maze);
        maze[userInput.getRow()][userInput.getColumn()] = MOUSE;
        maze[oldMousePosition.getRow()][oldMousePosition.getColumn()] = EMPTY_SPACE;
        mousePosition = userInput;
    }

    public void updateCatsAndMaze(char[][] maze) {
        ArrayList<Cell> catPositions = cat.getCatPositions(maze);
        ArrayList<Cell> newCatPositions = cat.getNewCatPositions(maze, catPositions);

        for (int i = 0; i < newCatPositions.size(); i++) {
            maze[newCatPositions.get(i).getRow()][newCatPositions.get(i).getColumn()] = CAT;
            Cell cell = makeCellObject(catPositions.get(i).getRow(), catPositions.get(i).getColumn());

            if (!cat.isCellInValidMoves(cell)){
                maze[cell.getRow()][cell.getColumn()] = EMPTY_SPACE;
            }
        }
    }
}
