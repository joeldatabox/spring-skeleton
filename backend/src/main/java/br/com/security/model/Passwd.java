package br.com.security.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class Passwd {
    private String id;
    @NotNull(message = "Informe a senha atual!")
    @NotEmpty(message = "Informe a senha atual!")
    private String actualPasswd;
    @NotNull(message = "Informe uma nova senha valida!")
    @NotEmpty(message = "Informe uma nova senha valida!")
    private String newPasswd;

    public Passwd() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActualPasswd() {
        return actualPasswd;
    }

    public void setActualPasswd(final String actualPasswd) {
        this.actualPasswd = actualPasswd;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public void setNewPasswd(final String newPasswd) {
        this.newPasswd = newPasswd;
    }


}
