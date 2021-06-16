package com.infotamia.weather.access;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * factory for providing user principal in request scope.
 *
 * @author Mohammed Al-Ani
 */
@Service
@RequestScope
public class UserPrincipalFactory extends AbstractFactoryBean<AbstractPrincipal> {

    @Override
    public Class<?> getObjectType() {
        return AbstractPrincipal.class;
    }

    @Override
    protected AbstractPrincipal createInstance() {
        return (AbstractPrincipal) SecurityContextHolder.getContext().getAuthentication();
    }
}
