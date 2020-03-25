package ca.game.wrappers;

import ca.game.controllers.GameController;
import ca.game.model.PlayGame;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT

    public static ApiGameWrapper makeFromGame(PlayGame game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        wrapper.numCheeseFound = game.getCurrentCheese();
        wrapper.numCheeseGoal = game.getTotalCheese();
//        GameController.Status gameStatus = game.getGameStatus();
//        updateGameStatus(wrapper, gameStatus);
        return wrapper;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

}
