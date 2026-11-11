package ua.kpi.tzyapz.autorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести RatingAggregator: рейтинг моделі (BL-10) і Top-N (BL-20).
 * Очікувані значення взято з розділу BL виправленої SRS «АвтоРейт», а не з поведінки коду.
 */
@Tag("unit")
@DisplayName("RatingAggregator — рейтинг моделі (BL-10) і Top-N (BL-20)")
class RatingAggregatorTest {

    private RatingAggregator aggregator;

    @BeforeEach
    void setUp() {
        aggregator = new RatingAggregator();
    }

    // ---------- BL-10: зважене середнє оцінок (автор: Баришполь Іван) ----------

    @ParameterizedTest(name = "BL-10: ({0}×{1} + {2}×{3}) / ({1} + {3}) = {4}")
    @DisplayName("BL-10: рейтинг — середнє оцінок, зважене на репутацію")
    @CsvSource({
            "5, 1, 3, 1, 4.0",
            "1, 3, 5, 1, 2.0",
            "5, 3, 1, 1, 4.0",
            "2, 2, 3, 1, 2.3"
    })
    void average_isWeightedByReputation(int rating1, int reputation1, int rating2, int reputation2, double expected) {
        List<Vote> votes = List.of(
                new Vote("u1", "m1", rating1, reputation1, ""),
                new Vote("u2", "m1", rating2, reputation2, ""));
        assertEquals(expected, aggregator.average(votes), 1e-9);
    }

    // BUG-014: модуль v1 округлює вниз (RoundingMode.DOWN) — тест лишається червоним до виправлення у v2
    @Test
    @DisplayName("BL-10: (5×2 + 4×1) / 3 = 4,666… → 4,7 (округлення до 0,1 за HALF_UP)")
    void average_roundsHalfUpToOneDecimal() {
        // arrange
        List<Vote> votes = List.of(
                new Vote("u1", "m1", 5, 2, "чудово"),
                new Vote("u2", "m1", 4, 1, ""));
        // act
        double rating = aggregator.average(votes);
        // assert
        assertEquals(4.7, rating, 1e-9, "BL-10: 14 / 3 = 4,666… має округлюватися до 4,7");
    }

    @ParameterizedTest(name = "BL-10: один голос з оцінкою {0} і репутацією {1} → рейтинг {0}")
    @DisplayName("BL-10: один голос — рейтинг дорівнює його оцінці за будь-якої репутації")
    @CsvSource({"1, 3", "5, 1", "3, 2"})
    void average_singleVoteEqualsItsRating(int rating, int reputation) {
        List<Vote> votes = List.of(new Vote("u1", "m1", rating, reputation, ""));
        assertEquals(rating, aggregator.average(votes), 1e-9);
    }

    @Test
    @DisplayName("BL-10: порожній список голосів → рейтинг 0,0")
    void average_emptyVotesReturnsZero() {
        assertEquals(0.0, aggregator.average(List.of()), 1e-9);
    }

    @ParameterizedTest(name = "BL-10: оцінка {0}, репутація {1} → IllegalArgumentException")
    @DisplayName("BL-10: оцінка поза 1..5 або репутація поза 1..3 — IllegalArgumentException")
    @CsvSource({"0, 1", "6, 1", "3, 0", "3, 4"})
    void average_outOfRangeVoteThrows(int rating, int reputation) {
        List<Vote> votes = List.of(new Vote("u1", "m1", rating, reputation, ""));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> aggregator.average(votes));
        assertNotNull(ex.getMessage());
    }

    // ---------- BL-20: Top-N моделей (автор: Сушинський Микола) ----------

    @Test
    @DisplayName("BL-20: topN(…, 2) — дві моделі з найвищим рейтингом, за спаданням")
    void topN_returnsBestModelsByRatingDescending() {
        // arrange
        List<CarModel> catalog = List.of(
                new CarModel("Lamborghini", "Diablo", 4.6, 12),
                new CarModel("Pagani", "Huayra", 4.9, 10),
                new CarModel("Lancia", "Delta", 3.8, 9),
                new CarModel("Ferrari", "F40", 4.7, 20));
        // act
        List<CarModel> top = aggregator.topN(catalog, 2);
        // assert
        assertAll("topN(catalog, 2)",
                () -> assertEquals(2, top.size()),
                () -> assertEquals("Pagani Huayra", top.get(0).fullName()),
                () -> assertEquals("Ferrari F40", top.get(1).fullName()));
    }

    @Test
    @DisplayName("BL-20: однаковий рейтинг — порядок за повною назвою за алфавітом")
    void topN_equalRatingsOrderedByFullName() {
        List<CarModel> catalog = List.of(
                new CarModel("Lancia", "Delta", 4.5, 10),
                new CarModel("Ferrari", "F40", 4.5, 10),
                new CarModel("Lamborghini", "Diablo", 4.5, 10));
        List<String> names = aggregator.topN(catalog, 3).stream().map(CarModel::fullName).toList();
        assertIterableEquals(List.of("Ferrari F40", "Lamborghini Diablo", "Lancia Delta"), names);
    }

    // BUG-015: модуль v1 бере у Top-N моделі з 4 голосами (votes > 3), а за BL-20 потрібно щонайменше 5
    @ParameterizedTest(name = "BL-20: модель із {0} голосами потрапляє у Top-N — {1}")
    @DisplayName("BL-20: у Top-N лише моделі щонайменше з 5 голосами (межа 4 / 5)")
    @CsvSource({"3, false", "4, false", "5, true", "6, true"})
    void topN_includesOnlyModelsWithEnoughVotes(int votes, boolean expectedInTop) {
        List<CarModel> catalog = List.of(new CarModel("Lancia", "Delta", 4.0, votes));
        assertEquals(expectedInTop, !aggregator.topN(catalog, 5).isEmpty());
    }

    @ParameterizedTest(name = "BL-20: topN(…, {0}) → порожній список")
    @DisplayName("BL-20: n ≤ 0 — порожній список")
    @ValueSource(ints = {0, -1})
    void topN_nonPositiveSizeReturnsEmptyList(int n) {
        List<CarModel> catalog = List.of(new CarModel("Ferrari", "F40", 4.7, 20));
        assertTrue(aggregator.topN(catalog, n).isEmpty());
    }

    @Test
    @DisplayName("BL-20: n більше за кількість придатних моделей — повертаються всі придатні")
    void topN_sizeLargerThanEligibleReturnsAllEligible() {
        List<CarModel> catalog = List.of(
                new CarModel("Ferrari", "F40", 4.7, 20),
                new CarModel("Pagani", "Huayra", 4.9, 10));
        assertEquals(2, aggregator.topN(catalog, 10).size());
    }
}
