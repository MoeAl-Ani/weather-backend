package com.infotamia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotamia.weather.access.SessionType;
import com.infotamia.weather.filter.AuthenticationFilter;
import com.infotamia.weather.filter.AuthorizationFilter;
import com.infotamia.weather.utils.JwtGeneratorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Mohammed Al-Ani
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
public abstract class BaseTest {
    protected String jwt = null;
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    RequestMappingHandlerMapping mappingHandlerMapping;
    @Autowired
    private AuthenticationFilter authenticationFilter;
    @Autowired
    private AuthorizationFilter authorizationFilter;
    public BaseTest() {
        jwt = JwtGeneratorUtil.issueJwtToken("mohammedalanny@gmail.com", SessionType.USER);
    }

    @BeforeEach
    public void beforeTest() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(authenticationFilter)
                .addFilters(authorizationFilter)
                .build();
    }
}
