package opponent.default_opponents;

import domain.Point;
import domain.Grid;
import opponent.Opponent;

/**
 * Implements the Opponent interface in a random based way,
 * this is the simplest implementation of the Opponent
 */
public class TonyRandomOpponent extends Opponent {
    @Override
    public Point move(Grid grid) {
        Point randomPoint;
        do {
            randomPoint = new Point(getRandomPosition(), getRandomPosition());
        } while(!grid.markIsEmpty(randomPoint));
        return randomPoint;
    }

    private int getRandomPosition() {
        return (int) (Math.random() * 3);
    }
}