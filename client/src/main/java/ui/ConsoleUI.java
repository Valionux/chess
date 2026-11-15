package ui;

import chess.*;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;
import facade.ServerFacades;
import model.Game;

import java.awt.*;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ConsoleUI {
    STATE state = STATE.START;
    List<Integer> gameIDs;

    final ServerFacades facade;

    public enum STATE {
        START,
        LOGGED_IN,
        LOGGED_OUT
    }

    public ConsoleUI(ServerFacades facade) {
        this.facade = facade;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print(ERASE_SCREEN);
        Menus.printMenu(STATE.START);
        state = STATE.LOGGED_OUT;
        Menus.printMenu(state);

        while (true) {
            printState();

            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();

            if (line.equalsIgnoreCase("quit")) {
                System.out.println("Have a good day!");
                break;
            }

            var args = line.split(" ");

            switch (args[0]) {
                case "help" -> Menus.printMenu(state);
                case "register" -> register(args);
                case "login" -> login(args);
                case "clear" -> clear();
                case "logout" -> logout();
                case "create" -> create(args);
                case "list" -> list();
                case "play" -> play(args);
                case "observe" -> observe(args);
                default -> System.out.println("Please provide a valid command");
            }
        }
    }

    private void observe(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException();
            }
            var games = facade.listGames();
            this.gameIDs = games.stream()
                    .map(Game::gameID)
                    .toList();
            int gameNumber = Integer.parseInt(args[1]) - 1;

            if (gameNumber < 0 || gameNumber >= games.size()) {
                throw new IllegalArgumentException();
            }

            var game = games.get(gameNumber);

            printBoard(game.game(), ChessGame.TeamColor.WHITE);
        } catch (IllegalArgumentException e) {
            System.out.println("Please provide the game number");
        } catch (UnauthorizedException e) {
            System.out.println("You must be logged in to accomplish this action");
        }
    }

    private void play(String[] args) {
        try {
            if (args.length != 3) {
                throw new IllegalArgumentException();
            }
            ChessGame.TeamColor color;
            if (args[2].equalsIgnoreCase("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (args[2].equalsIgnoreCase("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new IllegalArgumentException();
            }
            var games = facade.listGames();
            this.gameIDs = games.stream()
                    .map(Game::gameID)
                    .toList();
            int gameNumber = Integer.parseInt(args[1]) - 1;

            if (gameNumber < 0 || gameNumber >= games.size()) {
                throw new IllegalArgumentException();
            }

            var game = games.get(gameNumber);
            facade.joinGame(color, game.gameID());

            printBoard(game.game(), color);
        } catch (UnauthorizedException e) {
            System.out.println("You must be logged in to accomplish this action");
        } catch (IllegalArgumentException e) {
            System.out.println("Please provide the game number and your color");
        } catch (AlreadyTakenException e) {
            System.out.println("This team is already taken. Please join the other team or another game");
        }
    }

    private void list() {
        try {
            var games = facade.listGames();
            this.gameIDs = games.stream()
                    .map(Game::gameID)
                    .toList();
            if (!games.isEmpty()) {
                printGames(games);
            } else {
                System.out.println("There are no games in the database");
            }
        } catch (UnauthorizedException e) {
            System.out.println("You must be logged in to accomplish this action");
        }
    }

    private void create(String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide a name for the game");
            return;
        }
        try {
            facade.createGame(args[1]);
            System.out.println("Game successfully created");
        } catch (UnauthorizedException e) {
            System.out.println("You must be logged in to accomplish this action");
        }
    }

    private void logout() {
        try {
            facade.logout();
            System.out.println("Successfully logged out");
            state = STATE.LOGGED_OUT;
        } catch (UnauthorizedException e) {
            System.out.println("You must be logged in to accomplish this action");
        }
    }

    private void clear() {
        facade.clear();
        System.out.println("The database was successfully deleted");
        state = STATE.LOGGED_OUT;
    }

    private void login(String[] args) {
        if (args.length != 3) {
            System.out.println("Please provide a username and a password");
            return;
        }
        try {
            var username = facade.login(args[1], args[2]);
            state = STATE.LOGGED_IN;
            System.out.printf("Logged in as %s%n", username);
        } catch (IllegalArgumentException e) {
            System.out.println("Please provide a username and a password");
        } catch (UnauthorizedException e) {
            System.out.println("Username or password incorrect");
        }
    }

    private void register(String[] args) {
        if (args.length != 4) {
            System.out.println("Please provide a username, a password and an email");
            return;
        }
        try {
            var username = facade.register(args[1], args[2], args[3]);
            state = STATE.LOGGED_IN;
            System.out.printf("Logged in as %s%n", username);
        } catch (AlreadyTakenException e) {
            System.out.println("This username is already taken, please choose another one");
        } catch (IllegalArgumentException e) {
            System.out.println("Please provide a username, a password and an email");
        }
    }

    private void printGames(List<Game> games) {
        System.out.printf("%-10s %-10s %-20s %-20s%n", "", "Name", "White player", "Black player");
        System.out.println("----------------------------------------------------------------------------");
        for (int i = 0; i < games.size(); i++) {
            var game = games.get(i);
            System.out.printf("%-10d %-10s %-20s %-20s%n",
                    gameIDs.get(i),
                    game.gameName(),
                    game.whiteUsername(),
                    game.blackUsername());
        }
    }

    void printState() {
        System.out.printf("[%s] >>> ", state);
    }

    public static void printBoard(ChessGame game, ChessGame.TeamColor perspective) {
        ChessBoard board = game.getBoard();

        int start = (perspective == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int end = (perspective == ChessGame.TeamColor.WHITE) ? 0 : 9;
        int step = (perspective == ChessGame.TeamColor.WHITE) ? -1 : 1;

        System.out.println();

        for (int row = start; row != end; row += step) {
            System.out.print(row + " ");

            for (int col = 1; col <= 8; col++) {
                int actualCol = (perspective == ChessGame.TeamColor.WHITE) ? col : 9 - col;

                ChessPosition position = new ChessPosition(row, actualCol);
                ChessPiece piece = board.getPiece(position);

                printSquare(piece, row, actualCol);
            }

            System.out.println(EscapeSequences.RESET_BG_COLOR);
        }

        System.out.println("   a  b  c  d  e  f  g  h");
    }

    private static void printSquare(ChessPiece piece, int row, int col) {
        boolean lightSquare = (row + col) % 2 == 0;

        String background = lightSquare
                ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY
                : EscapeSequences.SET_BG_COLOR_DARK_GREY;

        String symbol = EscapeSequences.EMPTY;

        if (piece != null) {
            symbol = getPieceSymbol(piece);
        }

        System.out.print(background + symbol + EscapeSequences.RESET_TEXT_COLOR);
    }

    private static String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
        };
    }
}
