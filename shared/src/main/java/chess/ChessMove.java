package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public ChessMove(ChessMove other) {
        this.startPosition = other.startPosition == null ? null : new ChessPosition(other.startPosition);
        this.endPosition = other.endPosition == null ? null : new ChessPosition(other.endPosition);
        this.promotionPiece = other.promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition)
                && Objects.equals(endPosition, chessMove.endPosition)
                && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    /**
     * returns a castling move depending on the side and team
     *
     * @param team team of the castling King
     * @param side side towards which the King is moving
     * @return the castling move
     */

    public ChessMove castle(ChessGame.TeamColor team, ChessPiece.PieceType side){
        var backRow = team == ChessGame.TeamColor.WHITE ? 1 : 8;
        var movement = side == ChessPiece.PieceType.KING ? 2 : -2;
        return new ChessMove(new ChessPosition(backRow, 5), new ChessPosition(backRow, 5 + movement), null);
    }

    /**
     * Checks whether a move is a castle move
     *
     * @return piece type, KING if the move is king side, queen if queen side, null if the move is not a castle
     */

    public ChessPiece.PieceType castleSide(){
        for (ChessGame.TeamColor team: ChessGame.TeamColor.values()){
            for (ChessPiece.PieceType side: ChessPiece.SIDES){
                if (this.equals(castle(team, side))){
                    return side;
                }
            }
        }
        return null;
    }
}
