package client;

import client.model.request.LoginRequest;
import client.model.request.RegisterRequest;
import client.model.response.LoginResponse;
import com.google.gson.Gson;
import model.Game;
import service.AlreadyTakenException;
import service.UnauthorizedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HTTPServerFacade implements ServerFacade {

    private final HttpClient httpClient;
    private final String serverUrl;
    private final Gson gson = new Gson();
    private String authToken;
    private String username;

    public HTTPServerFacade(String serverUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.serverUrl = serverUrl;
    }

    @Override
    public void register(String username, String password, String email) throws AlreadyTakenException {
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
                this.username = result.username();
            } else if (response.statusCode() == 400) {
                throw new IllegalArgumentException("Wrong arguments");
            } else if (response.statusCode() == 403) {
                throw new AlreadyTakenException("Username already taken");
            } else {
                throw new ServerException("Could not communicate with server");
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
    }

    @Override
    public void login(String username, String password) throws UnauthorizedException {
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

            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());

            if (response.statusCode() == 200) {
                LoginResponse result = gson.fromJson(
                        response.body(),
                        LoginResponse.class
                );

                this.authToken = result.authToken();
                this.username = result.username();
            } else if (response.statusCode() == 401) {
                throw new UnauthorizedException("Username or password is incorrect");
            } else if (response.statusCode() == 400) {
                throw new IllegalArgumentException("Wrong arguments");
            } else {
                throw new ServerException("Could not communicate with server");
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
    }

    @Override
    public void logout() throws UnauthorizedException {
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
                if (response.statusCode() == 401) {
                    throw new UnauthorizedException("Username or password is incorrect");
                } else {
                    throw new ServerException("Could not communicate with server");
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with the server");
        }
    }

    @Override
    public void clear() {
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
                throw new ServerException("Could not communicate with server");
            }

        } catch (IOException | InterruptedException e) {
            throw new ServerException("Could not communicate with server");
        }
    }

    @Override
    public void creatGame(String gameName) {

    }

    @Override
    public List<Game> listGames() {
        return List.of();
    }

    @Override
    public void joinGame(int gameID, String color) {

    }
}
