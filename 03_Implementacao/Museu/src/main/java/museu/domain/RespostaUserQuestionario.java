package museu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A RespostaUserQuestionario.
 */
@Entity
@Table(name = "resposta_user_questionario")
public class RespostaUserQuestionario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "pontuacao")
    private Integer pontuacao;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "categoria", "respostaUserQuestionarios", "questaoQuestionarios" }, allowSetters = true)
    private Questionario questionario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RespostaUserQuestionario id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPontuacao() {
        return this.pontuacao;
    }

    public RespostaUserQuestionario pontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
        return this;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public User getUser() {
        return this.user;
    }

    public RespostaUserQuestionario user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Questionario getQuestionario() {
        return this.questionario;
    }

    public RespostaUserQuestionario questionario(Questionario questionario) {
        this.setQuestionario(questionario);
        return this;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RespostaUserQuestionario)) {
            return false;
        }
        return id != null && id.equals(((RespostaUserQuestionario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RespostaUserQuestionario{" +
            "id=" + getId() +
            ", pontuacao=" + getPontuacao() +
            "}";
    }
}
