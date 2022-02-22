package com.company;

public class InteriorGame extends Game {
    Game[][] board;
    private final int currentDepth;
    private final int maxDepth;

    InteriorGame(Game parent, int currentDepth, int maxDepth) {
        super(parent);
        this.currentDepth = currentDepth;
        this.maxDepth = maxDepth;
        construct();
    }

    private void construct() {
        board = new Game[3][3];
        if (currentDepth + 1 >= maxDepth) {
            constructLeafGames();
        } else {
            constructInteriorGames(currentDepth + 1, maxDepth);
        }
    }

    private void constructInteriorGames(int nextDepth, int maxDepth) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = new InteriorGame(this, nextDepth, maxDepth);
            }
        }
    }

    private void constructLeafGames() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = new LeafGame(this);
            }
        }
    }

    public void reset() {
        construct();
    }

    @Override
    public void place(Player player, int row, int col) {

    }

    @Override
    public boolean canPlace(Player player, int row, int col) {
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
            System.out.println("[InteriorGame.canPlace()] cannot place piece: index out of bounds");
            return false;
        } else if (winner != Player.NONE) {
            System.out.println("[InteriorGame.canPlace()] cannot place piece: game already finished");
            return false;
        } else if  (board[row][col].getWinner() != Player.NONE) {
            System.out.println("[LeafGame.canPlace()] cannot place piece: game already won");
            return false;
        }


        return true;
    }

    @Override
    public Player findWinner() {
        for (Game[] row : board) {
            if ((row[0].getWinner() == row[1].getWinner() && row[1].getWinner() == row[2].getWinner()) && row[0].getWinner() != Player.NONE) {
                return row[0].getWinner();
            }
        }

        //test veritcal wins
        for (int col = 0; col < board[0].length; col++) {
            if ((board[0][col].getWinner() == board[1][col].getWinner() && board[1][col].getWinner() == board[2][col].getWinner()) && board[0][col].getWinner() != Player.NONE) {
                return board[0][col].getWinner();
            }
        }

        //test diagonal wins
        if ((board[0][0].getWinner() == board[1][1].getWinner() && board[1][1].getWinner() == board[2][2].getWinner()) && board[0][0].getWinner() != Player.NONE) {
            return board[0][0].getWinner();
        }
        if ((board[2][0].getWinner() == board[1][1].getWinner() && board[1][1].getWinner() == board[0][2].getWinner()) && board[2][0].getWinner() != Player.NONE) {
            return board[2][0].getWinner();
        }

        return Player.NONE;
    }

    public Game getGameAt(int row, int col) {
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
            System.out.println("[InteriorGame.getGameAt()] could not fetch game: index out of bounds");
            return null;
        }
        return board[row][col];
    }

    public boolean isTied() {
        if (winner != Player.NONE)
            return false;

        for (Game[] row : board) {
            for (Game child : row) {
                if (child.getWinner() == Player.NONE)
                    return false;
            }
        }

        return true;
    }

    public int getDepth() {
        return currentDepth;
    }

}
