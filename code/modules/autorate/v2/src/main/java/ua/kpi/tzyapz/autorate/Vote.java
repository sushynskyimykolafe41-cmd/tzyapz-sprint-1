package ua.kpi.tzyapz.autorate;

/**
 * Голос користувача за модель автомобіля.
 *
 * @param userId     ідентифікатор користувача
 * @param modelId    ідентифікатор моделі
 * @param rating     оцінка 1..5
 * @param reputation репутація користувача 1..3 (вага голосу)
 * @param comment    коментар (може бути порожнім або null)
 */
public record Vote(String userId, String modelId, int rating, int reputation, String comment) {
}
