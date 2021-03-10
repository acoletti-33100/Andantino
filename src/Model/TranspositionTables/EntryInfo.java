package Model.TranspositionTables;

import Model.Tile;

/**
 * data entry for the transposition table using a Zobrist key.
 */
public interface EntryInfo {
    long getKey();
    int getScore();
    void setScore(int score);
    int getDepth();
    void setDepth(int depth);
    Tile getBestMove();
    void setBestMove(Tile move);
    boolean isExactValue();
    boolean isLowerBound();
    boolean isUpperBound();
    void setNodeType(Boolean nodeType);
}
