package utils.output;

import utils.constants.AppConstants;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Определяет интерфейс {@code Output} вывода в поток.
 * Как Адаптер связывает выбор потока {@code DualOutput} с интерфейсом вывода в поток {@code Output}.
 * Как Декоратор оборачивает {@code Output} и добавляет новую функциональность к {@code display}.
 * Классы проекта реализуют интерфейсы вывода в поток с префиксом в названии "out", например:
 *    {@code ANode<Integer, String>} node = new {@code ANode<>}(1, "abc");
 *    node.out.display();
 */
public class Display extends BaseDisplay {
    /**
     * Интерфейс вывода в поток.
     */
    private final Output output;

    /**
     * Определяет интерфейс {@code Output} вывода в поток.
     * @param output интерфейс вывода в поток.
     * @param header верхнее оформление сообщения потока вывода.
     * @param footer нижнее оформление сообщения потока вывода.
     */
    public Display(Output output, String header, String footer) {
        super(header, footer);
        this.output = output;
        this.header = header;
        this.footer = footer;
    }

    /**
     * Выводит в стандартный выходной поток (консоль).
     */
    public void display() {
        try(DualOutput out = new DualOutput()) {
            out.setHeader(header);
            out.setFooter(footer);
            output.display(out);
        }
    }

    /**
     * Создает поток вывода в файл.
     * @param fileName имя файла для записи строкового сообщения.
     * @param charset имя стандартной кодировки символов файла, например, StandardCharsets.UTF_8.
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public void display(String fileName, Charset charset) throws IOException {
            try(DualOutput out = new DualOutput(fileName, charset)) {
                out.setHeader(header);
                out.setFooter(footer);
                output.display(out);
        }
    }

    /**
     * Создает поток вывода в файл.
     * @param fileName имя файла для записи строкового сообщения.
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public void display(String fileName) throws IOException {
        display(fileName, AppConstants.STD_CHARSET);
    }
}
