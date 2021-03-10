package Model;

/**
 * represents a tile in the gameboard.
 * Note that this class is immutable.
 */
public final class HexTile implements Tile {
    /**
     * X axis of the tile.
     */
    private final int x;
    /**
     * Y axis of the tile.
     */
    private final int y;
    /**
     * Z axis of the tile.
     */
    private final int z;
    /**
     * color of the player.
     */
    private final int player;

    /**
     * index of this tile.
     */
    private final int index;

    /**
     * constructs a new HexTile with the specified parameters.
     * It is used in conjunction with the copy factory.
     * @param x x axis of the tile
     * @param y y axis of the tile
     * @param z z axis of the tile
     * @param player color of the player
     * @param index index of this tile
     */
    private HexTile(int x, int y, int z, int player, int index) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
        this.index = index;
    }

    /**
     * constructs a hex tile.
     * @param x x axis of the tile
     * @param y y axis of the tile
     * @param z z axis of the tile
     * @param player color of the player
     */
    public HexTile(int x, int y, int z, int player) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
        index = createIndex();
    }

    /**
     * determines the Manhattan distance between two tiles.
     * @param x axis X of the first tile
     * @param y axis Y of the first tile
     * @param z axis Z of the first tile
     * @param xx axis X of the second tile
     * @param yy axis Y of the second tile
     * @param zz axis Z of the second tile
     * @return distance between the two tiles
     */
    private static int distance(int x, int y, int z, int xx, int yy, int zz) {
        return (
                Math.abs(x - xx) +
                Math.abs(y - yy) +
                Math.abs(z - zz)
                ) / 2;
    }

    /**
     * determines the index of this tile. The index is equal
     * to 0 for the center (x: 0, y: 0, z: 0), then it increments
     * by 1 following the next ring. So, the ring closest to
     * the center (radius = 1), the tile x: -1,y: 0,z: 1 has index 1.
     * Then the index is incremented following a rotation of the vector
     * from the center to a tile in a clockwise manner. Also, the south west element in
     * a new ring is always the first element of the ring, with axis:
     * x: -radius, y: 0, z: radius.
     * @return index of this tile in a hexagonal gameboard
     * @see <a href="https://www.redblobgames.com/grids/hexagons/#rings-spiral">Ring hexagons</a>
     */
    private int createIndex() {
        if((x == 0) && (y == 0) && (z == 0))
            return 0;
        if(equalsAxis(-1, 0, 1))      // south west corner
            return 1;
        if(equalsAxis(0,-1, 1))     // south east corner
            return 2;
        if(equalsAxis(1, -1, 0))    // east corner
            return 3;
        if(equalsAxis(1, 0, -1))  // north east corner
            return 4;
        if(equalsAxis(0, 1, -1))    // north west corner
            return 5;
        if(equalsAxis(-1, 1, 0))      // west corner
            return 6;
        int radius = this.radiusFromCenter();
        // HexTile first = new HexTile(-radius, 0, radius, false);
        if(x == -radius && y == 0 && z == radius)
            return 1 + countInnerRingsTiles(radius); // the element + inner rings
        return countInnerRingsTiles(radius) + countTilesRing(radius);
    }

    /**
     * determines the radius of the tile from the center.
     * @return Manhattan distance of the tile from the center
     */
    private int radiusFromCenter() {
        return ( // center coordinates: 0,0,0
                Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
    }

    /**
     * counts the number of elements in the inner rings
     * from this tile. So, if this tile is in ring 3, then
     * it counts the total number of elements in ring 1 and 2.
     * Note that it does not count the center.
     * Also, note that ring 1 is the one closest to the center.
     * @param radius Manhattan distance from center
     * @return number of tiles in inner rings
     */
    private int countInnerRingsTiles(int radius) {
        int res = 0;
        for(int i = 1; i < radius; i++) {
            res += i * 6;
        }
        return res;
    }

    /**
     * counts the number of tiles in the ring of a specified
     * radius from begin to match.
     * The sides are considered as follows (n = number of elements
     * in a side - 1 = index of the tile = radius from center):
     * south: z fixed; -n <= x <= 0; -n <= y <= 0;
     * south east: y fixed; 0 < x <= n; 0 <= z < n;
     * north east: x fixed; -n < y <= 0; -n <= z < 0;
     * north: z fixed; 0 <= x < n; 0 < y <= n;
     * north west: y fixed; -n <= x < 0; -n < z <= 0;
     * south west: x fixed; 0 < y < n; 0 < z < n;
     * @param axisIndex index of the axis of this ring
     * @return number of elements found in the path
     */
    private int countTilesRing(int axisIndex) {
        // match is in the south side of the ring
        if(z == axisIndex && x >= -axisIndex &&
            x <= 0 && y <= 0
                && y >= -axisIndex) {
            return 1 + distance(-axisIndex, 0, axisIndex, x, y, z);
        }
        // match is in the south east side of the ring
        if(y == -axisIndex && x > 0 &&
                x <= axisIndex &&
            z >= 0 && z < axisIndex) {
            // return all tiles in the bottom plus the number of tiles
            // between the corner and x, y, z
            return 1 + axisIndex + distance(0, -axisIndex, axisIndex, x, y, z);
        }
        // match is in the north east side of the ring
        if(x == axisIndex && y <= 0
                && y > -axisIndex &&
            z < 0 && z >= -axisIndex) {
            return 1 + (axisIndex * 2) + distance(axisIndex, -axisIndex, 0, x, y, z);
        }
        // match is in the north side of the ring
        if(z == -axisIndex && y > 0 &&
            y <= axisIndex && x >= 0
                && x < axisIndex) {
            return 1 + (axisIndex * 3) + distance(axisIndex, 0, -axisIndex, x, y, z);
        }
        // match is in the north west side of the ring
        if(y == axisIndex && x < 0 &&
        x >= -axisIndex && z <= 0 &&
                z > -axisIndex) {
            return 1 + (axisIndex * 4) + distance(0, axisIndex, -axisIndex, x, y, z);
        }
        // match is in the south west side of the ring
            return 1 + (axisIndex * 5) + distance(-axisIndex, axisIndex, 0, x, y, z);
    }

    /**
     * copy factory for a HexTile.
     * @param tile tile to copy
     * @return a new instance, which is a deep copy of tile
     */
    public static HexTile newInstance(HexTile tile) {
        return new HexTile(tile.getX(), tile.getY(),
                tile.getZ(), tile.getPlayer(), tile.getIndex());
    }

    /**
     * returns the X axis value of a tile.
     */
    @Override
    public int getX() { return x; }

    /**
     * returns the Y axis value of a tile.
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * returns the Z axis value of a tile.
     */
    @Override
    public int getZ() {
        return z;
    }

    /**
     * returns the color of the player who owns this tile.
     */
    @Override
    public int getPlayer() {
        return player;
    }

    /**
     * checks if two tiles have the same axis values.
     * @param o object to confront with, if it is not a HexTile return false
     * @return true if the two tiles have the same axis values or
     * if it is the same instance, false otherwise
     */
    @Override
    public boolean equalsAxis(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof HexTile))
            return false;
        HexTile hTile = (HexTile)o;
        return hTile.x == x &&
                hTile.y == y &&
                hTile.z == z;
    }

    /**
     * checks if this tile has the specified axis values.
     * @param x axis X of the tile to check
     * @param y axis Y of the tile to check
     * @param z axis Z of the tile to check
     * @return true if this tile has the same axis values,
     * false otherwise
     */
    @Override
    public boolean equalsAxis(int x, int y, int z) {
        return this.x == x &&
                this.y == y &&
                this.z == z;
    }

    /**
     * checks if two tiles have all the same values (axis and player).
     * @param o object to confront with, if it is not a HexTile return false
     * @return true if the two tiles have the same axis and player values or
     * if it is the same instance, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof HexTile))
            return false;
        HexTile hTile = (HexTile)o;
        return hTile.x == x &&
                hTile.y == y &&
                hTile.z == z &&
                hTile.player == player;
    }

    /**
     * whenever equals is overridden, you should also override this
     * method.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int result = (31 * 17) + x;
        result = (31 * result) + y;
        result = (31 * result) + z;
        return result;
    }

    /**
     * gets the index of this tile.
     * @return index of this tile
     */
    @Override
    public int getIndex() {
        return index;
    }
}
