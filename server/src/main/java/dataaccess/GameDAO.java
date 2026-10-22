package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clear();

    void add(String gameName);

    Game get(int gameID);

    Collection<Game> listGames();

    void addUserToGame(int gameID, String username, ChessGame.TeamColor teamColor);
}
