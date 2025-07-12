package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ReferenciaHistoria.
 */
@Entity
@Table(name = "referencia_historia")
public class ReferenciaHistoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ancora")
    private String ancora;

    @Column(name = "conteudo")
    private String conteudo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fotos", "referenciaHistorias" }, allowSetters = true)
    private Historia historia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReferenciaHistoria id(Long id) {
        this.id = id;
        return this;
    }

    public String getAncora() {
        return this.ancora;
    }

    public ReferenciaHistoria ancora(String ancora) {
        this.ancora = ancora;
        return this;
    }

    public void setAncora(String ancora) {
        this.ancora = ancora;
    }

    public String getConteudo() {
        return this.conteudo;
    }

    public ReferenciaHistoria conteudo(String conteudo) {
        this.conteudo = conteudo;
        return this;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Historia getHistoria() {
        return this.historia;
    }

    public ReferenciaHistoria historia(Historia historia) {
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
        if (!(o instanceof ReferenciaHistoria)) {
            return false;
        }
        return id != null && id.equals(((ReferenciaHistoria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferenciaHistoria{" +
            "id=" + getId() +
            ", ancora='" + getAncora() + "'" +
            ", conteudo='" + getConteudo() + "'" +
            "}";
    }
}
