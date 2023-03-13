package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:51
 * @project socket-chess
 */
public class Player {
    public boolean whiteSide;
    public boolean isChecked;

    public Player(boolean whiteSide) {
        this.whiteSide = whiteSide;
        isChecked = false;
    }

    public boolean isWhiteSide() {
        return this.whiteSide;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}

