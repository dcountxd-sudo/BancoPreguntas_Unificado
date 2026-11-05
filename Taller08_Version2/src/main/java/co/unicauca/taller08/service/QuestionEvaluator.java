package co.unicauca.taller08.service;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.templatemethod.*;

import java.util.EnumMap;
import java.util.Map;

/**
 * Subsistema de evaluación: elige la plantilla (Template Method) según el tipo de pregunta.
 * Un tipo nuevo se incorpora con register(...) sin modificar evaluate(...).
 */
public class QuestionEvaluator {

    private final Map<QuestionType, QuestionEvaluationTemplate> templates = new EnumMap<>(QuestionType.class);

    public QuestionEvaluator() {
        register(QuestionType.SINGLE_CHOICE, new SingleChoiceEvaluator());
        register(QuestionType.MULTIPLE_CHOICE, new MultipleChoiceEvaluator());
        register(QuestionType.TRUE_FALSE, new TrueFalseEvaluator());
        register(QuestionType.OPEN, new OpenQuestionEvaluator());
    }

    public void register(QuestionType type, QuestionEvaluationTemplate template) {
        templates.put(type, template);
    }

    public EvaluationResult evaluate(Question question, String answer) {
        if (question == null) {
            throw new IllegalArgumentException("Pregunta obligatoria");
        }
        QuestionEvaluationTemplate template = templates.get(question.getType());
        if (template == null) {
            throw new IllegalArgumentException("Tipo de pregunta no soportado");
        }
        return template.evaluate(question, answer);
    }
}
