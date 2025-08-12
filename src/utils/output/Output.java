package utils.output;

/**
 * Функциональный интерфейс вывода в выходной поток.
 */
@FunctionalInterface
public interface Output {
    /**
     * Выводит в заданный поток.
     * @param out заданный поток вывода:
     *            - стандартный выходной поток (консоль) (использовать конструктор DualOutput());
     *            - файл (использовать конструктор DualOutput(String fileName)).
     */
    void display(DualOutput out);
}
