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
        if (endPosition.isValid() && (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != this.getTeamColor())) {
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
        ChessPosition position;
        switch (this.getPieceType()){
            case KING:
                for (int i = -1; i < 2; ++i) {
                    for (int j = -1; j < 2; ++j) {
                        position = new ChessPosition(row + i, col + j);
                        AddMove(moves, board, myPosition, position, null);
                    }
                }
                break;
            case QUEEN:
                var bishop = new ChessPiece(pieceColor, PieceType.BISHOP);
                var rook = new ChessPiece(pieceColor, PieceType.ROOK);
                moves.addAll(bishop.pieceMoves(board, myPosition));
                moves.addAll(rook.pieceMoves(board, myPosition));
                break;
            case ROOK:
                int rookRow = row;
                int rookCol = col;
                position = new ChessPosition(rookRow, rookCol);
                boolean rookStop = false;
                while (!rookStop){
                    rookRow += 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                };
                rookRow = row;
                rookStop = false;
                while (!rookStop){
                    rookRow -= 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                };
                rookRow = row;
                rookStop = false;
                while (!rookStop){
                    rookCol += 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                };
                rookCol = col;
                rookStop = false;
                while (!rookStop){
                    rookCol -= 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                };
                break;
            case BISHOP:
                int bishopRow = row;
                int bishopCol = col;
                position = new ChessPosition(bishopRow, bishopCol);
                boolean bishopStop = false;
                while (!bishopStop){
                    bishopRow += 1;
                    bishopCol += 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                };
                bishopRow = row;
                bishopCol = col;
                bishopStop = false;
                while (!bishopStop){
                    bishopRow += 1;
                    bishopCol -= 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                };
                bishopRow = row;
                bishopCol = col;
                bishopStop = false;
                while (!bishopStop){
                    bishopRow -= 1;
                    bishopCol += 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                };
                bishopRow = row;
                bishopCol = col;
                bishopStop = false;
                while (!bishopStop){
                    bishopRow -= 1;
                    bishopCol -= 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                };
                break;
            case KNIGHT:
                for (int i = - 2; i < 3; i += 4){
                    for (int j = -1; j < 2; j += 2){
                        var standingL = new ChessPosition(row + i, col + j);
                        var lyingL = new ChessPosition(row + j, col + i);
                        AddMove(moves, board, myPosition, standingL, null);
                        AddMove(moves, board, myPosition, lyingL, null);
                    }
                }
                break;
            case PAWN:
                int increment;
                int secondRow;
                int lastRow;
                if (this.getTeamColor() == ChessGame.TeamColor.WHITE){
                    increment = 1;
                    secondRow = 2;
                    lastRow = 8;
                } else {
                    increment = -1;
                    secondRow = 7;
                    lastRow = 1;
                }
                switch (row){
                    case secondRow:
                        if (board.getPiece(new ChessPosition(3, col)) == null){
                            AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), null);
                            if (board.getPiece(new ChessPosition(4, col)) == null){
                                AddMove(moves, board, myPosition, new ChessPosition(row + 2, col), null);
                            }
                        }
                        position = new ChessPosition(3, col + 1);
                        if (position.isValid() && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                            AddMove(moves, board, myPosition, new ChessPosition(row + 1, col + 1), null);
                        }
                        position = new ChessPosition(3, col - 1);
                        if (position.isValid() && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                            AddMove(moves, board, myPosition, new ChessPosition(row + 1, col - 1), null);
                        }
                        break;
                    case 7:
                        for (PieceType promotionType: PieceType.values()){
                            if (promotionType == PieceType.KING || promotionType == PieceType.PAWN){
                                continue;
                            }
                            if (board.getPiece(new ChessPosition(row + 1, col)) == null){
                                AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), promotionType);
                            }
                            position = new ChessPosition(row + 1, col + 1);
                            if (position.isValid() && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                                AddMove(moves, board, myPosition, new ChessPosition(row + 1, col + 1), promotionType);
                            }
                            position = new ChessPosition(row + 1, col - 1);
                            if (position.isValid() && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                                AddMove(moves, board, myPosition, new ChessPosition(row + 1, col - 1), promotionType);
                            }
                        }
                        break;
                    default:
                        if (board.getPiece(new ChessPosition(row + 1, col)) == null){
                            AddMove(moves, board, myPosition, new ChessPosition(row + 1, col), null);
                        }
                        position = new ChessPosition(row + 1, col + 1);
                        if (position.isValid() && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                            AddMove(moves, board, myPosition, new ChessPosition(row + 1, col + 1), null);
                        }
                        position = new ChessPosition(row + 1, col - 1);
                        if (position.isValid() && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                            AddMove(moves, board, myPosition, new ChessPosition(row + 1, col - 1), null);
                        }
                }
        }
        return moves;
    }
}
