package gift.common.util;

import java.time.LocalDateTime;

public class TokenUtils {

    public static LocalDateTime calculateExpiryDateTime(long expiresInSeconds) {
        return LocalDateTime.now().plusSeconds(expiresInSeconds);
    }
}
