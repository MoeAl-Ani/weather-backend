package com.infotamia.weather.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.infotamia.weather.config.JwtConfiguration;
import org.springframework.stereotype.Service;

/**
 * jwt verifier service.
 *
 * @author Mohammed Al-Ani
 */
@Service
class JwtTokenVerifier {

    private final JwtConfiguration jwtConfiguration;


    public JwtTokenVerifier(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    JwtTokenDetails verifyAndGetJwtToken(String jwtToken) {

        Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(jwtConfiguration.getIssuer())
                .build();
        DecodedJWT jwt = verifier.verify(jwtToken);
        return new JwtTokenDetails(
                JwtDecoderUtils.getJwtTokenId(jwt),
                JwtDecoderUtils.getJwtSubject(jwt),
                JwtDecoderUtils.getJwtSessionType(jwt),
                JwtDecoderUtils.getJwtAuthenticationProvider(jwt),
                JwtDecoderUtils.getJwtAuthenticationProviderAccessToken(jwt),
                JwtDecoderUtils.getJwtIssueAt(jwt),
                JwtDecoderUtils.getJwtExpireAt(jwt));
    }
}