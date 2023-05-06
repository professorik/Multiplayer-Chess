package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:24
 * @project socket-chess
 */
public enum GameStatus {
    ACTIVE,
    MATE_BLACK_WIN,
    MATE_WHITE_WIN,
    FORFEIT_BLACK,
    FORFEIT_WHITE,
    STALEMATE,
    RESIGNATION,
    NOT_ENOUGH_FIGURES
}
