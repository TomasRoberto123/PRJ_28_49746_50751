package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A OpcaoQuestaoQuestionario.
 */
@Entity
@Table(name = "opcao_questao_questionario")
public class OpcaoQuestaoQuestionario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "opcao_texto")
    private String opcaoTexto;

    @Column(name = "correcta")
    private Boolean correcta;

    @ManyToOne
    @JsonIgnoreProperties(value = { "instrumento", "questionario", "opcaoQuestaoQuestionarios" }, allowSetters = true)
    private QuestaoQuestionario questaoQuestionario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpcaoQuestaoQuestionario id(Long id) {
        this.id = id;
        return this;
    }

    public String getOpcaoTexto() {
        return this.opcaoTexto;
    }

    public OpcaoQuestaoQuestionario opcaoTexto(String opcaoTexto) {
        this.opcaoTexto = opcaoTexto;
        return this;
    }

    public void setOpcaoTexto(String opcaoTexto) {
        this.opcaoTexto = opcaoTexto;
    }

    public Boolean getCorrecta() {
        return this.correcta;
    }

    public OpcaoQuestaoQuestionario correcta(Boolean correcta) {
        this.correcta = correcta;
        return this;
    }

    public void setCorrecta(Boolean correcta) {
        this.correcta = correcta;
    }

    public QuestaoQuestionario getQuestaoQuestionario() {
        return this.questaoQuestionario;
    }

    public OpcaoQuestaoQuestionario questaoQuestionario(QuestaoQuestionario questaoQuestionario) {
        this.setQuestaoQuestionario(questaoQuestionario);
        return this;
    }

    public void setQuestaoQuestionario(QuestaoQuestionario questaoQuestionario) {
        this.questaoQuestionario = questaoQuestionario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpcaoQuestaoQuestionario)) {
            return false;
        }
        return id != null && id.equals(((OpcaoQuestaoQuestionario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpcaoQuestaoQuestionario{" +
            "id=" + getId() +
            ", opcaoTexto='" + getOpcaoTexto() + "'" +
            ", correcta='" + getCorrecta() + "'" +
            "}";
    }
}
