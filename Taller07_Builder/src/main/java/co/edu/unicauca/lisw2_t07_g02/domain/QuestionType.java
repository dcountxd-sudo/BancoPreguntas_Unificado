package co.edu.unicauca.lisw2_t07_g02.domain;

import java.text.Normalizer;
import java.util.Locale;

public enum QuestionType {
    OPEN,
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    ABIERTA,
    OPCION_MULTIPLE,
    VERDADERO_FALSO;

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
        if (normalized.contains("MULTIPLE") || normalized.contains("OPCION")) {
            return MULTIPLE_CHOICE;
        }
        if (normalized.contains("TRUE") || normalized.contains("VERDADERO") || normalized.contains("FALSO")) {
            return TRUE_FALSE;
        }
        return OPEN;
    }
}
