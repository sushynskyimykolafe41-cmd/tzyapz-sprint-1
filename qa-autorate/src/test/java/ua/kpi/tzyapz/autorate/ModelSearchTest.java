package ua.kpi.tzyapz.autorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести ModelSearch: пошук за префіксом (BL-60) і сортування за рейтингом (BL-70).
 * Автор: Куценко Дар'я. Очікувані значення — з розділу BL виправленої SRS «АвтоРейт».
 */
@Tag("unit")
@DisplayName("ModelSearch — пошук за префіксом (BL-60) і сортування за рейтингом (BL-70)")
class ModelSearchTest {

    private ModelSearch search;
    private List<CarModel> catalog;

    @BeforeEach
    void setUp() {
        search = new ModelSearch();
        catalog = List.of(
                new CarModel("Lamborghini", "Diablo", 4.6, 12),
                new CarModel("Lancia", "Delta", 4.6, 9),
                new CarModel("Ferrari", "F40", 4.9, 20),
                new CarModel("Pagani", "Huayra", 3.8, 7));
    }

    // ---------- BL-60: пошук ----------

    @ParameterizedTest(name = "BL-60: search(\"{0}\") → {1}")
    @DisplayName("BL-60: пошук за префіксом марки або моделі без урахування регістру")
    @CsvSource({
            "lam, Lamborghini Diablo",
            "LAN, Lancia Delta",
            "f4, Ferrari F40",
            "hUaY, Pagani Huayra"
    })
    void search_prefixIgnoresCase(String query, String expectedFullName) {
        List<CarModel> found = search.search(query, catalog);
        assertAll("search(\"" + query + "\")",
                () -> assertEquals(1, found.size()),
                () -> assertEquals(expectedFullName, found.get(0).fullName()));
    }

    @Test
    @DisplayName("BL-60: спільний префікс «la» знаходить обидві марки у порядку каталогу")
    void search_commonPrefixReturnsAllMatchesInCatalogOrder() {
        List<String> names = search.search("la", catalog).stream().map(CarModel::fullName).toList();
        assertIterableEquals(List.of("Lamborghini Diablo", "Lancia Delta"), names);
    }

    @Test
    @DisplayName("BL-60: збіг усередині назви («blo» у Diablo) — не префікс, не знаходиться")
    void search_substringIsNotPrefix() {
        assertTrue(search.search("blo", catalog).isEmpty());
    }

    // BUG-018: модуль v1 на порожній запит повертає весь каталог — тест лишається червоним до v2
    @ParameterizedTest(name = "BL-60: запит «{0}» → порожній результат")
    @DisplayName("BL-60: порожній або пробільний запит дає порожній результат")
    @ValueSource(strings = {"", "   "})
    void search_blankQueryReturnsEmpty(String query) {
        List<CarModel> found = search.search(query, catalog);
        assertTrue(found.isEmpty(), "Очікувався порожній результат, отримано моделей: " + found.size());
    }

    // ---------- BL-70: сортування ----------

    // BUG-019: модуль v1 сортує за зростанням рейтингу — тест лишається червоним до v2
    @Test
    @DisplayName("BL-70: sortByRating — за рейтингом за спаданням, однакові — за повною назвою")
    void sortByRating_descendingThenByFullName() {
        // act
        List<String> names = search.sortByRating(catalog).stream().map(CarModel::fullName).toList();
        // assert
        assertIterableEquals(
                List.of("Ferrari F40", "Lamborghini Diablo", "Lancia Delta", "Pagani Huayra"), names);
    }

    @Test
    @DisplayName("BL-70: sortByRating повертає новий список, вхідний не змінюється")
    void sortByRating_returnsNewListAndKeepsInput() {
        List<CarModel> input = new ArrayList<>(catalog);
        List<CarModel> sorted = search.sortByRating(input);
        assertAll("вхідний список",
                () -> assertNotSame(input, sorted),
                () -> assertIterableEquals(catalog, input));
    }
}
