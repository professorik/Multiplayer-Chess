package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {

    public enum Pieces {
        PAWN,
        ROOK,
        BISHOP,
        KNIGHT,
        KING,
        QUEEN
    }

    private static final Image[][] chessPieceImages = new Image[2][6];
    public static final Pieces[] STARTING_ROW = {
            Pieces.ROOK,
            Pieces.KNIGHT,
            Pieces.BISHOP,
            Pieces.QUEEN,
            Pieces.KING,
            Pieces.BISHOP,
            Pieces.KNIGHT,
            Pieces.ROOK
    };

    private final Color BLACK = new Color(183, 192, 216);
    private final Color WHITE = new Color(232, 237, 249);
    private final JPanel[][] chessBoardSquares = new JPanel[8][8];
    private final boolean white;

    private JLabel pieceToMoveButton = null;
    private int xAdj;
    private int yAdj;

    public ChessBoard(boolean white) {
        super(new GridLayout(0, 8));
        this.white = white;

        setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new LineBorder(Color.WHITE, 8)
        ));

        addMouseListener(this);
        addMouseMotionListener(this);

        Color ochre = new Color(43, 43, 44);
        setBackground(ochre);

        addButtons();
        addLabels();
        setupNewGame();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pieceToMoveButton = null;
        Component c = this.findComponentAt(e.getX(), e.getY());

        if (c instanceof JPanel) return;

        Point parentLocation = c.getParent().getLocation();
        xAdj = parentLocation.x - e.getX();
        yAdj = parentLocation.y - e.getY();
        pieceToMoveButton = (JLabel)c;
        pieceToMoveButton.setLocation(e.getX() + xAdj, e.getY() + yAdj);
        pieceToMoveButton.setSize(pieceToMoveButton.getWidth(), pieceToMoveButton.getHeight());
        this.add(pieceToMoveButton, JLayeredPane.DRAG_LAYER, 0);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pieceToMoveButton == null) return;

        pieceToMoveButton.setVisible(false);
        Component c = this.findComponentAt(e.getX(), e.getY());

        Container parent;
        if (c instanceof JLabel){
            parent = c.getParent();
            parent.remove(0);
        } else {
            parent = (Container) c;
        }
        putCentered(pieceToMoveButton, parent);
        pieceToMoveButton.setVisible(true);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pieceToMoveButton == null) return;
        pieceToMoveButton.setLocation(e.getX() + xAdj, e.getY() + yAdj);
    }

    private void addButtons(){
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                JPanel square = new JPanel(new SpringLayout());
                chessBoardSquares[j][i] = square;
                add(chessBoardSquares[j][i]);

                if ((j % 2 ^ i % 2) == 0)
                    square.setBackground(WHITE);
                else
                    square.setBackground(BLACK);
            }
        }
    }

    private void addLabels(){
        for (int i = 0; i < chessBoardSquares.length; i++) {
            JLabel column = new JLabel(String.valueOf((char) (white? (int) 'a' + i: (int) 'h' - i)));
            column.setForeground(i % 2 == 0? WHITE: BLACK);
            putBottomRight(column, chessBoardSquares[i][chessBoardSquares.length - 1]);

            JLabel row = new JLabel(String.valueOf(white? 8 - i: i + 1));
            row.setForeground(i % 2 == 1? WHITE: BLACK);
            putTopLeft(row, chessBoardSquares[0][i]);
        }
    }

    private void setupNewGame() {
        int[] order = white ? new int[]{0, 1} : new int[]{1, 0};
        for (int i = 0; i < order.length; i++) {
            for (int j = 0; j < STARTING_ROW.length; j++) {
                var figure1 = new JLabel(
                        new ImageIcon(chessPieceImages[i][STARTING_ROW[white ? j : (7 - j)].ordinal() * (1 - order[i])])
                );
                putCentered(figure1, chessBoardSquares[j][6 * order[i]]);

                var figure2 = new JLabel(
                        new ImageIcon(chessPieceImages[i][STARTING_ROW[white ? j : (7 - j)].ordinal() * order[i]])
                );
                putCentered(figure2, chessBoardSquares[j][1 + 6 * order[i]]);
            }
        }
    }

    private void putBottomRight(JLabel label, JPanel panel) {
        if (!(panel.getLayout() instanceof SpringLayout layout)) return;
        layout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, panel);
        panel.add(label);
    }

    private void putTopLeft(JLabel label, JPanel panel) {
        if (!(panel.getLayout() instanceof SpringLayout layout)) return;
        layout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, panel);
        panel.add(label);
    }

    private void putCentered(JLabel label, Container panel) {
        if (!(panel.getLayout() instanceof SpringLayout layout)) return;
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, panel);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, 0, SpringLayout.VERTICAL_CENTER, panel);
        panel.add(label);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        Dimension prefSize;
        Component c = getParent();
        if (c == null) {
            prefSize = new Dimension((int) d.getWidth(), (int) d.getHeight());
        } else if (c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
            prefSize = c.getSize();
        } else {
            prefSize = d;
        }
        int w = (int) prefSize.getWidth();
        int h = (int) prefSize.getHeight();
        int s = Math.min(w, h);
        return new Dimension(s, s);
    }

    protected static void createImages() {
        try {
            BufferedImage bi = ImageIO.read(new File("D:\\IdeaProjects\\socket-chess\\src\\client\\assets\\pieces.png"));
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    chessPieceImages[i][j] = bi.getSubimage(j * 72, i * 72, 72, 72);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
