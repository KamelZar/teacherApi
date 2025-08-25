package me.synology.techrevive.teacher.security;

import me.synology.techrevive.teacher.entities.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthentication implements Authentication {
    
    private final String token;
    private final Long userId;
    private final String email;
    private final UserRole role;
    private boolean authenticated;
    
    public JwtAuthentication(String token) {
        this.token = token;
        this.userId = null;
        this.email = null;
        this.role = null;
        this.authenticated = false;
    }
    
    public JwtAuthentication(String token, Long userId, String email, UserRole role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.authenticated = true;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authenticated && role != null) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }
        return Collections.emptyList();
    }
    
    @Override
    public Object getCredentials() {
        return token;
    }
    
    @Override
    public Object getDetails() {
        return null;
    }
    
    @Override
    public Object getPrincipal() {
        return email;
    }
    
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
    
    @Override
    public String getName() {
        return email;
    }
    
    public String getToken() {
        return token;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public UserRole getRole() {
        return role;
    }
}