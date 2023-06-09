package client.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author professorik
 * @created 17/03/2023 - 12:24
 * @project socket-chess
 */
public class ControlPanel extends JPanel {

    private static final int TIME = 600;

    private final ButtonPanel buttonPanel = new ButtonPanel();
    private final Ledger ledger = new Ledger();

    private Clock upper;
    private Clock lower;

    public ControlPanel() {
        super(new BorderLayout(2, 2));
        setBackground(GUI.background);

        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setBackground(GUI.background);
        centralPanel.add(ledger, BorderLayout.CENTER);
        centralPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(centralPanel, BorderLayout.CENTER);
        addClocks();
    }

    public void addLabel(String label) {
        ledger.addLabel(label);
    }

    public void setState(ButtonPanel.State state) {
        buttonPanel.setState(state);
    }

    private void addClocks() {
        upper = new Clock(TIME);
        lower = new Clock(TIME);

        add(upper, BorderLayout.NORTH);
        add(lower, BorderLayout.SOUTH);
    }

    protected void setClocks(boolean isLower) {
        if (isLower) {
            upper.stop();
            lower.start();
        } else {
            upper.start();
            lower.stop();
        }
    }

    protected void stopClocks() {
        upper.stop();
        lower.stop();
    }

    protected long getTime() {
        return lower.getSecs();
    }

    protected void resetClocks() {
        upper.setSecs(TIME);
        lower.setSecs(TIME);
    }

    protected void resetNotes() {
        ledger.reset();
    }
}
