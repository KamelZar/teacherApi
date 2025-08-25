package me.synology.techrevive.teacher.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    private final AuthenticationManager authenticationManager;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Ne pas filtrer les endpoints publics
        return pathMatcher.match("/auth/**", path) || 
               pathMatcher.match("/hello", path) ||
               pathMatcher.match("/h2-console/**", path) ||
               pathMatcher.match("/swagger-ui/**", path) ||
               pathMatcher.match("/v3/api-docs/**", path);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendUnauthorizedResponse(response, "Missing or invalid Authorization header");
            return;
        }
        
        try {
            String token = authHeader.substring(BEARER_PREFIX.length());
            JwtAuthentication authRequest = new JwtAuthentication(token);
            
            Authentication authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            
            filterChain.doFilter(request, response);
            
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            sendUnauthorizedResponse(response, "Invalid or expired JWT token");
        }
    }
    
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"" + message + "\"}");
    }
}