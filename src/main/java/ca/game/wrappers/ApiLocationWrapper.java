package ca.game.wrappers;

import java.util.ArrayList;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(CellLocation cell) {
        ApiLocationWrapper location = new ApiLocationWrapper();

        // Populate this object!

        return location;
    }
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<CellLocation> cells) {
        List<ApiLocationWrapper> locations = new ArrayList<>();

        // Populate this object!

        return locations;
    }
}
