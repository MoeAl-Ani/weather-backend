package com.infotamia.weather.services.oauth;

import com.infotamia.weather.pojos.common.user.ExternalAccountAbstract;

/**
 * Base auth 2 service.
 *
 * @author Mohammed Al-Ani
 */
public interface BaseOAuth20Service {
    /**
     * returns the authorization url loaded from the provided application configurations.
     *
     * @return String
     */
    String getAuthorizationUrl(Boolean isCustomer);

    /**
     * returns the login access token.
     *
     * @param code
     * @return String
     */
    String getAccessToken(String code);

    /**
     * returns the logged in account details.
     *
     * @param accessToken
     * @return ExternalAccountAbstract
     */
    <T extends ExternalAccountAbstract> T getAccountDetails(String accessToken);

}
