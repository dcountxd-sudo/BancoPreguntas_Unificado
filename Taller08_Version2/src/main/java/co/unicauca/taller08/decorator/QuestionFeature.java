package co.unicauca.taller08.decorator;

import co.unicauca.taller08.domain.Question;

/** COMPONENT del Decorator: lo que tienen en común la pregunta básica y sus decoradores. */
public interface QuestionFeature {
    Question getQuestion();
    String describe();
    double getScore();
}
