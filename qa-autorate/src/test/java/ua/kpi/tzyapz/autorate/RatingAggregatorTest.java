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
}
