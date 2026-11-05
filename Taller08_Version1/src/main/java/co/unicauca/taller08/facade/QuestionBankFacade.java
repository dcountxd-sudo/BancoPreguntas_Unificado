package co.unicauca.taller08.facade;

import co.unicauca.taller08.adapter.QuestionProvider;
import co.unicauca.taller08.decorator.BasicQuestion;
import co.unicauca.taller08.decorator.CompetencyQuestion;
import co.unicauca.taller08.decorator.ContextualizedQuestion;
import co.unicauca.taller08.decorator.FeedbackQuestion;
import co.unicauca.taller08.decorator.HighDifficultyQuestion;
import co.unicauca.taller08.decorator.MarkedForReviewQuestion;
import co.unicauca.taller08.decorator.QuestionComponent;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.templatemethod.MultipleAnswerEvaluator;
import co.unicauca.taller08.templatemethod.OpenQuestionEvaluator;
import co.unicauca.taller08.templatemethod.EvaluationResult;
import co.unicauca.taller08.templatemethod.QuestionEvaluator;
import co.unicauca.taller08.templatemethod.SingleChoiceEvaluator;
import co.unicauca.taller08.templatemethod.TrueFalseEvaluator;
import java.util.List;

public class QuestionBankFacade {
    private final QuestionRepository repository;
    private final QuestionValidator validator;
    private final QuestionService questionService;
    private final QuestionPublisher publisher;
    private final QuestionImporter importer;

    public QuestionBankFacade() {
        repository = new QuestionRepository();
        validator = new QuestionValidator();
        questionService = new QuestionService(repository, validator);
        publisher = new QuestionPublisher(validator);
        importer = new QuestionImporter(validator);
    }

    public String createQuestion(Question question) {
        return questionService.create(question);
    }

    public Question getQuestion(String id) {
        return questionService.get(id);
    }

    public List<Question> getQuestions() {
        return questionService.getAll();
    }

    public void submitForReview(String id) {
        getQuestion(id).submitForReview();
    }

    public void assignReviewer(String id) {
        getQuestion(id).assignReviewer();
    }

    public void approve(String id) {
        getQuestion(id).approve();
    }

    public void reject(String id) {
        getQuestion(id).reject();
    }

    public void publish(String id) {
        publisher.publish(getQuestion(id));
    }

    public void archive(String id) {
        getQuestion(id).archive();
    }

    public boolean evaluate(String id, List<String> answers) {
        Question question = getQuestion(id);
        if (!question.isAvailableForAssessment()) {
            throw new IllegalStateException("Solo se pueden evaluar preguntas publicadas.");
        }
        return evaluatorFor(question.getType()).evaluate(question, answers);
    }

    public QuestionComponent decorateQuestion(String id, QuestionFeature... features) {
        Question question = getQuestion(id);
        QuestionComponent component = new BasicQuestion(question);
        if (features == null) {
            return component;
        }

        for (QuestionFeature feature : features) {
            if (feature == null) {
                continue;
            }
            component = switch (feature) {
                case HIGH_DIFFICULTY -> new HighDifficultyQuestion(component);
                case CONTEXTUALIZED -> new ContextualizedQuestion(component);
                case FEEDBACK -> new FeedbackQuestion(component,
                        question.getExplanation().isBlank() ? "Revise cuidadosamente su respuesta." : question.getExplanation());
                case COMPETENCY -> new CompetencyQuestion(component, "Razonamiento");
                case MARKED_FOR_REVIEW -> new MarkedForReviewQuestion(component);
            };
        }
        return component;
    }

    public String importQuestion(QuestionProvider provider) {
        Question question = importer.importQuestion(provider);
        return questionService.create(question);
    }

    public EvaluationResult evaluateWithFeedback(String id, List<String> answers) {
        Question question = getQuestion(id);
        if (!question.isAvailableForAssessment()) {
            throw new IllegalStateException("Solo se pueden evaluar preguntas publicadas.");
        }
        return evaluatorFor(question.getType()).evaluateWithFeedback(question, answers);
    }

    private QuestionEvaluator evaluatorFor(QuestionType type) {
        return switch (type) {
            case MULTIPLE_CHOICE, SINGLE_CHOICE -> new SingleChoiceEvaluator();
            case MULTIPLE_ANSWER -> new MultipleAnswerEvaluator();
            case TRUE_FALSE -> new TrueFalseEvaluator();
            case OPEN -> new OpenQuestionEvaluator();
            default -> throw new IllegalArgumentException("No hay evaluador configurado para " + type + ".");
        };
    }
}
