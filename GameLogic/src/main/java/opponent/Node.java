package opponent;

import domain.Grid;
import domain.Mark;

import java.util.ArrayList;
import java.util.List;

public class Node implements Tree.TreePrintable {

    private Grid grid;
    private List<Integer> score = new ArrayList<>();
    private Mark minimax;

    public Node(Grid grid, int score, Mark minimax) {
        this.grid = grid;
        this.score.add(score);
        this.minimax = minimax;
    }

    public Node(Grid grid, int score) {
        this.grid=grid;
        this.score.add(score);
    }

    public Node(Grid grid, Mark minimax) {
        this.grid=grid;
        this.minimax=minimax;
    }

    public Node(Grid grid) {
        this.grid = grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void addScore(int score) {
        this.score.add(score);
    }

    public void setMinimaxStatus(Mark minimax) {
        this.minimax = minimax;
    }

    public Grid getGrid() {
        return grid;
    }

    public List<Integer> getScores() {
        return score;
    }

    public Mark getMinimax() {
        return minimax;
    }

    @Override
    public String toString(String padding) {
        /*
        return padding + "Scores: " + Arrays.toString(score.toArray())
                + "\n" + padding +"MinimaxStatus: " + minimax
                + "\n" + grid.toString(padding); */
        return "abc";
    }

    public String asString() {
        return grid.asString();
    }
}
