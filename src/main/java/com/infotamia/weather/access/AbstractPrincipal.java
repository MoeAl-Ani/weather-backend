package com.infotamia.weather.access;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Base principal generic type.
 *
 * @author Mohammed Al-Ani
 */
public abstract class AbstractPrincipal extends AbstractAuthenticationToken implements Serializable {
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public AbstractPrincipal(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
    }

    public abstract Integer getId();
    public abstract Integer getLanguageId();
    public abstract SessionType getSessionType();
    public abstract AuthenticationProvider getAuthenticationProvider();
    public abstract String getAuthenticationProviderAccessToken();
    public abstract Boolean isRoot();
    public abstract List<String> getRoles();



}
