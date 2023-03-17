package client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author professorik
 * @created 17/03/2023 - 14:21
 * @project socket-chess
 */
public class Ledger extends JPanel {

    private JTable table;
    private JScrollPane pane;

    public Ledger() {
        super(new BorderLayout());
        setBackground(GUI.background);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        String[][] data = {
                {"1", "Nf3", "Nf6"}, {"2", "c4", "g6"}, {"3", "Nc3", "Bg7"}, {"4", "d4", "O-O"}, {"5", "Bf4", "d5"}, {"6", "Qb3", "dxc4"}, {"7", "Qxc4", "c6"}, {"8", "e4", "Nbd7"}, {"9", "Rd1", "Nb6"}, {"10", "Qc5", "Bg4"},
                {"11", "Bg5", "Na4"}, {"12", "Qa3", "Nxc3"}, {"13", "bxc3", "Nxe4"}, {"14", "Bxe7", "Qb6"}, {"15", "Bc4", "Nxc3"}, {"16", "Bc5", "Rfe8+"}, {"17", "Kf1", "Be6"}, {"18", "Bxb6" ,"Bxc4+"},
                {"19", "Kg1", "Ne2+"}, {"20", "Kf1", "Nxd4+"}, {"21", "Kg1", "Ne2+"}, {"22", "Kf1", "Nc3+"}, {"23", "Kg1", "axb6"}, {"24", "Qb4", "Ra4"}, {"25", "Qxb6", "Nxd1"}, {"26", "h3", "Rxa2"}, {"27", "Kh2", "Nxf2"},
                {"28", "Re1", "Rxe1"}, {"29", "Qd8+", "Bf8"}, {"30", "Nxe1", "Bd5"}, {"31", "Nf3", "Ne4"}, {"32", "Qb8", "b5"}, {"33", "h4", "h5"}, {"34", "Ne5", "Kg7"}, {"35", "Kg1", "Bc5+"},
                {"36", "Kf1", "Ng3+"}, {"37", "Ke1", "Bb4+"}, {"38", "Kd1", "Bb3+"}, {"39", "Kc1", "Ne2+"}, {"40", "Kb1", "Nc3+"}, {"41", "Kc1", "Rc2#"}
        };
        table = new StripedTable(data);
        pane = new JScrollPane(table);
        pane.setBorder(new EmptyBorder(0,0,0,0));
        pane.setBackground(GUI.background);
        pane.getVerticalScrollBar().setUI(new FlatScrollBarUI());

        add(pane, BorderLayout.CENTER);
    }

    private static class StripedTable extends JTable {

        public StripedTable(Object[][] rowData) {
            super(rowData, new String[]{"№", "White", "Black"});
            setTableHeader(null);
            setBackground(GUI.background);
            setForeground(Color.WHITE);
            setFont(new Font("Dialog", Font.BOLD, 20));
            setRowHeight(30);
            setShowGrid(false);
        }

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component comp = super.prepareRenderer(renderer, row, column);

            if (row % 2 == 0) {
                comp.setBackground(GUI.background);
            } else {
                comp.setBackground(new Color(63, 63, 64, 255));
            }
            return comp;
        }
    }

    static class FlatScrollBarUI extends BasicScrollBarUI {
        private final Dimension d = new Dimension();

        public FlatScrollBarUI() {
            super();
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return d;
                }
            };
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return d;
                }
            };
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            JScrollBar sb = (JScrollBar) c;
            if (!sb.isEnabled() || r.width > r.height) {
                return;
            }
            sb.setBackground(GUI.background);
            g2.setPaint(Color.BLACK);
            g2.fillRoundRect(r.x, r.y, 8, r.height, 10, 10);
            g2.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            scrollbar.repaint();
        }
    }
}
