package controller;

import domain.GridHistory;
import domain.Mark;
import domain.Grid;
import domain.Point;
import opponent.Opponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller of a 3x3 tic-tac-toe field.
 * It can be given an opponent,
 * which the player can then play against
 * <p>
 * Will calculate if somebody wins after a move and
 * stores the complete history of the game
 */
public class GameController {
    private final List<Grid> historyList = new ArrayList<>();
    private Opponent opponent;
    private Grid grid;
    private GameState state;

    /**
     * @param opponent The opponent (from the Opponent interface)
     *                 that the player should face.
     *                 It is responsible for the countermove,
     *                 against the player
     */
    public GameController(Opponent opponent) {
        this.opponent = opponent;
        setGridAndAddToHistory(Grid.emptyGrid());
        state = GameState.running;
    }

    /**
     * @param opponent     The opponent responsible for the countermove
     * @param startingGrid The starting gridData data, the gridData should have
     */
    public GameController(Opponent opponent, Mark[][] startingGrid) {
        assert (startingGrid.length == 3);
        assert (startingGrid[0].length == 3);
        assert (startingGrid[1].length == 3);
        assert (startingGrid[2].length == 3);

        this.opponent = opponent;
        setGridAndAddToHistory(new Grid(startingGrid));
        state = calculateGameState();
    }

    /**
     * @param startingGrid A real Grid instead of just the gridData
     */
    public GameController(Opponent opponent, Grid startingGrid) {
        this.opponent = opponent;
        setGridAndAddToHistory(startingGrid);
        state = calculateGameState();
    }

    /**
     * @param startingHistory The already past history of the game
     */
    public GameController(Opponent opponent, GridHistory startingHistory) {
        this.opponent = opponent;
        grid = startingHistory.getLastHistoryRecord();
        state = calculateGameState();
        for (int i = 0; i < startingHistory.getLength(); i++) {
            historyList.add(startingHistory.getHistoryRecord(i));
        }
    }

    /**
     * Updates the state of the gridData with the players cross
     * and the opponents response
     * <p>
     * Also finds out if anyone has won,
     * lost or if there was a tie
     * <p>
     * Only works if the state is still GameState.running
     * <p>
     * Information of the game state
     * is updated inside the controller
     *
     * @param point The point where the player sets his cross
     * @return The point which the opponent sets,
     * is null when the opponent does not set a point,
     * because of a tie or win by the player
     */
    public Point setPoint(Point point) {
        assert (state == GameState.running);
        Point opponentPoint = null;

        setGridAndAddToHistory(grid.copyWith(point, Mark.self));
        state = calculateGameState();
        if (state == GameState.running) {
            opponentPoint = opponent.move(grid);
            setGridAndAddToHistory(grid.copyWith(opponentPoint, Mark.opponent));
            state = calculateGameState();
        }
        return opponentPoint;
    }

    /**
     * Get the Grid
     *
     * @return The current gridData of this Opponent
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * @return Current GameState of the GameController
     */
    public GameState getState() {
        return state;
    }

    /**
     * @return The history as a GridHistory
     */
    public GridHistory getHistory() {
        return new GridHistory(historyList);
    }

    /**
     * @return The opponent the player is playing against
     */
    public Opponent getOpponent() {
        return opponent;
    }

    public void setOpponent(Opponent opponent) {
        this.opponent = opponent;
    }

    /**
     * Add a new Grid to the history and set the current gridData to it
     *
     * @param grid The new Grid
     */
    private void setGridAndAddToHistory(Grid grid) {
        historyList.add(grid);
        this.grid = grid;
    }

    /**
     * @return The current GameState
     */
    private GameState calculateGameState() {
        if (checkWonOrLost(Mark.self)) {
            return GameState.won;
        } else if (checkWonOrLost(Mark.opponent)) {
            return GameState.lost;
        } else if (checkTie()) {
            return GameState.tie;
        }
        return GameState.running;
    }

    private boolean checkTie() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.markIsEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWonOrLost(Mark mark) {
        return checkForDiagonals(mark) || checkForLines(mark);
    }

    private boolean checkForDiagonals(Mark mark) {
        final boolean middleIs = grid.getMark(1, 1) == mark;
        final boolean firstDiagonal = grid.getMark(0, 0) == mark && grid.getMark(2, 2) == mark;
        final boolean secondDiagonal = grid.getMark(0, 2) == mark && grid.getMark(2, 0) == mark;
        return middleIs && (firstDiagonal || secondDiagonal);
    }

    private boolean checkForLines(Mark mark) {
        return checkForLine(mark, true) || checkForLine(mark, false);
    }

    private boolean checkForLine(Mark mark, boolean vertical) {
        xLoop:
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid.getMark(vertical ? x : y, vertical ? y : x) != mark) {
                    continue xLoop;
                }
            }
            return true;
        }
        return false;
    }
}
