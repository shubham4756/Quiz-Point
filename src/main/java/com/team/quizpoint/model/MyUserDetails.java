package com.team.quizpoint.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String username;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorityList;

    public MyUserDetails(User u) {
        this.username = u.getEmail();
        this.password = u.getPassword();
        this.active = u.isActive();
        this.authorityList = u.getRoles().stream().map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
    }

    public MyUserDetails() {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
