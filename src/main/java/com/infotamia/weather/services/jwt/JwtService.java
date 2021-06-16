package com.infotamia.weather.services.jwt;

/**
 * Generic jwt service.
 *
 * @author Mohammed Al-Ani
 */
public interface JwtService {
    /**
     * issue a jwt token from the given token params.
     *
     * @param t
     * @return generated jwt.
     */
    String issueToken(TokenDetail t);

    /**
     * verify the jwt signature and returns the token details.
     *
     * @param token
     * @return token details.
     */
    TokenDetail verifyAndGet(String token);
}
