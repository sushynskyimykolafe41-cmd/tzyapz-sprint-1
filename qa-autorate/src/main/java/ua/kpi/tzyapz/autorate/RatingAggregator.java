package ua.kpi.tzyapz.autorate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Агрегація рейтингів моделей: зважене середнє оцінок (BL-10) і список Top-N (BL-20).
 */
public class RatingAggregator {

    /** Мінімальна кількість голосів, з якою модель потрапляє до Top-N (BL-20). */
    public static final int MIN_VOTES = 3;

    /**
     * Обчислює рейтинг моделі як зважене середнє оцінок:
     * сума(оцінка × репутація) / сума(репутацій), округлене до 0,1 (BL-10).
     * Для порожнього списку голосів повертає 0,0.
     *
     * @param votes голоси за модель; оцінка 1..5, репутація 1..3
     * @return рейтинг 0,0..5,0
     * @throws IllegalArgumentException якщо {@code votes} дорівнює null або оцінка чи репутація поза межами
     */
    public double average(Collection<Vote> votes) {
        if (votes == null) {
            throw new IllegalArgumentException("votes не може бути null");
        }
        if (votes.isEmpty()) {
            return 0.0;
        }
        long weightedSum = 0;
        long weightTotal = 0;
        for (Vote vote : votes) {
            if (vote.rating() < 1 || vote.rating() > 5) {
                throw new IllegalArgumentException("оцінка поза межами 1..5: " + vote.rating());
            }
            if (vote.reputation() < 1 || vote.reputation() > 3) {
                throw new IllegalArgumentException("репутація поза межами 1..3: " + vote.reputation());
            }
            weightedSum += (long) vote.rating() * vote.reputation();
            weightTotal += vote.reputation();
        }
        return BigDecimal.valueOf(weightedSum)
                .divide(BigDecimal.valueOf(weightTotal), 1, RoundingMode.DOWN)
                .doubleValue();
    }

    /**
     * Повертає не більше ніж {@code n} моделей, упорядкованих за рейтингом за спаданням,
     * а при рівних рейтингах — за повною назвою за алфавітом; до списку потрапляють лише
     * моделі з достатньою кількістю голосів (BL-20).
     *
     * @param models моделі каталогу
     * @param n      розмір списку; для n ≤ 0 повертається порожній список
     * @return упорядкований список моделей
     * @throws IllegalArgumentException якщо {@code models} дорівнює null
     */
    public List<CarModel> topN(Collection<CarModel> models, int n) {
        if (models == null) {
            throw new IllegalArgumentException("models не може бути null");
        }
        if (n <= 0) {
            return List.of();
        }
        return models.stream()
                .filter(model -> model.votes() > MIN_VOTES)
                .sorted(Comparator.comparingDouble(CarModel::rating).reversed()
                        .thenComparing(CarModel::fullName, String.CASE_INSENSITIVE_ORDER))
                .limit(n)
                .collect(Collectors.toList());
    }
}
