package client.model.request;

public record RegisterRequest(
        String username,
        String password,
        String email) {
}
