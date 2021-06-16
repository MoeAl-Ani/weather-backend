package com.infotamia.weather.services.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.apis.facebook.FacebookAccessTokenErrorResponse;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.infotamia.weather.config.FacebookAuthenticationConfiguration;
import com.infotamia.weather.exception.BaseErrorCode;
import com.infotamia.weather.exception.ExceptionMessage;
import com.infotamia.weather.exception.RestCoreException;
import com.infotamia.weather.pojos.common.AppState;
import com.infotamia.weather.pojos.common.user.ExternalAccountAbstract;
import com.infotamia.weather.pojos.common.user.FacebookExternalAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Facebook authentication service.
 *
 * @author Mohammed Al-Ani
 */
@Service("facebook")
@RequestScope
public class FacebookAuthenticationService implements BaseOAuth20Service {
    private final Logger logger = LoggerFactory.getLogger(FacebookAuthenticationService.class);
    private final OAuth20Service service;
    private final FacebookAuthenticationConfiguration facebookConfig;
    private final ObjectMapper mapper;
    private final AppState state;


    public FacebookAuthenticationService(FacebookAuthenticationConfiguration configuration, ObjectMapper mapper, AppState state) {
        this.facebookConfig = configuration;
        this.service = getUserFacebook();
        this.mapper = mapper;
        this.state = state;
    }

    private OAuth20Service getUserFacebook() {
        return new ServiceBuilder(facebookConfig.getClientId())
                .apiSecret(facebookConfig.getClientSecret())
                .defaultScope(facebookConfig.getScope())
                .callback(facebookConfig.getCallbackUrl())
                .build(FacebookApi.instance());
    }

    @Override
    public String getAuthorizationUrl(Boolean isCustomer) {
        return service.getAuthorizationUrl(state.getDigitalSignature());
    }

    @Override
    public String getAccessToken(String code) {
        if (code == null || code.isEmpty()) {
            throw new RestCoreException(
                    new ExceptionMessage(
                            400,
                            BaseErrorCode.CODE_WAS_WRONG,
                            "code can not be null or empty"));
        }

        try {
            OAuth2AccessToken accessToken = service.getAccessToken(code);
            return accessToken.getAccessToken();
        } catch (OAuth2AccessTokenErrorResponse | FacebookAccessTokenErrorResponse e) {
            throw new RestCoreException(
                    new ExceptionMessage(
                            400,
                            BaseErrorCode.ACCESS_TOKEN_REQUEST_FAILED,
                            "Access token request has failed" + e.getMessage()));
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RestCoreException(500, BaseErrorCode.ACCESS_TOKEN_REQUEST_FAILED, e.getMessage(), e.getMessage());
        }
    }


    @Override
    public <T extends ExternalAccountAbstract> T getAccountDetails(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RestCoreException(
                    new ExceptionMessage(
                            500,
                            BaseErrorCode.INVALID_ACCESS_TOKEN,
                            "Access token must not be null or empty"));
        }

        Response response = executeRequest(accessToken, new OAuthRequest(Verb.GET, facebookConfig.getProfileUrl()));
        if (response.isSuccessful()) {
            FacebookExternalAccount facebookExternalAccount = parseResponse(response);
            facebookExternalAccount.setAccessToken(accessToken);
            return (T) facebookExternalAccount;
        } else {
            throw new RestCoreException(
                    new ExceptionMessage(
                            500,
                            BaseErrorCode.LOGIN_STEP_2_FAILED,
                            "Request has failed: " + response.getMessage()));
        }
    }

    private Response executeRequest(String accessToken, OAuthRequest request) {

        try {
            service.signRequest(accessToken, request);
            return service.execute(request);
        } catch (Exception e) {
            throw new RestCoreException(
                    new ExceptionMessage(
                            500,
                            BaseErrorCode.SERVER_ERROR,
                            "Can't execute request" + e.getMessage()));
        }
    }

    private FacebookExternalAccount parseResponse(com.github.scribejava.core.model.Response response) {
        try {
            return mapper.readValue(response.getBody(), FacebookExternalAccount.class);
        } catch (IOException e) {
            logger.error("Parsing external exception", e);
            throw new RestCoreException(500, BaseErrorCode.SERVER_ERROR, "parsing account failed");
        }
    }
}
