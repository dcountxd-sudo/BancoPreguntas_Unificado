package co.unicauca.taller08.domain;

import java.text.Normalizer;
import java.util.Locale;

public enum QuestionType {
    OPEN,
    MULTIPLE_CHOICE,
    SINGLE_CHOICE,
    MULTIPLE_ANSWER,
    TRUE_FALSE;

    public static QuestionType fromString(String value) {
        if (value == null) {
            return OPEN;
        }

        String normalized = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');

        if (normalized.contains("OPEN") || normalized.contains("ABIERTA")) {
            return OPEN;
        }
        if (normalized.contains("MULTIPLE_ANSWER") || normalized.contains("MULTIPLE_ANSWERS")
                || normalized.contains("MULTIPLES_RESPUESTAS")) {
            return MULTIPLE_ANSWER;
        }
        if (normalized.contains("SINGLE_CHOICE") || normalized.contains("UNICA_RESPUESTA")) {
            return SINGLE_CHOICE;
        }
        if (normalized.contains("MULTIPLE") || normalized.contains("OPCION")) {
            return MULTIPLE_CHOICE;
        }
        if (normalized.contains("TRUE") || normalized.contains("VERDADERO") || normalized.contains("FALSO")) {
            return TRUE_FALSE;
        }
        return OPEN;
    }
}
