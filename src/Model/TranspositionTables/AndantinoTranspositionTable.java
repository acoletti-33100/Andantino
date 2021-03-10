package Model.TranspositionTables;

import Model.SearchGameTree.Node;
import Model.Tile;

import java.util.*;

/**
 * Andantino transposition table using Zobrist key as a
 * hashing algorithm.
 * @see <a href="https://www.chessprogramming.org/Zobrist_Hashing">Zobrist hashing</a>
 */
public final class AndantinoTranspositionTable implements TranspositionTable {
    /**
     * transposition table, with table[i][j] where:
     * <ul>
     *     <li>i : index for the type of pawn (black = 0, white = 1)</li>
     *     <li>j : hash of the xor of all tiles of a specific gameboard
     *     configuration.</li>
     * </ul>
     */
    private EntryInfo table [][];

    /**
     * constructs a transposition table. table is initialized with
     * 272 random numbers for the key of each value.
     * This is because in Andantino we only have one type of piece,
     * 271 positions, and two types of players. Therefore I need a table
     * with 272 * 2 elements. This means 2^10 elements, therefore 54 bits are
     * used for the hash key while 10 bits are used for the primary hash code.
     * Normally, Zobrist uses table[piece][location]. The index "location"
     * is obtained thanks to the modulus operator between the Zobrist key
     * and the table size (136).
     * @see <a href="https://www.chessprogramming.org/Zobrist_Hashing#Initialization">Zobrist hashing</a>
     */
    public AndantinoTranspositionTable() {
        SplittableRandom random = new SplittableRandom();
        table = new EntryInfo[2][272];
        LinkedList<Long> tmpKeys = new LinkedList<>();
        // init keys in the table with random 64-bits numbers
        for (int piece = 0; piece < 2; piece++) {
            for (int location = 0; location < 272; location++) {
                                tmpKeys.add(random.nextLong(Long.MAX_VALUE));
                //table[piece][hash(tmpKeys.getLast())] = new AndantinoEntryInfo
                table[piece][location] = new AndantinoEntryInfo
                                    (tmpKeys.getLast(), null, -1, 0, null);
            }
        }
    }

    /**
     * determines the index of a position for the specified Zobrist key.
     * @param zobristKey key to determine the index in the transposition table
     * @return index of the table where the position is stored
     */
    private int hash(long zobristKey) {
        return (int)(zobristKey % 272);
    }

    /**
     * helper function to store entries in the table.
     * It stores in the table with index [color][X], where 'X'
     * is the index of the last position placed on the gameboard
     * (last element in the gameboard list).
     * It assumes a gameboard with at least two elements.
     * @param position position to store in table
     * @param nodeType type of the node
     * @param color index of the player to use
     * @param depth depth at which the node was found
     */
    private void storeHelper(Node position, Boolean nodeType, int color, int depth) {
        int index =  hash(hashConfiguration(position.getCurrentGameboard(), color));
        table[color][index].setScore(position.getScore());
        table[color][index].setNodeType(nodeType);
        table[color][index].setBestMove(position.getPosition());
        table[color][index].setDepth(depth);
    }

    /**
     * determines the hash for the configuration associated with gameboard.
     * It does so by xoring all elements in gameboard.
     * It assumes a gameboard with at least two elements.
     * @param gameBoard gameboard representation
     * @param color index of the player (0: black, 1: white)
     * @return hashkey of all elements in gameboard except for the last one
     */
    private long hashConfiguration(List<Tile> gameBoard, int color) {
            // iterate from the second last to the first element and xor their value
            ListIterator<Tile> it = gameBoard.listIterator(gameBoard.size() - 1);
            long res = table[color][it.previous().getIndex()].getKey();
                while (it.hasPrevious()) {
                    res ^= table[color][it.previous().getIndex()].getKey();
                }
                return res;
    }

    /**
     * retrieves the entry with the best move, associated with the
     * current configuration, from the table.
     * @param position position associated with the table, where to find the value
     * @param depth depth of the position associated with index
     * @param color indicates the owner of the position (0: player2, 1: player1).
     * @return entry of the table with the specified index
     */
    @Override
    public EntryInfo retrieve(Node position, int depth, int color) {
        int index =  hash(hashConfiguration(position.getCurrentGameboard(), color));
        return table[color][index];
    }

    /**
     * stores a position in the table. It stores the last tile
     * in position in the table. In the entry associated with the
     * index with the second last element in position.
     * In case of collision, it always replaces.
     * @param position position to store in table
     * @param depth depth at which the node was found
     * @param nodeType type of the node
     */
    @Override
    public void store(Node position, int depth, Boolean nodeType) {
        if(position.getGameBoardSize() >= 2) {
            storeHelper(position, nodeType, position.getBeforeLastPlayer(), depth);
        }
    }
}
