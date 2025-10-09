package com.zuehlke.securesoftwaredevelopment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    public static final String SESSION_ATTR_NAME = "AUTH_ERROR";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        logger.warn("Authentication failure: {}", exception.getMessage());

        request.getSession().setAttribute(SESSION_ATTR_NAME, "Invalid username or password.");

        response.sendRedirect(request.getContextPath() + "/login");
    }
}
