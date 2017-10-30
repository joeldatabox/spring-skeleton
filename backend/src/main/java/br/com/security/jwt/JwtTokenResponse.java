package br.com.security.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public final class JwtTokenResponse implements Serializable {
    private final String token;

    @JsonCreator
    public JwtTokenResponse(@JsonProperty("token") final String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
