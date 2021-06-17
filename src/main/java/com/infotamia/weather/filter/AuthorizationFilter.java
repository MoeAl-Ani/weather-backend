package com.infotamia.weather.filter;

import com.infotamia.weather.access.AbstractPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * pay attention to this :D
 * @author Mohammed Al-Ani
 */
@Component
public class AuthorizationFilter implements Filter {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public AuthorizationFilter(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // you might comment on this :D but yea that was a quick hack I did in my other project because spring security is bad :D
        Method hack = null;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            HandlerMethod handlerMethod = null;
            try {
                handlerMethod = (HandlerMethod) requestMappingHandlerMapping.getHandler((HttpServletRequest) request).getHandler();
            } catch (Exception e) {
                res.setStatus(404);
                return;
            }
            if (handlerMethod.getMethod().isAnnotationPresent(SkipFilter.class)) {
                chain.doFilter(request, response);
                return;
            }
            hack = Arrays.stream(requestMappingHandlerMapping.getClass().getDeclaredMethods()).filter(m -> m.getName().equals("getMappingForMethod"))
                    .findFirst().orElseThrow(() -> new RuntimeException("cunt"));
            hack.setAccessible(true);
            RequestMappingInfo mappingForMethod = (RequestMappingInfo) hack.invoke(requestMappingHandlerMapping, handlerMethod.getMethod(), handlerMethod.getBeanType());
            String path = mappingForMethod.getPatternsCondition().getPatterns().stream().findFirst().get();
            ResourceAccess resourceAccess = new ResourceAccess(((HttpServletRequest) request).getMethod(), path);
            AbstractPrincipal user = (AbstractPrincipal) SecurityContextHolder.getContext().getAuthentication();
            if (ResourceSecurityConfig.access.containsKey(resourceAccess)) {
                ResourceAccess resourceAccessValidRules = ResourceSecurityConfig.access.get(resourceAccess);
                if (!resourceAccessValidRules.roles.contains(user.getSessionType().name()) || !user.getRoles().contains(user.getSessionType().name())) {
                    res.setStatus(403);
                    return;
                }
            } else {
                res.setStatus(404);
            }
            chain.doFilter(request, response);
        } catch (HttpRequestMethodNotSupportedException | NullPointerException e) {
            res.setStatus(404);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (hack != null)
                hack.setAccessible(false);
        }
    }
}
