package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:50
 * @project socket-chess
 */
public class Board {

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
    Spot[][] boxes = new Spot[STARTING_ROW.length][STARTING_ROW.length];

    public Board() {
        this.resetBoard();
    }

    public Spot getBox(int x, int y) throws Exception {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new Exception("Index out of bound");
        }
        return boxes[y][x];
    }

    public void resetBoard() {
        for (int j = 0; j < STARTING_ROW.length; j++) {
            boxes[0][j] = new Spot(j, 0, fabric(STARTING_ROW[j], true));
            boxes[1][j] = new Spot(j, 1, new Pawn(true));

            for (int i = 2; i < 6; i++) {
                boxes[i][j] = new Spot(j, i, null);
            }

            boxes[6][j] = new Spot(j, 6, new Pawn(false));
            boxes[7][j] = new Spot(j, 7, fabric(STARTING_ROW[j], false));
        }
    }

    private Piece fabric(Pieces piece, boolean white) {
        return switch (piece){
            case PAWN -> new Pawn(white);
            case BISHOP -> new Bishop(white);
            case KNIGHT -> new Knight(white);
            case ROOK -> new Rook(white);
            case QUEEN -> new Queen(white);
            case KING -> new King(white);
        };
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].length; j++) {
                sb.append(boxes[i][j].toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
