package response;

import java.lang.reflect.Array;
import java.util.Collection;

public record ListGamesResult(Collection<Array> games) {
}
