package co.unicauca.taller08.domain;

import java.text.Normalizer;
import java.util.Locale;

public enum Difficultylevel {
    EASY,
    MEDIUM,
    HARD,
    BASICO,
    INTERMEDIO,
    AVANZADO;

    public static Difficultylevel fromString(String value) {
        if (value == null) {
            return MEDIUM;
        }

        String normalized = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');

        if (normalized.contains("EASY") || normalized.contains("FACIL") || normalized.contains("BASICO")) {
            return EASY;
        }
        if (normalized.contains("MEDIUM") || normalized.contains("INTERMEDIO") || normalized.contains("NORMAL")) {
            return MEDIUM;
        }
        if (normalized.contains("HARD") || normalized.contains("DIFICIL") || normalized.contains("AVANZADO")) {
            return HARD;
        }
        return MEDIUM;
    }
}
