package br.com.security.jwt.config;

import br.com.security.jwt.exception.SecurityUnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint, Serializable {

    private final String urlLogin;

    public UnauthorizedHandler(@Value("${security.jwt.controller.loginEndPoint}") final String urlLogin) {
        this.urlLogin = urlLogin;
    }

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .print(new SecurityUnauthorizedException("Unauthorized!")
                        .addDetails("urlLogin", request
                                .getRequestURL()
                                .toString()
                                .replace(request.getRequestURI(), "") + urlLogin
                        ).toJson());

    }
}
