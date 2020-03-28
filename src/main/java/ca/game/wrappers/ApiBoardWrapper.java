package ca.game.wrappers;

import ca.game.model.Game;

import java.util.List;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    public static final int ROW = 15;
    public static final int COLUMN = 20;

    public static ApiBoardWrapper makeFromGame(Game game) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.boardWidth = COLUMN;
        wrapper.boardHeight = ROW;
        wrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(game.getMousePosition());
        wrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheesePosition());
        wrapper.catLocations = ApiLocationWrapper.makeFromCellLocations(game.getCatPositions());
        wrapper.hasWalls = game.getHasWallsMaze();
        wrapper.isVisible = game.getIsVisibleMaze();

        return wrapper;
    }
}
