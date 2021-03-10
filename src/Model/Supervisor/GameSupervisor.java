package Model.Supervisor;

import java.util.List;
import Model.Tile;

/**
 * interface of a supervisor of a game.
 * It is the referee of a game. Determines the result of a game:
 * draw or win for one player.
 */
public interface GameSupervisor {
    boolean isDraw(List<Tile> gameBoard);

    Boolean hasWon(int color, List<Tile> gameBoard);

    boolean isNorthEastNeighbourEnemy(Tile tile, List<Tile> gameBoard);

    boolean isEastNeighbourEnemy(Tile tile, List<Tile> gameBoard);

    boolean isSouthEastNeighbourEnemy(Tile tile, List<Tile> gameBoard);

    boolean isSouthWestNeighbourEnemy(Tile tile, List<Tile> gameBoard);

    boolean isWestNeighbourEnemy(Tile tile, List<Tile> gameBoard);

    boolean isNorthWestNeighbourEnemy(Tile tile, List<Tile> gameBoard);
}