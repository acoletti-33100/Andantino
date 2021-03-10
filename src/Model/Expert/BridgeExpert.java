package Model.Expert;

import Model.Supervisor.AndantinoSupervisor;
import Model.Supervisor.GameSupervisor;
import Model.Tile;

import java.util.List;

/**
 * Bridge expert, it evaluates a bridge feature as follows:
 * player 1 has a position, then at least three of his
 * neighbours are enemies.
 */
public final class BridgeExpert implements Expert {

    /**
     * supervisor of the game.
     */
    private GameSupervisor supervisor;
    /**
     * weight of this feature.
     */
    private int weight;

    public BridgeExpert() {
        supervisor = new AndantinoSupervisor();
        weight = 4;
    }

    /**
     * checks if tile has at least three neighbours which belong
     * to the other player (enemy).
     * @param tile tile to check
     * @param gameBoard represents the gameboard of this game
     * @return true if it has at least three connected enemies,
     * false if it has at max 2 or if it has already been checked
     */
    private boolean hasAtLeastThreeEnemies(Tile tile, List<Tile> gameBoard) {
        boolean northEast, east, southEast,
                southWest, west, northWest;
        northEast = supervisor.isNorthEastNeighbourEnemy(tile, gameBoard);
        east = supervisor.isEastNeighbourEnemy(tile, gameBoard);
        southEast = supervisor.isSouthEastNeighbourEnemy(tile, gameBoard);
        southWest = supervisor.isSouthWestNeighbourEnemy(tile, gameBoard);
        west = supervisor.isWestNeighbourEnemy(tile, gameBoard);
        northWest = supervisor.isNorthWestNeighbourEnemy(tile, gameBoard);
        if(     east && northEast && southEast ||
                west && southWest && northWest ||
                southWest && west && southEast ||
                northEast && northWest && east ||
                west && northWest && northEast ||
                southWest && southEast && east)
            return true;
        return false;
    }

    /**
     * gets the weight of this feature.
     * @return weight of this feature
     */
    @Override
    public int getWeight() {
        return weight;
    }

    /**
     * sets the weight of this expert.
     * @param weight new weight to assign to this expert
     */
    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * determines if gameboard contains a bridge feature.
     * The feature is defined as:
     * player "x" has a position, then at least three of his
     * neighbours are enemies (they must be connected).
     * The occurrences are distinct, such that:
     * occurrences x,y do not have all the three same enemies tile, but
     * can share at best two of them.
     * The more there are, the higher the weight value returned will be.
     * It assumes a non empty gameboard.
     * @param gameBoard represents the gameboard of this game
     * @return weight of this feature such that if n distinct occurrences are
     * present, then it returns n * weight
     */
    @Override
    public int getWeight(List<Tile> gameBoard) {
        int lastTileColor = gameBoard.get(gameBoard.size() - 1).getPlayer();
        int bridgesCounter = (int) gameBoard.stream()
                .filter(tile -> tile.getPlayer() == lastTileColor &&
                        hasAtLeastThreeEnemies(tile, gameBoard)).count();
        return weight * bridgesCounter;
    }
}
