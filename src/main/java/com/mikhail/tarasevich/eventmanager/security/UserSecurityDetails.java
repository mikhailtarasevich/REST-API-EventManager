package com.mikhail.tarasevich.eventmanager.security;

import com.mikhail.tarasevich.eventmanager.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserSecurityDetails implements UserDetails {

    private final User user;

    private final List<String> privileges;

    public UserSecurityDetails(User user, List<String> privileges) {
        this.user = user;
        this.privileges = privileges;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return true;
    }

}
