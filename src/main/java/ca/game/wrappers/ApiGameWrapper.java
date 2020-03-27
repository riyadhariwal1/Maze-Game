package ca.game.wrappers;

import ca.game.model.Game;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiGameWrapper makeFromGame(Game game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        wrapper.numCheeseFound = game.getCurrentCheese();
        wrapper.numCheeseGoal = game.getTotalCheese();
        wrapper.isGameWon = game.didPlayerWon();
        wrapper.isGameLost = game.didPlayerLost();
        return wrapper;
    }
}
