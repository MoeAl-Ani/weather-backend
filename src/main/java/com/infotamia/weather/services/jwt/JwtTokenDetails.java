package com.infotamia.weather.services.jwt;

import com.infotamia.weather.access.AuthenticationProvider;
import com.infotamia.weather.access.SessionType;

import java.time.ZonedDateTime;

/**
 * @author Mohammed Al-Ani
 */
public class JwtTokenDetails implements TokenDetail {

    private String jwtId;
    private String sub;
    private String aud;
    private String email;
    private String issuer;
    private SessionType sessionType;
    private AuthenticationProvider provider;
    private String accessToken;
    private ZonedDateTime iat;
    private ZonedDateTime exp;

    public JwtTokenDetails(String jwtId,
                           String sub,
                           SessionType sessionType,
                           AuthenticationProvider provider,
                           String accessToken,
                           ZonedDateTime iat,
                           ZonedDateTime exp
    ) {
        this.jwtId = jwtId;
        this.sub = sub;
        this.sessionType = sessionType;
        this.provider = provider;
        this.accessToken = accessToken;
        this.iat = iat;
        this.exp = exp;
    }

    public JwtTokenDetails(String jwtSubject, String aud, String email, String issuer, ZonedDateTime jwtIssueAt, ZonedDateTime jwtExpireAt) {
        this.sub = jwtSubject;
        this.aud = aud;
        this.email = email;
        this.issuer = issuer;
        this.iat = jwtIssueAt;
        this.exp = jwtExpireAt;
    }

    public String getJwtId() {
        return jwtId;
    }

    @Override
    public String getKeyId() {
        return jwtId;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getAud() {
        return aud;
    }

    public String getSub() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public AuthenticationProvider getProvider() {
        return provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ZonedDateTime getIat() {
        return iat;
    }

    public ZonedDateTime getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "JwtTokenDetails{" +
                "id='" + jwtId + '\'' +
                ", subject='" + sub + '\'' +
                ", role='" + sessionType + '\'' +
                ", issuedDate=" + iat +
                ", expirationDate=" + exp +
                '}';
    }
}