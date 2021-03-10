package Model;

import java.util.List;

/**
 * represents the gameboard of a game.
 */
public interface GameBoard {
    void startGameHumanVsHuman();
    void startGameBotVsBot();
    void startGameHumanVsBot(boolean isBotPlayer1);
    void move(Tile tile);
    Tile makeBotMove();
    void restartGame();
    boolean isMoveIllegal(Tile tile);
    boolean isDraw();
    boolean hasWon(Tile tile);
    boolean isGameFinished();
    int getPlayerTurn();
    int getWinner();
    Tile undo();
    boolean isPlayer1Null();
    boolean isBotVsHumanGame();
    boolean isBotVsBotGame();
    boolean isBotTurnHumanVsBot();
}
