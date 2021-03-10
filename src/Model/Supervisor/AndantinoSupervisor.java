package Model.Supervisor;

import Model.Tile;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * supervisor of the Andantino game. It is the referee of
 * a game and determines the outcomes of a game.
 * Note that this class is stateless and final.
 */
public final class AndantinoSupervisor implements GameSupervisor {

        public AndantinoSupervisor () {}

        /**
         * determines if the north east neighbour is an enemy player.
         * @param tile tile to use as center to find the north east neighbour
         * @param gameBoard gameboard of the game
         * @return true if the enemy exists else false
         */
        @Override
        public boolean isNorthEastNeighbourEnemy(Tile tile, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            tile.getX() + 1 == t.getX() &&
                                    tile.getY()  == t.getY() &&
                                    tile.getZ() - 1 == t.getZ() &&
                                    tile.getPlayer() != t.getPlayer()
                    );
        }

        /**
         * determines if the east neighbour is an enemy player.
         * @param tile tile to use as center to find the east neighbour
         * @param gameBoard gameboard of the game
         * @return true if the enemy exists else false
         */
        @Override
        public boolean isEastNeighbourEnemy(Tile tile, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            tile.getX() + 1 == t.getX() &&
                                    tile.getY() - 1 == t.getY() &&
                                    tile.getZ() == t.getZ() &&
                                    tile.getPlayer() != t.getPlayer()
                    );
        }

        /**
         * determines if the south east neighbour is an enemy player.
         * @param tile tile to use as center to find the south east neighbour
         * @param gameBoard gameboard of the game
         * @return true if the enemy exists else false
         */
        @Override
        public boolean isSouthEastNeighbourEnemy(Tile tile, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            tile.getX() == t.getX() &&
                                    tile.getY() - 1 == t.getY() &&
                                    tile.getZ() + 1 == t.getZ() &&
                                    tile.getPlayer() != t.getPlayer()
                    );
        }

        /**
         * determines if the south west neighbour is an enemy player.
         * @param tile tile to use as center to find the south west neighbour
         * @param gameBoard gameboard of the game
         * @return true if the enemy exists else false
         */
        @Override
        public boolean isSouthWestNeighbourEnemy(Tile tile, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            tile.getX() - 1 == t.getX() &&
                                    tile.getY()  == t.getY() &&
                                    tile.getZ() + 1 == t.getZ() &&
                                    tile.getPlayer() != t.getPlayer()
                    );
        }

        /**
         * determines if the west neighbour is an enemy player.
         * @param tile tile to use as center to find the west neighbour
         * @param gameBoard gameboard of the game
         * @return true if the enemy exists else false
         */
        @Override
        public boolean isWestNeighbourEnemy(Tile tile, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            tile.getX() - 1 == t.getX() &&
                                    tile.getY() + 1 == t.getY() &&
                                    tile.getZ() == t.getZ() &&
                                    tile.getPlayer() != t.getPlayer()
                    );
        }

        /**
         * determines if the north west neighbour is an enemy player.
         * @param tile tile to use as center to find the north east neighbour
         * @param gameBoard gameboard of the game
         * @return true if the enemy exists else false
         */
        @Override
        public boolean isNorthWestNeighbourEnemy(Tile tile, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            tile.getX() == t.getX() &&
                                    tile.getY() + 1 == t.getY() &&
                                    tile.getZ() - 1 == t.getZ() &&
                                    tile.getPlayer() != t.getPlayer()
                    );
        }

        /**
         * finds two equals tiles with the same color with the given axis.
         * @param tile tile to match
         * @param xIndex axis X for the tile to match
         * @param yIndex axis Y for the tile to match
         * @param zIndex axis Z for the tile to match
         */
        private boolean findMate(Tile tile, int xIndex, int yIndex, int zIndex, List<Tile> gameBoard) {
            return gameBoard.stream()
                    .anyMatch(t ->
                            t.getX() == tile.getX() + xIndex &&
                                    t.getY() == tile.getY() + yIndex &&
                                    t.getZ() == tile.getZ() + zIndex &&
                                    t.getPlayer() == tile.getPlayer());
        }

        /**
         * determines if there is a row of five tiles, with the same color.
         * The row starts from @tile
         * in direction north east (x = +1, y = y, z = -1).
         * @param tile starting tile to find a row of five tiles
         */
        private boolean isNorthEastRowOfFive(Tile tile, List<Tile> gameBoard) {
            int xIndex = 1, zIndex = -1, size = gameBoard.size();
            for (int curr = 0; curr < size && curr < 5; curr++) {
                if (findMate(tile, xIndex, 0, zIndex, gameBoard)) {
                    xIndex++;
                    zIndex--;
                } // no ajdacent next element => no row of five
                else
                    return false;
            }
            return true;
        }

        /**
         * determines if there is a row of five tiles, with the same color.
         * The row starts from @tile
         * in direction east (x = +1, y = -1, z = z).
         * @param tile starting tile to find a row of five tiles
         */
        private boolean isEastRowOfFive(Tile tile, List<Tile> gameBoard) {
            int xIndex = 1, yIndex = -1, size = gameBoard.size();
            for (int curr = 0; curr < size && xIndex < 5; curr++) {
                if (findMate(tile, xIndex, yIndex, 0, gameBoard)) {
                    xIndex++;
                    yIndex--;
                } // no ajdacent next element => no row of five
                else
                    return false;
            }
            return true;
        }

        /***
         * determines if there is a row of five tiles, with the same color.
         * The row starts from @tile
         * in direction south east (x = x, y = -1, z = +1).
         * @param tile starting tile to find a row of five tiles
         */
        private boolean isSouthEastRowOfFive(Tile tile, List<Tile> gameBoard) {
            int zIndex = 1, yIndex = -1, size = gameBoard.size();
            for (int curr = 0; curr < size && zIndex < 5; curr++) {
                if (findMate(tile, 0, yIndex, zIndex, gameBoard)) {
                    zIndex++;
                    yIndex--;
                } // no ajdacent next element => no row of five
                else
                    return false;
            }
            return true;
        }


        /**
         * determines if there is a row of five tiles, with the same color.
         * The row starts from @tile
         * in direction south west (x = -1, y = y, z = +1).
         * @param tile starting tile to find a row of five tiles
         */
        private boolean isSouthWestRowOfFive(Tile tile, List<Tile> gameBoard) {
            int xIndex = -1, zIndex = 1, size = gameBoard.size();
            for (int curr = 0; curr < size && zIndex < 5; curr++) {
                if (findMate(tile, xIndex, 0, zIndex, gameBoard)) {
                    xIndex--;
                    zIndex++;
                } // no ajdacent next element => no row of five
                else
                    return false;
            }
            return true;
        }


        /**
         * determines if there is a row of five tiles, with the same color.
         * The row starts from @tile
         * in direction west (x = -1, y = 1, z = z).
         * @param tile starting tile to find a row of five tiles
         */
        private boolean isWestRowOfFive(Tile tile, List<Tile> gameBoard) {
            int xIndex = -1, yIndex = 1, size = gameBoard.size();
            for (int curr = 0; curr < size && yIndex < 5; curr++) {
                if (findMate(tile, xIndex, yIndex, 0, gameBoard)) {
                    xIndex--;
                    yIndex++;
                } // no ajdacent next element => no row of five
                else
                    return false;
            }
            return true;
        }


        /**
         * determines if there is a row of five tiles, with the same color.
         * The row starts from @tile
         * in direction north west (x = 0, y = 1, z = -1).
         * @param tile starting tile to find a row of five tiles
         */
        private boolean isNorthWestRowOfFive(Tile tile, List<Tile> gameBoard) {
            int zIndex = -1, yIndex = 1, size = gameBoard.size();
            for (int curr = 0; curr < size && yIndex < 5; curr++) {
                if (findMate(tile, 0, yIndex, zIndex, gameBoard)) {
                    zIndex--;
                    yIndex++;
                } // no ajdacent next element => no row of five
                else
                    return false;
            }
            return true;
        }


        /**
         * determines if there is at least one tile of the opposite player, who made
         *  the last move, enclosed by at least 6 enemies.
         * sets the winner, if a player has been fully enclosed.
         * @param color color of the player who made the last move
         * @return true if the opposite player has been fully enclosed, false otherwise
         */
        private boolean isFullyEnclosed(int color, List<Tile> gameBoard) {
            AtomicBoolean fullyEnclosed = new AtomicBoolean(false);
            gameBoard.stream()
                    .filter(tile -> !fullyEnclosed.get() && tile.getPlayer() != color)
                    .forEach(tile -> {
                        fullyEnclosed.set(setFullyEnclosed(tile, gameBoard));
                    });
            return fullyEnclosed.get();
        }

        /**
         * determines if there is a tile, only of one player
         * (different from the one who made the last move),
         * fully enclosed. If so, returns true.
         * @param tile tile to check if it is fully enclosed
         */
        private boolean setFullyEnclosed(Tile tile, List<Tile> gameBoard) {
            return
                    isNorthEastNeighbourEnemy(tile, gameBoard) &&
                            isEastNeighbourEnemy(tile, gameBoard) &&
                            isSouthEastNeighbourEnemy(tile, gameBoard) &&
                            isSouthWestNeighbourEnemy(tile, gameBoard) &&
                            isWestNeighbourEnemy(tile, gameBoard) &&
                            isNorthWestNeighbourEnemy(tile, gameBoard);
        }

        /**
         * determines if there is a row of five tiles of the same color.
         * The row must belong to the opposite player, who
         * is different from the one who made the last move).
         * If so, sets the flag @fiveInRow to true.
         * @param tile tile to check if it is in a row of 5
         */
        private boolean setFiveInRow(Tile tile, List<Tile> gameBoard) {
            return
                    isNorthEastRowOfFive(tile, gameBoard) ||
                            isEastRowOfFive(tile, gameBoard) ||
                            isSouthEastRowOfFive(tile, gameBoard) ||
                            isSouthWestRowOfFive(tile, gameBoard) ||
                            isWestRowOfFive(tile, gameBoard) ||
                            isNorthWestRowOfFive(tile, gameBoard);
        }

        /**
         * determines if there is a row of five tiles, only of one player
         * (the one who made the last move).
         * Sets the winner, if there are at least five tiles in a row.
         * @param color color of the player who made the last move
         * @return true if there are five tiles of the same color in a row, false otherwise
         */
        private boolean isRowWin (int color, List<Tile> gameBoard) {
            AtomicBoolean fiveInRow = new AtomicBoolean(false);
            gameBoard.stream()
                    .filter(tile -> !fiveInRow.get() && tile.getPlayer() == color)
                    .forEach(tile -> {
                        fiveInRow.set(setFiveInRow(tile, gameBoard));
                    });
            return fiveInRow.get();
        }

        /**
         * determines if a game is a draw
         */
        @Override
        public boolean isDraw(List<Tile> gameBoard) {
            return gameBoard.size() >= 271;
        }

        /**
         * determines if the last player who moved, has won.
         */
        @Override
        public Boolean hasWon(int color, List<Tile> gameBoard) {
            return gameBoard.size() >= 9 && (
                    isRowWin(color, gameBoard) ||
                            isFullyEnclosed(color, gameBoard)
            );
        }
}
