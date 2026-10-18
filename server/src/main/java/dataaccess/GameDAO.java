package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.Collection;

public interface GameDAO {
    void clear();

    void createGame(Game game);

    Game getGame(String gameID);

    Collection<Game> listGames();

    void updateGame(ChessGame game, String gameID);
}
