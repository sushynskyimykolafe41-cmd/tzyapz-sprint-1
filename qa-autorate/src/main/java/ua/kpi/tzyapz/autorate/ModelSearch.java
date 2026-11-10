package ua.kpi.tzyapz.autorate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Пошук моделей за префіксом (BL-60) і сортування за рейтингом (BL-70).
 */
public class ModelSearch {

    /**
     * Шукає моделі, назва марки або назва моделі яких починається із запиту; регістр не враховується.
     * Порожній або пробільний запит дає порожній результат (BL-60).
     *
     * @param query  пошуковий запит
     * @param models моделі каталогу
     * @return знайдені моделі у порядку вхідного списку
     * @throws IllegalArgumentException якщо {@code models} дорівнює null
     */
    public List<CarModel> search(String query, Collection<CarModel> models) {
        if (models == null) {
            throw new IllegalArgumentException("models не може бути null");
        }
        if (query == null || query.isBlank()) {
            return new ArrayList<>(models);
        }
        String prefix = query.trim().toLowerCase(Locale.ROOT);
        return models.stream()
                .filter(model -> model.make().toLowerCase(Locale.ROOT).startsWith(prefix)
                        || model.name().toLowerCase(Locale.ROOT).startsWith(prefix))
                .collect(Collectors.toList());
    }

    /**
     * Повертає новий список моделей, упорядкованих за рейтингом за спаданням, а при рівних
     * рейтингах — за повною назвою за алфавітом; вхідний список не змінюється (BL-70).
     *
     * @param models моделі каталогу
     * @return новий упорядкований список
     * @throws IllegalArgumentException якщо {@code models} дорівнює null
     */
    public List<CarModel> sortByRating(Collection<CarModel> models) {
        if (models == null) {
            throw new IllegalArgumentException("models не може бути null");
        }
        return models.stream()
                .sorted(Comparator.comparingDouble(CarModel::rating)
                        .thenComparing(CarModel::fullName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }
}
