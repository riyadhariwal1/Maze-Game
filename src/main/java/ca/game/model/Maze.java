package ca.game.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import static ca.game.model.Cell.makeCellObject;

/**
 * Generates maze using recursive back tracker algorithm.
 */
public class Maze {
    // Maze characters:
    public static final char EMPTY_SPACE = ' ';
    public static final char WALL = '#';
    public static final char PERIMETER_WALL = '?';
    public static final char UNEXPLORED_SPACES = '.';
    public static final char EXPLORED_SPACES = '+';

    public static final int ROW = 15;
    public static final int COLUMN = 20;

    // Token characters:
    public static final char MOUSE = '@';
    public static final char CHEESE = '$';
    public static final char CAT = '!';

    private static char[][] maze = new char[ROW][COLUMN];
    private Stack<Cell> exploredSpaces = new Stack<>();
    private ArrayList<Cell> neighbours = new ArrayList<>();
    private static ArrayList<Cell> walls = new ArrayList<>();

    private Maze() { }

    public static Maze makeMazeObject() {
        return new Maze();
    }

    public char[][] getMaze() {
        return maze;
    }

    public boolean isMazeValid() {
        return (areCornersEmpty() && !hasWallSquare(WALL) && !hasWallSquare(EMPTY_SPACE) && !hasWallWithPerimeter());
    }

    // checks for square of walls by perimeter
    private boolean hasWallWithPerimeter() {
        for (int i = 3; i < COLUMN - 1; i++) {
            if (maze[1][i - 1] == WALL && maze[1][i] == WALL) {
                return true;
            }
        }

        for (int i = 3; i < COLUMN - 1; i++) {
            if (maze[13][i - 1] == WALL && maze[13][i] == WALL) {
                return true;
            }
        }

        for (int i = 3; i < ROW - 1; i++) {
            if (maze[i - 1][1] == WALL && maze[i][1] == WALL) {
                return true;
            }
        }

        for (int i = 3; i < ROW - 1; i++) {
            if (maze[i - 1][18] == WALL && maze[i][18] == WALL) {
                return true;
            }
        }
        return false;
    }

    // Checks that there are no patches of walls
    public boolean hasWallSquare(char typeOfWall) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (maze[i][j] == typeOfWall){
                    Cell currentCell = makeCellObject(i, j);
                    if (numberOfAdjacentWalls(currentCell, typeOfWall) == 2) {
                        if(cellHasDiagonal(currentCell, typeOfWall)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean cellHasDiagonal(Cell currentCell, char typeOfWall) {
        if (currentCell.hasUpCell(currentCell, typeOfWall, maze) && currentCell.hasRightCell(currentCell, typeOfWall, maze)){
            if (maze[currentCell.getRow() - 1][currentCell.getColumn() + 1] == typeOfWall){
                return true;
            }
        }
        if (currentCell.hasUpCell(currentCell, typeOfWall, maze) && currentCell.hasLeftCell(currentCell, typeOfWall, maze)){
            if (maze[currentCell.getRow() - 1][currentCell.getColumn() - 1] == typeOfWall){
                return true;
            }
        }
        if (currentCell.hasDownCell(currentCell, typeOfWall, maze) && currentCell.hasRightCell(currentCell, typeOfWall, maze)){
            if (maze[currentCell.getRow() + 1][currentCell.getColumn() + 1] == typeOfWall){
                return true;
            }
        }
        if (currentCell.hasDownCell(currentCell, typeOfWall, maze) && currentCell.hasLeftCell(currentCell, typeOfWall, maze)){
            if (maze[currentCell.getRow() + 1][currentCell.getColumn() - 1] == typeOfWall){
                return true;
            }
        }
        return false;
    }

    private boolean areCornersEmpty() {
        return  maze[ROW - 2][1] == EMPTY_SPACE &&
                maze[ROW - 2][COLUMN - 2] == EMPTY_SPACE &&
                maze[1][COLUMN - 2] == EMPTY_SPACE &&
                maze[1][1] == EMPTY_SPACE;
    }

    public void setInitialMaze() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                maze[i][j] = WALL;
            }
        }
        for (int i = 0; i < COLUMN; i++) {
            maze[0][i] = PERIMETER_WALL;
            maze[ROW - 1][i] = PERIMETER_WALL;
        }
        for (int i = 0; i < ROW; i++) {
            maze[i][0] = PERIMETER_WALL;
            maze[i][COLUMN - 1] = PERIMETER_WALL;
        }
    }

