package View;

import javax.swing.*;

/**
 * represents a tile of the gameboard, in the view of the application.
 */
public final class HexButton extends JButton {
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
    // private final String id;

    HexButton(int x, int y, int z, ImageIcon icon) {
        super(icon);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * gets the x axis value of the tile.
     * @return x axis value
     */
    public int getXAxis() { return x; }

    /**
     * gets the y axis value of the tile.
     * @return y axis value
     */
    public int getYAxis() { return y; }

    /**
     * gets the z axis value of the tile.
     * @return z axis value
     */
    public int getZAxis() { return z; }

    // public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof HexButton))
            return false;
        HexButton hexBtn = (HexButton)o;
        return hexBtn.x == x &&
                hexBtn.y == y &&
                hexBtn.z == z;
    }

    @Override
    public int hashCode() {
        int result = (31 * 17) + x;
        result = (31 * result) + y;
        result = (31 * result) + z;
        return result;
    }
}
