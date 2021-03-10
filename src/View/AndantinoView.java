package View;

import Controller.GameController;
import Model.GameBoard;
import Model.Tile;
import Model.HexTile;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * represents the view of the application.
 */
public class AndantinoView implements GameView {
    private GameController controller;
    private GameBoard model;

    private JButton startGame;
    private JPanel mainPanel;
    private JPanel turnPanel;
    private JComboBox<ItemComboBox> player2cmb;
    private JComboBox<ItemComboBox> player1cmb;
    private JPanel menuPanel;
    private JButton undo;
    private JLabel Player1Img;
    private JLabel Player2Img;
    private JLabel playerTurnLabel;
    private JLabel playerTurnImg;
    private JLabel Player1Label;
    private JLabel Player2Label;
    private JButton restartButton;
    //private JLabel timerP1Bot;
    private JButton stopRestartBotsGame;
    private JFrame frame;
    private ImageIcon drawResult;
    private ImageIcon winP1Result;
    private ImageIcon winP2Result;
    private ImageIcon blackTile; // img to use for the gameboard's black tiles
    private ImageIcon whiteTile; // img to use for the gameboard's white tiles
    private ImageIcon blankTile; // img to use for the gameboard's blank tiles
    private ImageIcon stopBotsGame;
    private ImageIcon restartBotsGame;
    private ItemComboBox[] itemsCmbPlayer1;
    /**
    * contains the buttons, which represent the tiles of the gameboard.
    */
    private List<JButton> buttons;

    /**
     * create the gameboard hexagonal game board.
     */
    private void createAndantinoGameBoard() {
        SpringLayout springLayout = new SpringLayout(); // creates layout for gameBoardPanel
        Container content = frame.getContentPane();
        content.setBackground(Color.darkGray);
        content.setLayout(springLayout);
        // float to the right menuPanel
        springLayout.putConstraint(SpringLayout.WEST, menuPanel, 400, SpringLayout.WEST, content);
        // float turnPanel
        springLayout.putConstraint(SpringLayout.WEST, turnPanel, 50, SpringLayout.WEST, content);
        springLayout.putConstraint(SpringLayout.NORTH, turnPanel, 60, SpringLayout.NORTH, content);

        createUpperHalfAndantinoGameBoard(springLayout, content);
        createLowerHalfAndantinoGameBoard(springLayout, content);
    }

    /**
    * helper function to update the view in case of draw or win.
    * @param dialog dialog for the win/draw result
    * @param image image to use for @dialog
    */
    private void updateGameHelper(JDialog dialog, ImageIcon image) {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Game result");
        dialog.add(new JLabel(image));
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        undo.setEnabled(false);
        stopRestartBotsGame.setEnabled(false);
    }

    /**
     * sets a game to be played between two bots players.
     * No inputs from the users to play a tile.
     */
    private void setGameBotVsBot() {
        buttons.forEach(button -> {
            button.setEnabled(true);
            button.addActionListener(actionEvent -> {});
        });
    }

    /**
    * sets a game to be played between two human players.
    */
    private void setGameHuman() {
        buttons.forEach(button -> {
            HexButton hexBtn = (HexButton)button;
            button.setEnabled(true);
            button.addActionListener(actionEvent ->
                    controller.move(new HexTile(hexBtn.getXAxis(), hexBtn.getYAxis(),
                            hexBtn.getZAxis(), model.getPlayerTurn()))
            );
        });
    }

    /**
     * sets a game to be played between a human and a bot players.
     * The buttons take only inputs from the human, so you need to know
     * which player is human.
     * @param humanPlayer flag to tell which player is the human
     */
    private void setGameHumanVsBot(int humanPlayer) {
        buttons.forEach(button -> {
            HexButton hexBtn = (HexButton)button;
            button.setEnabled(true);
            button.addActionListener(actionEvent ->
                    controller.moveHumanVsBot(new HexTile(
                            hexBtn.getXAxis(),
                            hexBtn.getYAxis(),
                            hexBtn.getZAxis(), humanPlayer))
            );
        });
    }

