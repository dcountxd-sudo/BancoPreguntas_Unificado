package co.unicauca.questionbank.api;

import co.unicauca.questionbank.api.ApiModels.*;
import co.unicauca.questionbank.application.QuizService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Quiz endpoints. */
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizzes;

    /** @param quizzes quiz use cases */
    public QuizController(QuizService quizzes) { this.quizzes = quizzes; }

    /** Creates a quiz from published questions. */
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public QuizResponse create(@RequestBody CreateQuizRequest r) { return QuizResponse.from(quizzes.create(r.creatorId(), r.title(), r.questionIds())); }
    /** Gets a quiz. */
    @GetMapping("/{id}")
    public QuizResponse get(@PathVariable Long id) { return QuizResponse.from(quizzes.findById(id)); }
    /** Lists quizzes. */
    @GetMapping
    public List<QuizResponse> list() { return quizzes.findAll().stream().map(QuizResponse::from).toList(); }
}
