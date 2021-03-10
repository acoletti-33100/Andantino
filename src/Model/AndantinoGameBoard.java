package Model;

import Model.Supervisor.GameSupervisor;
import Model.Supervisor.AndantinoSupervisor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * model of the application. Represents the gameboard of Andantino.
 */
public final class AndantinoGameBoard implements GameBoard {
    /**
    * tells which player's turn is.
    */
    private int playerTurn;

    /**
     * represents the index to identify the bot player.
     */
    private int botIndex;

    /**
     * flag to indicate whether a game has ended.
     */
    private boolean endGame;

    /**
     * flag to indicate if it is a game between a bot and human players.
     */
    private boolean botVsHumanGame;

    /**
     * flag value set to the winner of the game, default: null.
     */
    private int winner;

    /**
     * players of the game.
     */
    private Player[] players = new Player[2];

    /**
     * supervisor of the game.
     */
    private final GameSupervisor supervisor;
    /**
    * store the played tiles by the players. Does not store the blank tiles.
    * Always add elements to the end of the list to ease the undo of the turn.
    */
    private List<Tile> gameBoard;

    /**
    * constructs a gameboard with a list of tiles containing only the center.
     * Initializes the players to null.
    */
    public AndantinoGameBoard () {
        botVsHumanGame = false;
        botIndex = 0;
        playerTurn = 1;
        winner = -1;
        endGame = false;
        gameBoard = new ArrayList<>();
        gameBoard.add(new HexTile(0, 0, 0, 0)); // add center
        supervisor = new AndantinoSupervisor();
        players[0] = null;
        players[1] = null;
    }

    /**
     * determines if @tile is out of bounds. The gameboard is
     * assumed to be a 10x10 with 271 tiles.
     * @param x axis X of the tile to check
     * @param y axis Y of the tile to check
     * @param z axis Z of the tile to check
     * @return returns true if @tile is out of bounds, false
     * otherwise
     */
    private static boolean isOutOfBounds(int x, int y, int z) {
        if(
                x < -9 || x > 9 ||
                y < -9 || y > 9 ||
                z < -9 || z > 9
        )
            return true;
        return false;
    }


    /**
     * checks if tile is adjacent to the center (0,0,0).
     * @param tile tile to check if it's adjacent
     * @return true if @tile is adjacent to the center
     */
    private static boolean isAdjacentToCenter(Tile tile) {
            return ( // center coordinates: 0,0,0
                    Math.abs(tile.getX()) +
                            Math.abs(tile.getY()) +
                            Math.abs(tile.getZ())
            )
                    / 2 == 1;
    }

    /**
     * checks if a position already exists in the gameboard with
     * the specified axis values.
     * @param gameBoard gameboard to in which to check if the tile already exists
     * @param x axis X of the tile to check
     * @param y axis Y of the tile to check
     * @param z axis Z of the tile to check
     * @return true if the position already exists, false otherwise
     */
    private static boolean existsPosition(List<Tile> gameBoard, int x, int y, int z) {
        return gameBoard.stream()
                .anyMatch(tile ->
                        tile.getX() == x &&
                                tile.getY() == y &&
                                tile.getZ() == z);
    }

    /**
     * checks if tile is adjacent to at least two existing tiles in the boardgame.
     * @param gameBoard gameboard to check
     * @param x axis X of the tile to check
     * @param y axis Y of the tile to check
     * @param z axis Z of the tile to check
     * @return true if @tile is adjacent to at least two existing tiles (distance equals 1)
     */
    private static boolean isAdjacent(List<Tile> gameBoard, int x, int y, int z) {
        return gameBoard.stream()
                .filter(currTile ->
                        (
                                Math.abs(x - currTile.getX()) +
                                        Math.abs(y - currTile.getY()) +
                                        Math.abs(z - currTile.getZ())
                        ) / 2 == 1)
                .count() >= 2;
    }


    /**
     * checks if tile is adjacent to at least two existing tiles in the boardgame.
     * @param x axis X of the tile to check
     * @param y axis Y of the tile to check
     * @param z axis Z of the tile to check
    * @return true if @tile is adjacent to at least two existing tiles (distance equals 1)
    */
    private boolean isAdjacent(int x, int y, int z) {
             return gameBoard.stream()
                     .filter(currTile ->
                                     (
                                            Math.abs(x - currTile.getX()) +
                                            Math.abs(y - currTile.getY()) +
                                            Math.abs(z - currTile.getZ())
                                     ) / 2 == 1)
                     .count() >= 2;
    }