    /**
    * creates the upper half and the central row of the hexagonal game-board.
     * fixedPaddingWest: padding from the content west border
     * fixedPaddingNorth: padding from the content north border
     * tileHeight: pixel size of the img of the tile
     * halfTileHeight: pixel size of half a tile
     * axisX: axis X of the tile
     * axisY: axis Y of the tile
     * axisZ: axis Z of the tile
     * sizeX: number of tiles per row; initialized to the number of elements of the row "1"
     * the buttons' toolTip is in the format XY, where X is a number [1..19]; Y is a letter [a..s]
     * growFactorWest: pixel rate's distance from west border used to align the tiles
     * growFactorNorth: pixel rate's distance from north border used to align the tiles
     * @param springLayout springLayout of the application
     * @param content Container of the application
    */
    private void createUpperHalfAndantinoGameBoard(SpringLayout springLayout, Container content) {
        char toolTip = 'a';
        int fixedPaddingWest = 224,
                tileHeight = 48,
                halfTileHeight = 24,
                fixedPaddingNorth = 5,
                growFactorWest = 10,
                growFactorNorth = 1;
        /*
         * creates the hexagon grid.
         */
        int axisX = 0, axisZ = -9, sizeX = 10, numberToolTip = 1;
        char letterToolTip = 'a';
        for(; axisX >= -9; axisX--, axisZ++,
                sizeX++, growFactorWest--, growFactorNorth++, numberToolTip++) {
            fixedPaddingWest += tileHeight;
            /*
             * creates the tiles inside a row of the hexagon
             */
            letterToolTip = 'a';
            for(int tmpX = axisX, currX = 0, axisY = 9; currX < sizeX; tmpX++, axisY--, currX++, letterToolTip++) {
                JButton button = createTile(tmpX, axisY, axisZ,
                        numberToolTip + Character.toString(letterToolTip));
                springLayout.putConstraint(SpringLayout.EAST, button,
                        fixedPaddingWest + (halfTileHeight * growFactorWest) + (tileHeight * tmpX),
                        SpringLayout.WEST, content);
                springLayout.putConstraint(SpringLayout.NORTH, button, fixedPaddingNorth + (tileHeight * growFactorNorth),
                        SpringLayout.NORTH, content);
                buttons.add(button);
                content.add(button);
            }
        }
    }

    /**
     * creates the lower half of the hexagonal game-board.
     * fixedPaddingWest: padding from the content west border
     * fixedPaddingNorth: padding from the content north border
     * tileHeight: pixel size of the img of the tile
     * halfTileHeight: pixel size of half a tile
     * axisX: axis X of the tile
     * axisY: axis Y of the tile
     * axisZ: axis Z of the tile
     * sizeX: number of tiles per row; initialized to the number of elements of the row "1"
     * the buttons' toolTip is in the format XY, where X is a number [1..19]; Y is a letter [a..s]
     * growFactorWest: pixel rate's distance from west border used to align the tiles
     * growFactorNorth: pixel rate's distance from north border used to align the tiles
     */
    private void createLowerHalfAndantinoGameBoard(SpringLayout springLayout, Container content) {
        int fixedPaddingWest = 728,
                tileHeight = 48,
                halfTileHeight = 24,
                axisX = -9,
                axisZ = 1,
                sizeX = 18,
                numberToolTip = 11,
                fixedPaddingNorth = 5,
                growFactorNorth = 11,
                growFactorWest = 1;
        char letterToolTip = 'b';

        for(int axisY = 8; axisY >= 0; sizeX--, axisZ++, axisY--, growFactorNorth++, growFactorWest++,
                letterToolTip++, numberToolTip++) {
            char tmpLetterToolTip = letterToolTip;
            for(int currX = 0, tmpX = axisX, tmpY = axisY; currX < sizeX; currX++, tmpX++, tmpY--, tmpLetterToolTip++) {
                JButton button = createTile(tmpX, tmpY, axisZ,
                        numberToolTip + Character.toString(tmpLetterToolTip));
                springLayout.putConstraint(SpringLayout.EAST, button,
                        fixedPaddingWest + (halfTileHeight * growFactorWest) + (tileHeight * tmpX),
                        SpringLayout.WEST, content);
                springLayout.putConstraint(SpringLayout.NORTH, button, fixedPaddingNorth + (tileHeight * growFactorNorth),
                        SpringLayout.NORTH, content);
                buttons.add(button);
                content.add(button);
            }
        }
    }

    /**
    * Returns a JButton which is a blank tile. The Dimension(width, height) of the
    * button must be the same width and height in pixel of the image file.
    * Note that the buttons are not enabled till the start of the game.
    * The tile is initialized to the blank image.
     * @param x coordinate X of the tile in the game board
     * @param y coordinate Y of the tile in the game board
     * @param z coordinate Z of the tile in the game board
     * @param toolTip is the toolTip of the returned button
    */
    private JButton createTile(int x, int y, int z, String toolTip) {
        JButton button = new HexButton(x, y, z, blankTile);
        button.setPreferredSize(new Dimension(40, 48)); // same pixel size of image file used
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setToolTipText(toolTip);
        button.setEnabled(false); // initially the buttons are disabled
        return button;
    }

