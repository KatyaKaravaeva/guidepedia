package com.guidepedia.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guidepedia.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String username;

    private String login;

    @JsonIgnore
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    private String role;

    public UserDetailsImpl(Long id, String login, String username, String password, String role) {
        this.id = id;
        this.login = login;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static UserDetailsImpl build(UserEntity user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getLogin(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getUserRole());
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
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}