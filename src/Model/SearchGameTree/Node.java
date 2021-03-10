package Model.SearchGameTree;

import Model.Tile;

import java.util.List;

/**
 * node of a game. Represents a particular game configuration
 * of the gameboard.
 */
public interface Node {
    Node getParent();
    int getScore();
    List getChildren();
    Tile getPosition();
    List<Tile> getCurrentGameboard();
    boolean isRoot();
    boolean isParentRoot();
    boolean isLeaf();
    void setScore(int score);
    boolean generateSuccessors();
    int getNumberOfChildren();
    int getLastIndex();
    int getLastPlayer();
    int getBeforeLastIndex() throws IndexOutOfBoundsException;
    int getBeforeLastPlayer() throws IndexOutOfBoundsException;
    int getGameBoardSize();
}
