package museu.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Bibliografia.
 */
@Entity
@Table(name = "bibliografia")
public class Bibliografia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "texto_path")
    private String textoPath;

    @Column(name = "is_livro")
    private Boolean isLivro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bibliografia id(Long id) {
        this.id = id;
        return this;
    }

    public String getTextoPath() {
        return textoPath;
    }

    public void setTextoPath(String textoPath) {
        this.textoPath = textoPath;
    }

    public Boolean getIsLivro() {
        return isLivro;
    }

    public void setIsLivro(Boolean isLivro) {
        this.isLivro = isLivro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bibliografia)) {
            return false;
        }
        return id != null && id.equals(((Bibliografia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bibliografia{" +
            "id=" + getId() +
            ", textoPath='" + getTextoPath() + "'" +
            ", isLivro=" + getIsLivro() +
            "}";
    }
}
