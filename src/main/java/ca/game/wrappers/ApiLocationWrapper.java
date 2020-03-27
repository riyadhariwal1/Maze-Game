package ca.game.wrappers;

import ca.game.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(Cell cell) {
        ApiLocationWrapper location = new ApiLocationWrapper();

        location.x = cell.getColumn();
        location.y = cell.getRow();

        return location;
    }
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<Cell> cells) {
        List<ApiLocationWrapper> locations = new ArrayList<>();

        for (Cell cell : cells) {
            locations.add(makeFromCellLocation(cell));
        }

        return locations;
    }

    @Override
    public String toString() {
        return "ApiLocationWrapper{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
