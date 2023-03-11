package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Test {

    private final JPanel GUI = new JPanel(new BorderLayout(3, 3));
    private JPanel boardConstrain;

    Test() {
        initializeGui(true);
    }

    public final void initializeGui(boolean white) {
        ChessBoard.createImages();

        GUI.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        GUI.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame(!white);
            }
        };
        tools.add(newGameAction);

        setupNewGame(white);
    }

    public final JComponent getGUI() {
        return GUI;
    }

    private void setupNewGame(boolean white) {
        if (boardConstrain != null) {
            boardConstrain.setVisible(false);
            GUI.remove(boardConstrain);
        }
        ChessBoard chessBoard = new ChessBoard(white);
        boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(chessBoard.getBackground());
        boardConstrain.add(chessBoard);
        GUI.add(boardConstrain);
    }

    public static void main(String[] args) {
        Runnable r = () -> {
            Test cg = new Test();

            JFrame f = new JFrame("Socket-Chess");
            f.add(cg.getGUI());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);
            f.pack();
            Dimension size = new Dimension(Math.max(f.getWidth(), 650), Math.max(f.getHeight(), 710));
            f.setMinimumSize(size);
            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }
}
