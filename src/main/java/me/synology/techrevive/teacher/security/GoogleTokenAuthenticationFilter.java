package me.synology.techrevive.teacher.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

public class GoogleTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    public GoogleTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(createRequestMatcher());
        setAuthenticationManager(authenticationManager);
    }
    
    private static RequestMatcher createRequestMatcher() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return request -> {
            String path = request.getServletPath();
            return pathMatcher.match("/student/**", path) || pathMatcher.match("/user/**", path);
        };
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new AuthenticationException("Missing or invalid Authorization header") {};
        }
        
        String token = authHeader.substring(BEARER_PREFIX.length());
        GoogleTokenAuthentication authRequest = new GoogleTokenAuthentication(token);
        
        return getAuthenticationManager().authenticate(authRequest);
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed)
            throws IOException, ServletException {
        
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Invalid Google token\"}");
    }
}