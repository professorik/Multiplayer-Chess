package client.ui;

import client.Client;
import client.hudcmd.Command;
import client.hudcmd.DrawCmd;
import client.hudcmd.ResignCmd;
import client.hudcmd.StartCmd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;

/**
 * @author professorik
 * @created 17/03/2023 - 14:42
 * @project socket-chess
 */
public class ButtonPanel extends JPanel {

    private final JButton start = new FlatButton("Play");
    private final JButton draw = new FlatButton("Draw");
    private final JButton acceptDraw = new FlatButton("Accept");
    private final JButton resign = new FlatButton("Resign");

    public enum State {
        Initial {
            @Override
            public void show(ButtonPanel panel) {
                panel.start.setVisible(true);
                panel.draw.setVisible(false);
                panel.acceptDraw.setVisible(false);
                panel.resign.setVisible(false);
            }
        },
        Standard {
            @Override
            public void show(ButtonPanel panel) {
                panel.start.setVisible(false);
                panel.draw.setVisible(true);
                panel.acceptDraw.setVisible(false);
                panel.resign.setVisible(true);
            }
        },
        Offered {
            @Override
            public void show(ButtonPanel panel) {
                panel.start.setVisible(false);
                panel.draw.setVisible(false);
                panel.acceptDraw.setVisible(true);
                panel.resign.setVisible(true);
            }
        };

        public abstract void show(ButtonPanel panel);
    }

    public ButtonPanel() {
        super(new FlowLayout());
        setBackground(GUI.background);
        add(start);
        add(draw);
        add(resign);
        add(acceptDraw);

        start.addActionListener(e -> executeCommand(new StartCmd(Client.client)));
        draw.addActionListener(e -> executeCommand(new DrawCmd(Client.client)));
        resign.addActionListener(e -> executeCommand(new ResignCmd(Client.client)));
        acceptDraw.addActionListener(e -> executeCommand(new DrawCmd(Client.client)));

        setState(State.Initial);
    }

    private void executeCommand(Command command) {
        if (command.execute()) {
            System.out.println("Success");
        }
    }

    public void setState(State state) {
        state.show(this);
    }

    private static class FlatButton extends JButton {

        private static final Color standard = new Color(63, 63, 64, 255);
        private static final Color hover = new Color(107, 107, 107, 255);
        private static final Color focused = new Color(84, 84, 85, 255);
        private Shape shape;

        public FlatButton(String text) {
            super(text);

            setBackground(standard);
            setFocusable(false);
            setContentAreaFilled(false);
            setBorderPainted(false);

            setFont(new Font("Dialog", Font.BOLD, 30));
            setForeground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hover);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(standard);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isArmed()) {
                g.setColor(focused);
            } else {
                g.setColor(getBackground());
            }
            g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 10, 10);

            super.paintComponent(g);
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10f, 10f);
            }
            return shape.contains(x, y);
        }
    }
}
