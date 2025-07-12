package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Questionario.
 */
@Entity
@Table(name = "questionario")
public class Questionario implements Serializable, Comparable<Questionario> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @JsonIgnoreProperties(value = { "subTemas", "questionario" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Categoria categoria;

    @OneToMany(mappedBy = "questionario")
    @JsonIgnoreProperties(value = { "user", "questionario" }, allowSetters = true)
    private Set<RespostaUserQuestionario> respostaUserQuestionarios = new HashSet<>();

    @OneToMany(mappedBy = "questionario")
    @JsonIgnoreProperties(value = { "questionario", "opcaoQuestaoQuestionarios" }, allowSetters = true)
    private Set<QuestaoQuestionario> questaoQuestionarios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Questionario id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Questionario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Questionario descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Questionario categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Set<RespostaUserQuestionario> getRespostaUserQuestionarios() {
        return this.respostaUserQuestionarios;
    }

    public Questionario respostaUserQuestionarios(Set<RespostaUserQuestionario> respostaUserQuestionarios) {
        this.setRespostaUserQuestionarios(respostaUserQuestionarios);
        return this;
    }

    public Questionario addRespostaUserQuestionario(RespostaUserQuestionario respostaUserQuestionario) {
        this.respostaUserQuestionarios.add(respostaUserQuestionario);
        respostaUserQuestionario.setQuestionario(this);
        return this;
    }

    public Questionario removeRespostaUserQuestionario(RespostaUserQuestionario respostaUserQuestionario) {
        this.respostaUserQuestionarios.remove(respostaUserQuestionario);
        respostaUserQuestionario.setQuestionario(null);
        return this;
    }

    public void setRespostaUserQuestionarios(Set<RespostaUserQuestionario> respostaUserQuestionarios) {
        if (this.respostaUserQuestionarios != null) {
            this.respostaUserQuestionarios.forEach(i -> i.setQuestionario(null));
        }
        if (respostaUserQuestionarios != null) {
            respostaUserQuestionarios.forEach(i -> i.setQuestionario(this));
        }
        this.respostaUserQuestionarios = respostaUserQuestionarios;
    }

    public Set<QuestaoQuestionario> getQuestaoQuestionarios() {
        return this.questaoQuestionarios;
    }

    public Questionario questaoQuestionarios(Set<QuestaoQuestionario> questaoQuestionarios) {
        this.setQuestaoQuestionarios(questaoQuestionarios);
        return this;
    }

    public Questionario addQuestaoQuestionario(QuestaoQuestionario questaoQuestionario) {
        this.questaoQuestionarios.add(questaoQuestionario);
        questaoQuestionario.setQuestionario(this);
        return this;
    }

    public Questionario removeQuestaoQuestionario(QuestaoQuestionario questaoQuestionario) {
        this.questaoQuestionarios.remove(questaoQuestionario);
        questaoQuestionario.setQuestionario(null);
        return this;
    }

    public void setQuestaoQuestionarios(Set<QuestaoQuestionario> questaoQuestionarios) {
        if (this.questaoQuestionarios != null) {
            this.questaoQuestionarios.forEach(i -> i.setQuestionario(null));
        }
        if (questaoQuestionarios != null) {
            questaoQuestionarios.forEach(i -> i.setQuestionario(this));
        }
        this.questaoQuestionarios = questaoQuestionarios;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questionario)) {
            return false;
        }
        return id != null && id.equals(((Questionario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questionario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }

    @Override
    public int compareTo(Questionario o) {
        return this.getNome().compareTo(o.getNome());
    }
}
