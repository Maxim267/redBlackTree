package utils.output;

/**
 * Абстрактный класс декоратора вывода в выходной поток.
 */
public abstract class OutputDecorator {
    /**
     * Выводит строковое сообщение в выходной поток с завершающим символом новой строки.
     * @param message строковое сообщение.
     */
    public abstract void println(String message);

    /**
     * Выводит строковое сообщение в выходной поток.
     * @param message строковое сообщение.
     */
    public abstract void print(String message);
}