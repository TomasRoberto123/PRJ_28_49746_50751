package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Instrumento.
 */
@Entity
@Table(name = "instrumento")
public class Instrumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "fabricante")
    private String fabricante;

    @Column(name = "dimensoes")
    private String dimensoes;

    @Column(name = "descricao_path")
    private String descricaoPath;

    @OneToMany(mappedBy = "instrumento")
    @JsonIgnoreProperties(value = { "instrumento", "historia" }, allowSetters = true)
    private Set<Foto> fotos = new HashSet<>();

    @JsonIgnoreProperties(value = { "instrumento", "questionario", "opcaoQuestaoQuestionarios" }, allowSetters = true)
    @OneToOne(mappedBy = "instrumento")
    private QuestaoQuestionario questaoQuestionario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "instrumentos" }, allowSetters = true)
    private SubTema subTema;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instrumento id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Instrumento nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Instrumento codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLocalizacao() {
        return this.localizacao;
    }

    public Instrumento localizacao(String localizacao) {
        this.localizacao = localizacao;
        return this;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getFabricante() {
        return this.fabricante;
    }

    public Instrumento fabricante(String fabricante) {
        this.fabricante = fabricante;
        return this;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getDimensoes() {
        return this.dimensoes;
    }

    public Instrumento dimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
        return this;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public String getDescricaoPath() {
        return this.descricaoPath;
    }

    public Instrumento descricaoPath(String descricaoPath) {
        this.descricaoPath = descricaoPath;
        return this;
    }

    public void setDescricaoPath(String descricaoPath) {
        this.descricaoPath = descricaoPath;
    }

    public Set<Foto> getFotos() {
        return this.fotos;
    }

    public Instrumento fotos(Set<Foto> fotos) {
        this.setFotos(fotos);
        return this;
    }

    public Instrumento addFoto(Foto foto) {
        this.fotos.add(foto);
        foto.setInstrumento(this);
        return this;
    }

    public Instrumento removeFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.setInstrumento(null);
        return this;
    }

    public void setFotos(Set<Foto> fotos) {
        if (this.fotos != null) {
            this.fotos.forEach(i -> i.setInstrumento(null));
        }
        if (fotos != null) {
            fotos.forEach(i -> i.setInstrumento(this));
        }
        this.fotos = fotos;
    }

    public QuestaoQuestionario getQuestaoQuestionario() {
        return this.questaoQuestionario;
    }

    public Instrumento questaoQuestionario(QuestaoQuestionario questaoQuestionario) {
        this.setQuestaoQuestionario(questaoQuestionario);
        return this;
    }

    public void setQuestaoQuestionario(QuestaoQuestionario questaoQuestionario) {
        if (this.questaoQuestionario != null) {
            this.questaoQuestionario.setInstrumento(null);
        }
        if (questaoQuestionario != null) {
            questaoQuestionario.setInstrumento(this);
        }
        this.questaoQuestionario = questaoQuestionario;
    }

    public SubTema getSubTema() {
        return this.subTema;
    }

    public Instrumento subTema(SubTema subTema) {
        this.setSubTema(subTema);
        return this;
    }

    public void setSubTema(SubTema subTema) {
        this.subTema = subTema;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instrumento)) {
            return false;
        }
        return id != null && id.equals(((Instrumento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Instrumento{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", localizacao='" + getLocalizacao() + "'" +
            ", fabricante='" + getFabricante() + "'" +
            ", dimensoes='" + getDimensoes() + "'" +
            ", descricaoPath='" + getDescricaoPath() + "'" +
            "}";
    }
}
