package View;
import Model.Tile;

public interface GameView {
    void updateTile(Tile tile);
    void updateWinGame(int winner);
    void updateDrawGame();
    void undo(Tile tile);
    void updateTimer(long minutes, long seconds);
}
