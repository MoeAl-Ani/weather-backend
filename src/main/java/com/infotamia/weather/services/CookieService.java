package com.infotamia.weather.services;

import com.infotamia.weather.services.jwt.JwtTokenService;
import com.infotamia.weather.services.jwt.TokenDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CookieService {
    private static final Logger log = LoggerFactory.getLogger(CookieService.class);
    private static final ZoneId ZONE = ZoneId.systemDefault();

    private CookieService() {
        throw new RuntimeException("fuck off");
    }

    public static void extendCookie(HttpServletRequest req, HttpServletResponse res, ApplicationContext context) {
        JwtTokenService jwtService = context.getBean(JwtTokenService.class);
        if (req.getHeader(HttpHeaders.SET_COOKIE) == null) {
            Cookie[] cookies = req.getCookies();
            Optional<String> jwtString = Arrays.stream(cookies == null ? new Cookie[0] : cookies)
                    .filter(c -> c.getName().equals(HttpHeaders.AUTHORIZATION))
                    .map(Cookie::getValue)
                    .findAny();
            if (jwtString.isPresent()) {
                try {
                    TokenDetail jwt = jwtService.verifyAndReturnAuthTokenDetails(jwtString.get());
                    long now = OffsetDateTime.now(ZONE).toEpochSecond();
                    long expires = jwt.getExp().toEpochSecond();
                    long difference = expires - now;
                    // Extend if the contained jwt is not expired yet, but will expire within the next 30 minutes.
                    if (difference > 0 && difference <= 60 * 30) {
                        res.setHeader(HttpHeaders.SET_COOKIE, newCookie(jwtService.withSubject(jwt.getSub())
                                .withAuthenticationProvider(jwt.getProvider())
                                .withAccessToken(jwt.getAccessToken())
                                .withSessionType(jwt.getSessionType())
                                .buildAndIssueJwtToken()));
                    }
                } catch (Exception e) {
                    log.error("Error extending cookie: ", e);
                    // Expire the cookie.
                    res.setHeader(HttpHeaders.SET_COOKIE, CookieService.newExpiredCookie());
                }
            }
        }
    }

    /**
     * @return New auth cookie with a valid JWT as its value and an expiry time that's 7 days in the future.
     */
    public static String newCookie(String jwt) {
        return makeCookie(jwt, OffsetDateTime.now(ZONE).plusMinutes(60));
    }

    /**
     * @return New auth cookie with an empty value and an expiry time that's a year in the past.
     */
    public static String newExpiredCookie() {
        return makeCookie(null, OffsetDateTime.now(ZONE).minusYears(1));
    }

    private static String makeCookie(String jwt, OffsetDateTime expires) {
        if (Objects.isNull(jwt)) jwt = "";
        final String formattedExpiryTime = DateTimeFormatter.RFC_1123_DATE_TIME.format(expires);
        return HttpHeaders.AUTHORIZATION + "=" + jwt + "; Path=/; Domain=.infotamia.com; HttpOnly; Expires=" + formattedExpiryTime + ";";
    }
}

