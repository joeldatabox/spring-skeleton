package br.com.security.jwt;

import br.com.security.model.Authority;
import br.com.security.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class JwtUser implements UserDetails {

    private final String id;
    private final String name;

    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
    private final Date lastPasswordResetDate;
    private final User originUser;

    public JwtUser(final User user) {
        this.id = user.getId();
        this.name = user.getNome();
        this.password = user.getPasswd();
        this.email = user.getEmail();
        this.authorities = mapToGrantedAuthorities(user.getAuthorities());
        this.enabled = user.isEnable();
        this.lastPasswordResetDate = user.getLastPasswordResetDate();
        this.originUser = user;
    }

    @JsonIgnore
    public User getOriginUser() {
        return originUser;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    private static final List<GrantedAuthority> mapToGrantedAuthorities(final Collection<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}