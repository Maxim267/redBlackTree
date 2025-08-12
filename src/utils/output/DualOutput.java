package utils.output;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Как Декоратор реализует Выбор потока вывода в зависимости от параметров конструктора {@code DualOutput}.
 * Унифицирует вывод в поток как для отдельного объекта, так и для связанных объектов,
 * с передачей выбранного потока по цепочке вывода.
 * При работе с {@code DualOutput}, реализующим AutoCloseable, рекомендуется
 * использовать try-with-resources для корректного закрытия ресурса {@code PrintWriter}.
 */
public class DualOutput extends OutputDecorator implements AutoCloseable {
    /**
     * Стандартный выходной поток (консоль).
     */
    private PrintStream console;

    /**
     * Поток вывода в файл.
     */
    private PrintWriter fileWriter;

    /**
     * Выбор потока вывода:
     *      true: поток вывода в файл;
     *      false: стандартный выходной поток (консоль).
     */
    private final boolean useFile;

    /**
     * Верхнее оформление сообщения.
     */
    private String header;
    /**
     * Признак сброса верхнего оформления сообщения.
     */
    private boolean isHeaderEmpty;

    /**
     * Нижнее оформление сообщения.
     */
    private String footer;

    /**
     * Признак сброса нижнего оформления сообщения.
     */
    private boolean isFooterEmpty;

    /**
     * Создает стандартный выходной поток (консоль).
     */
    public DualOutput() {
        useFile = false;
        console = System.out;
    }

    /**
     * Создает поток вывода в файл.
     * @param fileName имя файла для записи строкового сообщения.
     * @param charset имя стандартной кодировки символов файла, например, StandardCharsets.UTF_8.
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public DualOutput(String fileName, Charset charset) throws IOException {
        useFile = true;
        fileWriter = new PrintWriter(new FileWriter(fileName, charset));
    }

    /**
     * Устанавливает верхнее оформление сообщения.
     * @param str верхнее оформление сообщения.
     */
    public void setHeader(String str) {
        header = str;
        isHeaderEmpty = false;
    }

    /**
     * Получает верхнее оформление сообщения.
     * @return верхнее оформление сообщения.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Получает верхнее оформление сообщения.
     * @return верхнее оформление сообщения.
     */
    public boolean getUseFile() {
        return useFile;
    }

    /**
     * Получает верхнее оформление сообщения один раз.
     * Используется в цепочке вложенных объектов при выводе сообщения в поток.
     * Инициирующий объект выводит свое оформление сообщения и сбрасывает его в пробел.
     * Остальные потомки выводят этот пробел вместо своего оформления сообщения.
     * @return верхнее оформление сообщение.
     */
    public String getHeaderOnce() {
        if(isHeaderEmpty) {
            return " ";
        }
        else {
            isHeaderEmpty = true;
            return header;
        }
    }

    /**
     * Устанавливает нижнее оформление сообщения.
     * @param str нижнее оформление сообщения.
     */
    public void setFooter(String str) {
        footer = str;
        isFooterEmpty = false;
    }

    /**
     * Получает нижнее оформление сообщения.
     * @return нижнее оформление сообщения.
     */
    public String getFooter() {
        return footer;
    }

    /**
     * Получает нижнее оформление сообщения один раз.
     * Используется в цепочке вложенных объектов при выводе оформления сообщения в поток.
     * Инициирующий объект выводит свое оформление сообщения и сбрасывает его в пробел.
     * Остальные потомки выводят этот пробел вместо своего оформления сообщения.
     * @return нижнее оформление сообщения.
     */
    public String getFooterOnce() {
        if(isFooterEmpty) {
            return " ";
        }
        else {
            isFooterEmpty = true;
            return footer;
        }
    }

    @Override
    public void print(String message) {
        if(useFile) {
            fileWriter.print(message);
        }
        else {
            console.print(message);
        }
    }

    @Override
    public void println(String message) {
        if(useFile) {
            fileWriter.println(message);
        }
        else {
            console.println(message);
        }
    }

    @Override
    public void close() {
        if(fileWriter != null) {
            fileWriter.flush();
            fileWriter.close();
        }
    }
}
