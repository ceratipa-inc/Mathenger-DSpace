package com.example.mathengerapi.models;

import com.example.mathengerapi.security.configuration.AuthenticationConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    private Long id;
    @Column(unique = true, nullable = false, length = 30)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @JsonIgnore
    @MapsId
    @OneToOne
    @EqualsAndHashCode.Exclude
    private Account account;
    @JsonIgnore
    @Column(nullable = false)
    private boolean active;
    @JsonIgnore
    private String passwordRecoveryCode;
    @JsonIgnore
    private String activationCode;

    @Transient
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities =
            Collections.singletonList(new SimpleGrantedAuthority(AuthenticationConstant.ROLE_USER));

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return active;
    }
}
