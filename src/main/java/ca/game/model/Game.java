package ca.game.model;

import java.util.ArrayList;
import java.util.Scanner;

import static ca.game.model.GamePlay.getMaze;
import static ca.game.model.Maze.*;
import static ca.game.model.Mouse.makeMouseObject;

/**
 * Prints the game to the screen.
 */
public class Game {
    public int currentCheese = 0;
    public int totalCheese = 5;
    private final char DEAD = 'X';
    private char[][] mazeLost;
    public static char[][] maze;
    private boolean playerIsNotDead = true;
    public static Mouse mouse = makeMouseObject();
    private static InputTokens inputTokens = InputTokens.makeInputTokensObject();
    private static Cat cat = Cat.makeCatObject();
    public static Cell mousePosition;
    public static Cell cheesePosition;
    public static ArrayList<Cell> catPositions;
    public static MazeRevealer mazeRevealer;
    public static char[][] hiddenMaze;
    private static boolean playerLost;
    private static boolean playerWon;
    public static GamePlay gamePlay;

    public static boolean didPlayerLost() {
        return playerLost;
    }

    public boolean didPlayerWon() {
        return playerWon;
    }

    public static Game makePlayGameObject() {
        gamePlay = GamePlay.makeGamePlayObject();
        gamePlay.setInitialMaze();
        maze = getMaze();

        playerLost = false;
        playerWon = false;

        mazeRevealer = MazeRevealer.makeMazeRevealerObject();
        hiddenMaze = mazeRevealer.getHiddenMaze();

        mazeRevealer = MazeRevealer.makeMazeRevealerObject();
        mazeRevealer.setInitialHiddenMaze();

        hiddenMaze = mazeRevealer.getHiddenMaze();

        mousePosition = mouse.findMousePosition(maze);
        mazeRevealer.updateHiddenMaze(mousePosition, maze, hiddenMaze);

        cheesePosition = inputTokens.getCheesePosition(maze);
        catPositions = cat.getCatPositions(maze);

        return new Game();
    }

    public int getCurrentCheese() {
        return currentCheese;
    }

    public int getTotalCheese() {
        return totalCheese;
    }

    public boolean isPlayerIsNotDead() {
        return playerIsNotDead;
    }

