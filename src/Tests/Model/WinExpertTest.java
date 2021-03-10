package Tests.Model;

import Model.Expert.Expert;
import Model.Expert.WinExpert;
import Model.HexTile;
import Model.Tile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WinExpertTest {

    @BeforeAll
    static void setUp() {
        System.out.println("### tests Model - win expert ###");
    }


    @Test
    @DisplayName("result win")
    public void getWeightWin() {
        List<Tile> mock = new ArrayList<>();
        Expert winExpert = new WinExpert();
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
        assertEquals(0, winExpert.getWeight(mock),
                "evaluate gameboard, expects win");
    }


    @Test
    @DisplayName("result = 0")
    public void getWeight() {
        List<Tile> mock = new ArrayList<>();
        Expert winExpert = new WinExpert();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(-1, 0, 1, 1));
        mock.add(new HexTile(0, -1, 1, 0));
        mock.add(new HexTile(1, -1, 0, 1));
        mock.add(new HexTile(-1, -1, 2, 0));
        mock.add(new HexTile(0, -2, 2, 1));
        mock.add(new HexTile(-1, 1, 0, 0));
        mock.add(new HexTile(-2, 1, 1, 1));
        mock.add(new HexTile(-2, 2, 0, 0));
        assertEquals(0, winExpert.getWeight(mock),"evaluate gameboard, expects 0");
    }

    @AfterAll
    static void tear() {
        System.out.println("### executed all ###");
    }
}