package client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author professorik
 * @created 17/03/2023 - 14:21
 * @project socket-chess
 */
public class Ledger extends JPanel {

    private final JTable table;
    private boolean white;
    private final ArrayList<String[]> data;

    public Ledger() {
        super(new BorderLayout());
        setBackground(GUI.background);

        setBorder(new EmptyBorder(10, 10, 10, 10));
        white = true;
        data = new ArrayList<>();
        data.add(new String[]{"", "White", "Black"});
        table = new StripedTable(getDataVector());

        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(new EmptyBorder(0, 0, 0, 0));
        pane.getViewport().setBackground(GUI.background);
        pane.getVerticalScrollBar().setUI(new FlatScrollBarUI());

        add(pane, BorderLayout.CENTER);
    }

    public void addLabel(String label) {
        if (white) {
            data.add(new String[]{String.valueOf(data.size()), label, ""});
        } else {
            data.get(data.size() - 1)[2] = label;
        }
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(getDataVector(), new Object[]{"№", "White", "Black"});
        table.setModel(model);
        white = !white;
    }

    private Object[][] getDataVector() {
        Object[][] vector = new Object[data.size()][3];
        for (int i = 0; i < data.size(); i++) {
            System.arraycopy(data.get(i), 0, vector[i], 0, 3);
        }
        return vector;
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
