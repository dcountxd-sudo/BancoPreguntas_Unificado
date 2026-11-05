package co.edu.unicauca.lisw2_t07_g02;

import co.edu.unicauca.lisw2_t07_g02.builder.MultipleChoiceQuestionBuilder;
import co.edu.unicauca.lisw2_t07_g02.builder.OpenQuestionBuilder;
import co.edu.unicauca.lisw2_t07_g02.builder.QuestionBuilder;
import co.edu.unicauca.lisw2_t07_g02.builder.TrueFalseQuestionBuilder;
import co.edu.unicauca.lisw2_t07_g02.director.QuestionDirector;
import co.edu.unicauca.lisw2_t07_g02.domain.Question;

public class Cliente {

    public static void main(String[] args) {
        System.out.println("=== Cliente - Patrón Builder ===");

        QuestionDirector director = new QuestionDirector();
        QuestionBuilder[] builders = new QuestionBuilder[] {
                new OpenQuestionBuilder(),
                new MultipleChoiceQuestionBuilder(),
                new TrueFalseQuestionBuilder()
        };

        for (QuestionBuilder builder : builders) {
            director.setQuestionBuilder(builder);
            director.construirPregunta();
            Question pregunta = director.getQuestion();
            System.out.println("----------------------------------------");
            System.out.println(pregunta);
        }
    }
}
