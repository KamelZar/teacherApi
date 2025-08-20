package me.synology.techrevive.teacher.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class GoogleTokenAuthentication implements Authentication {
    
    private final String token;
    private final String googleId;
    private final String email;
    private final String name;
    private boolean authenticated;
    
    public GoogleTokenAuthentication(String token) {
        this.token = token;
        this.googleId = null;
        this.email = null;
        this.name = null;
        this.authenticated = false;
    }
    
    public GoogleTokenAuthentication(String token, String googleId, String email, String name) {
        this.token = token;
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.authenticated = true;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authenticated) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
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
        return googleId;
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
        return name != null ? name : email;
    }
    
    public String getGoogleId() {
        return googleId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getToken() {
        return token;
    }
}