package com.infotamia.weather.services.jwt;

import com.infotamia.weather.access.AuthenticationProvider;
import com.infotamia.weather.access.SessionType;
import com.infotamia.weather.config.JwtConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Mohammed Al-Ani
 */
@Service
@Scope("prototype")
public class JwtTokenService {
    private final JwtConfiguration jwtConfiguration;
    private final JwtTokenIssuer tokenIssuer;
    private final JwtTokenVerifier jwtTokenVerifier;

    public JwtTokenService(JwtTokenIssuer tokenIssuer, JwtTokenVerifier jwtTokenVerifier, JwtConfiguration jwtConfiguration) {
        this.tokenIssuer = tokenIssuer;
        this.jwtTokenVerifier = jwtTokenVerifier;
        this.jwtConfiguration = jwtConfiguration;
    }

    private String subject;
    private int expireInMin;
    private AuthenticationProvider provider;
    private String accessToken;
    private SessionType sessionType;


    public JwtTokenService withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public JwtTokenService withExpireInMin(int expireInMin) {
        this.expireInMin = expireInMin;
        return this;
    }

    public JwtTokenService withAuthenticationProvider(AuthenticationProvider provider) {
        this.provider = provider;
        return this;
    }

    public JwtTokenService withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public JwtTokenService withSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
        return this;
    }

    public String buildAndIssueJwtToken() {
        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = expireInMin == 0 ? calculateExpirationDate(issuedDate) : issuedDate.plusMinutes(expireInMin);

        JwtTokenDetails jwtTokenDetails = new JwtTokenDetails(
                id,
                subject,
                sessionType,
                provider,
                accessToken,
                issuedDate,
                expirationDate
        );

        return tokenIssuer.issueJwtToken(jwtTokenDetails);
    }


    public JwtTokenDetails verifyAndReturnAuthTokenDetails(String token) {
        return jwtTokenVerifier.verifyAndGetJwtToken(token);
    }

    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        //1036800
        return issuedDate.plusSeconds(jwtConfiguration.getExpireAt());
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}