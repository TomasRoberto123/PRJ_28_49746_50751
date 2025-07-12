package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Categoria.
 */
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "imagem_path")
    private String imagemPath;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnoreProperties(value = { "instrumentos", "categoria" }, allowSetters = true)
    private Set<SubTema> subTemas = new HashSet<>();

    @JsonIgnoreProperties(value = { "categoria", "respostaUserQuestionarios", "questaoQuestionarios" }, allowSetters = true)
    @OneToOne(mappedBy = "categoria")
    private Questionario questionario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Categoria nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Categoria descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemPath() {
        return this.imagemPath;
    }

    public Categoria imagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
        return this;
    }

    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    public Set<SubTema> getSubTemas() {
        return this.subTemas;
    }

    public Categoria subTemas(Set<SubTema> subTemas) {
        this.setSubTemas(subTemas);
        return this;
    }

    public Categoria addSubTema(SubTema subTema) {
        this.subTemas.add(subTema);
        subTema.setCategoria(this);
        return this;
    }

    public Categoria removeSubTema(SubTema subTema) {
        this.subTemas.remove(subTema);
        subTema.setCategoria(null);
        return this;
    }

    public void setSubTemas(Set<SubTema> subTemas) {
        if (this.subTemas != null) {
            this.subTemas.forEach(i -> i.setCategoria(null));
        }
        if (subTemas != null) {
            subTemas.forEach(i -> i.setCategoria(this));
        }
        this.subTemas = subTemas;
    }

    public Questionario getQuestionario() {
        return this.questionario;
    }

    public Categoria questionario(Questionario questionario) {
        this.setQuestionario(questionario);
        return this;
    }

    public void setQuestionario(Questionario questionario) {
        if (this.questionario != null) {
            this.questionario.setCategoria(null);
        }
        if (questionario != null) {
            questionario.setCategoria(this);
        }
        this.questionario = questionario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        return id != null && id.equals(((Categoria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categoria{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", imagemPath='" + getImagemPath() + "'" +
            "}";
    }
}
