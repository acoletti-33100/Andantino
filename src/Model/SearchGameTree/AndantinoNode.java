package Model.SearchGameTree;

import Model.AndantinoGameBoard;
import Model.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Node in a search tree for the Andantino game. A node
 * corresponds to a configuration in the gameboard and must
 * contain at least one tile.
 */
public final class AndantinoNode implements Node {
    /**
     * score of the node.
     */
    private int score;
    /**
     * parent of this node, the root has parent equal to null.
     */
    private final Node parent;
    /**
    * gameboard's configuration.
    */
    private final List<Tile> gameBoard;
    /**
    * children of the node.
    */
    private List<Node> children;

    /**
     * Constructs an Andantino node with an empty list of children and a
     * score equal to zero.
     * @param gameBoard tiles in the gameboard representation
     * @param parent parent of this node
     */
    public AndantinoNode(List<Tile> gameBoard, Node parent) {
        score = 0;
        this.parent = parent;
        this.gameBoard = gameBoard;
        children = new LinkedList<>();
    }


    /**
     * Constructs an Andantino node with an empty list of children and a
     * score equal to zero and adds to gameboard the tile passed
     * as parameter.
     * @param parent parent of this node
     * @param position tile to add to the gameboard
     */
    public AndantinoNode(Node parent, Tile position) {
        score = 0;
        this.parent = parent;
        this.gameBoard = parent.getCurrentGameboard();
        gameBoard.add(position);
        children = new LinkedList<>();
    }

    @Override
    public int getScore() {
        return score;
    }

    /**
     * sets the score of this node.
     * @param score new score for this node
     */
    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public List getChildren() {
        return children;
    }

     /**
     * generates all successors for this node.
      * Note that the successors will have tiles owned
      * by the opposite player.
      * Then, it adds all successor to the children of this node.
      * @return true if this node has children, false otherwise
      */
     @Override
    public boolean generateSuccessors() {
         //if(children.size() > 0 && gameBoard.size() < 271)
           //  return true; // children already exists, there are legal moves
         if(gameBoard.size() >= 271) // no more children
             return false; // full gameboard, no more legal moves

         children.clear();

         List<Tile> childrenPositions = AndantinoGameBoard.getAllLegalMoves(
                 gameBoard, gameBoard.get(gameBoard.size() - 1).getPlayer() ^ 1);

         for (Tile childPosition : childrenPositions) {
             List<Tile> tmp = new ArrayList<>(gameBoard);
             tmp.add(tmp.size(), childPosition);
             children.add(new AndantinoNode(tmp,this));
         }
         return true;
    }

    /**
     * determines the number of children of this node.
     * @return number of children of this node
     */
    @Override
    public int getNumberOfChildren() {
        return children.size();
    }

    /**
     * gets the index of the last placed tile in the gameboard.
     * @return index of the last placed tile in the gameboard configuration
     */
    @Override
    public int getLastIndex() {
        return gameBoard.get(gameBoard.size() - 1).getIndex();
    }

    /**
     * gets the player of the last placed tile in the gameboard.
     * @return true corresponds to player 1, false corresponds to
     * player 2
     */
    @Override
    public int getLastPlayer() {
        return gameBoard.get(gameBoard.size() - 1).getPlayer();
    }

    /**
     * gets the index of the tile placed before the last in this
     * node gameboard.
     * @return index of the second last tile
     * @throws IndexOutOfBoundsException if the node contains only one element
     * in the gameboard
     */
    @Override
    public int getBeforeLastIndex() throws IndexOutOfBoundsException {
        if (gameBoard.size() >= 2)
            return gameBoard.get(gameBoard.size() - 2).getIndex();
        else
            throw new IndexOutOfBoundsException();
    }

    /**
     * gets the player of the tile placed before the last in this
     * node gameboard.
     * @return player of the second last tile
     * @throws IndexOutOfBoundsException if the node contains only one element
     * in the gameboard
     */
    @Override
    public int getBeforeLastPlayer() throws IndexOutOfBoundsException {
        if (gameBoard.size() >= 2)
            return gameBoard.get(gameBoard.size() - 2).getPlayer();
        else
            throw new IndexOutOfBoundsException();
    }

    /**
     * gets the number of tiles of this gameboard configuration,
     * inside this node.
     * @return size of gameboard
     */
    @Override
    public int getGameBoardSize() {
        return gameBoard.size();
    }

    /**
     * gets the last position of the gameboard.
     * @return last tile placed in the gameboard
     */
    @Override
    public final Tile getPosition() {
        return gameBoard.get(gameBoard.size() - 1);
    }

    /**
     * returns this node gameboard configuration.
     * @return this node gameboard
     */
    @Override
    public List<Tile> getCurrentGameboard() {
       return gameBoard;
    }

    /**
     * determines if this node is the root. The root has
     * parent equal to null.
     * @return true if this node is the root, false otherwise
     */
    @Override
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * tries to reach the node at the first ply by going by to the parent
     * iteratively.
     * It makes sure this node is not the root before checking.
     * @return false if this node is the root, true otherwise
     */
    @Override
    public boolean isParentRoot() {
        Node next = this;
        while (!next.isRoot()) {
            if(next.getParent().isRoot())
                return true;
            next = next.getParent();
        }
        return false;
    }

    /**
     * checks if this node is a leaf.
     * @return true if it has no children, false otherwise
     */
    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }
}
