package com.infotamia.weather.rest.user.login;

import com.infotamia.weather.access.AuthenticationProvider;
import com.infotamia.weather.access.SessionType;
import com.infotamia.weather.exception.BaseErrorCode;
import com.infotamia.weather.exception.RestCoreException;
import com.infotamia.weather.filter.SkipFilter;
import com.infotamia.weather.pojos.common.AppState;
import com.infotamia.weather.pojos.common.user.AuthenticationToken;
import com.infotamia.weather.pojos.common.user.FacebookExternalAccount;
import com.infotamia.weather.pojos.entities.UserEntity;
import com.infotamia.weather.services.CookieService;
import com.infotamia.weather.services.jwt.JwtTokenDetails;
import com.infotamia.weather.services.jwt.JwtTokenService;
import com.infotamia.weather.services.oauth.BaseOAuth20Service;
import com.infotamia.weather.services.user.UserService;
import com.infotamia.weather.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author Mohammed Al-Ani
 */
@RestController
@RequestMapping(path = "oauth/facebook")
public class FacebookUserLoginResource {

    private static final Logger logger = LoggerFactory.getLogger(FacebookUserLoginResource.class);
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final BaseOAuth20Service facebookAuthenticationService;
    private AppState state;

    public FacebookUserLoginResource(
            UserService userService,
            JwtTokenService jwtTokenService,
            @Qualifier("facebook") BaseOAuth20Service facebookAuthenticationService,
            AppState state) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.facebookAuthenticationService = facebookAuthenticationService;
        this.state = state;
    }

    @GetMapping(path = "customer")
    @SkipFilter
    public ResponseEntity<?> getRedirectCustomer() throws URISyntaxException {
        String authorizationUrl = facebookAuthenticationService.getAuthorizationUrl(true);
        return ResponseEntity.status(HttpStatus.OK)
                .location(new URI(authorizationUrl))
                .build();
    }

    @GetMapping(path = "admin")
    @SkipFilter
    public ResponseEntity<?> getRedirectAdmin() throws URISyntaxException {
        String authorizationUrl = facebookAuthenticationService.getAuthorizationUrl(false);
        return ResponseEntity.status(HttpStatus.OK)
                .location(new URI(authorizationUrl))
                .build();
    }
    @GetMapping(path = "callback", produces = MediaType.TEXT_HTML_VALUE)
    @SkipFilter
    public ResponseEntity<?> callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        JwtTokenDetails jwtDetails = jwtTokenService.verifyAndReturnAuthTokenDetails(state);
        SessionType sType = jwtDetails.getSessionType();
        if (sType == SessionType.USER) {
            String accessToken = facebookAuthenticationService.getAccessToken(code);
            AuthenticationToken authenticationToken = new AuthenticationToken(accessToken);
            return customerLogin(authenticationToken);
        } else {
            String accessToken = facebookAuthenticationService.getAccessToken(code);
            try {
                FacebookExternalAccount facebookExternalAccount = facebookAuthenticationService.getAccountDetails(accessToken);
                Optional<UserEntity> opUser = userService.findUserByEmail(facebookExternalAccount.getEmail());
                if (!opUser.isPresent()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setFirstName(facebookExternalAccount.getFirst_name());
                    userEntity.setLastName(facebookExternalAccount.getLast_name());
                    userEntity.setEmail(facebookExternalAccount.getEmail());
                    userService.insertUser(userEntity);
                }
                String jwt = jwtTokenService
                        .withSubject(facebookExternalAccount.getEmail())
                        .withAuthenticationProvider(AuthenticationProvider.FACEBOOK)
                        .withAccessToken(accessToken)
                        .withSessionType(SessionType.USER)
                        .buildAndIssueJwtToken();

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, CookieService.newCookie(jwt))
                        .body(FileUtils.loadFile("static/facebook_success.html"));
            } catch (Exception e) {
                logger.error("FAILED LOGIN STEP 2 :", e);
                return ResponseEntity.ok()
                        .body(FileUtils.loadFile("static/facebook_fail.html"));
            }
        }
    }

    @PostMapping(path = "customer/login")
    @SkipFilter
    public ResponseEntity<?> signUp(AuthenticationToken loginToken) {
        return customerLogin(loginToken);
    }


    private ResponseEntity<?> customerLogin(AuthenticationToken loginToken) {
        try {
            if (loginToken == null || loginToken.getToken() == null || loginToken.getToken().trim().length() == 0) {
                return ResponseEntity.status(401).build();
            }
            FacebookExternalAccount facebookExternalAccount = facebookAuthenticationService.getAccountDetails(loginToken.getToken());
            Optional<UserEntity> user = userService.findUserByEmail(facebookExternalAccount.getEmail());
            if (user.isPresent()) {
                String jwt = jwtTokenService.withSubject(facebookExternalAccount.getEmail())
                        .withAuthenticationProvider(AuthenticationProvider.FACEBOOK)
                        .withAccessToken(loginToken.getToken())
                        .withSessionType(SessionType.USER)
                        .buildAndIssueJwtToken();
                return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                        .location(URI.create("https://localdev.infotamia.com:3000/#/success"))
                        .header(HttpHeaders.SET_COOKIE, CookieService.newCookie(jwt))
                        .build();
            } else {
                UserEntity userEntity = new UserEntity();
                userEntity.setFirstName(facebookExternalAccount.getFirst_name());
                userEntity.setLastName(facebookExternalAccount.getLast_name());
                userEntity.setEmail(facebookExternalAccount.getEmail());
                String imgUrl = "";
                try {
                    imgUrl = facebookExternalAccount.getPicture().getData().getUrl();
                } catch (Exception ignore) {
                    //ignore
                }
                userEntity.setProfileImageUrl(imgUrl);
                userService.insertUser(userEntity);
                String jwt = jwtTokenService.withSubject(facebookExternalAccount.getEmail())
                        .withAuthenticationProvider(AuthenticationProvider.FACEBOOK)
                        .withAccessToken(loginToken.getToken())
                        .withSessionType(SessionType.USER)
                        .buildAndIssueJwtToken();
                return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                        .location(URI.create("https://localdev.infotamia.com:3000/#/success"))
                        .header(HttpHeaders.SET_COOKIE, CookieService.newCookie(jwt))
                        .build();
            }

        } catch (Exception e) {
            throw new RestCoreException(500, BaseErrorCode.UNKNOWN, e.getMessage());
        }
    }
}
