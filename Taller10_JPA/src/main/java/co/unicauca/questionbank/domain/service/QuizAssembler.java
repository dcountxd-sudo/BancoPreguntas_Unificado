package co.unicauca.questionbank.domain.service;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.model.Question;
import co.unicauca.questionbank.domain.model.Quiz;
import co.unicauca.questionbank.domain.model.User;
import java.util.List;

/**
 * Domain service: builds a {@link Quiz} from a creator and a set of questions. The rule involves two
 * different aggregates (who may create a quiz, and which questions may enter it), so it does not belong
 * to either of them. It is stateless and knows nothing about repositories or transactions.
 */
public class QuizAssembler {

    /**
     * Creates a quiz after checking the creator and every question.
     *
     * @param creator the user creating the quiz; must be a teacher
     * @param title quiz title
     * @param questions stored questions to include; every one must be PUBLISHED
     * @return a new, unstored quiz
     * @throws BusinessRuleException if the creator is not a teacher or a question is not allowed
     */
    public Quiz assemble(User creator, String title, List<Question> questions) {
        if (!creator.canCreateQuizzes()) throw new BusinessRuleException("Only users with role TEACHER can create quizzes");
        Quiz quiz = new Quiz(null, title, creator.getId());
        questions.forEach(quiz::addQuestion);
        return quiz;
    }
}
