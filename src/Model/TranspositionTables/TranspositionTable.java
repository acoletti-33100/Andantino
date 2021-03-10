package Model.TranspositionTables;

import Model.SearchGameTree.Node;

/**
 * transposition table for a game.
 */
public interface TranspositionTable {
    EntryInfo retrieve(Node position, int depth, int color);
    void store(Node position, int depth, Boolean nodeType);
}
