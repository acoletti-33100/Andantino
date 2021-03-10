package Controller;
import Model.GameBoard;
import Model.Tile;
import View.AndantinoView;
import View.GameView;

import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * controller of the application.
 */
public class AndantinoController implements GameController {
    private GameBoard model;
    private GameView view;
    /**
     * flag to stop/restart a game between 2 players
     * (human vs bot, bot vs bot). It assumes the following values:
     * <ul>
     * <li>false = game is going on</li>
     * <li>true = game has been halted</li>
     * </ul>
     */
    private boolean gameStopped;

    /**
     * It keeps track of the nano seconds a bot takes to make
     * a move.
     */
    private long nanoSeconds;

    private ExecutorService executor;

    /**
     * bot vs bot executor.
     */
    private ScheduledExecutorService botExecutor;

    public AndantinoController(GameBoard model) {
        this.model = model;
        view = new AndantinoView(this, model);
        gameStopped = false;
        nanoSeconds = 0;
        executor = Executors.newFixedThreadPool(1);
        botExecutor = null;
        botExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * makes a move for the human player.
     */
    private void humanMove(Tile tile) {
        model.move(tile);
        // update the view
        view.updateTile(tile);
        if (model.isDraw()) { // draw, stop the game
            view.updateDrawGame();
        }
        if (model.hasWon(tile)) { // win, stop the game
            view.updateWinGame(model.getWinner());
        }
    }

    /**
     * coordinates a bot's move. Make the bot move, then updates the
     * timer and gameboard.
     */
    private void makeBotMove() {
        view.updateTile(model.makeBotMove()); // make bot move, update gameboard
        //view.updateTimer(seconds / 60, seconds % 60); // updates timer
    }

    /**
     * creates a move made by a human player for a match between 2 human players.
     *
     * @param tile tile to place on the game-board
     */
    @Override
    public void move(Tile tile) {
        // game ended or move not legal => notify the user, do nothing to the gameboard
        if (model.isGameFinished() || model.isMoveIllegal(tile))
            return;
        else  // legal move, update model and gameboard
            humanMove(tile);
    }

    /**
     * creates a move for a human player for a match between a human
     * and a bot players.
     * It returns and does not make a move, if:
     * <ul>
     *     <li>user has halted the game.</li>
     *     <li>human tries to place tile but it is not his turn.</li>
     *     <li>the game is finished (win, draw).</li>
     *     <li>human tries an illegal move.</li>
     * </ul>
     *
     * @param tile new tile to place on the gameboard
     */
    @Override
    public void moveHumanVsBot(Tile tile) {
        // game stopped or completed (win/draw); or not human turn
        if (model.isGameFinished() || model.getPlayerTurn() != tile.getPlayer())
            return;
        if (model.isMoveIllegal(tile)) {
            return;
        } else { // everything is ok, make move
            humanMove(tile); // make human move
            executor.execute(new BotTask());
        }
    }

    /**
     * starts a game between two humans players.
     */
    @Override
    public void startGame() {
        model.startGameHumanVsHuman();
    }

    /**
     * starts a game between two bots players.
     */
    @Override
    public void startGameBotVsBot() {
        if (model.isPlayer1Null())
            model.startGameBotVsBot();
        startBotVsBot();
    }

    /**
     * starts a game between a bot and a human player.
     *
     * @param isPlayer1Bot flag, true if player1 is the bot player; false otherwise
     */
    @Override
    public void startGameHumanVsBot(boolean isPlayer1Bot) {
        model.startGameHumanVsBot(isPlayer1Bot);
        if (isPlayer1Bot)
            makeBotMove();
    }

    /**
     * restarts a game.
     */
    @Override
    public void restartGame() {
        // botGameFlag, because model.restartGame sets players to null
        boolean botGameFlag = false;
        if (model.isBotVsBotGame())
                botGameFlag = true;
        model.restartGame();
        if (botGameFlag) {
            model.startGameBotVsBot();
            botExecutor = null;
            startBotVsBot();
            return;
        }
        executor = Executors.newFixedThreadPool(1);
    }

    /**
     * undo the last played move. It must:
     * <ul>
     * <li>notify the model;</li>
     * <li>notify the view to update itself;</li>
     * </ul>
     * Note that:
     * <ul>
     * <li>if the game is bot vs bot and the game has NOT been stopped, then do nothing;</li>
     * <li>if the game is bot vs human and it's the bot's turn and
     * the game is running, then make the bot move</li>
     * </ul>
     */
    @Override
    public void undo() {
        if (!gameStopped && model.isBotVsBotGame())
            return;
        view.undo(model.undo()); // undo before bot moves again
        // if (model.isBotTurnHumanVsBot())
          //  makeBotMove();
    }

    /**
     * determines the status of a game.
     *
     * @return flag to indicate if a game is stopped or not
     */
    @Override
    public boolean getBotsGameStatus() {
        return gameStopped;
    }

    /**
     * starts a bot player in a game bot vs bot..
     * It is used when the user wants to restart a game.
     */
    @Override
    public void startBotVsBot() {
        gameStopped = false;
        botExecutor = Executors.newSingleThreadScheduledExecutor();
        botExecutor.scheduleAtFixedRate(new BotGameTask(), 1000L, 1500L, TimeUnit.MILLISECONDS);
    }

    /**
     * stops a bot player in a game bot vs bot.
     * It is used when the user wants to stop a game.
     */
    @Override
    public void stopBotVsBot() {
        gameStopped = true;
        if (botExecutor != null && !botExecutor.isShutdown())
            botExecutor.shutdownNow();
    }

    private class BotTask implements Runnable {

        public BotTask() {
        }

        public void run() {
            if (!model.isGameFinished()) {
                Tile result = model.makeBotMove();
                view.updateTile(result);
                if (model.isDraw()) { // draw, stop the game
                    view.updateDrawGame();
                    executor.shutdownNow();
                }
                if (model.hasWon(result)) { // win, stop the game
                    view.updateWinGame(model.getWinner());
                    executor.shutdownNow();
                }
            }
        }
    }

    private class BotGameTask implements Runnable {

        public void run() {
            if (!gameStopped && !model.isGameFinished()) {
                Tile result = model.makeBotMove();
                view.updateTile(result);
                if (model.isDraw()) { // draw, stop the game
                    view.updateDrawGame();
                    botExecutor.shutdownNow();
                }
                if (model.hasWon(result)) { // win, stop the game
                    view.updateWinGame(model.getWinner());
                    botExecutor.shutdownNow();
                }

            }
        }
    }
}
