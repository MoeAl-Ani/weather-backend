package com.infotamia.weather.services.oauth;

import com.infotamia.weather.pojos.common.AppState;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * CDI factory for creating a jwt state used in the login a long size the XSRF token.
 *
 * @author Mohammed Al-Ani
 */
@Service
@RequestScope
public class AppStateFactory extends AbstractFactoryBean<AppState> {
    private AppState state;

    @Override
    public Class<?> getObjectType() {
        return AppState.class;
    }

    @Override
    protected AppState createInstance() throws Exception {
        if (state == null) {
            state = new AppState();
        }
        return state;
    }
}
