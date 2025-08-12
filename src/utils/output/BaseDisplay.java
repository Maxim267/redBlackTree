package utils.output;

/**
 * Верхнее и нижнее оформления сообщения вывода в поток.
 */
public abstract class BaseDisplay {
    /**
     * Верхнее оформление (шапка) сообщения вывода в поток.
     */
    protected String header;

    /**
     * Нижнее оформление (подвал) сообщения вывода в поток.
     */
    protected String footer;

    /**
     * Создает верхнее и нижнее оформления сообщения вывода в поток.
     * @param header верхнее оформление сообщения.
     * @param footer нижнее оформление сообщения.
     */
    public BaseDisplay(String header, String footer) {
        this.header = header;
        this.footer = footer;
    }

    /**
     * Получает верхнее оформление сообщения вывода в поток.
     * @return верхнее оформление сообщения.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Получает нижнее оформление сообщения вывода в поток.
     * @return нижнее оформление сообщения.
     */
    public String getFooter() {
        return footer;
    }
}
