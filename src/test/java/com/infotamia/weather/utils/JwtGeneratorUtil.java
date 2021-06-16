package com.infotamia.weather.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.infotamia.weather.access.SessionType;
import com.infotamia.weather.services.jwt.JwtTokenDetails;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author Mohammed Al-Ani
 */
public class JwtGeneratorUtil {

    private JwtGeneratorUtil() {
        //
    }

    private static final String USER_1 = "replace by your own db user email";
    private static final String USER_2 = "replace by your own db user email";

    public static String issueJwtToken(String subject, SessionType sessionType) {
        String id = UUID.randomUUID().toString();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = issuedDate.plusSeconds(1036800 * 10);

        JwtTokenDetails jwtTokenDetails = new JwtTokenDetails(
                id,
                subject,
                sessionType,
                null,
                "something",
                issuedDate,
                expirationDate
        );
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String r = jwtTokenDetails.getSessionType() != null ? jwtTokenDetails.getSessionType().name() : null;
        return JWT.create()
                .withIssuer("https://infotamia.com")
                .withJWTId(jwtTokenDetails.getJwtId())
                .withSubject(jwtTokenDetails.getSub())
                .withClaim("sessionType", r)
                .withIssuedAt(Date.from(jwtTokenDetails.getIat().toInstant()))
                .withExpiresAt(Date.from(jwtTokenDetails.getExp().toInstant()))
                .sign(algorithm);
    }

    public static void main(String[] args) {
        String user1JwtToken = issueJwtToken(USER_1, SessionType.USER);
        String user2JwtToken = issueJwtToken(USER_2, SessionType.USER);
        System.out.println("JWT-TOKEN_USER1=" + user1JwtToken);
        System.out.println("JWT-TOKEN_USER2=" + user2JwtToken);
    }
}
