package rttt;

public class TicTacToe {
    private Game head;
    private int maxDepth;
    private Player currentPlayer;
    private Game currentGame;
    boolean started;

    public TicTacToe(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private void constructGame() {
        started = true;
        currentPlayer = Math.random() * 2 < 1 ? Player.X : Player.O;
        if (maxDepth == 0) {
            head = new LeafGame(null);
        } else {
            head = new InteriorGame(null,0, maxDepth);
        }
        currentGame = head;
    }

    public void backtrack() {
        if (currentGame.getParent() != null) {
            currentGame = currentGame.getParent();
        }
    }

    public boolean canBacktrack() {
        if (currentGame instanceof InteriorGame && currentGame.getParent() != null) {
            return true;
        }
        return false;
    }

    public void chooseSelection(int row, int col) {
        if (this.getWinner() != Player.NONE) {
            System.out.println("[TicTacToe.chooseSelection] game already finished");
            return;
        }

        if (currentGame instanceof LeafGame) {
            LeafGame leafGame = (LeafGame) currentGame;
            if (leafGame.canPlace(currentPlayer, row, col)) {
                leafGame.place(currentPlayer, row, col);
                switchPlayer();
                System.out.println(leafGame.isTied());

                if (leafGame.isFinished() || leafGame.isTied()) {
                    while (currentGame.getParent() != null && (currentGame.isFinished() || currentGame.isTied())) {
                        System.out.println("Backtracking");
                        if (currentGame.isTied()) {
                            currentGame.reset();
                        }
                        currentGame = currentGame.getParent();
                        currentGame.setWinner(currentGame.findWinner());
                    }
                }

            }
        } else if (currentGame instanceof InteriorGame) {
            Game selectedGame = ( (InteriorGame) currentGame ).getGameAt(row, col);
            if (!selectedGame.isFinished()) {
                currentGame = selectedGame;
            }
        }

        if (this.getWinner() != Player.NONE) {
            win(this.getWinner());
        }
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;
    }

    public void start() { started = true; constructGame(); }
    public void restart() {
        constructGame();
    }

    private void win(Player player) {
        started = false;
    }

    public void setMaxDepth(int maxDepth) {
        if (started) {
            System.out.println("Cannot change Max-Depth while game is in session");
            return;
        }
        this.maxDepth = maxDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public Game getHead() {
        return head;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWinner() {
        return head.getWinner();
    }
}
