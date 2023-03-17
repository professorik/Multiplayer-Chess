package client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Test {

    private final JPanel GUI = new JPanel(new BorderLayout(2, 2));
    private ChessBoard chessBoard;
    private final JPanel controlPanel = new ControlPanel();

    Test() {
        initializeGui(true);
    }

    public final void initializeGui(boolean white) {
        GUI.setBackground(ChessBoard.background);
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

    public static void main(String[] args) {
        Runnable r = () -> {
            Test cg = new Test();

            JFrame f = new JFrame("Socket-Chess");
            f.add(cg.getGUI());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);
            f.pack();
            Dimension size = new Dimension(Math.max(f.getWidth(), 1024), Math.max(f.getHeight(), 710));
            f.setMinimumSize(size);
            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }
}
