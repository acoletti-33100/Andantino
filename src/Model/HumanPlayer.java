package Model;

import Model.Supervisor.GameSupervisor;

import java.util.List;

/**
 * represents a human player in a two-players game.
 */
public class HumanPlayer implements Player {
    /**
     * color of the player.
     */
    private final int color;

    /**
     * constructs a human player.
     * @param color color of the player
     */
    HumanPlayer(int color) {
        this.color = color;

    }

    /**
     * gets the player's color.
     * @return false if the player is black (player2),
     * true otherwise
     */
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isBot() {
        return false;
    }

    /**
     * unsupported method. The user gives the input for the human player from the GUI.
     */
    @Override
    public Tile move(List<Tile> gameBoard) {
        throw new UnsupportedOperationException();
    }
}
