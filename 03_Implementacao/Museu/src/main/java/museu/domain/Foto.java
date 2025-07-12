package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Foto.
 */
@Entity
@Table(name = "foto")
public class Foto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "imagem_path")
    private String imagemPath;

    @Column(name = "is_figura")
    private Boolean isFigura;

    @Column(name = "legenda")
    private String legenda;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fotos", "questaoQuestionario", "subTema" }, allowSetters = true)
    private Instrumento instrumento;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fotos", "referenciaHistorias" }, allowSetters = true)
    private Historia historia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Foto id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Foto nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagemPath() {
        return this.imagemPath;
    }

    public Foto imagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
        return this;
    }

    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    public Boolean getIsFigura() {
        return this.isFigura;
    }

    public Foto isFigura(Boolean isFigura) {
        this.isFigura = isFigura;
        return this;
    }

    public void setIsFigura(Boolean isFigura) {
        this.isFigura = isFigura;
    }

    public String getLegenda() {
        return this.legenda;
    }

    public Foto legenda(String legenda) {
        this.legenda = legenda;
        return this;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public Instrumento getInstrumento() {
        return this.instrumento;
    }

    public Foto instrumento(Instrumento instrumento) {
        this.setInstrumento(instrumento);
        return this;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public Historia getHistoria() {
        return this.historia;
    }

    public Foto historia(Historia historia) {
        this.setHistoria(historia);
        return this;
    }

    public void setHistoria(Historia historia) {
        this.historia = historia;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Foto)) {
            return false;
        }
        return id != null && id.equals(((Foto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Foto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", imagemPath='" + getImagemPath() + "'" +
            ", isFigura='" + getIsFigura() + "'" +
            ", legenda='" + getLegenda() + "'" +
            "}";
    }
}
