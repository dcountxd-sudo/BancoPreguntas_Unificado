package co.unicauca.questionbank.config;

import co.unicauca.questionbank.application.CatalogService;
import co.unicauca.questionbank.domain.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Loads reference data at startup so the API can be tried right away. Disable with app.seed-demo-data=false. */
@Component
@ConditionalOnProperty(name = "app.seed-demo-data", havingValue = "true", matchIfMissing = true)
public class DemoDataLoader implements CommandLineRunner {
    private final CatalogService catalog;

    /** @param catalog catalog use cases */
    public DemoDataLoader(CatalogService catalog) { this.catalog = catalog; }

    @Override
    public void run(String... args) {
        catalog.createUser("admin", "Admin User", "admin@unicauca.edu.co", Role.ADMIN);
        catalog.createUser("author", "Author User", "author@unicauca.edu.co", Role.AUTHOR);
        catalog.createUser("reviewer", "Reviewer User", "reviewer@unicauca.edu.co", Role.REVIEWER);
        catalog.createUser("teacher", "Teacher User", "teacher@unicauca.edu.co", Role.TEACHER);
        catalog.createUser("student", "Student User", "student@unicauca.edu.co", Role.STUDENT);
        catalog.createTopic("Software architecture");
        catalog.createTopic("Software design");
        catalog.createCompetency("Problem analysis", "Understands a problem and breaks it down");
        catalog.createCompetency("Solution design", "Proposes and justifies a design");
        catalog.createCompetency("Software quality", "Evaluates quality attributes");
    }
}
