package Controller;
import Model.Tile;

/**
 * interface for the controller of a game.
 */
public interface GameController {
    void startGame();
    void move(Tile tile);
    void startGameBotVsBot();
    void startGameHumanVsBot(boolean isPlayer1Bot);
    void restartGame();
    void moveHumanVsBot(Tile tile);
    void undo();
    boolean getBotsGameStatus();
    void startBotVsBot();
    void stopBotVsBot();
}
