package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Adds a move to a list of moves
     *
     */

    private void AddMove(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, PieceType promotionType){
        if (endPosition.isValid() && board.getPiece(endPosition) == null) {
            moves.add(new ChessMove(startPosition, endPosition, promotionType));
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        switch (this.getPieceType()){
            case KING:
                for (int i = -1; i < 2; ++i) {
                    for (int j = -1; j < 2; ++j) {
                        var position = new ChessPosition(row + i, col + j);
                        AddMove(moves, board, myPosition, position, PieceType.PAWN);
                    }
                }
            case QUEEN:
                var bishop = new ChessPiece(pieceColor, PieceType.BISHOP);
                var rook = new ChessPiece(pieceColor, PieceType.ROOK);
                moves.addAll(bishop.pieceMoves(board, myPosition));
                moves.addAll(rook.pieceMoves(board, myPosition));
            case ROOK:
                for (int i = 0; i < 8; ++i){
                    var throughRows = new ChessPosition(i, col);
                    var throughColumns = new ChessPosition(row, i);
                    AddMove(moves, board, myPosition, throughColumns, PieceType.PAWN);
                    AddMove(moves, board, myPosition, throughRows, PieceType.PAWN);
                }
            case BISHOP:
                for (int i = -7; i < 8; ++i){
                    var leftToRight = new ChessPosition(row + i, col + i);
                    var rightToLeft = new ChessPosition(row - i, col + i);
                    AddMove(moves, board, myPosition, leftToRight, PieceType.PAWN);
                    AddMove(moves, board, myPosition, rightToLeft, PieceType.PAWN);
                }
            case KNIGHT:
                for (int i = - 2; i < 3; i += 4){
                    for (int j = -1; j < 2; j += 2){
                        var standingL = new ChessPosition(row + i, col + j);
                        var lyingL = new ChessPosition(row + j, col + i);
                        AddMove(moves, board, myPosition, standingL, PieceType.PAWN);
                        AddMove(moves, board, myPosition, lyingL, PieceType.PAWN);
                    }
                }
            case PAWN:
                switch (row){
                    case 1:
                        AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), PieceType.PAWN);
                        AddMove(moves, board, myPosition, new ChessPosition(row + 2, col), PieceType.PAWN);
                    case 6:
                        AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), PieceType.QUEEN);
                        AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), PieceType.ROOK);
                        AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), PieceType.BISHOP);
                        AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), PieceType.KNIGHT);
                    default:
                        AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), PieceType.PAWN);
                }
        }
        return moves;
    }
}
