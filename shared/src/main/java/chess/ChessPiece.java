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
    private boolean hasMoved;

    public static final PieceType[] SIDES = {PieceType.KING, PieceType.QUEEN};

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.hasMoved = false;
    }

    public ChessPiece(ChessPiece other){
        this.pieceColor = other.pieceColor;
        this.type = other.type;
        this.hasMoved = other.hasMoved;
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

    public boolean getHasMoved(){
        return hasMoved;
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
                boolean rookStop = false;
                while (!rookStop){
                    rookRow += 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                }
                rookRow = row;
                rookStop = false;
                while (!rookStop){
                    rookRow -= 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                }
                rookRow = row;
                rookStop = false;
                while (!rookStop){
                    rookCol += 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                }
                rookCol = col;
                rookStop = false;
                while (!rookStop){
                    rookCol -= 1;
                    position = new ChessPosition(rookRow, rookCol);
                    AddMove(moves, board, myPosition, position, null);
                    rookStop = !position.isValid() || board.getPiece(position) != null;
                }
                break;
            case BISHOP:
                int bishopRow = row;
                int bishopCol = col;
                boolean bishopStop = false;
                while (!bishopStop){
                    bishopRow += 1;
                    bishopCol += 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                }
                bishopRow = row;
                bishopCol = col;
                bishopStop = false;
                while (!bishopStop){
                    bishopRow += 1;
                    bishopCol -= 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                }
                bishopRow = row;
                bishopCol = col;
                bishopStop = false;
                while (!bishopStop){
                    bishopRow -= 1;
                    bishopCol += 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                }
                bishopRow = row;
                bishopCol = col;
                bishopStop = false;
                while (!bishopStop){
                    bishopRow -= 1;
                    bishopCol -= 1;
                    position = new ChessPosition(bishopRow, bishopCol);
                    AddMove(moves, board, myPosition, position, null);
                    bishopStop = !position.isValid() || board.getPiece(position) != null;
                }
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
                int startingRow;
                int promotionRow;

                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    increment = 1;
                    startingRow = 2;
                    promotionRow = 7;
                } else {
                    increment = -1;
                    startingRow = 7;
                    promotionRow = 2;
                }

                if (row == promotionRow) {
                    for (PieceType promotionType : PieceType.values()) {
                        if (promotionType == PieceType.KING || promotionType == PieceType.PAWN) {
                            continue;
                        }

                        ChessPosition forward = new ChessPosition(row + increment, col);
                        if (forward.isValid() && board.getPiece(forward) == null) {
                            AddMove(moves, board, myPosition, forward, promotionType);
                        }

                        ChessPosition captureRight = new ChessPosition(row + increment, col + 1);
                        if (captureRight.isValid()
                                && board.getPiece(captureRight) != null
                                && board.getPiece(captureRight).getTeamColor() != this.getTeamColor()) {
                            AddMove(moves, board, myPosition, captureRight, promotionType);
                        }

                        ChessPosition captureLeft = new ChessPosition(row + increment, col - 1);
                        if (captureLeft.isValid()
                                && board.getPiece(captureLeft) != null
                                && board.getPiece(captureLeft).getTeamColor() != this.getTeamColor()) {
                            AddMove(moves, board, myPosition, captureLeft, promotionType);
                        }
                    }
                } else {
                    ChessPosition forward = new ChessPosition(row + increment, col);

                    if (forward.isValid() && board.getPiece(forward) == null) {
                        AddMove(moves, board, myPosition, forward, null);

                        if (row == startingRow) {
                            ChessPosition doubleForward = new ChessPosition(row + 2 * increment, col);

                            if (doubleForward.isValid() && board.getPiece(doubleForward) == null) {
                                AddMove(moves, board, myPosition, doubleForward, null);
                            }
                        }
                    }

                    ChessPosition captureRight = new ChessPosition(row + increment, col + 1);
                    if (captureRight.isValid()
                            && board.getPiece(captureRight) != null
                            && board.getPiece(captureRight).getTeamColor() != this.getTeamColor()) {
                        AddMove(moves, board, myPosition, captureRight, null);
                    }

                    ChessPosition captureLeft = new ChessPosition(row + increment, col - 1);
                    if (captureLeft.isValid()
                            && board.getPiece(captureLeft) != null
                            && board.getPiece(captureLeft).getTeamColor() != this.getTeamColor()) {
                        AddMove(moves, board, myPosition, captureLeft, null);
                    }
                }
                break;
        }
        return moves;
    }

    /**
     * mark a piece as having moved
     */

    public void setHasMoved(){
        this.hasMoved = true;
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
}
