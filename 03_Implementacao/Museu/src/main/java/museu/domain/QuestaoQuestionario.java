package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A QuestaoQuestionario.
 */
@Entity
@Table(name = "questao_questionario")
public class QuestaoQuestionario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "pergunta")
    private String pergunta;

    @Column(name = "pontuacao")
    private String pontuacao;

    @JsonIgnoreProperties(value = { "fotos", "questaoQuestionario", "subTema" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Instrumento instrumento;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categoria", "respostaUserQuestionarios", "questaoQuestionarios" }, allowSetters = true)
    private Questionario questionario;

    @OneToMany(mappedBy = "questaoQuestionario")
    @JsonIgnoreProperties(value = { "questaoQuestionario" }, allowSetters = true)
    private Set<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestaoQuestionario id(Long id) {
        this.id = id;
        return this;
    }

    public String getPergunta() {
        return this.pergunta;
    }

    public QuestaoQuestionario pergunta(String pergunta) {
        this.pergunta = pergunta;
        return this;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getPontuacao() {
        return this.pontuacao;
    }

    public QuestaoQuestionario pontuacao(String pontuacao) {
        this.pontuacao = pontuacao;
        return this;
    }

    public void setPontuacao(String pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Instrumento getInstrumento() {
        return this.instrumento;
    }

    public QuestaoQuestionario instrumento(Instrumento instrumento) {
        this.setInstrumento(instrumento);
        return this;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public Questionario getQuestionario() {
        return this.questionario;
    }

    public QuestaoQuestionario questionario(Questionario questionario) {
        this.setQuestionario(questionario);
        return this;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public Set<OpcaoQuestaoQuestionario> getOpcaoQuestaoQuestionarios() {
        return this.opcaoQuestaoQuestionarios;
    }

    public QuestaoQuestionario opcaoQuestaoQuestionarios(Set<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarios) {
        this.setOpcaoQuestaoQuestionarios(opcaoQuestaoQuestionarios);
        return this;
    }

    public QuestaoQuestionario addOpcaoQuestaoQuestionario(OpcaoQuestaoQuestionario opcaoQuestaoQuestionario) {
        this.opcaoQuestaoQuestionarios.add(opcaoQuestaoQuestionario);
        opcaoQuestaoQuestionario.setQuestaoQuestionario(this);
        return this;
    }

    public QuestaoQuestionario removeOpcaoQuestaoQuestionario(OpcaoQuestaoQuestionario opcaoQuestaoQuestionario) {
        this.opcaoQuestaoQuestionarios.remove(opcaoQuestaoQuestionario);
        opcaoQuestaoQuestionario.setQuestaoQuestionario(null);
        return this;
    }

    public void setOpcaoQuestaoQuestionarios(Set<OpcaoQuestaoQuestionario> opcaoQuestaoQuestionarios) {
        if (this.opcaoQuestaoQuestionarios != null) {
            this.opcaoQuestaoQuestionarios.forEach(i -> i.setQuestaoQuestionario(null));
        }
        if (opcaoQuestaoQuestionarios != null) {
            opcaoQuestaoQuestionarios.forEach(i -> i.setQuestaoQuestionario(this));
        }
        this.opcaoQuestaoQuestionarios = opcaoQuestaoQuestionarios;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestaoQuestionario)) {
            return false;
        }
        return id != null && id.equals(((QuestaoQuestionario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestaoQuestionario{" +
            "id=" + getId() +
            ", pergunta='" + getPergunta() + "'" +
            ", pontuacao='" + getPontuacao() + "'" +
            "}";
    }
}
