package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard gameBoard = new ChessBoard();

    private ChessMove lastMove = null;

    public ChessGame() {

        gameBoard.resetBoard();

    }

    public ChessGame(ChessGame other){
        this.teamTurn = other.teamTurn;
        this.gameBoard = new ChessBoard(other.gameBoard);
        this.lastMove = other.lastMove;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = gameBoard.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        var possibleMoves = piece.pieceMoves(gameBoard, startPosition);
        possibleMoves.removeIf(move -> {
            var testGame = new ChessGame(this);
            testGame.makeAnyMove(move);
            return testGame.isInCheck(piece.getTeamColor());
        });

        //Special moves

        if (piece.getPieceType() == ChessPiece.PieceType.KING || piece.getPieceType() == ChessPiece.PieceType.ROOK){
            possibleMoves.addAll(allowedCastles(piece.getTeamColor()));
        }

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
            possibleMoves.addAll(allowedEnPassants(piece.getTeamColor(), startPosition.getColumn()));
        }

        return possibleMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var startPosition = move.getStartPosition();
        var piece = gameBoard.getPiece(startPosition);
        var legalMoves = validMoves(startPosition);
        if (piece == null){
            throw new InvalidMoveException("There us no piece on this square");
        } else if (getTeamTurn() != piece.getTeamColor()) {
            throw new InvalidMoveException("It is not this team's turn");
        } else if (!legalMoves.contains(move)){
            throw new InvalidMoveException("This move is not legal");
        }

        makeAnyMove(move);

        if (teamTurn == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }else{
            setTeamTurn(TeamColor.BLACK);
        }

        piece.setHasMoved();

        lastMove = move;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        var kingPosition = gameBoard.findKing(teamColor);
        for(int row = 1; row < 9; ++row){
            for(int col = 1; col < 9; ++col){
                var position = new ChessPosition(row, col);
                ChessPiece piece = gameBoard.getPiece(position);
                if(piece != null && piece.getTeamColor() != teamColor){
                    for(ChessMove move: piece.pieceMoves(gameBoard, position)){
                        if(move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, gameBoard);
    }


    /**
     * Makes a move in the game, regardless of whether it is legal
     *
     * @param move chess move to perform
     */

    private void makeAnyMove(ChessMove move){
        var startPosition = move.getStartPosition();
        var endPosition = move.getEndPosition();
        var promotionPiece = move.getPromotionPiece();
        var piece = gameBoard.getPiece(startPosition);

        //Special moves

        var castleSide = move.castleSide();
        if(castleSide != null){
            castleRookMove(piece.getTeamColor(), castleSide);
        }

        if(isEnPassant(move)){
            gameBoard.addPiece(new ChessPosition(startPosition.getRow(), endPosition.getColumn()), null);
        }

        if (promotionPiece == null){
            gameBoard.addPiece(endPosition, piece);
        }else{
            gameBoard.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), promotionPiece));
        }
        gameBoard.addPiece(move.getStartPosition(), null);
    }

    /**
     * Checks whether a team has no legal move.
     *
     * @param teamColor team which legal moves have to be checked
     * @return a boolean, true if there are no legal moves, false if not
     */

    private boolean noValidMoves(TeamColor teamColor){
        for (int row = 1; row < 9; ++row){
            for (int col = 1; col < 9; ++col){
                var position = new ChessPosition(row, col);
                var piece = gameBoard.getPiece(position);
                if (piece == null || piece.getTeamColor() != teamColor){
                    continue;
                }
                if (!validMoves(position).isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Performs the rook's move in a castle
     *
     * @param team team the rook belongs to
     * @param side side the rook is on (KING or QUEEN)
     */

    private void castleRookMove(TeamColor team, ChessPiece.PieceType side){
        var backRow = team == ChessGame.TeamColor.WHITE ? 1 : 8;
        var col = side == ChessPiece.PieceType.KING ? 8 : 1;
        var movement = side == ChessPiece.PieceType.KING ? -2 : 3;
        makeAnyMove(new ChessMove(new ChessPosition(backRow, col), new ChessPosition(backRow, col + movement), null));
    }

    /**
     * Gives a list of the allowed castle moves for a certain team.
     *
     * @param team team which castling rights we're examining
     * @return an empty list, kingside castle, queenside castle or both
     */

    private Collection<ChessMove> allowedCastles(TeamColor team){
        ArrayList<ChessMove> castles = new ArrayList<>();
        var backRow = team == ChessGame.TeamColor.WHITE ? 1 : 8;
        var kingPosition = new ChessPosition(backRow, 5);
        var king = gameBoard.getPiece(kingPosition);
        if(king != null && king.getPieceType() == ChessPiece.PieceType.KING && !king.getHasMoved() && !isInCheck(team)){
            for(int i = - 1; i < 2; i += 2){
                var rookPosition = new ChessPosition(backRow, i == - 1 ? 1 : 8);
                var rook = gameBoard.getPiece(rookPosition);
                if(rook == null || rook.getPieceType() != ChessPiece.PieceType.ROOK || rook.getHasMoved()){
                    continue;
                }
                var halfway = new ChessPosition(backRow, 5 + i);
                var endPosition = new ChessPosition(backRow, 5 + 2*i);
                var nextToRook = i == - 1 ? new ChessPosition(backRow, 2) : endPosition;
                if(gameBoard.getPiece(halfway) != null || gameBoard.getPiece(nextToRook) != null || gameBoard.getPiece(endPosition) != null){
                    continue;
                }
                var testGame = new ChessGame(this);
                testGame.makeAnyMove(new ChessMove(kingPosition, halfway, null));
                if(!testGame.isInCheck(team)){
                    testGame.makeAnyMove(new ChessMove(halfway, endPosition, null));
                    if(!testGame.isInCheck(team)){
                        castles.add(new ChessMove(kingPosition, endPosition, null));
                    }
                }
            }
        }
        return castles;
    }

    private boolean isEnPassant(ChessMove move){
        var thisPawn = gameBoard.getPiece(move.getStartPosition());
        var enemyPawnPosition = new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn());
        var enemyPawn = gameBoard.getPiece(enemyPawnPosition);
        if(enemyPawn == null || thisPawn == null){
            return false;
        }
        var enemyBackRow = enemyPawn.getTeamColor() == TeamColor.WHITE ? 2 : 7;
        var enemyPawnLastPosition = new ChessPosition(enemyBackRow, enemyPawnPosition.getColumn());
        return thisPawn.getPieceType() == ChessPiece.PieceType.PAWN
                && enemyPawn.getPieceType() == ChessPiece.PieceType.PAWN
                && enemyPawn.getTeamColor() != thisPawn.getTeamColor()
                && (lastMove == null ||
                lastMove.getEndPosition().equals(enemyPawnPosition)
                        && lastMove.getStartPosition().equals(enemyPawnLastPosition)); // Checks whether the last move was the enemy pawn moving forward
    }

    private Collection<ChessMove> allowedEnPassants(TeamColor team, int col){
        var enPassants = new ArrayList<ChessMove>();
        int row = team == TeamColor.WHITE ? 5 : 4;
        int forward = team == TeamColor.WHITE ? 1 : -1;
        var startPosition = new ChessPosition(row, col);
        for(int i = - 1; i < 2; i += 2){
            var endPosition = new ChessPosition(row + forward, col + i);
            if(!endPosition.isValid()){
                continue;
            }
            var enPassant = new ChessMove(startPosition, endPosition, null);
            if(isEnPassant(enPassant)){
                enPassants.add(enPassant);
            }
        }
        return enPassants;
    }
}
