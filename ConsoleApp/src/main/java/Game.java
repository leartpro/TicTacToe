import controller.GameController;
import controller.GameState;
import domain.GridHistory;
import domain.Mark;
import domain.Point;
import exceptions.TicTacToeMenuException;
import exceptions.TicTacToeQuitException;
import exceptions.TicTacToeRestartException;
import exceptions.TicTacToeRestartSameOpponentException;
import opponent.*;
import utils.TerminalColors;
import utils.TerminalUtils;
import utils.TicTacToeUtils;

import java.util.HashMap;

public class Game {

    private final GameController controller;

    public Game(Opponent withOpponent) {
        controller = new GameController(withOpponent);
    }

    public Game(boolean firstGame) throws TicTacToeQuitException, TicTacToeRestartException, TicTacToeRestartSameOpponentException, TicTacToeMenuException {
        TerminalUtils.printStatus("A game has started, select a opponent");

        final Opponent opponent = getOpponentInput();
        TerminalUtils.printStatus("The opponent \"" + opponent.getClass().getSimpleName() + "\" was chosen\n");

        controller = new GameController(opponent);
        if (firstGame) {
            printTutorial();
        }
    }

    public void start() throws TicTacToeQuitException, TicTacToeRestartException, TicTacToeRestartSameOpponentException, TicTacToeMenuException {
        System.out.println(TicTacToeUtils.gridToString(controller.getGrid()));
        while (controller.getState() == GameState.running) {
            final Point point = getPointInput();
            controller.setPoint(point);
            System.out.println(TicTacToeUtils.gridToString(controller.getGrid()));
        }

        final GameState finalState = controller.getState();

        System.out.println(TicTacToeUtils.gameStateToString(finalState) + "\n");
    }

    public GridHistory getHistory() {
        return controller.getHistory();
    }

    public Opponent getOpponent() {
        return controller.getOpponent();
    }

    private void printTutorial() {
        System.out.println("Press the number key of the respective field");
        System.out.println("you want to place your cross on");
        System.out.println();

        System.out.println("You are: " + TicTacToeUtils.markToString(Mark.self));
        System.out.println("And the opponent is: " + TicTacToeUtils.markToString(Mark.opponent));
        System.out.println();

        System.out.print("To select a number, take a number between ");
        System.out.print(TerminalUtils.colorString("1", TerminalColors.black) + " and ");
        System.out.print(TerminalUtils.colorString("9", TerminalColors.black) + "\n");
        System.out.println();
    }


    private Opponent getOpponentInput() throws TicTacToeQuitException, TicTacToeRestartSameOpponentException, TicTacToeRestartException, TicTacToeMenuException {
        final Opponent[] availableOpponents = Opponent.defaultOpponents();

        for (int i = 0; i < availableOpponents.length; i++) {
            System.out.println(i + 1 + ": " + availableOpponents[i].getClass().getSimpleName());
        }
        final int choice = TerminalUtils.getIntInputWithAllExits(1, availableOpponents.length);
        return availableOpponents[choice - 1];
    }

    private Point getPointInput() throws TicTacToeQuitException, TicTacToeRestartException, TicTacToeRestartSameOpponentException, TicTacToeMenuException {
        int count = 0;
        HashMap<Integer, Point> map = new HashMap<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                count++;
                map.put(count, new Point(x, y));
            }
        }

        int input = -1;
        do {
            if (input == -1) {
                TerminalUtils.printStatus("Choose your Point:");
            } else {
                TerminalUtils.printError("The point was already taken, choose a new one:");
            }
            input = TerminalUtils.getIntInputWithAllExits(1, 9);
        } while (!controller.getGrid().markIsEmpty(map.get(input)));
        return map.get(input);
    }
}
