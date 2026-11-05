package co.unicauca.questionbank.application;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.repository.*;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use cases about question content: create, query, classify and submit for review. */
@Service
@Transactional
public class QuestionService {
    private final QuestionRepository questions;
    private final UserRepository users;
    private final TopicRepository topics;
    private final CompetencyRepository competencies;

    /** Injects repository contracts, never JPA types. */
    public QuestionService(QuestionRepository questions, UserRepository users, TopicRepository topics, CompetencyRepository competencies) {
        this.questions = questions; this.users = users; this.topics = topics; this.competencies = competencies;
    }

    /** Use case 1: creates a draft multiple-choice question. */
    public Question createMultipleChoice(Long authorId, String statement, DifficultyLevel difficulty, Long topicId,
                                         Set<Long> competencyIds, List<AnswerOption> options, QuestionMetadata metadata) {
        MultipleChoiceQuestion.Builder builder = prepare(MultipleChoiceQuestion.builder(), authorId, statement, difficulty, topicId, competencyIds, metadata);
        return questions.save(builder.options(options).build());
    }

    /** Use case 1: creates a draft true/false question. */
    public Question createTrueFalse(Long authorId, String statement, DifficultyLevel difficulty, Long topicId,
                                    Set<Long> competencyIds, Boolean expectedValue, QuestionMetadata metadata) {
        TrueFalseQuestion.Builder builder = prepare(TrueFalseQuestion.builder(), authorId, statement, difficulty, topicId, competencyIds, metadata);
        return questions.save(builder.expectedValue(expectedValue).build());
    }

    /** Use case 2: finds one question. @throws NotFoundException if it does not exist */
    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questions.findById(id).orElseThrow(() -> new NotFoundException("Question not found: " + id));
    }
    /** Use case 2: lists all questions. */
    @Transactional(readOnly = true)
    public List<Question> findAll() { return questions.findAll(); }
    /** Use case 2: lists the questions of an author. */
    @Transactional(readOnly = true)
    public List<Question> findByAuthor(Long authorId) { return questions.findByAuthor(authorId); }
    /** Use case 2: lists the questions in a status. */
    @Transactional(readOnly = true)
    public List<Question> findByStatus(QuestionStatus status) { return questions.findByStatus(status); }
    /** Use case 2: lists the published questions of a competency. */
    @Transactional(readOnly = true)
    public List<Question> findPublishedByCompetency(Long competencyId) { return questions.findPublishedByCompetency(competencyId); }

    /** Use case 3: classifies a draft question with a topic and one or more competencies. */
    public Question classify(Long questionId, Long topicId, Set<Long> competencyIds) {
        Question question = findById(questionId);
        question.classify(loadTopic(topicId), loadCompetencies(competencyIds));
        return questions.save(question);
    }

    /** Use case 4: sends a draft to review; the domain checks the minimum conditions and the transition. */
    public Question submitForReview(Long questionId) {
        Question question = findById(questionId);
        question.submitForReview();
        return questions.save(question);
    }

    // ------------------------------------------------------------ helpers

    private <B extends Question.Builder<B>> B prepare(B builder, Long authorId, String statement, DifficultyLevel difficulty,
                                                      Long topicId, Set<Long> competencyIds, QuestionMetadata metadata) {
        User author = users.findById(authorId).orElseThrow(() -> new NotFoundException("Author not found: " + authorId));
        return builder.statement(statement).difficulty(difficulty).author(author).metadata(metadata)
                .topic(topicId == null ? null : loadTopic(topicId))
                .competencies(competencyIds == null ? Set.of() : loadCompetencies(competencyIds));
    }

    private Topic loadTopic(Long id) {
        return topics.findById(id).orElseThrow(() -> new NotFoundException("Topic not found: " + id));
    }

    private List<Competency> loadCompetencies(Set<Long> ids) {
        List<Competency> found = competencies.findAllById(ids);
        if (found.size() != ids.size()) throw new NotFoundException("One or more competencies do not exist: " + ids);
        return found;
    }
}
