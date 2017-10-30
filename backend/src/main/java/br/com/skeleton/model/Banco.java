package br.com.skeleton.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by joel on 03/07/17.
 */
@Document(collection = "bancos")
@CompoundIndexes({
        @CompoundIndex(name = "codigo_index_unique", def = "{'codigo' : 1}", unique = true)
})
public final class Banco {
    @Id
    private String id;
    @NotBlank(message = "Informe um código válido.")
    private String codigo;
    @NotBlank(message = "Informe um apelido para o banco.")
    @Indexed
    private String apelido;
    @NotBlank(message = "Informe uma descrição válida.")
    private String descricao;
    private String site;
    private String img;


    public Banco() {
    }

    public String getId() {
        return id;
    }

    public Banco setId(final String id) {
        this.id = id;
        return this;
    }

    public String getCodigo() {
        return codigo;
    }

    public Banco setCodigo(final String codigo) {
        this.codigo = codigo;
        return this;
    }

    public String getApelido() {
        return apelido;
    }

    public Banco setApelido(final String apelido) {
        this.apelido = apelido;
        return this;
    }

    public String getDescricao() {
        return descricao;
    }

    public Banco setDescricao(final String descricao) {
        this.descricao = descricao;
        return this;
    }

    public String getSite() {
        return site;
    }

    public Banco setSite(final String site) {
        this.site = site;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Banco setImg(final String img) {
        this.img = img;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Banco banco = (Banco) o;

        return id != null ? id.equals(banco.id) : banco.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
