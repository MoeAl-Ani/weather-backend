package com.infotamia.weather.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.infotamia.weather.config.JwtConfiguration;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * CDI jwt issuer service.
 *
 * @author Mohammed Al-Ani
 */
@Service
class JwtTokenIssuer {

    private final JwtConfiguration jwtConfiguration;

    public JwtTokenIssuer(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    public String issueJwtToken(JwtTokenDetails jwtTokenDetails) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret());
        String sessionType = jwtTokenDetails.getSessionType() != null ? jwtTokenDetails.getSessionType().name() : null;
        String authenticationProvider = jwtTokenDetails.getProvider() != null ? jwtTokenDetails.getProvider().name() : null;
        return JWT.create()
                .withIssuer(jwtConfiguration.getIssuer())
                .withJWTId(jwtTokenDetails.getJwtId())
                .withSubject(jwtTokenDetails.getSub())
                .withClaim("sessionType", sessionType)
                .withClaim("authenticationProvider", authenticationProvider)
                .withClaim("accessToken", jwtTokenDetails.getAccessToken())
                .withIssuedAt(Date.from(jwtTokenDetails.getIat().toInstant()))
                .withExpiresAt(Date.from(jwtTokenDetails.getExp().toInstant()))
                .sign(algorithm);
    }
}