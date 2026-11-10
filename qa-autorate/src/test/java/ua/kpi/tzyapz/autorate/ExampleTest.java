package ua.kpi.tzyapz.autorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Зразок стилю юніт-тестів для модуля «АвтоРейт»: один клас — одна тема,
 * говірні @DisplayName з посиланням на правило BL, перевірка меж.
 */
@DisplayName("autorate-module — зразок юніт-тестів JUnit 5")
class ExampleTest {

    private RatingAggregator aggregator;
    private VoteValidator validator;
    private ModelSearch search;
    private List<CarModel> catalog;

    @BeforeEach
    void setUp() {
        aggregator = new RatingAggregator();
        validator = new VoteValidator();
        search = new ModelSearch();
        catalog = List.of(
                new CarModel("Lamborghini", "Diablo", 4.6, 12),
                new CarModel("Ferrari", "F40", 4.2, 8));
    }

    @Test
    @DisplayName("BL-10: average — (5×2 + 3×1) / 3 = 4,3")
    void weightedAverage() {
        List<Vote> votes = List.of(
                new Vote("u1", "m1", 5, 2, "чудово"),
                new Vote("u2", "m1", 3, 1, ""));
        assertEquals(4.3, aggregator.average(votes), 1e-9);
    }

    @Test
    @DisplayName("BL-40: validate — оцінка 0 відхиляється з повідомленням")
    void ratingZeroRejected() {
        List<String> errors = validator.validate("u1", "m1", 0, "", List.of());
        assertAll(
                () -> assertFalse(errors.isEmpty()),
                () -> assertTrue(errors.contains("оцінка поза межами 1..5")));
    }

    @ParameterizedTest(name = "search(\"{0}\") знаходить {1}")
    @CsvSource({"lam,Diablo", "DIA,Diablo", "fer,F40"})
    @DisplayName("BL-60: search — префікс марки або моделі без урахування регістру")
    void prefixSearchIgnoresCase(String query, String expectedName) {
        List<CarModel> found = search.search(query, catalog);
        assertEquals(1, found.size());
        assertEquals(expectedName, found.get(0).name());
    }
}
