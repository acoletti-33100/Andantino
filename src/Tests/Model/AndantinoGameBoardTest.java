package Tests.Model;

import Model.AndantinoGameBoard;
import Model.HexTile;
import Model.SearchGameTree.AndantinoNode;
import Model.SearchGameTree.Node;
import Model.Tile;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AndantinoGameBoardTest {

    Node n;

    @BeforeAll
    static void setUp() {
        System.out.println("### tests Model - bot GameBoard ###");
    }

    @Disabled
    @Test
    @DisplayName("test - generate all legal moves")
    void getSecondTurnMoves() {
        List<Tile> gameBoard = new ArrayList<>();
        gameBoard.add(new HexTile(0, 0, 0, 0));
        gameBoard.add(new HexTile(1, 0, -1, 1));
        n = new AndantinoNode(gameBoard,null);
        n.generateSuccessors();
        List<Node> children = n.getChildren();
        List<Tile> result = new ArrayList<>();
        for(Node child : children) {
            result.add(child.getCurrentGameboard().get(child.getGameBoardSize() - 1));
        }
        // expected
        // 0,1,-1: x,y,z; color=0;
        // 1,-1,0: x,y,z; color=0;
        for(Tile t : result) {
            System.out.println("###");
            System.out.println("x: " + t.getX());
            System.out.println("y: " + t.getY());
            System.out.println("z: " + t.getZ());
            System.out.println("color: " + t.getPlayer());
            System.out.println("###");
        }
        assertEquals(2,result.size(), "number of tiles");
    }


    @Disabled
    @Test
    @DisplayName("test - generate all legal moves")
    void getThirdTurnMoves() {
        System.out.println("#################################");
        System.out.println("third turn moves");
        List<Tile> gameBoard = new ArrayList<>();
        gameBoard.add(new HexTile(0, 0, 0, 0));
        gameBoard.add(new HexTile(1, 0, -1, 1));
        gameBoard.add(new HexTile(0, 1, -1, 0));
        n = new AndantinoNode(gameBoard,null);
        n.generateSuccessors();
        List<Node> children = n.getChildren();
        List<Tile> result = new ArrayList<>();
        for(Node child : children) {
            result.add(child.getCurrentGameboard().get(child.getGameBoardSize() - 1));
        }
        // expected
        // 1,-1,0: x,y,z; color=1;
        // -1,1,0: x,y,z; color=1;
        // 1,1,-2: x,y,z; color=1;
        System.out.println("number of moves: " + result.size());
        for(Tile t : result) {
            System.out.println("###");
            System.out.println("x: " + t.getX());
            System.out.println("y: " + t.getY());
            System.out.println("z: " + t.getZ());
            System.out.println("color: " + t.getPlayer());
            System.out.println("###");
        }
        System.out.println("#################################");
        assertEquals(3,result.size(), "number of tiles");
    }

    @Test
    @DisplayName("test - generate move")
    void getMove2() {
        List<Tile> gameBoard = new ArrayList<>();

        gameBoard.add(new HexTile(0, 0, 0, 0));
        gameBoard.add(new HexTile(-1, 0, 1, 1));
        gameBoard.add(new HexTile(0, -1, 1, 0));
        gameBoard.add(new HexTile(1, -1, 0, 1));
        gameBoard.add(new HexTile(-1, -1, 2, 0));
        gameBoard.add(new HexTile(0, -2, 2, 1));
        gameBoard.add(new HexTile(-1, 1, 0, 0));
        gameBoard.add(new HexTile(-2, 1, 1, 1));
        gameBoard.add(new HexTile(-2, 2, 0, 0));

        List<Tile> result = AndantinoGameBoard.getAllLegalMoves(gameBoard, 1);
        List<Integer> indexes = new ArrayList<>();
        indexes.add(35);
        indexes.add(7);
        indexes.add(21);
        indexes.add(10);
        indexes.add(4);
        indexes.add(5);
        indexes.add(16);
        assertEquals(indexes.size(), result.size(), "must be the same size");
        for (Tile t : result) {
            if(indexes.stream().noneMatch(index -> index == t.getIndex())) {
                fail();
            }
        }
        // to get the indexes
        // for(Tile t : gameBoard)
           // System.out.println("input: " + t.getIndex());
    }



    @Test
    @DisplayName("test - generate first move")
    void getFirstMove() {
        List<Tile> gameBoard = new ArrayList<>();
        gameBoard.add(new HexTile(0, 0, 0, 0));
        List<Tile> result = AndantinoGameBoard.getAllLegalMoves(gameBoard, 1);
        System.out.println("###");
        System.out.println("x: " + result.get(0).getX());
        System.out.println("y: " + result.get(0).getY());
        System.out.println("z: " + result.get(0).getZ());
        System.out.println("index: " + result.get(0).getIndex());
        System.out.println("color: " + result.get(0).getPlayer());
        System.out.println("###");
        assertEquals(0,result.get(0).getX() +
                                    result.get(0).getY() +
                                    result.get(0).getZ(), "sum equals zero");
    }

    @AfterAll
    static void tear() {
        System.out.println("### executed all ###");
    }
}