    /**
     * creates a tile with axis:
     * <ul>
     *     <li>x + y + z equals 0</li>
     *     <li>x,y,z are in [-1,1]</li>
     * </ul>
     * Note that, the first to move is always white. So,
     * the color of this tile is 1.
     * @return tile which is the first move of the game
     */
    public static Tile generateFirstMove() {
       Random generator = new Random();
       int counter = 0;
       while (counter < 200) {
           int x = generator.nextInt(3) - 1;
           int y = generator.nextInt(3) - 1;
           if((x + y) == -1)
               return new HexTile(x, y, 1, 1);
           counter++;
       }
        return new HexTile(-1, 1, 0, 1);
    }

    /**
     * creates a list of all the legal moves for the gameboard
     * configuration passed as parameter.
     * Remember that the sum of all axis is zero for each tile.
     * @param gameboard representation of the Andantino gameboard
     * @param color color of the tiles to add
     * @return list of tiles with all legal moves for the next player
     */
    public static List<Tile> getAllLegalMoves(List<Tile> gameboard, int color) {
        List<Tile> result = new ArrayList<>();
        if(gameboard.size() == 1) {
            result.add(generateFirstMove());
            return result;
        }
        for (Tile tile : gameboard) {
            // north east
            int x = tile.getX() + 1, y = tile.getY(), z = tile.getZ() - 1;
            if(!existsPosition(gameboard, x, y, z) && !existsPosition(result, x, y, z) &&
                    !isOutOfBounds(x, y, z) && isAdjacent(gameboard, x, y, z))
                result.add(new HexTile(x, y, z, color));
            // east
            x = tile.getX() + 1;
            y = tile.getY() - 1;
            z = tile.getZ();
            if(!existsPosition(gameboard, x, y, z) && !existsPosition(result, x, y, z) &&
                    !isOutOfBounds(x, y, z) && isAdjacent(gameboard, x, y, z))
                result.add(new HexTile(x, y, z, color));
            // south east
            x = tile.getX();
            y = tile.getY() - 1;
            z = tile.getZ() + 1;
            if(!existsPosition(gameboard, x, y, z) && !existsPosition(result, x, y, z) &&
                    !isOutOfBounds(x, y, z) && isAdjacent(gameboard, x, y, z))
                result.add(new HexTile(x, y, z, color));
            // south west
            x = tile.getX() - 1;
            y = tile.getY();
            z = tile.getZ() + 1;
            if(!existsPosition(gameboard, x, y, z) && !existsPosition(result, x, y, z) &&
                    !isOutOfBounds(x, y, z) && isAdjacent(gameboard, x, y, z))
                result.add(new HexTile(x, y, z, color));
            // west
            x = tile.getX() - 1;
            y = tile.getY() + 1;
            z = tile.getZ();
            if(!existsPosition(gameboard, x, y, z) && !existsPosition(result, x, y, z) &&
                    !isOutOfBounds(x, y, z) && isAdjacent(gameboard, x, y, z))
                result.add(new HexTile(x, y, z, color));
            // north west
            x = tile.getX();
            y = tile.getY() + 1;
            z = tile.getZ() - 1;
            if(!existsPosition(gameboard, x, y, z) && !existsPosition(result, x, y, z) &&
                    !isOutOfBounds(x, y, z) && isAdjacent(gameboard, x, y, z))
                result.add(new HexTile(x, y, z, color));
        }
       return result;
    }

    /**
    * determines which player has the current turn.
    * @return true if it is the white player turn, otherwise false (black player)
    */
    @Override
    public int getPlayerTurn() { return playerTurn; }

    /**
    * determines if a game has ended.
    * @return true if the game has ended, false otherwise
    */
    @Override
    public boolean isGameFinished() { return endGame; }

    /**
    * determines the winner of a game.
    * @return 1 if white won, 0 if black won, -1
    * if there is no winner yet
    */
    @Override
    public int getWinner() {
        return winner;
    }

    /**
    * starts a game between two humans players.
    */
    @Override
    public void startGameHumanVsHuman() {
        players[0] = new HumanPlayer(0);
        players[1] = new HumanPlayer(1);
    }

    /**
     * starts a game between two bots players.
     */
    @Override
    public void startGameBotVsBot() {
        players[0] = new BotPlayer(0);
        players[1] = new BotPlayer(1);
    }

