package utils.output;

import utils.constants.AppConstants;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Определяет интерфейс {@code IntOutput} вывода в поток.
 * Как Адаптер связывает выбор потока {@code DualOutput} с интерфейсом вывода в поток {@code IntOutput}.
 * Как Декоратор обертывает {@code IntOutput} и добавляет новую функциональность к {@code display}.
 * Классы проекта реализуют интерфейсы вывода в поток с префиксом в названии "out", например:
 *    {@code BSTree <Integer, String>} tree = new {@code BSTree<>}();
 *    tree.add(1, "abc");
 *    tree.out.display(0);
 */
public class IntDisplay extends BaseDisplay {
    /**
     * Интерфейс вывода в поток.
     */
    protected final IntOutput output;

    /**
     * Определяет интерфейс {@code IntOutput} вывода в поток.
     * @param output интерфейс вывода в поток.
     * @param header верхнее оформление сообщения.
     * @param footer нижнее оформление сообщения.
     */
    public IntDisplay(IntOutput output, String header, String footer) {
        super(header, footer);
        this.output = output;
        this.header = header;
        this.footer = footer;
    }

    /**
     * Выводит в стандартный выходной поток (консоль).
     * @param blanks двоичное значение количества пробелов позиционирования дерева, начиная с корневого узла дерева.
     *              Чем больше значение {@code blanks}, тем больше дерево растягивается по горизонтали.
     *              При значении 0 (или меньше 0) производится автоматический расчет значения {@code blanks}.
     *              Не двоичное значение {@code blanks} будет автоматически преобразовано к ближайшему двоичному значению (вверх или вниз).
     */
    public void display(int blanks) {
        try(DualOutput out = new DualOutput()) {
            out.setHeader(header);
            out.setFooter(footer);
            output.display(blanks, out);
        }
    }

    /**
     * Создает поток вывода в файл.
     * @param blanks двоичное значение количества пробелов позиционирования дерева, начиная с корневого узла дерева.
     *              Чем больше значение {@code blanks}, тем больше дерево растягивается по горизонтали.
     *              При значении 0 (или меньше 0) производится автоматический расчет значения {@code blanks}.
     *              Не двоичное значение {@code blanks} будет автоматически преобразовано к ближайшему двоичному значению (вверх или вниз).
     * @param fileName имя файла для записи строкового сообщения.
     * @param charset имя стандартной кодировки символов файла, например, StandardCharsets.UTF_8.
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public void display(int blanks, String fileName, Charset charset) throws IOException {
        try(DualOutput out = new DualOutput(fileName, charset)) {
            out.setHeader(header);
            out.setFooter(footer);
            output.display(blanks, out);
        }
    }

    /**
     * Создает поток вывода в файл.
     * @param blanks двоичное значение количества пробелов позиционирования дерева, начиная с корневого узла дерева.
     *              Чем больше значение {@code blanks}, тем больше дерево растягивается по горизонтали.
     *              При значении 0 (или меньше 0) производится автоматический расчет значения {@code blanks}.
     *              Не двоичное значение {@code blanks} будет автоматически преобразовано к ближайшему двоичному значению (вверх или вниз).
     * @param fileName имя файла для записи строкового сообщения.
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public void display(int blanks, String fileName) throws IOException {
        display(blanks, fileName, AppConstants.STD_CHARSET);
    }
}
