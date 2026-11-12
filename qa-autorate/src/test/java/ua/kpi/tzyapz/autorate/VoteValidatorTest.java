package ua.kpi.tzyapz.autorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юніт-тести VoteValidator: один голос на модель (BL-30), оцінка 1..5 (BL-40),
 * коментар до 200 символів (BL-50), стоп-список (BL-80). Очікувані значення — з розділу BL SRS.
 */
@Tag("unit")
@DisplayName("VoteValidator — голос (BL-30), оцінка (BL-40), коментар (BL-50), стоп-список (BL-80)")
class VoteValidatorTest {

    private VoteValidator validator;

    @BeforeEach
    void setUp() {
        validator = new VoteValidator();
    }

    // ---------- BL-40 і BL-50: оцінка і довжина коментаря (автор: Потеряйло Вероніка) ----------

    // BUG-017: модуль v1 відхиляє оцінку 5 (умова rating >= 5) — тест лишається червоним до v2
    @ParameterizedTest(name = "BL-40: оцінка {0} (межа діапазону) приймається")
    @DisplayName("BL-40: оцінки на межах діапазону 1 і 5 приймаються")
    @ValueSource(ints = {1, 5})
    void validate_boundaryRatingAccepted(int rating) {
        List<String> errors = validator.validate("u1", "m1", rating, "", List.of());
        assertTrue(errors.isEmpty(), "Очікувався порожній перелік порушень, отримано: " + errors);
    }

    @ParameterizedTest(name = "BL-40: оцінка {0} (за межею діапазону) відхиляється")
    @DisplayName("BL-40: оцінки 0 і 6 відхиляються з повідомленням «оцінка поза межами 1..5»")
    @ValueSource(ints = {0, 6})
    void validate_outOfRangeRatingRejected(int rating) {
        List<String> errors = validator.validate("u1", "m1", rating, "", List.of());
        assertTrue(errors.contains("оцінка поза межами 1..5"), "Отримано: " + errors);
    }

    @Test
    @DisplayName("BL-50: коментар рівно 200 символів (верхня межа) приймається")
    void validate_comment200CharsAccepted() {
        String comment = "a".repeat(200);
        assertTrue(validator.isValid("u1", "m1", 4, comment, List.of()));
    }

    @Test
    @DisplayName("BL-50: коментар 201 символ відхиляється з повідомленням «коментар задовгий»")
    void validate_comment201CharsRejected() {
        // arrange
        String comment = "a".repeat(201);
        // act
        List<String> errors = validator.validate("u1", "m1", 4, comment, List.of());
        // assert
        assertEquals(List.of("коментар задовгий"), errors);
    }

    @Test
    @DisplayName("BL-50: порожній коментар і null дозволені")
    void validate_emptyOrNullCommentAccepted() {
        assertAll("коментар не обов'язковий",
                () -> assertTrue(validator.isValid("u1", "m1", 3, "", List.of()), "порожній рядок"),
                () -> assertTrue(validator.isValid("u1", "m1", 3, null, List.of()), "null"));
    }

    // ---------- BL-30 і BL-80: один голос на модель і стоп-список (автор: Рзяніна Валерія) ----------

    @Test
    @DisplayName("BL-30: повторний голос того самого користувача за ту саму модель відхиляється")
    void validate_secondVoteForSameModelRejected() {
        List<Vote> existing = List.of(new Vote("u1", "m1", 4, 1, ""));
        List<String> errors = validator.validate("u1", "m1", 3, "", existing);
        assertTrue(errors.contains("повторний голос"), "Отримано: " + errors);
    }

    // BUG-016: модуль v1 перевіряє лише userId і відхиляє голос того самого користувача за іншу модель
    @Test
    @DisplayName("BL-30: голос того самого користувача за іншу модель приймається")
    void validate_voteForAnotherModelAccepted() {
        // arrange: u1 уже голосував за m1
        List<Vote> existing = List.of(new Vote("u1", "m1", 4, 1, ""));
        // act: u1 голосує за m2
        List<String> errors = validator.validate("u1", "m2", 3, "", existing);
        // assert
        assertTrue(errors.isEmpty(), "Очікувався порожній перелік порушень, отримано: " + errors);
    }

    @Test
    @DisplayName("BL-30: голос іншого користувача за ту саму модель приймається")
    void validate_otherUserSameModelAccepted() {
        List<Vote> existing = List.of(new Vote("u1", "m1", 4, 1, ""));
        assertTrue(validator.isValid("u2", "m1", 3, "", existing));
    }

    @ParameterizedTest(name = "BL-80: «{0}» — недопустиме слово")
    @DisplayName("BL-80: стоп-слово окремим словом у будь-якому регістрі відхиляється")
    @ValueSource(strings = {"spam", "Це SPAM!", "дешеве Казино тут", "best casino, ever"})
    void validate_stopWordRejected(String comment) {
        List<String> errors = validator.validate("u1", "m1", 3, comment, List.of());
        assertTrue(errors.contains("недопустиме слово"), "Отримано: " + errors);
    }

    @ParameterizedTest(name = "BL-80: «{0}» — стоп-слова як окремого слова немає")
    @DisplayName("BL-80: стоп-слово як частина іншого слова не є порушенням")
    @ValueSource(strings = {"spammer", "scampi з часником", "casinos"})
    void containsStopWord_partOfWordIgnored(String comment) {
        assertFalse(validator.containsStopWord(comment));
    }

    @Test
    @DisplayName("BL-80: власний стоп-список замінює типовий; null замість списку — IllegalArgumentException")
    void customStopList_replacesDefaultAndRejectsNull() {
        VoteValidator custom = new VoteValidator(List.of("Lada"));
        assertAll("власний стоп-список",
                () -> assertTrue(custom.containsStopWord("моя LADA")),
                () -> assertFalse(custom.containsStopWord("spam")),
                () -> assertThrows(IllegalArgumentException.class, () -> new VoteValidator(null)));
    }
}
