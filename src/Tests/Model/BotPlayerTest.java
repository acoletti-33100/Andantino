package Tests.Model;

import Model.AndantinoGameBoard;
import Model.BotPlayer;
import Model.HexTile;
import Model.SearchGameTree.AndantinoNode;
import Model.SearchGameTree.Node;
import Model.Tile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;


public class BotPlayerTest {
    BotPlayer player = new BotPlayer(0);
    BotPlayer player1 = new BotPlayer(1);

    @BeforeAll
    static void setUp() {
        System.out.println("### tests Model - bot player ###");
    }

    @Test
    public void move4() {
        List<Tile> mock = new ArrayList<>();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(0, -1, 1, 1));
        mock.add(new HexTile(1, -1, 0, 0));
        mock.add(new HexTile(1, -2, 1, 1));
        mock.add(new HexTile(-1, 0, 1, 0));
        Tile result = player.move(mock);
        boolean legal = false;
        List<Tile> legalMoves = AndantinoGameBoard.getAllLegalMoves(
                mock, mock.get(mock.size() - 1).getPlayer() ^ 1);
        if(legalMoves.stream().anyMatch(tile -> tile.equals(result)))
            legal = true;
        System.out.println("### gameboard ###");
        for(Tile t : mock) {
            System.out.println(t.getIndex());
        }
        System.out.println("### new move ###");
        System.out.println("index: " + result.getIndex());
        assertEquals(true,legal,"check if result is a legal move");
    }


    @Test
    public void move3() {
        List<Tile> mock = new ArrayList<>();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(1, 0, -1, 1));
        mock.add(new HexTile(1, -1, 0, 0));
        Tile result = player.move(mock);
        boolean legal = false;
        List<Tile> legalMoves = AndantinoGameBoard.getAllLegalMoves(
                mock, mock.get(mock.size() - 1).getPlayer() ^ 1);
        if(legalMoves.stream().anyMatch(tile -> tile.equals(result)))
            legal = true;
        System.out.println("### gameboard ###");
        for(Tile t : mock) {
            System.out.println(t.getIndex());
        }
        System.out.println("### new move ###");
        System.out.println("index: " + result.getIndex());
        assertEquals(true,legal,"check if result is a legal move");
    }


    @Test
    public void move() {
        List<Tile> mock = new ArrayList<>();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(-1, 1, 0, 1));
        mock.add(new HexTile(-1, 0, 1, 0));
        mock.add(new HexTile(-2, 1, 1, 1));
        mock.add(new HexTile(-2, 2, 0, 0));
        Tile result = player.move(mock);
        boolean legal = false;
        List<Tile> legalMoves = AndantinoGameBoard.getAllLegalMoves(
                mock, mock.get(mock.size() - 1).getPlayer() ^ 1);
        if(legalMoves.stream().anyMatch(tile -> tile.equals(result)))
            legal = true;
        System.out.println("### gameboard ###");
        for(Tile t : mock) {
            System.out.println(t.getIndex());
        }
        System.out.println("### new move ###");
        System.out.println("index: " + result.getIndex());
        assertEquals(1, result.getPlayer(), "check if move has the right player");
        assertEquals(true,legal,"check if result is a legal move");
    }


    @Test
    @DisplayName("ten turns simulation")
    public void simulation() {
        List<Tile> mock = new ArrayList<>();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(-1, 0, 1, 1));
        mock.add(new HexTile(0, -1, 1, 0));
        mock.add(new HexTile(1, -1, 0, 1));
        mock.add(new HexTile(-1, -1, 2, 0));
        mock.add(new HexTile(0, -2, 2, 1));
        mock.add(new HexTile(-1, 1, 0, 0));
        mock.add(new HexTile(-2, 1, 1, 1));
        mock.add(new HexTile(-2, 2, 0, 0));

        Tile result;
        for(int counter = 0; counter < 10; counter ++) {
            if(mock.get(mock.size() - 1).getPlayer() == 0)
                result = player1.move(mock);
            else
                result = player.move(mock);
            List<Tile> legalMoves = AndantinoGameBoard.getAllLegalMoves(
                    mock, mock.get(mock.size() - 1).getPlayer() ^ 1);
            Tile finalResult = result;
            if(!legalMoves.stream().anyMatch(tile -> tile.equals(finalResult))) {
                System.out.println("move not legal -> break");
                assertFalse(true);
            }

            mock.add(result);
            System.out.println("### Begin gameboard ###");
            for(Tile t : mock) {
                System.out.println(t.getIndex());
            }
            System.out.println("### End gameboard ###");
            System.out.println("");
        }
    }

    /*
    /**
     * to test it you must uncomment the methods, from BotPlayer:
     * {@link Model.BotPlayer#testSearch(Node)}
     * {@link Model.BotPlayer#plainAlphaBeta(Node, int, int, int)}
     * {@link Model.BotPlayer#searchITPlain(Node, int)}
     */
    /*
    @Test
    @DisplayName("compares alpha beta enhancements")
    public void alphaBetaTest() {
        List<Tile> mock = new ArrayList<>();
        mock.add(new HexTile(0, 0, 0, 0));
        mock.add(new HexTile(-1, 0, 1, 1));
        mock.add(new HexTile(0, -1, 1, 0));
        mock.add(new HexTile(1, -1, 0, 1));
        mock.add(new HexTile(-1, -1, 2, 0));
        mock.add(new HexTile(0, -2, 2, 1));
        mock.add(new HexTile(-1, 1, 0, 0));
        mock.add(new HexTile(-2, 1, 1, 1));
        mock.add(new HexTile(-2, 2, 0, 0));

        // depth to search
        int depth = 6;

        // plain alpha beta search (PVS), no TT, no iterative deepening
        long start = System.currentTimeMillis();
        Node result1 = player1.plainAlphaBeta(new AndantinoNode(mock, null),
                depth,-Integer.MAX_VALUE, Integer.MAX_VALUE);
        long end = System.currentTimeMillis() - start;
        System.out.println("PVS time - : " + end);

        // plain alpha beta search (PVS), iterative deepening, no TT
        start = System.currentTimeMillis();
        Node result2 = player.searchITPlain(new AndantinoNode(mock, null), depth);
        end = System.currentTimeMillis() - start;
        System.out.println("PVS, iterative deepening - time: " + end);

        // plain alpha beta search (PVS), iterative deepening, TT
        start = System.currentTimeMillis();
        Node result3 = player.testSearch(new AndantinoNode(mock, null));
        end = System.currentTimeMillis() - start;
        System.out.println("PVS, iterative deepening, TT - time: " + end);
    }
     */

    @AfterAll
    static void tear() {
        System.out.println("### executed all ###");
    }
}