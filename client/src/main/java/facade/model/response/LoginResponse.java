package facade.model.response;

public record LoginResponse(
        String username,
        String authToken
) {
}
