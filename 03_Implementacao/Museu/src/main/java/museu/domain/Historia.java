package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Historia.
 */
@Entity
@Table(name = "historia")
public class Historia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "autor")
    private String autor;

    @Column(name = "conteudo_path")
    private String conteudoPath;

    @OneToMany(mappedBy = "historia")
    @JsonIgnoreProperties(value = { "instrumento", "historia" }, allowSetters = true)
    private Set<Foto> fotos = new HashSet<>();

    @OneToMany(mappedBy = "historia")
    @JsonIgnoreProperties(value = { "historia" }, allowSetters = true)
    private Set<ReferenciaHistoria> referenciaHistorias = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Historia id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Historia titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return this.autor;
    }

    public Historia autor(String autor) {
        this.autor = autor;
        return this;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getConteudoPath() {
        return this.conteudoPath;
    }

    public Historia conteudoPath(String conteudoPath) {
        this.conteudoPath = conteudoPath;
        return this;
    }

    public void setConteudoPath(String conteudoPath) {
        this.conteudoPath = conteudoPath;
    }

    public Set<Foto> getFotos() {
        return this.fotos;
    }

    public Historia fotos(Set<Foto> fotos) {
        this.setFotos(fotos);
        return this;
    }

    public Historia addFoto(Foto foto) {
        this.fotos.add(foto);
        foto.setHistoria(this);
        return this;
    }

    public Historia removeFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.setHistoria(null);
        return this;
    }

    public void setFotos(Set<Foto> fotos) {
        if (this.fotos != null) {
            this.fotos.forEach(i -> i.setHistoria(null));
        }
        if (fotos != null) {
            fotos.forEach(i -> i.setHistoria(this));
        }
        this.fotos = fotos;
    }

    public Set<ReferenciaHistoria> getReferenciaHistorias() {
        return this.referenciaHistorias;
    }

    public Historia referenciaHistorias(Set<ReferenciaHistoria> referenciaHistorias) {
        this.setReferenciaHistorias(referenciaHistorias);
        return this;
    }

    public Historia addReferenciaHistoria(ReferenciaHistoria referenciaHistoria) {
        this.referenciaHistorias.add(referenciaHistoria);
        referenciaHistoria.setHistoria(this);
        return this;
    }

    public Historia removeReferenciaHistoria(ReferenciaHistoria referenciaHistoria) {
        this.referenciaHistorias.remove(referenciaHistoria);
        referenciaHistoria.setHistoria(null);
        return this;
    }

    public void setReferenciaHistorias(Set<ReferenciaHistoria> referenciaHistorias) {
        if (this.referenciaHistorias != null) {
            this.referenciaHistorias.forEach(i -> i.setHistoria(null));
        }
        if (referenciaHistorias != null) {
            referenciaHistorias.forEach(i -> i.setHistoria(this));
        }
        this.referenciaHistorias = referenciaHistorias;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historia)) {
            return false;
        }
        return id != null && id.equals(((Historia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historia{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", autor='" + getAutor() + "'" +
            ", conteudoPath='" + getConteudoPath() + "'" +
            "}";
    }
}
