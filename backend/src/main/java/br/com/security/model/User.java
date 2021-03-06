package br.com.security.model;

import br.com.security.jwt.exception.SecurityBadRequestException;
import br.com.security.model.enumeration.Authorities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Document(collection = "users")
@CompoundIndexes({
        @CompoundIndex(name = "email_index_unique", def = "{'email' : 1}", unique = true)
})
public class User {
    @Transient
    @JsonIgnore
    private static final int SALT = 8;
    @Transient
    @JsonIgnore
    private static final String EMAIL_PATTERN = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

    @Id
    private String id;
    @NotBlank(message = "O campo nome não pode ser nulo!")
    @Size(min = 4, max = 200, message = "O campo nome deve ter de 4 a 200 caracteres!")
    private String nome;
    @NotBlank(message = "Informe um email válido!")
    @Email(message = "Informe um email válido!")
    private String email;
    @NotBlank(message = "Informe uma senha valida!")
    private String passwd;
    private Date lastPasswordResetDate;

    private Boolean enable;

    private Set<Authority> authorities;

    public User() {
        this.authorities = new HashSet();
        this.enable = true;
        this.lastPasswordResetDate = Date.from(Instant.now());
    }


    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPasswd() {
        return passwd;
    }

    @JsonProperty
    public void setPasswd(final String passwd) {
        if (passwd == null || passwd.trim().isEmpty()) {
            throw new SecurityBadRequestException(User.class, "passwd", "Informe uma senha valida!");
        }
        this.passwd = new BCryptPasswordEncoder(SALT).encode(passwd);
    }

    public void setPasswd(final User user) {
        this.passwd = user.passwd;
    }

    public void setPasswd(final Passwd passwd) {
        if (!checkPasswd(passwd.getActualPasswd())) {
            throw new SecurityBadRequestException(User.class, "passwd", "A senha atual informada é invalida!").setStatus(HttpStatus.NOT_ACCEPTABLE);
        }
        this.passwd = new BCryptPasswordEncoder(SALT).encode(passwd.getNewPasswd());
    }

    public boolean checkPasswd(final String passwd) {
        return checkPasswd(passwd, this.passwd);
    }

    public boolean checkPasswd(final String passwd, String cryptPasswd) {
        return new BCryptPasswordEncoder().matches(passwd, cryptPasswd);
    }

    @JsonIgnore
    public boolean isValidPasswd() {
        return this.passwd != null && !this.passwd.isEmpty();
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public User setAuthorities(final Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public void addAuthority(final Authority authority) {
        this.authorities.add(authority);
    }

    public void addAuthorities(final Collection<Authority> authorities) {
        this.authorities.addAll(authorities);
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(final Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public final boolean inRole(final String role) {
        try {
            return role == null ? false : inRole(Authorities.valueOf(role));
        } catch (IllegalArgumentException iex) {
            return false;
        }
    }

    public final boolean inRole(final Authorities role) {
        return getAuthorities().contains(new Authority(role));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public String toJson() {
        String json = "";
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);

        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
        //adicionando a senha;
        if (passwd != null) {
            return json.substring(0, json.length() - 1) + ", \"passwd\":\"" + this.passwd + "\"}";
        }
        return json;
    }

    @JsonIgnore
    public boolean isValidEmail() {
        if ((email == null) || (email.trim().isEmpty()))
            return false;
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
