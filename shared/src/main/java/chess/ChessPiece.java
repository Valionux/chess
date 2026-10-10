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

    public int getBackRow() {
        return this.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : 8;
    }

    public int getForward() {
        return this.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
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
        return switch (type) {
            case KING -> new King(pieceColor).pieceMoves(board, myPosition);
            case QUEEN -> new Queen(pieceColor).pieceMoves(board, myPosition);
            case ROOK -> new Rook(pieceColor).pieceMoves(board, myPosition);
            case BISHOP -> new Bishop(pieceColor).pieceMoves(board, myPosition);
            case KNIGHT -> new Knight(pieceColor).pieceMoves(board, myPosition);
            case PAWN -> new Pawn(pieceColor).pieceMoves(board, myPosition);
        };
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

    protected void addMove(ArrayList<ChessMove> moves, ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, PieceType promotionType){
        if (endPosition.isValid() && (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != this.getTeamColor())) {
            moves.add(new ChessMove(startPosition, endPosition, promotionType));
        }
    }
}

class King extends ChessPiece {

    public King(ChessGame.TeamColor pieceColor){
        super(pieceColor, PieceType.KING);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                ChessPosition position = new ChessPosition(row + i, col + j);
                addMove(moves, board, myPosition, position, null);
            }
        }
        return moves;
    }
}

class Queen extends ChessPiece {

    public Queen(ChessGame.TeamColor pieceColor){
        super(pieceColor, PieceType.QUEEN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();
        Rook rook = new Rook(this.getTeamColor());
        Bishop bishop = new Bishop(this.getTeamColor());
        moves.addAll(rook.pieceMoves(board, myPosition));
        moves.addAll(bishop.pieceMoves(board, myPosition));
        return moves;
    }
}

class Rook extends ChessPiece {

    public Rook(ChessGame.TeamColor pieceColor){
        super(pieceColor, PieceType.ROOK);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {
                {-1, 0},  // up
                {1, 0},   // down
                {0, -1},  // left
                {0, 1}    // right
        };

        for (int[] dir : directions) {
            int rookRow = row;
            int rookCol = col;
            boolean rookStop = false;

            while (!rookStop) {
                rookRow += dir[0];
                rookCol += dir[1];
                ChessPosition position = new ChessPosition(rookRow, rookCol);
                addMove(moves, board, myPosition, position, null);
                rookStop = !position.isValid() || board.getPiece(position) != null;
            }
        }
        return moves;
    }
}

class Bishop extends ChessPiece {

    public Bishop(ChessGame.TeamColor pieceColor){
        super(pieceColor, PieceType.BISHOP);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {
                {-1, -1},   // up and left
                {-1,  1},   // up and right
                { 1, -1},   // down and left
                { 1,  1}    // up and right
        };

        for (int[] dr: directions){
            int bishopRow = row;
            int bishopCol = col;
            boolean bishopStop = false;
            while (!bishopStop){
                bishopRow += dr[0];
                bishopCol += dr[1];
                ChessPosition position = new ChessPosition(bishopRow, bishopCol);
                addMove(moves, board, myPosition, position, null);
                bishopStop = !position.isValid() || board.getPiece(position) != null;
            }
        }
        return moves;
    }
}

class Knight extends ChessPiece {

    public Knight(ChessGame.TeamColor pieceColor){
        super(pieceColor, PieceType.KNIGHT);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] offsets = {
                {-2, -1},
                {-2,  1},
                {-1, -2},
                {-1,  2},
                { 1, -2},
                { 1,  2},
                { 2, -1},
                { 2,  1}
        };

        for (int[] offset: offsets){
            addMove(moves, board, myPosition, myPosition.addOffset(offset), null);
        }

        return moves;
    }
}

class Pawn extends ChessPiece {

    public Pawn(ChessGame.TeamColor pieceColor){
        super(pieceColor, PieceType.PAWN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startingRow = getBackRow() + getForward();
        int[][] directions = {
                {getForward()     ,  0},
                {getForward() * 2 ,  0},
                {getForward()     ,  1},
                {getForward()     , -1}
        };

        var endPosition = myPosition.addOffset(directions[0]);
        if (endPosition.isValid() && board.getPiece(endPosition) == null){
            addPawnMove(moves, board, myPosition, endPosition);
        }

        ChessPosition doubleForward = myPosition.addOffset(directions[1]);

        if (myPosition.getRow() == startingRow
                && doubleForward.isValid()
                && board.getPiece(endPosition) == null
                && board.getPiece(doubleForward) == null) {
            addPawnMove(moves, board, myPosition, doubleForward);
        }

        for (int i = 2; i < 4; ++i){
            endPosition = myPosition.addOffset(directions[i]);
            if (!endPosition.isValid()){
                continue;
            }
            var targetPiece = board.getPiece(endPosition);
            if (targetPiece != null
                    && targetPiece.getTeamColor() != getTeamColor()){
                addPawnMove(moves, board, myPosition, endPosition);
            }
        }
        return moves;
    }

    private void addPawnMove(ArrayList<ChessMove> moves,
                             ChessBoard board,
                             ChessPosition startPosition,
                             ChessPosition endPosition) {

        int promotionRow = getBackRow() + 7 * getForward();

        if (endPosition.getRow() == promotionRow) {
            addMove(moves, board, startPosition, endPosition, PieceType.QUEEN);
            addMove(moves, board, startPosition, endPosition, PieceType.ROOK);
            addMove(moves, board, startPosition, endPosition, PieceType.BISHOP);
            addMove(moves, board, startPosition, endPosition, PieceType.KNIGHT);
        } else {
            addMove(moves, board, startPosition, endPosition, null);
        }
    }
}