package client.ui;

import client.Client;
import client.hudcmd.MoveCmd;
import utils.chess.Pieces;

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

    private final JLayeredPane layeredPane;
    private final JPanel board;
    private final JPanel pieces;
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
    private final JPanel[][] boardSquares = new JPanel[8][8];
    private final JPanel[][] piecesSquares = new JPanel[8][8];
    private final boolean white;

    private JLabel pieceToMoveButton = null;
    private int xAdj;
    private int yAdj;
    private int prevX;
    private int prevY;

    static {
        createImages();
    }

    public ChessBoard(boolean white) {
        super(new BorderLayout());
        Dimension boardSize = new Dimension(700, 700);

        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addComponentListener(new AdaptiveBoard());

        pieces = new JPanel();
        layeredPane.add(pieces, JLayeredPane.PALETTE_LAYER);
        pieces.setOpaque(false);
        pieces.setLayout(new GridLayout(0, 8));
        pieces.setPreferredSize(boardSize);
        pieces.setMinimumSize(boardSize);

        board = new JPanel();
        layeredPane.add(board, JLayeredPane.DEFAULT_LAYER);
        board.setLayout(new GridLayout(0, 8));
        board.setPreferredSize(boardSize);
        board.setMinimumSize(boardSize);

        this.white = white;

        setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new LineBorder(Color.WHITE, 8)
        ));

        addMouseListener(this);
        addMouseMotionListener(this);

        setBackground(GUI.background);

        addButtons();
        addLabels();
        setupNewGame();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pieceToMoveButton = null;
        Component c = pieces.findComponentAt(e.getX(), e.getY());

        if (c instanceof JPanel) return;

        Point parentLocation = c.getParent().getLocation();
        xAdj = parentLocation.x - e.getX();
        yAdj = parentLocation.y - e.getY();
        pieceToMoveButton = (JLabel) c;
        pieceToMoveButton.setLocation(e.getX() + xAdj, e.getY() + yAdj);
        prevX = parentLocation.x;
        prevY = parentLocation.y;
        pieceToMoveButton.setSize(pieceToMoveButton.getWidth(), pieceToMoveButton.getHeight());
        layeredPane.add(pieceToMoveButton, JLayeredPane.DRAG_LAYER);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pieceToMoveButton == null) return;

        pieceToMoveButton.setVisible(false);

        Container parent = getParentByPos(e.getX(), e.getY());
        if (parent == null) {
            parent = getParentByPos(prevX, prevY);
        } else if (parent != getParentByPos(prevX, prevY)) {
            new MoveCmd(Client.client, getCoordsByPos(prevX, prevY), getCoordsByPos(e.getX(), e.getY())).execute();
            prevX = e.getX();
            prevY = e.getY();
        }
        pieceToMoveButton.setVisible(true);
        putCentered(pieceToMoveButton, parent);
    }

    public void movePiece(int from, int to) {
        int f_x = from / 8, f_y = from % 8;
        int t_x = to / 8, t_y = to % 8;
        var ptm = (JLabel) piecesSquares[f_x][f_y].getComponent(0);
        piecesSquares[f_x][f_y].remove(0);

        if (piecesSquares[t_x][t_y].getComponents().length > 0) {
            piecesSquares[t_x][t_y].remove(0);
        }
        ptm.setVisible(true);
        putCentered(ptm, piecesSquares[t_x][t_y]);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pieceToMoveButton == null) return;
        pieceToMoveButton.setLocation(e.getX() + xAdj, e.getY() + yAdj);
    }

    private Container getParentByPos(int x, int y) {
        Component c = pieces.findComponentAt(x, y);
        Container parent;
        if (c instanceof JLabel) {
            parent = c.getParent();
        } else {
            parent = (Container) c;
        }
        return parent;
    }

    private int getCoordsByPos(int x, int y) {
        Component c = pieces.findComponentAt(x, y);
        Container parent = (Container) c;
        if (c instanceof JLabel) {
            parent = c.getParent();
        }
        for (int i = 0; i < piecesSquares.length; i++) {
            for (int j = 0; j < piecesSquares.length; j++) {
                if (piecesSquares[i][j] == parent) {
                    return 63 - i * 8 - j;
                }
            }
        }
        return -1;
    }

    private void addButtons() {
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                JPanel boardSq = new JPanel(new SpringLayout());
                boardSquares[j][i] = boardSq;
                board.add(boardSquares[j][i]);

                if ((j % 2 ^ i % 2) == 0)
                    boardSq.setBackground(WHITE);
                else
                    boardSq.setBackground(BLACK);

                JPanel pieceSq = new JPanel(new SpringLayout());
                pieceSq.setOpaque(false);
                piecesSquares[j][i] = pieceSq;
                pieces.add(piecesSquares[j][i]);
            }
        }
    }

    private void addLabels() {
        for (int i = 0; i < boardSquares.length; i++) {
            JLabel column = new JLabel(String.valueOf((char) (white ? (int) 'a' + i : (int) 'h' - i)));
            column.setIgnoreRepaint(true);
            column.setForeground(i % 2 == 0 ? WHITE : BLACK);
            putBottomRight(column, boardSquares[i][boardSquares.length - 1]);

            JLabel row = new JLabel(String.valueOf(white ? 8 - i : i + 1));
            row.setForeground(i % 2 == 1 ? WHITE : BLACK);
            row.setIgnoreRepaint(true);
            putTopLeft(row, boardSquares[0][i]);
        }
    }

    private void setupNewGame() {
        int[] order = white ? new int[]{0, 1} : new int[]{1, 0};
        for (int i = 0; i < order.length; i++) {
            for (int j = 0; j < STARTING_ROW.length; j++) {
                var figure1 = new JLabel(
                        new ImageIcon(chessPieceImages[i][STARTING_ROW[white ? j : (7 - j)].ordinal() * (1 - order[i])])
                );
                putCentered(figure1, piecesSquares[j][6 * order[i]]);

                var figure2 = new JLabel(
                        new ImageIcon(chessPieceImages[i][STARTING_ROW[white ? j : (7 - j)].ordinal() * order[i]])
                );
                putCentered(figure2, piecesSquares[j][1 + 6 * order[i]]);
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

    private static void createImages() {
        try {
            BufferedImage bi = ImageIO.read(new File("src/client/assets/pieces.png"));
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

    private class AdaptiveBoard implements ComponentListener {
        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            board.setPreferredSize(e.getComponent().getSize());
            board.setMaximumSize(e.getComponent().getSize());
            board.setSize(e.getComponent().getSize());
            board.revalidate();
            board.repaint();

            pieces.setPreferredSize(e.getComponent().getSize());
            pieces.setMaximumSize(e.getComponent().getSize());
            pieces.setSize(e.getComponent().getSize());
            pieces.revalidate();
            pieces.repaint();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }
    }
}
