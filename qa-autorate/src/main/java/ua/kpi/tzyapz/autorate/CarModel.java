package ua.kpi.tzyapz.autorate;

/**
 * Модель автомобіля у каталозі.
 *
 * @param make   марка (наприклад, «Lamborghini»)
 * @param name   назва моделі (наприклад, «Diablo»)
 * @param rating поточний рейтинг 0,0..5,0
 * @param votes  кількість голосів
 */
public record CarModel(String make, String name, double rating, int votes) {

    /**
     * Повна назва «марка модель», за якою впорядковуються моделі з однаковим рейтингом.
     *
     * @return повна назва
     */
    public String fullName() {
        return make + " " + name;
    }
}
