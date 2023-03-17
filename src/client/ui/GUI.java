package client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI {

    protected static Color background = new Color(43, 43, 44);

    private final JPanel GUI = new JPanel(new BorderLayout(2, 2));
    private final JPanel controlPanel = new ControlPanel();

    private ChessBoard chessBoard;

    public GUI() {
        initializeGui(true);
    }

    public final void initializeGui(boolean white) {
        GUI.setBackground(background);
        GUI.setBorder(new EmptyBorder(5, 5, 5, 5));
        ChessBoard.createImages();

        setupNewGame(white);

        GUI.add(controlPanel, BorderLayout.CENTER);
    }

    public final JComponent getGUI() {
        return GUI;
    }

    private void setupNewGame(boolean white) {
        if (chessBoard != null) {
            chessBoard.setVisible(false);
            GUI.remove(chessBoard);
        }
        chessBoard = new ChessBoard(white);
        chessBoard.setBorder(null);
        GUI.add(chessBoard, BorderLayout.WEST);
    }
}