package com.infotamia.weather.filter;

import com.infotamia.weather.access.AbstractPrincipal;
import com.infotamia.weather.access.SessionType;
import com.infotamia.weather.access.WeatherUserPrincipal;
import com.infotamia.weather.dao.UserDao;
import com.infotamia.weather.pojos.entities.RoleEntity;
import com.infotamia.weather.pojos.entities.UserEntity;
import com.infotamia.weather.services.CookieService;
import com.infotamia.weather.services.jwt.JwtTokenDetails;
import com.infotamia.weather.services.jwt.JwtTokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    UserDao userDao;
    JwtTokenService jwtTokenService;
    ApplicationContext applicationContext;
    final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public AuthenticationFilter(
            UserDao userDao,
            JwtTokenService jwtTokenService,
            ApplicationContext applicationContext,
            RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.userDao = userDao;
        this.jwtTokenService = jwtTokenService;
        this.applicationContext = applicationContext;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Logging request [{}, {}]", request.getMethod(), request.getRequestURI());
        Cookie[] cookies = request.getCookies();
        Optional<String> jwtString = Arrays.stream(cookies == null ? new Cookie[0] : cookies)
                .filter(Objects::nonNull)
                .filter(c -> c.getName().equals(HttpHeaders.AUTHORIZATION))
                .map(Cookie::getValue)
                .findAny();

        if (!jwtString.isPresent() || Boolean.FALSE.equals(!StringUtils.isBlank(jwtString.get()))) {
            SecurityContextHolder.getContext().setAuthentication(createAnonymousSession());
            filterChain.doFilter(request, response);
            return;
        }
        try {
            HandlerMethod handlerMethod = (HandlerMethod) requestMappingHandlerMapping.getHandler(request).getHandler();
            if (handlerMethod.getMethod().isAnnotationPresent(SkipFilter.class)) {
                filterChain.doFilter(request, response);
                return;
            }
            JwtTokenDetails jwt = jwtTokenService.verifyAndReturnAuthTokenDetails(jwtString.get());
            Optional<UserEntity> optionalUsersEntity = userDao.findUserByEmail(jwt.getSub());
            if (optionalUsersEntity.isPresent()) {
                UserEntity user = optionalUsersEntity.get();
                List<String> userRoleNames = user.getRoles()
                        .stream()
                        .sorted()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toList());

                WeatherUserPrincipal weatherUserPrincipal = new WeatherUserPrincipal(userRoleNames);
                weatherUserPrincipal.setId(user.getId());
                weatherUserPrincipal.setName(jwt.getSub());
                weatherUserPrincipal.setAuthenticated(true);
                weatherUserPrincipal.setAuthenticationProvider(jwt.getProvider());
                weatherUserPrincipal.setSessionType(jwt.getSessionType());
                SecurityContextHolder.getContext().setAuthentication(weatherUserPrincipal);
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error("exception in authentication filter : ", e);
            response.setHeader(HttpHeaders.SET_COOKIE, CookieService.newExpiredCookie());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    private AbstractPrincipal createAnonymousSession() {
        WeatherUserPrincipal anonymousSession = new WeatherUserPrincipal(Collections.singletonList("ANONYMOUS"));
        anonymousSession.setId(null);
        anonymousSession.setName(null);
        anonymousSession.setAuthenticated(true);
        anonymousSession.setAuthenticationProvider(null);
        anonymousSession.setSessionType(SessionType.ANONYMOUS);
        return anonymousSession;
    }
}
