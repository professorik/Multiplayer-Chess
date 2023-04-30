package client.ui;

import utils.chess.Pieces;

import javax.swing.*;

/**
 * @author professorik
 * @created 04/04/2023 - 12:28
 * @project socket-chess
 */
public class Popup {

    public static void show(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void finish(String reason, boolean white, boolean black, boolean isWhite) {
        if (black == white) {
            show(reason, "Draw");
            return;
        }
        show(reason, white == isWhite? "You won": "You lost");
    }

    public static Pieces showPromotion() {
        String[] options = {"Queen", "Rook", "Knight", "Bishop"};
        int idx = JOptionPane.showOptionDialog(null, "Choose a figure",
                "Promotion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return Pieces.valueOf(options[idx].toUpperCase());
    }
}
