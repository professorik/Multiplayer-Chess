package client.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author professorik
 * @created 17/03/2023 - 11:43
 * @project socket-chess
 */
public class Clock extends JPanel {

    private Timer timer;
    private int secs;
    private JLabel label;

    public Clock(int secs) {
        super(new BorderLayout());
        this.secs = secs;
        this.setBackground(GUI.background);

        setLabel();
        setTimer();
    }

    private void setLabel() {
        label = new JLabel(format(secs));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Dialog", Font.BOLD, 50));

        add(label, BorderLayout.WEST);
    }

    private void setTimer() {
        int delay = 1000;
        timer = new Timer(delay, e -> {
            --secs;
            label.setText(format(secs));
        });
    }

    public void setSecs(int secs) {
        this.secs = secs;
        label.setText(format(this.secs));
    }

    protected void start() {
        timer.start();
    }

    protected void stop() {
        timer.stop();
    }

    private String format(int secs) {
        return String.format("%02d:%02d", secs / 60, secs % 60);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(200, 60);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 60);
    }
}
