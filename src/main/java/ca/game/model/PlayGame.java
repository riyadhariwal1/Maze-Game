package ca.game.model;

import java.util.ArrayList;
import java.util.Scanner;

import static ca.game.model.Maze.*;
import static ca.game.model.Mouse.makeMouseObject;

/**
 * Prints the game to the screen.
 */
public class PlayGame {
    private int currentCheese = 0;
    private int totalCheese = 5;
    private final char DEAD = 'X';
    char[][] mazeLost;

    private Mouse mouse = makeMouseObject();
    private InputTokens inputTokens = InputTokens.makeInputTokensObject();
    private Cat cat = Cat.makeCatObject();


    public static PlayGame makePlayGameObject() {
        return new PlayGame();
    }

    public int getCurrentCheese() {
        return currentCheese;
    }

    public int getTotalCheese() {
        return totalCheese;
    }

    private PlayGame() {

//        initialMenu();

        boolean playerIsNotDead = true;

        // While player has not gotten all the cheeses and isn't dead:
        while(currentCheese != totalCheese && playerIsNotDead) {
            GamePlay gamePlay = GamePlay.makeGamePlayObject();
            gamePlay.setInitialMaze();
            char[][] maze = gamePlay.getMaze();

            MazeRevealer mazeRevealer = MazeRevealer.makeMazeRevealerObject();
            mazeRevealer.setInitialHiddenMaze();

            char[][] hiddenMaze = mazeRevealer.getHiddenMaze();

            Cell mousePosition = mouse.findMousePosition(maze);
            mazeRevealer.updateHiddenMaze(mousePosition, maze, hiddenMaze);
//            hiddenMazeDisplay(hiddenMaze, maze);

            Cell cheesePosition = inputTokens.getCheesePosition(maze);

            // While the mouse didn't get the cheese and the player isn't dead:
            while(playerIsNotDead && !gamePlay.didMouseGetCheese(cheesePosition, mouse.findMousePosition(maze))) {
                ArrayList<Cell> catPositions = cat.getCatPositions(maze);
                mousePosition = mouse.findMousePosition(maze);

//                cheeseCounterDisplay();
//                enterMoveDisplay();

                Scanner userInput = new Scanner(System.in);
                char choice = userInput.next().charAt(0);

                System.out.println();

                // Handles user input
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
                        System.out.println("Invalid move. Please enter just A (left), S (down), D (right), or W (up).");
                }
            }

            // When player finds cheese
            if (playerIsNotDead) {
                currentCheese++;
//                System.out.println("Congratulations! You won!");
//                cheeseCounterDisplay();
//                System.out.println();
//                revealedMazeDisplay(maze);
            }
        }

        // When player dies
        if (!playerIsNotDead) {
//            cheeseCounterDisplay();
//            System.out.println("I'm sorry, you have been eaten!");
//            System.out.println();
//            revealedMazeDisplay(mazeLost);
//            cheeseCounterDisplay();
//            System.out.println("GAME OVER; please try again.");
        }
    }

    public void setCheese(char[][] maze, Cell cheesePosition, ArrayList<Cell> catPositions, Cell mousePosition) {
        GamePlay gamePlay = GamePlay.makeGamePlayObject();
        if (!gamePlay.didCatGetCheese(catPositions, cheesePosition) && !gamePlay.didMouseGetCheese(cheesePosition, mousePosition)) {
            maze[cheesePosition.getRow()][cheesePosition.getColumn()] = CHEESE;
        }
    }

    public boolean isPlayerIsNotDead(boolean playerIsNotDead, GamePlay gamePlay, char[][] maze,
                                     ArrayList<Cell> catPositions, Cell mousePosition, char[][] hiddenMaze,
                                     Cell cheesePosition) {
        MazeRevealer mazeRevealer = MazeRevealer.makeMazeRevealerObject();
        Mouse mouse = makeMouseObject();

        // Checks if mouse move is valid
        if (mouse.isValidMove(mousePosition, maze)) {

            // If yes, update the maze
            inputTokens.updateMouseAndMaze(mousePosition, maze);
            Cell currentMousePosition = mouse.findMousePosition(maze);

            // If the mouse is eaten by the cat, display message
            if (gamePlay.didCatGetMouse(catPositions, currentMousePosition)) {
                maze[mousePosition.getRow()][mousePosition.getColumn()] = DEAD;
                mazeLost = gamePlay.getMaze();
                mazeRevealer.updateHiddenMaze(currentMousePosition, maze, hiddenMaze);
                setCheese(maze, cheesePosition, catPositions, currentMousePosition);
//                hiddenMazeDisplay(hiddenMaze, maze);

                playerIsNotDead = false;

            } else {
                inputTokens.updateCatsAndMaze(maze);
                if (gamePlay.didCatGetMouse(cat.getCatPositions(maze), currentMousePosition)) {
                    maze[mousePosition.getRow()][mousePosition.getColumn()] = DEAD;
                    mazeLost = gamePlay.getMaze();

                    playerIsNotDead = false;
                }

                mazeRevealer.updateHiddenMaze(currentMousePosition, maze, hiddenMaze);
                setCheese(maze, cheesePosition, catPositions, currentMousePosition);
//                hiddenMazeDisplay(hiddenMaze, maze);
            }
        } else {
            System.out.println("Invalid move: you cannot move through walls!");
        }
        return playerIsNotDead;
    }

//    public void initialMenu() {
//        title();
//        displayMenu();
//        System.out.println();
//    }
//    public void title() {
//        String dashes = "----------------------------------------";
//        String firstTitle = "Welcome to Cat and Mouse Maze Adventure!";
//        String secondTitle = "by Riya Dhariwal and Danielle Nix.";
//        System.out.println(dashes);
//        System.out.println(firstTitle);
//        System.out.println(secondTitle);
//        System.out.println(dashes);
//    }
//
//    public void displayMenu(){
//        String menu = "DIRECTIONS:\n" +
//                "\tFind 5 cheese before a cat eats you!\n" +
//                "LEGEND:\n" +
//                "\t#: Wall\n" +
//                "\t@: You (a mouse)\n" +
//                "\t!: Cat\n" +
//                "\t$: Cheese\n" +
//                "\t.: Unexplored space\n" +
//                "MOVES:\n" +
//                "\tUse W (up), A (left), S (down) and D (right) to move.\n" +
//                "\t(You must press enter after each move).";
//
//        System.out.println(menu);
//    }
//
//    private void cheeseCounterDisplay() {
//        String cheeseLine = "Cheese collected: " + currentCheese + " of " + totalCheese;
//        System.out.println(cheeseLine);
//    }
//
//    private void enterMoveDisplay() {
//        String userEntry = "Enter your move [WASD?]: ";
//        System.out.print(userEntry);

//    }

}
