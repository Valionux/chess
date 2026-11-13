package client;

import chess.ChessGame;
import client.model.request.CreateRequest;
import client.model.request.JoinRequest;
import client.model.request.LoginRequest;
import client.model.request.RegisterRequest;
import client.model.response.CreateResponse;
import client.model.response.ListGameResponse;
import client.model.response.LoginResponse;
import com.google.gson.Gson;
import exceptions.ServerException;
import model.Game;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ServerFacade implements ServerFacades {

    private final HttpClient httpClient;
    private final String serverUrl;
    private final Gson gson = new Gson();
    private String authToken;

    public ServerFacade(String serverUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.serverUrl = serverUrl;
    }

    @Override
    public String register(String username, String password, String email) throws AlreadyTakenException, UnauthorizedException {
        try {
            String body = gson.toJson(new RegisterRequest(username, password, email));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/user"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                LoginResponse result = gson.fromJson(
                        response.body(),
                        LoginResponse.class
                );

                this.authToken = result.authToken();
                return username;
            } else {
                errorHandler(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
        return null;
    }

    @Override
    public String login(String username, String password) throws UnauthorizedException, AlreadyTakenException {
        try {
            String body = gson.toJson(new LoginRequest(username, password));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/session"))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                LoginResponse result = gson.fromJson(
                        response.body(),
                        LoginResponse.class
                );

                this.authToken = result.authToken();
                return username;
            } else {
                errorHandler(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
        return null;
    }

    @Override
    public void logout() throws UnauthorizedException, AlreadyTakenException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/session"))
                    .header("Authorization", authToken)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                errorHandler(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with the server");
        }
    }

    @Override
    public void clear() throws UnauthorizedException, AlreadyTakenException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/db"))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                errorHandler(response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
    }

    @Override
    public int createGame(String gameName) throws UnauthorizedException, AlreadyTakenException {
        try {
            String body = gson.toJson(new CreateRequest(gameName));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/game"))
                    .header("Authorization", authToken)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                CreateResponse result = gson.fromJson(
                        response.body(),
                        CreateResponse.class
                );

                return result.gameID();
            } else {
                errorHandler(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
        return 0;
    }

    @Override
    public List<Game> listGames() throws UnauthorizedException, AlreadyTakenException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/game"))
                    .header("Authorization", authToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                ListGameResponse result = gson.fromJson(
                        response.body(),
                        ListGameResponse.class
                );

                return result.games();
            } else {
                errorHandler(response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
        return null;
    }

    @Override
    public void joinGame(ChessGame.TeamColor color, int gameID) throws UnauthorizedException, AlreadyTakenException {
        try {
            String body = gson.toJson(new JoinRequest(color, gameID));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/game"))
                    .header("Authorization", authToken)
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                errorHandler(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
    }

    private void errorHandler(int statusCode) throws UnauthorizedException, AlreadyTakenException {
        if (statusCode == 400) {
            throw new IllegalArgumentException("Wrong arguments");
        } else if (statusCode == 401) {
            throw new UnauthorizedException("Wrong credentials");
        } else if (statusCode == 403) {
            throw new AlreadyTakenException("");
        } else {
            throw new ServerException("Could not communicate with server");
        }
    }
}
