package client.model.response;

public record LoginResponse(
        String username,
        String authToken
) {
}