    /**
     * find the button with the specified axis.
     * @param x axis X of the tile to update
     * @param y axis Y of the tile to update
     * @param z axis Z of the tile to update
     * @return button with the desired axis
     */
    private HexButton findButton(int x, int y, int z) {
        for(JButton button : buttons) {
            HexButton b = (HexButton)button;
            if (b.getYAxis() == y && b.getXAxis() == x && b.getZAxis() == z)
                return b;
        }
        return null; // throw exception
    }

    /**
     * sets the current player's turn label.
     */
    private void updatePlayerTurnLabel() {
        if(model.getPlayerTurn() == 0) { // black's turn
            playerTurnLabel.setText("Player 2 turn");
            playerTurnImg.setIcon(blackTile);
        }
        else { // white's turn
            playerTurnLabel.setText("Player 1 turn");
            playerTurnImg.setIcon(whiteTile);
        }
    }

    /**
    * sets all tiles images to the blank tile. It is called after a restart
    * for games:
    * human vs human
    * human vs bot
    * if the game is bot vs bot:
    * disable all buttons
    */
    private void clearAllTiles() {
        buttons.forEach(button -> button.setIcon(blankTile));
    }

    /**
    * sets menuPanel and turnPanel.
    */
    private void setPanels() {
        playerTurnImg.setIcon(whiteTile);
        Player1Img.setIcon(new ImageIcon(getClass().getClassLoader().getResource("tilesImages/whiteTile20.png"))); // set icon for player1
        Player1Img.setPreferredSize(new Dimension(20, 20)); // size must be equal to the img pixels
        Player2Img.setIcon(new ImageIcon(getClass().getClassLoader().getResource("tilesImages/blackTile20.png"))); // set icon for player2
        Player2Img.setPreferredSize(new Dimension(20, 20));
        menuPanel.setBackground(Color.gray);
        turnPanel.setBackground(Color.gray);
    }

    /**
    * sets the ComboBoxModel for the comboBox of the players.
    */
    private void setModelComboBox() {
        itemsCmbPlayer1 = new ItemComboBox[2];
        itemsCmbPlayer1[0] = new ItemComboBox(true, "Human");
        itemsCmbPlayer1[1] = new ItemComboBox(false, "Bot");
        player1cmb.setModel(new DefaultComboBoxModel<>(itemsCmbPlayer1));
        player2cmb.setModel(new DefaultComboBoxModel<>(itemsCmbPlayer1));
    }

    /**
    * sets the central tile of the gameboard to the color black.
    */
    private void setCenterTile() {
        final Optional<JButton> center = buttons.stream()
                .filter(button -> {
                    HexButton b = (HexButton) button;
                    return b.getXAxis() == 0 &&
                            b.getYAxis() == 0 &&
                            b.getZAxis() == 0;
                })
                .findFirst();
        center.get().setIcon(blackTile);
    }

    /**
    * auxiliary function to start/restart a game.
    */
    private void startGameInit() {
        ItemComboBox cmb1 = (ItemComboBox)player1cmb.getSelectedItem();
        ItemComboBox cmb2 = (ItemComboBox)player2cmb.getSelectedItem();
        setCenterTile();
        stopRestartBotsGame.setToolTipText("stop bot, current game");
        if (!cmb1.getKey() && !cmb2.getKey()) { // bot vs bot
            // do not activate the gameboard. There are no user inputs
            // undo not available for bot vs bot
            stopRestartBotsGame.setEnabled(true);
            stopRestartBotsGame.setIcon(stopBotsGame);
            setGameBotVsBot();
            undo.setEnabled(false);
            controller.startGameBotVsBot();
            return;
        }
        stopRestartBotsGame.setEnabled(false);
        if (cmb1.getKey() && cmb2.getKey()) { // human vs human
            undo.setEnabled(true);
            setGameHuman();
            controller.startGame();
            return;
        }
        undo.setEnabled(false);
        /*
        * note: game human vs bot, bot's turn, human asks to move -> human can not move
        */
        if(cmb1.getKey() && !cmb2.getKey()) { // player1: human, player2:bot
            setGameHumanVsBot(1);
            controller.startGameHumanVsBot(false);
        }
        else { // player1: bot, player2: human
            setGameHumanVsBot(0);
            controller.startGameHumanVsBot(true);
        }
    }

