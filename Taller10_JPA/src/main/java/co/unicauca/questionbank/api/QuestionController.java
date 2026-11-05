package co.unicauca.questionbank.api;

import co.unicauca.questionbank.api.ApiModels.*;
import co.unicauca.questionbank.application.QuestionService;
import co.unicauca.questionbank.application.ReviewService;
import co.unicauca.questionbank.domain.model.AnswerOption;
import co.unicauca.questionbank.domain.model.Question;
import co.unicauca.questionbank.domain.model.QuestionStatus;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Question endpoints. The controller only translates HTTP to use cases; it holds no business rule. */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questions;
    private final ReviewService reviews;

    /** @param questions content use cases @param reviews review and publication use cases */
    public QuestionController(QuestionService questions, ReviewService reviews) { this.questions = questions; this.reviews = reviews; }

    /** Creates a draft multiple-choice question. */
    @PostMapping("/multiple-choice") @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse createMultipleChoice(@RequestBody CreateMultipleChoiceRequest r) {
        List<AnswerOption> options = r.options() == null ? List.of() : r.options().stream().map(OptionRequest::toDomain).toList();
        return QuestionResponse.from(questions.createMultipleChoice(r.authorId(), r.statement(), r.difficulty(), r.topicId(),
            r.competencyIds(), options, r.metadata() == null ? null : r.metadata().toDomain()));
    }

    /** Creates a draft true/false question. */
    @PostMapping("/true-false") @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse createTrueFalse(@RequestBody CreateTrueFalseRequest r) {
        return QuestionResponse.from(questions.createTrueFalse(r.authorId(), r.statement(), r.difficulty(), r.topicId(),
            r.competencyIds(), r.expectedValue(), r.metadata() == null ? null : r.metadata().toDomain()));
    }

    /** Lists questions; optional filters: authorId, status, or competencyId (published questions only). */
    @GetMapping
    public List<QuestionResponse> list(@RequestParam(required = false) Long authorId,
                                       @RequestParam(required = false) QuestionStatus status,
                                       @RequestParam(required = false) Long competencyId) {
        List<Question> result;
        if (authorId != null) result = questions.findByAuthor(authorId);
        else if (status != null) result = questions.findByStatus(status);
        else if (competencyId != null) result = questions.findPublishedByCompetency(competencyId);
        else result = questions.findAll();
        return result.stream().map(QuestionResponse::from).toList();
    }

    /** Gets one question. */
    @GetMapping("/{id}")
    public QuestionResponse get(@PathVariable Long id) { return QuestionResponse.from(questions.findById(id)); }

    /** Classifies a draft question. */
    @PutMapping("/{id}/classification")
    public QuestionResponse classify(@PathVariable Long id, @RequestBody ClassifyRequest r) {
        return QuestionResponse.from(questions.classify(id, r.topicId(), r.competencyIds()));
    }

    /** DRAFT to PENDING_REVIEW. */
    @PostMapping("/{id}/submit")
    public QuestionResponse submit(@PathVariable Long id) { return QuestionResponse.from(questions.submitForReview(id)); }
    /** PENDING_REVIEW to IN_REVIEW. */
    @PostMapping("/{id}/assign-reviewer")
    public QuestionResponse assignReviewer(@PathVariable Long id) { return QuestionResponse.from(reviews.assignReviewer(id)); }
    /** IN_REVIEW to APPROVED. */
    @PostMapping("/{id}/approve")
    public QuestionResponse approve(@PathVariable Long id) { return QuestionResponse.from(reviews.approve(id)); }
    /** IN_REVIEW back to DRAFT. */
    @PostMapping("/{id}/reject")
    public QuestionResponse reject(@PathVariable Long id) { return QuestionResponse.from(reviews.reject(id)); }
    /** APPROVED to PUBLISHED. */
    @PostMapping("/{id}/publish")
    public QuestionResponse publish(@PathVariable Long id) { return QuestionResponse.from(reviews.publish(id)); }
    /** PUBLISHED to ARCHIVED. */
    @PostMapping("/{id}/archive")
    public QuestionResponse archive(@PathVariable Long id) { return QuestionResponse.from(reviews.archive(id)); }
}
