package co.unicauca.taller08.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import co.unicauca.taller08.state.QuestionState;
import co.unicauca.taller08.state.DraftState;

/**
 * PRODUCTO del patron Builder: una pregunta del Banco de Preguntas.
 * Se construye paso a paso desde un QuestionBuilder.
 */
public class Question {

    private QuestionType type;

    private String statement;          
    private String context;            
    private DifficultyLevel difficulty;
    private String category;          
    private String subtopic;         
    private String competence;         
    private String author;

    private String justification;     
    private String source;             

    private QuestionState state = new DraftState();

    private List<Option> options = new ArrayList<>();
    private String correctAnswer;     

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return state.getName();
    }

    /** Compatibilidad con el modelo del Taller 07; solo para reconstruir un estado conocido. */
    public void setStatus(String status) {
        if (status == null) throw new IllegalArgumentException("El estado no puede ser nulo");
        switch (status.trim().toUpperCase()) {
            case "BORRADOR": state = new DraftState(); break;
            case "PENDIENTE DE REVISION": case "PENDIENTE DE REVISIÓN": state = new co.unicauca.taller08.state.PendingReviewState(); break;
            case "EN REVISION": case "EN REVISIÓN": state = new co.unicauca.taller08.state.ReviewState(); break;
            case "RECHAZADA": state = new co.unicauca.taller08.state.RejectedState(); break;
            case "APROBADA": state = new co.unicauca.taller08.state.ApprovedState(); break;
            case "PUBLICADA": state = new co.unicauca.taller08.state.PublishedState(); break;
            case "ARCHIVADA": state = new co.unicauca.taller08.state.ArchivedState(); break;
            default: throw new IllegalArgumentException("Estado desconocido: " + status);
        }
    }

    public QuestionState getState() { return state; }

    public void setState(QuestionState state) {
        if (state == null) throw new IllegalArgumentException("El estado no puede ser nulo");
        this.state = state;
    }

    // Las operaciones se delegan en el estado actual (patrón State).
    public void submitForReview() { state.submitForReview(this); }
    public void assignReviewer() { state.assignReviewer(this); }
    public void approve() { state.approve(this); }
    public void reject() { state.reject(this); }
    public void returnToDraft() { state.returnToDraft(this); }
    public void publish() { state.publish(this); }
    public void archive() { state.archive(this); }

    public List<Option> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public void setOptions(List<Option> options) {
        this.options = (options == null) ? new ArrayList<>() : new ArrayList<>(options);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(type).append("] ").append(statement).append("\n");

        if (context != null) {
            sb.append("  Contexto: ").append(context).append("\n");
        }

        sb.append("  Dificultad: ").append(difficulty)
          .append(" | Categoria: ").append(category)
          .append(" | Subtema: ").append(subtopic).append("\n");

        sb.append("  Competencia: ").append(competence)
          .append(" | Autor: ").append(author).append("\n");

        sb.append("  Estado: ").append(getStatus()).append("\n");

        for (Option o : options) {
            sb.append("    ").append(o).append("\n");
        }

        sb.append("  Respuesta correcta: ").append(correctAnswer).append("\n");

        if (justification != null) {
            sb.append("  Justificacion: ").append(justification).append("\n");
        }

        if (source != null) {
            sb.append("  Fuente: ").append(source).append("\n");
        }

        return sb.toString();
    }
}