    /**
     * ctor.
     * @param controller controller component of the MVC pattern
    * @param model model component of the MVC pattern
    */
    public AndantinoView(GameController controller, GameBoard model) {
        this.controller = controller;
        this.model = model;
        buttons = new ArrayList<>();
        // gameBoard = new HashMap<>();
        // create and set main window
        frame = new JFrame("Andantino");
        mainPanel = new JPanel();
        frame.setContentPane(mainPanel);
        frame.setPreferredSize(new Dimension(1600,1300));
        frame.pack();
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // frame full screen

        // create turnPanel
        turnPanel = new JPanel();
        turnPanel.setPreferredSize(new Dimension(170, 60));
        playerTurnLabel = new JLabel("Player 1 turn");
        playerTurnImg = new JLabel();

        // create reusable images
        whiteTile = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/whiteTileb.png"));
        blankTile = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/blankTileb.png"));
        blackTile = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/blackTileb.png"));
        drawResult = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/drawResult.png"));
        winP1Result = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/p1Won.png"));
        winP2Result = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/p2Won.png"));
        stopBotsGame = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/stop.png"));
        restartBotsGame = new ImageIcon(getClass().getClassLoader().getResource("tilesImages/restart.png"));


        setModelComboBox();
        restartButton.setEnabled(false); // can't restart the game if you haven't started one first
        undo.setEnabled(false); // same as restart
        stopRestartBotsGame.setIcon(stopBotsGame);
        stopRestartBotsGame.setEnabled(false);
        setPanels();

        // add labels to turn's panel
        turnPanel.add(playerTurnImg);
        turnPanel.add(playerTurnLabel);
        // add components to the frame
        frame.add(menuPanel);
        frame.add(turnPanel);

        createAndantinoGameBoard(); // create gameBoard

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        startGame.addActionListener(actionEvent -> {
            startGame.setEnabled(false);
            restartButton.setEnabled(true);
            startGameInit();
        });

        /**
        * opens a new dialog window to ask for confirmation.
        */
        restartButton.addActionListener(actionEvent -> {
            int restart = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to restart the game?",
                    "Restart the game",
                    JOptionPane.YES_NO_OPTION);
            if (restart == 0) { // restart the game
                clearAllTiles();
                controller.restartGame();
                updatePlayerTurnLabel();
                startGameInit();
            }
        });

        undo.addActionListener(actionEvent -> controller.undo());
        stopRestartBotsGame.addActionListener(actionEvent -> {
            if (!controller.getBotsGameStatus()) { // game is going on, I want to stop it
                controller.stopBotVsBot();
                stopRestartBotsGame.setIcon(restartBotsGame);
                stopRestartBotsGame.setToolTipText("restart bot, current game");
            }
            else { // game is halted, I want to restart it
                controller.startGameBotVsBot();
                stopRestartBotsGame.setIcon(stopBotsGame);
                stopRestartBotsGame.setToolTipText("stop bot, current game");
            }
        });
    }

    /**
    * updates the tile ImageIcon with axis equals to x,y,z.
    * Then updates the player's turn label.
    * Note that since Gameboard has already updated the TURN, then you have to
    * use the opposite color of the current TURN value (0 -> white, 1 -> black).
     * @param tile tile to update, found in buttons
    */
    @Override
    public void updateTile(Tile tile) {
        HexButton button = findButton(tile.getX(), tile.getY(), tile.getZ());
        if (model.getPlayerTurn() == 0)
            button.setIcon(whiteTile); // because AndantinoGameBoard has updated the turn
        else
            button.setIcon(blackTile); // because AndantinoGameBoard has updated the turn
        updatePlayerTurnLabel();
    }

    /**
    * shows a game result equal to a draw.
    */
    @Override
    public void updateDrawGame() {
        JDialog dialog = new JDialog();
        updateGameHelper(dialog, drawResult);
    }

    /**
     * shows a game result equal to a win.
     * @param winner flag to know which player has won
     */
    @Override
    public void updateWinGame(int winner) {
        JDialog dialog = new JDialog();
        if (winner == 1)
            updateGameHelper(dialog, winP1Result);
        if (winner == 0)
            updateGameHelper(dialog, winP2Result);
    }

    /**
    * undo the last played tile. So, it must:
    * updates the current turn;
    * turn to blank the tile, in the gameboard,
    * which is represented by the button in @buttons with the same axis x,y,z as @tile;
    * @param tile it is the last played tile to turn blank
    */
    @Override
    public void undo(Tile tile) {
        if(tile != null) {
            updateTile(tile);
            HexButton button = findButton(tile.getX(), tile.getY(), tile.getZ());
            button.setIcon(blankTile);
        }
    }

    /**
     * updates the timer after a turn, in a game
     * bot vs bot or bot vs human.
     * @param minutes minutes taken to make a move
     * @param seconds seconds taken to make a move
     */
    @Override
    public void updateTimer(long minutes, long seconds) {
            //timerP1Bot.setText(minutes + ":" + seconds);
    }
}

