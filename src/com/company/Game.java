package com.company;

public abstract class Game {
    Player winner;
    Game parent;

    Game(Game parent) {
        this.parent = parent;
        winner = Player.NONE;
    }


    public abstract void place(Player player, int row, int col);
    public abstract boolean canPlace(Player player, int row, int col);
    public abstract Player findWinner();
    public abstract boolean isTied();
    public abstract void reset();

    public boolean isFinished() {
        if (winner == Player.NONE)
            return false;
        return true;
    }

    public Game getParent() {
        return parent;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }
}
