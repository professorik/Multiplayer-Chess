package client.ui;

import client.Client;
import utils.Coord;
import utils.chess.Pawn;
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
import java.util.HashMap;

public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {

    private static final Image[][] chessPieceImages = new Image[2][6];
    private static final HashMap<Integer, ImageIcon> chessPieceLabels = new HashMap<>();

    private final JLayeredPane layeredPane;
    private final JPanel board;
    private final JPanel pieces;
    private final Color BLACK = new Color(183, 192, 216);
    private final Color WHITE = new Color(232, 237, 249);
    private final JPanel[][] boardSquares = new JPanel[8][8];
    private final JPanel[][] piecesSquares = new JPanel[8][8];
    private final boolean white;

    private boolean turn = false;
    private final int[][] local = new int[][]{
            {-2,-4,-3,-6,-5,-3,-4,-2},
            {-1,-1,-1,-1,-1,-1,-1,-1},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {1,1,1,1,1,1,1,1},
            {2,4,3,6,5,3,4,2},
    };
    private JLabel pieceToMoveButton = null;
    private int xAdj;
    private int yAdj;
    private int prevX;
    private int prevY;
    private int clickX = -1;
    private int clickY = -1;

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
        if (!turn) return;
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
        if (!turn || pieceToMoveButton == null) return;

        pieceToMoveButton.setVisible(false);

        Container parent = getParentByPos(e.getX(), e.getY());
        if (parent == null) {
            parent = getParentByPos(prevX, prevY);
        } else if (parent != getParentByPos(prevX, prevY)) {
            var f = getCoordByPos(prevX, prevY);
            var t = getCoordByPos(e.getX(), e.getY());

            processMove(f, t);

            prevX = e.getX();
            prevY = e.getY();
        }
        pieceToMoveButton.setVisible(true);
        putCenteredAndRepaint(pieceToMoveButton, parent);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!turn || pieceToMoveButton == null) return;
        pieceToMoveButton.setLocation(e.getX() + xAdj, e.getY() + yAdj);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!turn) return;
        if (clickX != -1) {
            var f = getCoordByPos(clickX, clickY);
            var t = getCoordByPos(e.getX(), e.getY());

            processMove(f, t);

            movePiece(f, t);
            clickX = clickY = -1;
            return;
        }
        Component c = pieces.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel tmp && tmp.getComponents().length == 0) return;

        clickX = e.getX();
        clickY = e.getY();
    }

    private void processMove(Coord f, Coord t) {
        Coord fc = new Coord(f.r, f.c), tc = new Coord(t.r, t.c);
        f.orient(white);
        t.orient(white);
        if (local[f.r][f.c] == (white? 1: -1) && tc.r == 0) {
            var figure = Popup.showPromotion();
            Client.client.move(fc, tc, figure);

            local[f.r][f.c] = figure.getPiece(white).getIndex();
            ImageIcon icon = getByCode(local[f.r][f.c]);
            pieceToMoveButton = new JLabel(icon);
        } else {
            Client.client.move(fc, tc);
        }
        local[t.r][t.c] = local[f.r][f.c];
        local[f.r][f.c] = 0;
    }

    public void movePiece(Coord from, Coord to) {
        var ptm = (JLabel) piecesSquares[from.r][from.c].getComponent(0);
        if (piecesSquares[from.r][from.c].getComponents().length > 0) {
            piecesSquares[from.r][from.c].remove(0);
        }

        if (piecesSquares[to.r][to.c].getComponents().length > 0) {
            piecesSquares[to.r][to.c].remove(0);
        }
        ptm.setVisible(true);
        putCenteredAndRepaint(ptm, piecesSquares[to.r][to.c]);
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

    private Coord getCoordByPos(int x, int y) {
        Component c = pieces.findComponentAt(x, y);
        Container parent = (Container) c;
        if (c instanceof JLabel) {
            parent = c.getParent();
        }
        for (int i = 0; i < piecesSquares.length; i++) {
            for (int j = 0; j < piecesSquares[i].length; j++) {
                if (piecesSquares[i][j] == parent) {
                    return new Coord(i, j);
                }
            }
        }
        return Coord.EMPTY;
    }

    private void addButtons() {
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                JPanel boardSq = new JPanel(new SpringLayout());
                boardSquares[i][j] = boardSq;
                board.add(boardSquares[i][j]);

                if ((i % 2 ^ j % 2) == 0)
                    boardSq.setBackground(WHITE);
                else
                    boardSq.setBackground(BLACK);

                JPanel pieceSq = new JPanel(new SpringLayout());
                pieceSq.setOpaque(false);
                piecesSquares[i][j] = pieceSq;
                pieces.add(piecesSquares[i][j]);
            }
        }
    }

    private void addLabels() {
        for (int i = 0; i < boardSquares.length; i++) {
            JLabel column = new JLabel(String.valueOf((char) (white ? (int) 'a' + i : (int) 'h' - i)));
            column.setIgnoreRepaint(true);
            column.setForeground(i % 2 == 0 ? WHITE : BLACK);
            putBottomRight(column, boardSquares[boardSquares.length - 1][i]);

            JLabel row = new JLabel(String.valueOf(white ? 8 - i : i + 1));
            row.setIgnoreRepaint(true);
            row.setForeground(i % 2 == 1 ? WHITE : BLACK);
            putTopLeft(row, boardSquares[i][0]);
        }
    }

    private void setupNewGame() {
        for (int i = 0; i < local.length; i++) {
            for (int j = 0; j < local[i].length; j++) {
                ImageIcon icon = getByCode(local[i][j]);
                if (icon == null) continue;
                JLabel figure = new JLabel(icon);
                putCentered(figure, piecesSquares[white? i: 7 - i][white? j: 7 - j]);
            }
        }
        repaint();
    }

    public void mergeBoards(int[][] board) {
        for (int i = 0; i < 8; i++) {
            var row = white? i: 7 - i;
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == local[i][j]) continue;
                local[i][j] = board[i][j];

                var sq = piecesSquares[row][white? j: 7 - j];
                if (sq.getComponents().length > 0) {
                    sq.remove(0);
                }

                ImageIcon icon = getByCode(board[i][j]);
                if (icon == null) continue;
                JLabel figure = new JLabel(icon);
                putCentered(figure, sq);
            }
        }
        repaint();
    }

    private ImageIcon getByCode(int code) {
        return chessPieceLabels.get(code);
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
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
        if (panel.getComponents().length > 0) {
            panel.remove(panel.getComponents().length - 1);
        }
        panel.add(label);
    }

    private void putCenteredAndRepaint(JLabel label, Container panel) {
        putCentered(label, panel);
        repaint();
    }

    private static void createImages() {
        try {
            BufferedImage bi = ImageIO.read(new File("src/client/assets/pieces.png"));
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 6; j++) {
                    chessPieceImages[i][j] = bi.getSubimage(j * 72, i * 72, 72, 72);
                    int index = (i == 0? -1: 1) * (j + 1);
                    chessPieceLabels.put(index, new ImageIcon(chessPieceImages[i][j]));
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
