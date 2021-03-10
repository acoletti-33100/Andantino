package Model.TranspositionTables;

import Model.Tile;

/**
 * Andantino data entry for the transposition table using
 * Zobrist key.
 */
public final class AndantinoEntryInfo implements EntryInfo {
    /**
     * hash key of a position.
     */
    private long key;
    /**
     * type of the node, determines the type of score.
     * It assumes the following values:
     * <ul>
     * <li>true -> PV- (exact value)</li>
     * <li>false -> Cut- (lower bounded)</li>
     * <li>null -> All-nodes (upper bounded)</li>
     * </ul>
     * @see <a href="https://www.chessprogramming.org/Node_Types">node types explanation</a>
     */
    private Boolean nodeType;
    /**
     * determines the depth of the investigation of a previously found
     * position.
     */
    private int depth;
    /**
     * value of the best move in the position (used to determine the
     * value of a position).
     */
    private int score;

    /**
     * best move of for the position (used for move ordering).
     */
    private Tile bestMove;

    /**
     * Construct an entry for the Andantino transposition table.
     * @param key hash key of the table
     * @param nodeType type of the node
     * @param depth search depth where the node has been found
     * @param score score associated with a node
     * @param bestMove best move for the current node
     */
    public AndantinoEntryInfo(long key, Boolean nodeType, int depth, int score, Tile bestMove) {
        this.key = key;
        this.nodeType = nodeType;
        this.depth = depth;
        this.score = score;
        this.bestMove = bestMove;
    }

    /**
     * gets the score of this entry.
     * @return score of the entry
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * sets the score of this entry.
     * @param score new value of score
     */
    @Override
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * checks if the position's score is an exact value.
     * @return true if it is an exact value, false otherwise
     */
    @Override
    public boolean isExactValue() {
        return nodeType == true;
    }

    /**
     * sets the node's type of this entry.
     * @param nodeType new value of nodeType
     */
    @Override
    public void setNodeType(Boolean nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * gets the investigation depth of the node.
     * @return depth of investigation of the node
     */
    @Override
    public int getDepth() {
        return depth;
    }

    /**
     * sets the depth of this entry.
     * @param depth new value of depth
     */
    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * gets the best move of this entry.
     * @return tile, which represents the best move
     */
    @Override
    public Tile getBestMove() {
        return bestMove;
    }

    /**
     * sets the best move of this entry.
     * @param move new value of bestMove
     */
    @Override
    public void setBestMove(Tile move) {
        bestMove = move;
    }

    /**
     * checks if the position's score is a lower bound.
     * @return true if it is a lower bound, false otherwise
     */
    @Override
    public boolean isLowerBound() {
        return nodeType == false;
    }

    /**
     * checks if the position's score is an upper bound.
     * @return true if it is an upper bound, false otherwise
     */
    @Override
    public boolean isUpperBound() {
        return nodeType == null;
    }

    /**
     * gets the key of the entry.
     * @return entry's key
     */
    @Override
    public long getKey() {
        return key;
    }
}
