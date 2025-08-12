package utils.calculations;

/**
 *  Методы расчета значений приложения.
 */
public final class MathUtils {
    /**
     * Создает Методы расчета.
     */
    private MathUtils() {}

    /**
     * Получает ближайшее от заданного округленное (вверх или вниз) двоичное значение.
     * @param value целочисленное значение для округления.
     * @return ближайшее округленное двоичное значение.
     */
    public static int getBinaryRound(int value) {
        double val = value;
        val = Math.log(val) / Math.log(2);
        val = Math.round(val);
        val = Math.pow(2, val);
        if(val < 2) {
            val = 2;
        }
        return (int) val;
    }
}
