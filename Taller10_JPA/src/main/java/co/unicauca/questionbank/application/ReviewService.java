package co.unicauca.questionbank.application;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.Question;
import co.unicauca.questionbank.domain.repository.QuestionRepository;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Review and publication use cases. The service only loads, delegates to the domain
 * (which owns the transition rules) and stores: it never writes a status column by hand.
 */
@Service
@Transactional
public class ReviewService {
    private final QuestionRepository questions;

    /** Injects the repository contract. */
    public ReviewService(QuestionRepository questions) { this.questions = questions; }

    /** PENDING_REVIEW to IN_REVIEW. */
    public Question assignReviewer(Long questionId) { return apply(questionId, Question::assignReviewer); }
    /** IN_REVIEW to APPROVED. */
    public Question approve(Long questionId) { return apply(questionId, Question::approve); }
    /** IN_REVIEW back to DRAFT. */
    public Question reject(Long questionId) { return apply(questionId, Question::reject); }
    /** APPROVED to PUBLISHED. */
    public Question publish(Long questionId) { return apply(questionId, Question::publish); }
    /** PUBLISHED to ARCHIVED. */
    public Question archive(Long questionId) { return apply(questionId, Question::archive); }

    /** The whole operation runs in one transaction: if the domain rejects the transition nothing is stored. */
    private Question apply(Long questionId, Consumer<Question> transition) {
        Question question = questions.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found: " + questionId));
        transition.accept(question);
        return questions.save(question);
    }
}
