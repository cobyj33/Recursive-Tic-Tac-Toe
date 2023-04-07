package rttt;


public class LeafGame extends Game {
    Player[][] board;
    private int depth;

    LeafGame(Game parent) {
        super(parent);
        if (parent instanceof InteriorGame)
            depth = ( (InteriorGame) parent).getDepth() + 1;
        construct();
    }

    private void construct() {
        board = new Player[3][3];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = Player.NONE;
            }
        }
    }

    public void reset() {
        construct();
    }


    @Override
    public void place(Player player, int row, int col) {
        board[row][col] = player;
        this.setWinner(findWinner());
    }

    @Override
    public boolean canPlace(Player player, int row, int col) {
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
            System.out.println("[LeafGame.canPlace()] cannot place piece: index out of bounds");
            return false;
        } else if (winner != Player.NONE) {
            System.out.println("[LeafGame.canPlace()] cannot place piece: game already finished");
            return false;
        } else if  (board[row][col] != Player.NONE) {
            System.out.println("[LeafGame.canPlace()] cannot place piece: area already occupied");
            return false;
        }
        return true;
    }

    @Override
    public Player findWinner() {
        //test horizontal wins
        for (int row = 0; row < board.length; row++) {
            if ((board[row][0] == board[row][1] && board[row][1] == board[row][2]) && board[row][0] != Player.NONE) {
                return board[row][0];
            }
        }

        //test veritcal wins
        for (int col = 0; col < board[0].length; col++) {
            if ((board[0][col] == board[1][col] && board[1][col] == board[2][col]) && board[0][col] != Player.NONE) {
                return board[0][col];
            }
        }

        //test diagonal wins
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) && board[0][0] != Player.NONE) {
            return board[0][0];
        }
        if ((board[2][0] == board[1][1] && board[1][1] == board[0][2]) && board[2][0] != Player.NONE) {
            return board[2][0];
        }

        return Player.NONE;
    }

    public Player getPlayerAt(int row, int col) {
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
            System.out.println("[InteriorGame.getGameAt()] could not fetch game: index out of bounds");
            return Player.NONE;
        }
        return board[row][col];
    }

    public boolean isTied() {
        if (winner != Player.NONE)
            return false;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == Player.NONE)
                    return false;
            }
        }

        return true;
    }

    public int getDepth() {
        return depth;
    }
}
