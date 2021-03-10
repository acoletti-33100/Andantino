package Tests.Model;

import Model.HexTile;
import Model.SearchGameTree.AndantinoNode;
import Model.SearchGameTree.Node;
import Model.Tile;
import Model.TranspositionTables.AndantinoTranspositionTable;
import Model.TranspositionTables.EntryInfo;
import Model.TranspositionTables.TranspositionTable;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AndantinoTranspositionTableTest {

    TranspositionTable table;
    List<Tile> tiles;
    Tile last;
    Node node;
    Node n;

    public AndantinoTranspositionTableTest() {
        table = new AndantinoTranspositionTable();
        tiles = new ArrayList<>();
        tiles.add(new HexTile(0, 0, 0, 0));
        tiles.add(new HexTile(-1, 1, 0, 1));
        tiles.add(new HexTile(-1, 0, 1, 0));
        tiles.add(new HexTile(-2, 2, 0, 0));
        last = new HexTile(-2, 1, 1, 1);
        n = new AndantinoNode(new ArrayList<>(tiles),null);
        tiles.add(last);
        node = new AndantinoNode(tiles, n);
    }

    @BeforeAll
    static void setUp() {
        System.out.println("### tests Model - transposition table ###");
    }

    @Test
    @DisplayName("store in TT")
    public void store() {
        table.store(node,5,null);
        EntryInfo entry = table.retrieve(n, 5, n.getLastPlayer());
        assertEquals(node.getBeforeLastIndex(),n.getLastIndex(), "the last tile in" +
                "the gameboard in 'node', it's the best move for 'n'");
    }

    @AfterAll
    static void tear() {
        System.out.println("### executed all ###");
    }
}