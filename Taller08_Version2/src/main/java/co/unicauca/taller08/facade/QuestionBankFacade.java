package co.unicauca.taller08.facade;
import co.unicauca.taller08.domain.*;
import co.unicauca.taller08.builder.*;
import co.unicauca.taller08.adapter.*;
import co.unicauca.taller08.decorator.*;
import co.unicauca.taller08.templatemethod.*;
import co.unicauca.taller08.repository.*;
import co.unicauca.taller08.service.*;
import java.util.*;
/** Facade: el cliente usa una sola clase; esta coordina los seis subsistemas requeridos por la guía. */
public class QuestionBankFacade  {
    private final QuestionRepository repository=new QuestionRepository();
    private final QuestionValidator validator=new QuestionValidator();
    private final QuestionService service=new QuestionService(repository, validator);
    private final QuestionEvaluator evaluator=new QuestionEvaluator();
    private final QuestionPublisher publisher=new QuestionPublisher(validator);
    private final QuestionImporter importer=new QuestionImporter();
    public Question createQuestion(QuestionBuilder builder, String statement, String context, DifficultyLevel difficulty, String category, String subtopic, String competence, String author, String justification, String source, List<Option> options, String correctAnswer) {
        return service.create(builder, statement, context, difficulty, category, subtopic, competence, author, justification, source, options, correctAnswer);
    }
    /** Registra una pregunta ya construida (valida y guarda). */
    public void createQuestion(Question question) {
        service.registerImported(question);
    }
    public List<Question> getQuestions() {
        return service.findAll();
    }
    public Optional<Question> getQuestion(int index) {
        return service.findByIndex(index);
    }
    public void validate(Question q) {
        validator.validate(q);
    }
    public Question importQuestion(ExternalQuestionDTO dto) {
        Question q=importer.importQuestion(dto);
        service.registerImported(q);
        return q;
    }
    /** Importa una pregunta desde un servicio externo usando el Adapter. */
    public Question importExternalQuestion() {
        Question q=importer.importFrom(new ExternalQuestionAdapter(new ExternalQuestionService()));
        service.registerImported(q);
        return q;
    }
    public EvaluationResult evaluate(Question q, String answer) {
        return evaluator.evaluate(q, answer);
    }
    public QuestionFeature decorate(Question q, String feature) {
        return decorate(new BasicQuestionFeature(q), feature);
    }
    /** Permite apilar decoradores: envuelve un componente ya decorado. */
    public QuestionFeature decorate(QuestionFeature f, String feature) {
        if(feature==null)return f;
        switch(feature.trim().toLowerCase()) {
            case "dificultad":case "alta dificultad":return new HighDifficultyDecorator(f);
            case "contexto":case "contextualizada":return new ContextualizedDecorator(f);
            case "retroalimentacion":case "retroalimentación":return new FeedbackDecorator(f);
            case "competencia":return new CompetencyDecorator(f);
            case "revision":case "revisión":case "marcada para revisión":return new ReviewFlagDecorator(f);
            default:throw new IllegalArgumentException("Característica no reconocida: "+feature);
        }
    }
    // ---- Ciclo de vida (State) ----
    public void submitForReview(Question q) { publisher.submitForReview(q); }
    public void assignReviewer(Question q) { publisher.assignReviewer(q); }
    public void approve(Question q) { publisher.approve(q); }
    public void reject(Question q) { publisher.reject(q); }
    public void returnToDraft(Question q) { publisher.returnToDraft(q); }
    public void publish(Question q) { publisher.publish(q); }
    public void archive(Question q) { publisher.archive(q); }

    /** Operación de alto nivel: valida, aprueba (si está en revisión) y publica en una sola llamada. */
    public void publishQuestion(Question q) {
        validator.validate(q);
        if ("EN REVISIÓN".equals(q.getStatus())) {
            publisher.approve(q);
        }
        publisher.publish(q);
    }

    public String subsystemStatus() {
        return "QuestionService=OK, QuestionRepository="+repository.count()+" pregunta(s), QuestionValidator=OK, QuestionEvaluator=OK, QuestionPublisher=OK, QuestionImporter=OK";
    }
}
