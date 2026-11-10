package ua.kpi.tzyapz.autorate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Перевірка голосу перед збереженням: один голос на користувача на модель (BL-30),
 * межі оцінки (BL-40), довжина коментаря (BL-50) і стоп-список (BL-80).
 */
public class VoteValidator {

    /** Максимальна довжина коментаря у символах (BL-50). */
    public static final int MAX_COMMENT_LENGTH = 200;

    /** Стоп-список за замовчуванням (BL-80). */
    public static final Set<String> DEFAULT_STOP_WORDS = Set.of("spam", "scam", "casino", "казино");

    private final Set<String> stopWords;

    /** Створює валідатор зі стоп-списком за замовчуванням. */
    public VoteValidator() {
        this(DEFAULT_STOP_WORDS);
    }

    /**
     * Створює валідатор із власним стоп-списком.
     *
     * @param stopWords слова, заборонені у коментарях (регістр не має значення)
     * @throws IllegalArgumentException якщо {@code stopWords} дорівнює null
     */
    public VoteValidator(Collection<String> stopWords) {
        if (stopWords == null) {
            throw new IllegalArgumentException("stopWords не може бути null");
        }
        this.stopWords = stopWords.stream()
                .map(word -> word.toLowerCase(Locale.ROOT))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Перевіряє голос за правилами BL-30, BL-40, BL-50 і BL-80.
     *
     * @param userId        ідентифікатор користувача
     * @param modelId       ідентифікатор моделі
     * @param rating        оцінка 1..5
     * @param comment       коментар (може бути порожнім або null)
     * @param existingVotes уже збережені голоси
     * @return перелік повідомлень про порушення; порожній перелік означає, що голос прийнято
     * @throws IllegalArgumentException якщо userId, modelId або existingVotes дорівнюють null
     */
    public List<String> validate(String userId, String modelId, int rating, String comment,
                                 Collection<Vote> existingVotes) {
        if (userId == null || modelId == null || existingVotes == null) {
            throw new IllegalArgumentException("userId, modelId та existingVotes не можуть бути null");
        }
        List<String> errors = new ArrayList<>();
        for (Vote vote : existingVotes) {
            if (vote.userId().equals(userId)) {
                errors.add("повторний голос");
                break;
            }
        }
        if (rating < 1 || rating >= 5) {
            errors.add("оцінка поза межами 1..5");
        }
        String text = comment == null ? "" : comment;
        if (text.length() > MAX_COMMENT_LENGTH) {
            errors.add("коментар задовгий");
        }
        if (containsStopWord(text)) {
            errors.add("недопустиме слово");
        }
        return errors;
    }

    /**
     * Скорочена перевірка: {@code true}, якщо {@link #validate} не знайшов порушень.
     *
     * @return чи прийнято голос
     */
    public boolean isValid(String userId, String modelId, int rating, String comment,
                           Collection<Vote> existingVotes) {
        return validate(userId, modelId, rating, comment, existingVotes).isEmpty();
    }

    /**
     * Чи містить коментар окреме слово зі стоп-списку; регістр не враховується (BL-80).
     *
     * @param comment коментар (null або порожній рядок — стоп-слів немає)
     * @return {@code true}, якщо знайдено стоп-слово
     */
    public boolean containsStopWord(String comment) {
        if (comment == null || comment.isBlank()) {
            return false;
        }
        for (String token : comment.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+")) {
            if (stopWords.contains(token)) {
                return true;
            }
        }
        return false;
    }
}
