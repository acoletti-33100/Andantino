package Tests.Model;

import Model.Expert.BridgeExpert;
import Model.Expert.Expert;
import Model.HexTile;
import Model.Tile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BridgeExpertTest {

    @BeforeAll
    static void setUp() {
        System.out.println("### tests Model - bridge expert ###");
    }

    @Test
    void getWeight() {
        List<Tile> mock = new ArrayList<>();
        Expert bridge = new BridgeExpert();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(-1, 0, 1, 1));
        mock.add(new HexTile(0, -1, 1, 0));
        mock.add(new HexTile(1, -1, 0, 1));
        mock.add(new HexTile(-1, -1, 2, 0));
        mock.add(new HexTile(0, -2, 2, 1));
        mock.add(new HexTile(-1, 1, 0, 0));
        mock.add(new HexTile(-2, 1, 1, 1));
        mock.add(new HexTile(-2, 2, 0, 0));
        mock.add(new HexTile(-3, 2, 1, 1));
        System.out.println("### gameboard ###");
        for(Tile t : mock) {
            System.out.println(t.getIndex());
        }
        assertEquals(4, bridge.getWeight(mock),
                "evaluate gameboard");
    }

    @AfterAll
    static void tear() {
        System.out.println("### executed all ###");
    }
}