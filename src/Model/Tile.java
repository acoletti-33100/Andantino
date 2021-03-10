package Model;

/**
 * represents a tile with 3 axis in a two players game.
 */
public interface Tile {
    int getX();
    int getY();
    int getZ();
    int getPlayer();
    boolean equalsAxis(Object o);
    boolean equalsAxis(int x, int y, int z);
    int getIndex();
}
