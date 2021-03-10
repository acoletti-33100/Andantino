package Model;

import java.util.List;

/**
 * player of a game.
 */
public interface Player {
    Tile move(List<Tile> gameBoard);
    int getColor();
    /**
    * determines wether the player is a bot or not
     * @return true if the player is not a human, false otherwise
    * */
    boolean isBot();
}
