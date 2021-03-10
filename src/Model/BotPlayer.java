package Model;
import Model.Expert.*;
import Model.SearchGameTree.AndantinoNode;
import Model.SearchGameTree.Node;
import Model.TranspositionTables.AndantinoTranspositionTable;
import Model.TranspositionTables.EntryInfo;
import Model.TranspositionTables.TranspositionTable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * represents the AI player in the game.
 * To find the next move it implements a search in a NegaMax
 * framework, combined with a transposition table. It also
 * tries to take advantage of the Iterative deepening and
 * minimal window search.
 */
public final class BotPlayer implements Player {
    /**
     * color of the player.
     */
    private final int color;
    /**
     * adds random factor to the evaluation.
     */
    private Random randomExpert;

    /**
     * list of experts to evaluates features of
     * a terminal position.
     */
    private final List<Expert> experts;

    /**
     * transposition table.
     */
    private TranspositionTable table;

    /**
     * Constructs a bot player.
     *
     * @param color color of the player (0 is black, 1 is white)
     */
    public BotPlayer(int color) {
        this.color = color;
        randomExpert = new Random();
        experts = new ArrayList<>();
        experts.add(new WinExpert());
        experts.add(new LossExpert());
        experts.add(new RandomExpert());
        experts.add(new BridgeExpert());
        table = new AndantinoTranspositionTable();
    }

    /**
     * evaluates a gameboard. To do so, it iterates over its
     * experts and returns the sum of the evaluation of the
     * experts method {@link Model.Expert.Expert#getWeight(List)}.
     *
     * @return sum of the evaluation of all the experts or
     * 0 if gameboard is empty
     */
    private int evaluate(List<Tile> gameBoard) {
        if (gameBoard.size() == 0)
            return 0;
        int res = 0;
        for (Expert expert : experts)
            res += expert.getWeight(gameBoard);
        return res;
    }

    /**
     * iterative deepening search.
     * It iterates two at a time to avoid the Odd/Even effect.
     *
     * @param position root for the search tree
     * @return most promising node which represents the next move
     */
    private Node iterativeDeepeningSearch(Node position) {
        Node result = null;
        int alpha = Integer.MAX_VALUE, beta = Integer.MAX_VALUE;
        long time = 0;
        for (int depth = 0; time < 400; depth += 2) {
            long begin = System.currentTimeMillis();
            result = alphaBetaSearch(position, depth, -alpha, beta);
            long end = System.currentTimeMillis();
            time += (end - begin);
        }
        return result;
    }

    /**
     * stores in the transposition table the best value for this
     * position.
     *
     * @param best     best value found
     * @param oldAlpha previous alpha
     * @param beta     upper bound
     * @param depth    depth at which the node was found
     * @return best node for the position
     */
    private Node onDoneSearch(Node best, int oldAlpha, int beta, int depth) {
        Boolean nodeType;
        if (best.getScore() < oldAlpha)
            nodeType = null;
        else if (best.getScore() >= beta)
            nodeType = false;
        else
            nodeType = true;
        table.store(best, depth, nodeType);
        return best;
    }