    /**
     * starts a game between a human and bot players.
     * @param isBotPlayer1 flag to know if player1 is the bot
     */
    @Override
    public void startGameHumanVsBot(boolean isBotPlayer1) {
        botVsHumanGame = true;
        if (isBotPlayer1) {
            players[1] = new BotPlayer(1);
            players[0] = new HumanPlayer(0);
            botIndex = 1;
        }
        else {
            players[1] = new HumanPlayer(1);
            players[0] = new BotPlayer(0);
            botIndex = 0;
        }
    }

    /**
    * play the move of the human player. Updates the turn.
    * @param tile tile to place on the gameboard
    */
    @Override
    public void move(Tile tile) {
        gameBoard.add(tile);
        playerTurn ^= 1;
    }

    /**
    * makes the bot player make a move.
    * @return the last added tile to @gameBoard
    */
    @Override
    public Tile makeBotMove() {
        Tile tile = players[botIndex].move(gameBoard);
        gameBoard.add(tile);
        playerTurn ^= 1;
        return tile;
    }

    /**
     * restarts a game by resetting to the default values the gameboard.
     */
    @Override
    public void restartGame() {
        winner = -1;
        botVsHumanGame = false;
        playerTurn = 1;
        endGame = false;
        gameBoard.clear();
        players[0] = null;
        players[1] = null;
        gameBoard.add(new HexTile(0, 0, 0, 0));
    }

    /**
    * A move is illegal if:
    * tile is already occupied;
    * there is only the center and tile has a distance > 1 from it;
    * tile is not adjacent to at least two existing tile on the gameboard.
    * So, it searches for @tile inside gameBoard. If a match is found the tile has
    * already been played. Also, for the move to be legal, @tile must be placed
    * adjacent to at least two existing tiles.
    * @param tile tile to look for
    * @return true if the tile is occupied else false
    */
    @Override
    public boolean isMoveIllegal(Tile tile) {
        if(gameBoard.size() == 1 && isAdjacentToCenter(tile))
            return false;
        return gameBoard.stream()
                .anyMatch(t -> t.equalsAxis(tile)) ||
                !isAdjacent(tile.getX(), tile.getY(), tile.getZ());
    }

    /**
    * determines if a game results in a draw. Note that gameBoard.size() is equal
    * to the current turn number.
     * @return true if the game results in a draw, false otherwise
    */
    @Override
    public boolean isDraw() {
        return endGame = supervisor.isDraw(gameBoard);
    }

    /**
     * determines if a game results in a win.
     * @param tile last placed tile
     * @return true if a player has won, false otherwise
     */
    @Override
    public boolean hasWon(Tile tile) {
        endGame = supervisor.hasWon(tile.getPlayer(), gameBoard);
        if (endGame)
            winner = tile.getPlayer();
        return endGame;
    }

    /**
    * undo the last played move. So, it has to:
    * remove last played tile from @gameBoard;
    * switch the value of @playerTurn;
    * Note that it can not undo the last move if @gameBoard contains
    * only the center tile. This, because the center tile is the starting position.
    * @return returns the last played tile
    */
    @Override
    public Tile undo() {
        if(gameBoard.size() > 1) { // at least 2 tiles in @gameBoard
            playerTurn ^= 1;
            return gameBoard.remove(gameBoard.size() - 1);
        }
        return null;
    }

    /**
    * determines if player1 is null.
     * @return true if the player1 is null, false otherwise
    */
    @Override
    public boolean isPlayer1Null() {
        return players[1] == null;
    }

    /**
    * determines if the current game is played by a bot
    * and a human.
    * @return true if one of the players is a bot and the other a human, false otherwise
    */
    @Override
    public boolean isBotVsHumanGame() {
        return botVsHumanGame;
    }

    /**
     * determines if both players are bots.
     * @return true if both players are bots, false otherwise
     */
    @Override
    public boolean isBotVsBotGame() {
        return players[0] != null && players[1] != null &&
                players[0].isBot() && players[1].isBot();
    }

    /**
     * determines if it is the bot's turn in a game
     * between a human and a bot players.
     * @return true if the game is between a bot and human players and
     * it's the bot turn, false otherwise
     */
    @Override
    public boolean isBotTurnHumanVsBot() {
        return players[botIndex].isBot() != players[botIndex ^ 1].isBot() && // bot vs human game
                players[botIndex].getColor() == playerTurn; // bot's turn
    }
}
