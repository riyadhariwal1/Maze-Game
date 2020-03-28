package ca.game.controllers;

import ca.game.model.Cell;
import ca.game.model.Game;
import ca.game.wrappers.ApiBoardWrapper;
import ca.game.wrappers.ApiGameWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.game.model.Game.*;

@RestController
public class GameController {

    private static List<ApiGameWrapper> apiGameWrappers = new ArrayList<>();
    private static List<ApiBoardWrapper> apiBoardWrappers = new ArrayList<>();
    private static List<Game> playGames = new ArrayList<>();
    private AtomicInteger nextGameNumber = new AtomicInteger();

    @GetMapping("/api/about")
    @ResponseStatus(HttpStatus.OK)
    public String getName() {
        return "Riya Dhariwal";
    }


    @GetMapping("/api/games")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiGameWrapper> getApiGameWrappers() {
        return apiGameWrappers;
    }

    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createNewApiGameWrapper() {
        Game game = Game.makePlayGameObject();
        playGames.add(game);

        ApiGameWrapper apiGameWrapper = ApiGameWrapper.makeFromGame(game, nextGameNumber.incrementAndGet() - 1);
        apiGameWrappers.add(apiGameWrapper);

        ApiBoardWrapper apiBoardWrapper = ApiBoardWrapper.makeFromGame(game);
        apiBoardWrappers.add(apiBoardWrapper);
        return apiGameWrapper;
    }

    @GetMapping("/api/games/{gameNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ApiGameWrapper getOneApiGameWrapper(@PathVariable("gameNumber") int apiGameWrapperId) {
        for (ApiGameWrapper gameWrapper : apiGameWrappers) {
            if (gameWrapper.gameNumber == apiGameWrapperId) {
                playGames.get(apiGameWrapperId);
                return gameWrapper;
            }
        }

        throw new IllegalArgumentException();
    }

    @GetMapping("/api/games/{gameNumber}/board")
    @ResponseStatus(HttpStatus.OK)
    public ApiBoardWrapper getOneApiBoardWrapper(@PathVariable("gameNumber") int apiGameWrapperId) {
        for (ApiGameWrapper gameWrapper : apiGameWrappers) {
            if (gameWrapper.gameNumber == apiGameWrapperId) {
                playGames.get(apiGameWrapperId) ;
                ApiBoardWrapper apiBoardWrapper = apiBoardWrappers.get(apiGameWrapperId);
                return apiBoardWrapper;
            }
        }
        throw new IllegalArgumentException();
    }

    @PostMapping("/api/games/{gameNumber}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiBoardWrapper makeMoves(@PathVariable("gameNumber") int apiGameWrapperId, @RequestBody String move) {
        for (ApiGameWrapper gameWrapper : apiGameWrappers) {
            if (gameWrapper.gameNumber == apiGameWrapperId) {
                Game game = playGames.get(apiGameWrapperId);
                ApiBoardWrapper apiBoardWrapper = apiBoardWrappers.get(apiGameWrapperId);

                switch(move) {
                    case "MOVE_DOWN":
                        return returnBoard(mousePosition.getDown(mousePosition), game, gameWrapper, apiGameWrapperId, apiBoardWrapper);

                    case "MOVE_UP":
                        return returnBoard(mousePosition.getUp(mousePosition), game, gameWrapper, apiGameWrapperId, apiBoardWrapper);

                    case "MOVE_LEFT":
                        return returnBoard(mousePosition.getLeft(mousePosition), game, gameWrapper, apiGameWrapperId, apiBoardWrapper);

                    case "MOVE_RIGHT":
                        return returnBoard(mousePosition.getRight(mousePosition), game, gameWrapper, apiGameWrapperId, apiBoardWrapper);

                    case "MOVE_CATS":
                        boolean playerIsNotDead = game.moveCats(true, gamePlay, maze,
                                catPositions, mousePosition, hiddenMaze, cheesePosition);
                        game.setCheese(maze, cheesePosition, catPositions, mouse.findMousePosition(maze));

                        gameWrapper.numCheeseFound = game.getCurrentCheese();
                        gameWrapper.isGameWon = game.didPlayerWon();
                        gameWrapper.isGameLost = didPlayerLost();
                        apiGameWrappers.set(apiGameWrapperId, gameWrapper);

                        apiBoardWrapper = ApiBoardWrapper.makeFromGame(game);
                        apiBoardWrappers.set(apiGameWrapperId, apiBoardWrapper);
                        return apiBoardWrapper;

                    default:
                        throw new IllegalAccessError();
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private ApiBoardWrapper returnBoard(Cell mousePosition, Game game, ApiGameWrapper gameWrapper,
                                        int apiGameWrapperId, ApiBoardWrapper apiBoardWrapper ) {
        if (mouse.isValidMove(mousePosition, maze)) {
            boolean playerIsNotDead = game.moveMouse(true, gamePlay, maze,
                    catPositions, mousePosition, hiddenMaze, cheesePosition);
            game.setCheese(maze, cheesePosition, catPositions, mouse.findMousePosition(maze));

            gameWrapper.numCheeseFound = game.getCurrentCheese();
            gameWrapper.isGameWon = game.didPlayerWon();
            gameWrapper.isGameLost = didPlayerLost();
            apiGameWrappers.set(apiGameWrapperId, gameWrapper);

            apiBoardWrapper = ApiBoardWrapper.makeFromGame(game);
            apiBoardWrappers.set(apiGameWrapperId, apiBoardWrapper);
            return apiBoardWrapper;
        } else {
            throw new IllegalAccessError();
        }
    }

    @PostMapping("/api/games/{gameNumber}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiGameWrapper activateCheats(@PathVariable("gameNumber") int apiGameWrapperId, @RequestBody String cheat) {
        for (ApiGameWrapper gameWrapper : apiGameWrappers) {
            if (gameWrapper.gameNumber == apiGameWrapperId) {
                Game game = playGames.get(apiGameWrapperId);

                switch (cheat) {
                    case "1_CHEESE":
                        game.totalCheese = 1;
                        gameWrapper.numCheeseGoal = 1;
                        apiGameWrappers.set(apiGameWrapperId, gameWrapper);
                        return gameWrapper;

                    case "SHOW_ALL":
                        ApiBoardWrapper apiBoardWrapper = apiBoardWrappers.get(apiGameWrapperId);
                        apiBoardWrapper.isVisible = game.makeMazeVisible();
                        apiBoardWrappers.set(apiGameWrapperId, apiBoardWrapper);
                        return gameWrapper;
                    default:
                        throw new IllegalAccessError();
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal move")
    @ExceptionHandler(IllegalAccessError.class)
    public void badMove() {

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request game number not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {

    }
}
