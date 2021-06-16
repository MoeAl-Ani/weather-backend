package com.infotamia.weather.access;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 *  user principal.
 *
 * @author Mohammed Al-Ani
 */
public class WeatherUserPrincipal extends AbstractPrincipal  {
    private Integer id;
    private Integer languageId;
    private String name;
    private AuthenticationProvider authenticationProvider;
    private String authenticationProviderAccessToken;
    private SessionType sessionType;
    private final List<String> roles;

    public WeatherUserPrincipal(List<String> roles) {
        super(Collections.emptyList());
        if (CollectionUtils.isEmpty(roles)) throw new RuntimeException("something really bad happening");
        this.roles = roles;
    }

    /**
     * @return the name of the principal, name === email.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * set user email. name === email
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the unique database user identifier.
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * set user unique identifier.
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return user language preference.
     */
    @Override
    public Integer getLanguageId() {
        return languageId;
    }

    /**
     * set user language preference.
     *
     * @param languageId
     */
    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    /**
     * @return the current session type.
     */
    @Override
    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * @return Login authentication provider.
     */
    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * set login authentication provider.
     *
     * @param authenticationProvider
     */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * @return Login access token.
     */
    @Override
    public String getAuthenticationProviderAccessToken() {
        return authenticationProviderAccessToken;
    }

    @Override
    public Boolean isRoot() {
        return sessionType != null && sessionType.equals(SessionType.SYSTEM_ADMIN);
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    /**
     * set login access token to the current principal.
     *
     * @param authenticationProviderAccessToken
     */
    public void setAuthenticationProviderAccessToken(String authenticationProviderAccessToken) {
        this.authenticationProviderAccessToken = authenticationProviderAccessToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public String toString() {
        return "WeatherUserPrincipal{" +
                "name='" + name + '\'' +
                '}';
    }
}
