package utils.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Константы приложения.
 */
public final class AppConstants {
    /**
     * Создает константы приложения.
     */
    private AppConstants() {}

    // Кодирование текста
    /**
     * Однобайтовый символ новой строки в формате UNIX (используется при кодировании).
     */
    public static final String UNIX_NEW_ROW = "\n";

    /**
     * Символ замены символа новой строки, чтобы исключить переход на новую строку (используется при выводе в выходной поток).
     */
    public static final String PRINT_NEW_ROW = "LF";

    // Вывод текста
    /**
     * Стандартная кодировка символов.
     */
    public static final Charset STD_CHARSET = StandardCharsets.UTF_8;

    /**
     * Код красного цвета вывода текста в консоль.
     */
    public static final String COLOR_RED = "\u001B[31m";

    /**
     * Символ красного цвета вывода текста в файл.
     */
    public static final String SYMBOL_RED = "*";
    /**
     * Код белого цвета вывода текста в консоль.
     */
    public static final String COLOR_WHITE = "\u001B[38m";

    // Вывод дерева
    /**
     * Двоичное значение максимального количество пробелов.
     * Используется для позиционирования вывода вершины дерева.
     * Позиционирование каждого следующего уровня последовательно уменьшает это значение вдвое.
     * При значении 0 константа MAX_BLANKS игнорируется.
     */
    public static final int MAX_BLANKS = 65536;  // 0

}
