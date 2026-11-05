package co.unicauca.taller08;

import co.unicauca.taller08.builder.MultipleChoiceQuestionBuilder;
import co.unicauca.taller08.builder.OpenQuestionBuilder;
import co.unicauca.taller08.builder.QuestionBuilder;
import co.unicauca.taller08.builder.TrueFalseQuestionBuilder;
import co.unicauca.taller08.director.QuestionDirector;
import co.unicauca.taller08.adapter.ExternalQuestionAdapter;
import co.unicauca.taller08.adapter.SampleExternalQuestionService;
import co.unicauca.taller08.facade.QuestionBankFacade;
import co.unicauca.taller08.facade.QuestionFeature;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;

public class Cliente {

    public static void main(String[] args) {
        System.out.println("=== Banco de Preguntas Saber PRO ===");

        QuestionDirector director = new QuestionDirector();
        QuestionBuilder[] builders = new QuestionBuilder[] {
                new OpenQuestionBuilder(),
                new MultipleChoiceQuestionBuilder(),
                new TrueFalseQuestionBuilder()
        };

        QuestionBankFacade bank = new QuestionBankFacade();
        String multipleChoiceId = null;
        for (QuestionBuilder builder : builders) {
            director.setQuestionBuilder(builder);
            director.construirPregunta();
            Question pregunta = director.getQuestion();
            String id = bank.createQuestion(pregunta);
            if (pregunta.getType() == QuestionType.MULTIPLE_CHOICE) {
                multipleChoiceId = id;
            }
        }

        System.out.println("Preguntas registradas: " + bank.getQuestions().size());
        System.out.println("Estado inicial: " + bank.getQuestion(multipleChoiceId).getState().getName());
        bank.submitForReview(multipleChoiceId);
        bank.assignReviewer(multipleChoiceId);
        bank.approve(multipleChoiceId);
        bank.publish(multipleChoiceId);
        System.out.println("Estado final: " + bank.getQuestion(multipleChoiceId).getState().getName());

        System.out.println("Evaluación de la opción C: "
                + (bank.evaluate(multipleChoiceId, List.of("C")) ? "correcta" : "incorrecta"));

        System.out.println("Pregunta decorada: " + bank.decorateQuestion(
                multipleChoiceId,
                QuestionFeature.HIGH_DIFFICULTY,
                QuestionFeature.CONTEXTUALIZED
        ).getDescription());

        SampleExternalQuestionService externalService = new SampleExternalQuestionService(
            "{\"question_text\":\"¿Qué planeta es conocido como el planeta rojo?\",\"answer_text\":\"Marte\"}"
        );
        String importedId = bank.importQuestion(new ExternalQuestionAdapter(externalService));
        Question imported = bank.getQuestion(importedId);
        System.out.println("Pregunta importada con Adapter: " + imported.getStatement());
        System.out.println("Respuesta externa adaptada: " + imported.getCorrectAnswer());
        bank.submitForReview(importedId);
        bank.assignReviewer(importedId);
        bank.approve(importedId);
        bank.publish(importedId);
        System.out.println("Evaluación de pregunta importada: "
                + bank.evaluateWithFeedback(importedId, List.of("MARTE")));
    }
}