    public boolean[][] makeMazeVisible() {
        boolean[][] isVisible = new boolean[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                isVisible[i][j] = true;
                hiddenMaze[i][j] = EXPLORED_SPACES;
            }
        }
        return isVisible;
    }

    public boolean[][] getIsVisibleMaze() {
        boolean[][] isVisible = new boolean[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (maze[i][j] == MOUSE || maze[i][j] == CHEESE ||
                        maze[i][j] == CAT || maze[i][j] == PERIMETER_WALL  ||
                        hiddenMaze[i][j] == EXPLORED_SPACES){
                    isVisible[i][j] = true;
                } else {
                    isVisible[i][j] = false;
                }
            }
        }
        return isVisible;
    }

    public boolean[][] getHasWallsMaze() {
        boolean[][] hasWalls = new boolean[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                hasWalls[i][j] = maze[i][j] == PERIMETER_WALL || maze[i][j] == WALL;
            }
        }
        return hasWalls;
    }

    private void playGame() {

        while(currentCheese != totalCheese && playerIsNotDead) {
            gamePlay = GamePlay.makeGamePlayObject();
            gamePlay.setInitialMaze();
            char[][] maze = getMaze();

            mazeRevealer = MazeRevealer.makeMazeRevealerObject();
            mazeRevealer.setInitialHiddenMaze();

            hiddenMaze = mazeRevealer.getHiddenMaze();

            mousePosition = mouse.findMousePosition(maze);
            mazeRevealer.updateHiddenMaze(mousePosition, maze, hiddenMaze);

            while(playerIsNotDead && !gamePlay.didMouseGetCheese(cheesePosition, mouse.findMousePosition(maze))) {
                catPositions = cat.getCatPositions(maze);
                mousePosition = mouse.findMousePosition(maze);

                Scanner userInput = new Scanner(System.in);
                char choice = userInput.next().charAt(0);

                switch (choice) {
                    case 'w':
                    case 'W':
                        playerIsNotDead = isPlayerIsNotDead(true, gamePlay, maze, catPositions,
                                mousePosition.getUp(mousePosition), hiddenMaze, cheesePosition);
                        setCheese(maze, cheesePosition, catPositions, mouse.findMousePosition(maze));
                        break;
                    case 'd':
                    case 'D':
                        playerIsNotDead = isPlayerIsNotDead(true, gamePlay, maze, catPositions,
                                mousePosition.getRight(mousePosition), hiddenMaze, cheesePosition);
                        setCheese(maze, cheesePosition, catPositions, mouse.findMousePosition(maze));
                        break;
                    case 's':
                    case 'S':
                        playerIsNotDead = isPlayerIsNotDead(true, gamePlay, maze, catPositions,
                                mousePosition.getDown(mousePosition), hiddenMaze, cheesePosition);
                        setCheese(maze, cheesePosition, catPositions, mouse.findMousePosition(maze));
                        break;
                    case 'a':
                    case 'A':
                        playerIsNotDead = isPlayerIsNotDead(true, gamePlay, maze, catPositions,
                                mousePosition.getLeft(mousePosition), hiddenMaze, cheesePosition);
                        setCheese(maze, cheesePosition, catPositions, mouse.findMousePosition(maze));
                        break;
                    case '?':
//                        displayMenu();
                        break;
                    case 'm':
                    case 'M':
//                        revealedMazeDisplay(maze);
                        break;
                    case 'c':
                    case 'C':
                        totalCheese = 1;
                        break;
                    default:
                        break;
                }
            }

            if (playerIsNotDead) {
            }
        }

        if (!playerIsNotDead) {
            playerLost = true;
        }
    }

    public void setCheese(char[][] maze, Cell cheesePosition, ArrayList<Cell> catPositions, Cell mousePosition) {
        GamePlay gamePlay = GamePlay.makeGamePlayObject();
        if (!gamePlay.didCatGetCheese(catPositions, cheesePosition) && !gamePlay.didMouseGetCheese(cheesePosition, mousePosition)) {
            maze[cheesePosition.getRow()][cheesePosition.getColumn()] = CHEESE;
        } else if (gamePlay.didMouseGetCheese(cheesePosition, mousePosition)) {
            playerWon = true;
            if (currentCheese != totalCheese) {
                currentCheese++;
            }
        }
    }


    public boolean moveMouse(boolean playerIsNotDead, GamePlay gamePlay, char[][] maze,
                                     ArrayList<Cell> catPositions, Cell mPosition, char[][] hiddenMaze,
                                     Cell cheesePosition) {
        MazeRevealer mazeRevealer = MazeRevealer.makeMazeRevealerObject();
        Mouse mouse = makeMouseObject();

        if (mouse.isValidMove(mPosition, maze)) {

            inputTokens.updateMouseAndMaze(mPosition, maze);
            mousePosition = mouse.findMousePosition(maze);

            Cell currentMousePosition = mouse.findMousePosition(maze);

            if (gamePlay.didCatGetMouse(catPositions, currentMousePosition)) {
                maze[mPosition.getRow()][mPosition.getColumn()] = DEAD;
                mazeLost = getMaze();
                mazeRevealer.updateHiddenMaze(currentMousePosition, maze, hiddenMaze);
                setCheese(maze, cheesePosition, catPositions, currentMousePosition);

                playerIsNotDead = false;
                playerLost = true;

            }
        }
        return playerIsNotDead;
    }

    public boolean moveCats(boolean playerIsNotDead, GamePlay gamePlay, char[][] maze,
                                     ArrayList<Cell> catPositions, Cell mPosition, char[][] hiddenMaze,
                                     Cell cheesePosition) {
        MazeRevealer mazeRevealer = MazeRevealer.makeMazeRevealerObject();

        inputTokens.updateCatsAndMaze(maze);
        if (gamePlay.didCatGetMouse(cat.getCatPositions(maze), mPosition)) {
            maze[mPosition.getRow()][mPosition.getColumn()] = DEAD;
            mazeLost = getMaze();

            playerIsNotDead = false;
            playerLost = true;
        }

        mazeRevealer.updateHiddenMaze(mPosition, maze, hiddenMaze);
        setCheese(maze, cheesePosition, catPositions, mPosition);
        return playerIsNotDead;
    }

    public boolean isPlayerIsNotDead(boolean playerIsNotDead, GamePlay gamePlay, char[][] maze,
                                     ArrayList<Cell> catPositions, Cell mPosition, char[][] hiddenMaze,
                                     Cell cheesePosition) {
        MazeRevealer mazeRevealer = MazeRevealer.makeMazeRevealerObject();
        Mouse mouse = makeMouseObject();

        if (mouse.isValidMove(mPosition, maze)) {

            inputTokens.updateMouseAndMaze(mPosition, maze);
            mousePosition = mouse.findMousePosition(maze);

            Cell currentMousePosition = mouse.findMousePosition(maze);

            if (gamePlay.didCatGetMouse(catPositions, currentMousePosition)) {
                maze[mPosition.getRow()][mPosition.getColumn()] = DEAD;
                mazeLost = getMaze();
                mazeRevealer.updateHiddenMaze(currentMousePosition, maze, hiddenMaze);
                setCheese(maze, cheesePosition, catPositions, currentMousePosition);

                playerIsNotDead = false;
                playerLost = true;

            } else {
                inputTokens.updateCatsAndMaze(maze);
                if (gamePlay.didCatGetMouse(cat.getCatPositions(maze), currentMousePosition)) {
                    maze[mPosition.getRow()][mPosition.getColumn()] = DEAD;
                    mazeLost = getMaze();

                    playerIsNotDead = false;
                    playerLost = true;
                }

                mazeRevealer.updateHiddenMaze(currentMousePosition, maze, hiddenMaze);
                setCheese(maze, cheesePosition, catPositions, currentMousePosition);
            }
        }
        return playerIsNotDead;
    }

    public Cell getMousePosition() {
        return mousePosition;
    }

    public Cell getCheesePosition() {
        return cheesePosition;
    }

    public ArrayList<Cell> getCatPositions() {
        return catPositions;
    }
}