    /**
     * alpha-beta search algorithm in a NegaMax framework.
     * The first thing to do is check if a best move already exists in the
     * transposition table for this node. If it does, then update the node
     * and return it, else keep searching.
     * Also, this method does not return an int. Therefore, when
     * you need to negate the values, you have to remember to
     * negate the score of the position passed as a parameter in the
     * recursion call.
     * When a cut-off happens, it invokes the method {@link #onDoneSearch(Node, int, int, int)}.
     *
     * @param node  current node to investigate
     * @param depth depth to be searched
     * @param alpha lower bound of the window
     * @param beta  upper bound of the window
     * @return next move to make
     */
    private Node alphaBetaSearch(Node node, int depth, int alpha, int beta) {
        int oldAlpha = alpha; // saves the old alpha value
        EntryInfo entry = table.retrieve(node,
                depth, node.getPosition().getPlayer());
        if (entry != null && entry.getDepth() >= depth) {
            if (entry.isExactValue())
                return new AndantinoNode(node, entry.getBestMove());
            if (entry.isLowerBound())
                alpha = Integer.max(alpha, entry.getScore());
            else // score in entry is an upper bound
                beta = Integer.max(beta, entry.getScore());
            if (alpha >= beta)
                return new AndantinoNode(node, entry.getBestMove());
        }

        if (depth == 0) { // reached a leaf (horizon)
            node.setScore(evaluate(node.getCurrentGameboard()));
            return node;
        }
        // PVS/NegaScout
        Node result = null;
        if (node.generateSuccessors()) { // false => full gameboard
            List<Node> children = node.getChildren();

            ListIterator<Node> child = children.listIterator(); // there is at least one child
            result = alphaBetaSearch(child.next(), depth - 1, -beta, -alpha);
            result.setScore(-result.getScore());
            if (result.getScore() < beta) {
                while (child.hasNext()) {
                    int lowerBound = Integer.max(result.getScore(), alpha); // fail-soft condition
                    int upperBound = lowerBound + 1;
                    // move iterator one position to the right
                    result = alphaBetaSearch(child.next(), depth - 1,
                            -upperBound, -lowerBound);
                    result.setScore(-result.getScore());
                    Node value = null;
                    boolean found = false; // flag to check if value is initialized
                    // if result is no good => research
                    if (upperBound < result.getScore() && result.getScore() < beta
                            && depth > 2) {
                        // move iterator one position to the left
                        value = alphaBetaSearch(child.previous(), depth - 1, -beta,
                                -result.getScore());
                        value.setScore(-value.getScore());
                        found = true;
                    }
                    if (found && value.getScore() > result.getScore())
                        result = value;
                    if (result.getScore() >= beta)
                        return onDoneSearch(result, oldAlpha, beta, depth);
                }
            }
        }
        if (result == null)
            return node;
        return result;
    }
/*
    /**
     * Test method.
     * Iterative deepening with the
     * use of transposition table.
     */
/*
    public Node testSearch(Node node) {
        return iterativeDeepeningSearch(node);
    }

    /**
     * Test method.
     * Iterative deepening search without transposition table.
     */
/*
    public Node searchITPlain(Node position, int depth) {
        Node result = null;
        int alpha = Integer.MAX_VALUE, beta = Integer.MAX_VALUE;
        long time = 0;
        for (int i = 0; time < 400 && i <= depth; depth += 2) {
            long begin = System.currentTimeMillis();
            result = plainAlphaBeta(position, depth, -alpha, beta);
            long end = System.currentTimeMillis();
            time += (end - begin);
        }
        return result;
    }

    /**
     * Test method.
     * Plain alpha beta search without the use of transposition table.
     */
/*
    public Node plainAlphaBeta(Node node, int depth, int alpha, int beta) {
        int oldAlpha = alpha; // saves the old alpha value

        if (depth == 0) { // reached a leaf (horizon)
            node.setScore(evaluate(node.getCurrentGameboard()));
            return node;
        }
        // PVS/NegaScout
        Node result = null;
        if (node.generateSuccessors()) { // false => full gameboard
            List<Node> children = node.getChildren();

            ListIterator<Node> child = children.listIterator(); // there is at least one child
            result = plainAlphaBeta(child.next(), depth - 1, -beta, -alpha);
            result.setScore(-result.getScore());
            if (result.getScore() < beta) {
                while (child.hasNext()) {
                    int lowerBound = Integer.max(result.getScore(), alpha); // fail-soft condition
                    int upperBound = lowerBound + 1;
                    // move iterator one position to the right
                    result = plainAlphaBeta(child.next(), depth - 1,
                            -upperBound, -lowerBound);
                    result.setScore(-result.getScore());
                    Node value = null;
                    boolean found = false; // flag to check if value is initialized
                    // if result is no good => research
                    if (upperBound < result.getScore() && result.getScore() < beta
                            && depth > 2) {
                        // move iterator one position to the left
                        value = plainAlphaBeta(child.previous(), depth - 1, -beta,
                                -result.getScore());
                        value.setScore(-value.getScore());
                        found = true;
                    }
                    if (found && value.getScore() > result.getScore())
                        result = value;
                    if (result.getScore() >= beta)
                        return result;
                }
            }
        }
        if (result == null)
            return node;
        return result;
    }
    */

    /**
     * gets the child of the root of the search tree.
     * The child is on the principal variation of the
     * search tree.
     *
     * @param node interior node of the search tree
     * @return tile associated with the node at ply n+1,
     * where n is the ply of the root. The node is on the
     * principal variation
     */
    private Tile nextMove(Node node) {
        Node next = node;
        while (!next.isRoot()) {
            if (next.getParent().isRoot())
                return next.getPosition();
            next = next.getParent();
        }
        return null; // impossible a node must have a root
    }

    /**
     * gets the player's color.
     *
     * @return true if the players is white (player1), false
     * otherwise
     */
    @Override
    public int getColor() {
        return color;
    }

    /**
     * determines if this player is a bot.
     *
     * @return true
     */
    @Override
    public boolean isBot() {
        return true;
    }

    /**
     * makes a move.
     * This is the core part of this AI.
     * It uses an alpha-beta search in a NegaMax framework
     * to find the next move.
     *
     * @param gameBoard current gameboard configuration
     * @return last placed tile
     */
    @Override
    public Tile move(List<Tile> gameBoard) {
        if (gameBoard.size() == 1)
            return AndantinoGameBoard.generateFirstMove();
        Node res = iterativeDeepeningSearch(
                new AndantinoNode(gameBoard, null));
        return nextMove(res);
    }
}

