package client;

import javax.swing.*;
import java.awt.*;

/**
 * @author professorik
 * @created 17/03/2023 - 12:24
 * @project socket-chess
 */
public class ControlPanel extends JPanel {

    private final ButtonPanel buttonPanel = new ButtonPanel();
    private final Ledger ledger = new Ledger();

    public ControlPanel() {
        super(new BorderLayout(2, 2));
        setBackground(ChessBoard.background);

        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setBackground(ChessBoard.background);
        centralPanel.add(ledger, BorderLayout.CENTER);
        centralPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centralPanel, BorderLayout.CENTER);
        addClocks();
    }

    private void addClocks() {
        Clock upper = new Clock(300);
        Clock lower = new Clock(300);
        lower.start();

        add(upper, BorderLayout.NORTH);
        add(lower, BorderLayout.SOUTH);
    }
}
