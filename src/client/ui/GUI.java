package client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI {

    protected static Color background = new Color(43, 43, 44);

    private final JPanel GUI = new JPanel(new BorderLayout(2, 2));
    private final ControlPanel controlPanel = new ControlPanel();

    private ChessBoard chessBoard;

    public GUI() {
        initializeGui(false);
    }

    public final void initializeGui(boolean white) {
        GUI.setBackground(background);
        GUI.setBorder(new EmptyBorder(5, 5, 5, 5));

        setupNewGame(white);

        GUI.add(controlPanel, BorderLayout.CENTER);
    }

    public void setState(ButtonPanel.State state) {
        controlPanel.setState(state);
    }

    public final JComponent getGUI() {
        return GUI;
    }

    public void setupNewGame(boolean white) {
        if (chessBoard != null) {
            chessBoard.setVisible(false);
            GUI.remove(chessBoard);
        }
        chessBoard = new ChessBoard(white);
        chessBoard.setBorder(null);
        GUI.add(chessBoard, BorderLayout.WEST);
        controlPanel.resetClocks();
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void toggleClocks(boolean isActive) {
        controlPanel.setClocks(isActive);
        chessBoard.setTurn(isActive);
    }

    public void stopClocks() {
        controlPanel.stopClocks();
    }
}
