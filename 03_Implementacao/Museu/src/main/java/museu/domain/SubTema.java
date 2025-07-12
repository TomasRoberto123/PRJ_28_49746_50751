package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A SubTema.
 */
@Entity
@Table(name = "sub_tema")
public class SubTema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(mappedBy = "subTema")
    @JsonIgnoreProperties(value = { "fotos", "questaoQuestionario", "subTema" }, allowSetters = true)
    private Set<Instrumento> instrumentos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subTemas", "questionario" }, allowSetters = true)
    private Categoria categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubTema id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public SubTema nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public SubTema descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Instrumento> getInstrumentos() {
        return this.instrumentos;
    }

    public SubTema instrumentos(Set<Instrumento> instrumentos) {
        this.setInstrumentos(instrumentos);
        return this;
    }

    public SubTema addInstrumento(Instrumento instrumento) {
        this.instrumentos.add(instrumento);
        instrumento.setSubTema(this);
        return this;
    }

    public SubTema removeInstrumento(Instrumento instrumento) {
        this.instrumentos.remove(instrumento);
        instrumento.setSubTema(null);
        return this;
    }

    public void setInstrumentos(Set<Instrumento> instrumentos) {
        if (this.instrumentos != null) {
            this.instrumentos.forEach(i -> i.setSubTema(null));
        }
        if (instrumentos != null) {
            instrumentos.forEach(i -> i.setSubTema(this));
        }
        this.instrumentos = instrumentos;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public SubTema categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubTema)) {
            return false;
        }
        return id != null && id.equals(((SubTema) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubTema{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
