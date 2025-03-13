package com.personal.user.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TokenExpirationUtils {

    public static Date toDate(LocalDateTime expiration) {
        return Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
    }
}