    // Creates maze using algorithm
    public void makeMaze(){
        setInitialMaze();

        Cell startPoint = makeCellObject(1, 1);
        exploredSpaces.push(startPoint);
        maze[startPoint.getRow()][startPoint.getColumn()] = EMPTY_SPACE;

        while (!exploredSpaces.empty()){
            findNeighbours(startPoint);
            if (neighbours.isEmpty()){
                if (!exploredSpaces.empty()){
                    startPoint = exploredSpaces.pop();
                }
            } else {
                startPoint = chooseRandomNeighbour();
                maze[startPoint.getRow()][startPoint.getColumn()] = EMPTY_SPACE;
                exploredSpaces.push(startPoint);
            }
        }

        // Creates islands in maze
        makeIslands();

    }

    private Cell chooseRandomIsland() {
        perimeterIsland();
        Random random = new Random();
        return walls.get(random.nextInt(walls.size()));
    }

    private void perimeterIsland() {
        walls.clear();

        for (int i = 1; i < COLUMN - 1; i++) {
            if(maze[1][i] == WALL) {
                walls.add(makeCellObject(1, i));
            }
            if (maze[13][i] == WALL) {
                walls.add(makeCellObject(13, i));
            }
        }
        for (int i = 1; i < ROW - 1; i++) {
            if(maze[i][1] == WALL) {
                walls.add(makeCellObject(i, 1));
            }
            if (maze[i][18] == WALL) {
                walls.add(makeCellObject(i, 18));
            }
        }
    }

    public static void findSpaces(char typeOfWall, char[][] maze, ArrayList<Cell> walls) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if(maze[i][j] == typeOfWall){
                    Cell cell = makeCellObject(i, j);
                    walls.add(cell);
                }
            }
        }
    }

    public void makeIslands() {
        int counter = 0;
        while (counter != 0){
            makeIsland();
            counter++;
        }
    }

    public void makeIsland() {
        Cell deleteWall = chooseRandomIsland();
        maze[deleteWall.getRow()][deleteWall.getColumn()] = EMPTY_SPACE;
    }

    private Cell chooseRandomNeighbour() {
        Random random = new Random();
        return neighbours.get(random.nextInt(neighbours.size()));
    }

    private boolean doesCellEqualTo(Cell cell, char typeOfWall){
        if (cell.getRow() < 0) {
            System.out.println("cell.getRow() is = " + cell.getRow());
        }
        if (cell.getColumn() < 0) {
            System.out.println("cell.getColumn() is = " + cell.getColumn());
        }

        return maze[cell.getRow()][cell.getColumn()] == typeOfWall;
    }

    private void findNeighbours(Cell cell) {
        neighbours.clear();

        Cell upCell = cell.getUp(cell);
        if(doesCellEqualTo(upCell, WALL) && numberOfAdjacentWalls(upCell, EMPTY_SPACE) == 1){
            neighbours.add(upCell);
        }

        Cell downCell = cell.getDown(cell);
        if (doesCellEqualTo(downCell, WALL) && numberOfAdjacentWalls(downCell, EMPTY_SPACE) == 1){
            neighbours.add(downCell);
        }

        Cell leftCell = cell.getLeft(cell);
        if(doesCellEqualTo(leftCell, WALL) && numberOfAdjacentWalls(leftCell, EMPTY_SPACE) == 1){
            neighbours.add(leftCell);
        }

        Cell rightCell = cell.getRight(cell);
        if(doesCellEqualTo(rightCell, WALL) && numberOfAdjacentWalls(rightCell, EMPTY_SPACE) == 1){
            neighbours.add(rightCell);
        }
    }

    private int numberOfAdjacentWalls(Cell cell, char typeOfWall) {
        int counter = 0;

        Cell upCell = cell.getUp(cell);
        if(doesCellEqualTo(upCell, typeOfWall)){
            counter++;
        }

        Cell downCell = cell.getDown(cell);
        if (doesCellEqualTo(downCell, typeOfWall)){
            counter++;
        }

        Cell leftCell = cell.getLeft(cell);
        if(doesCellEqualTo(leftCell, typeOfWall)){
            counter++;
        }

        Cell rightCell = cell.getRight(cell);
        if(doesCellEqualTo(rightCell, typeOfWall)){
            counter++;
        }
        return counter;
    }
}