package com.infotamia.weather.services.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.infotamia.weather.access.AuthenticationProvider;
import com.infotamia.weather.access.SessionType;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Mohammed Al-Ani
 */
public class JwtDecoderUtils {

    public static String getJwtTokenId(DecodedJWT jwt) {
        return jwt.getId();
    }

    public static String getIssuer(DecodedJWT jwt) {
        return jwt.getIssuer();
    }

    public static String getAud(DecodedJWT jwt) {
        return jwt.getClaim("aud").asString();
    }

    public static String getJwtSubject(DecodedJWT jwt) {
        return jwt.getSubject();
    }

    public static ZonedDateTime getJwtIssueAt(DecodedJWT jwt) {
        return ZonedDateTime.ofInstant(jwt.getIssuedAt().toInstant(), ZoneId.systemDefault());
    }

    public static ZonedDateTime getJwtExpireAt(DecodedJWT jwt) {
        return ZonedDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneId.systemDefault());
    }

    public static SessionType getJwtSessionType(DecodedJWT jwt) {
        return jwt.getClaim("sessionType") != null && jwt.getClaim("sessionType").asString() != null ?
                SessionType.valueOf(jwt.getClaim("sessionType").asString()) : null;
    }


    public static AuthenticationProvider getJwtAuthenticationProvider(DecodedJWT jwt) {
        return jwt.getClaim("authenticationProvider") != null ?
                jwt.getClaim("authenticationProvider").as(AuthenticationProvider.class) : null;
    }

    public static String getJwtAuthenticationProviderAccessToken(DecodedJWT jwt) {
        return jwt.getClaim("accessToken") != null ? jwt.getClaim("accessToken").asString() : null;
    }
}
