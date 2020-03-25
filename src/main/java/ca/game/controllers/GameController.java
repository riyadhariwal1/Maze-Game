package ca.game.controllers;

import ca.game.wrappers.ApiGameWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class GameController {

    private static ArrayList<ApiGameWrapper> apiGameWrappers = new ArrayList<>();
    private AtomicInteger nextGameNumber = new AtomicInteger();

    @GetMapping("/api/about")
    @ResponseStatus(HttpStatus.CREATED)
    public String getName() {
        return "Riya Dhariwal";
    }

    @PostMapping("/pledges")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createNewApiGameWrapper(@RequestBody ApiGameWrapper apiGameWrapper) {
        // Set pledge to have next ID:
        apiGameWrapper.setGameNumber(nextGameNumber.incrementAndGet());

        apiGameWrappers.add(apiGameWrapper);
        return apiGameWrapper;
    }

    @GetMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ArrayList<ApiGameWrapper> getApiGameWrappers() {
        return apiGameWrappers;
    }

}
