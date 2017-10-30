package br.com.skeleton.model;

import br.com.security.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by joel on 17/01/17.
 */
@Document(collection = "contas")
@CompoundIndexes({
        @CompoundIndex(name = "user_nome_index_unique", def = "{'user' : 1, 'nome': 1}", unique = true)
})
public final class Conta implements Model<String> {

    @Id
    private String id;
    private Date dtCreate;
    private Date lastUpdate;
    @JsonIgnore
    @DBRef
    @Indexed
    private User user;
    @DBRef(lazy = true)
    @Indexed
    private User createdBy;
    @NotBlank(message = "O nome deve conter no mínimo 1 carater!")
    @Indexed
    private String nome;
    @DBRef
    @Indexed
    private Banco banco;
    private BigDecimal saldo;
    private boolean principal;
    private String observacao;

    public Conta() {
        this.principal = false;
        this.setSaldo(BigDecimal.ZERO);
    }

    public Conta(final String nome, final String observacao) {
        this();
        this.nome = nome;
        this.observacao = observacao;
    }

    public Conta(final User user, final String nome, final String observacao) {
        this.user = user;
    }

    public Conta(final String id, final User user, final String nome, final String observacao) {
        this(user, nome, observacao);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Conta setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public Conta setDtCreate(final Date dtCreate) {
        this.dtCreate = dtCreate;
        return this;
    }

    @Override
    public Date getDtCreate() {
        return this.dtCreate;
    }

    @Override
    public Conta setLastUpdate(final Date dtUpdate) {
        this.lastUpdate = dtUpdate;
        return this;
    }

    @Override
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    @JsonIgnore
    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Conta setUser(final User user) {
        this.user = user;
        return this;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public Conta setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Conta setNome(final String nome) {
        this.nome = nome;
        return this;
    }

    public Banco getBanco() {
        return banco;
    }

    public Conta setBanco(final Banco banco) {
        this.banco = banco;
        return this;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Conta setSaldo(final BigDecimal saldo) {
        this.saldo = setDefaultBigDecimal(saldo);
        return this;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public Conta setPrincipal(final boolean principal) {
        this.principal = principal;
        return this;
    }

    public String getObservacao() {
        return observacao;
    }

    public Conta setObservacao(final String observacao) {
        this.observacao = observacao;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Conta)) return false;
        final Conta conta = (Conta) o;
        return Objects.equals(getId(), conta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
