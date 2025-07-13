package binaryTree;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Stack;

/**
 * Двоичное дерево поиска (binary search tree, BST)
 */
public class BSTree {
    /**
     * Корневой узел дерева
     */
    private Node root;
    /**
     * Размер дерева (количество узлов)
     */
    private int size;
    /**
     * Перечисление цветов: "красный" - {@code red}, "черный" - {@code white}
     */
    private enum eColor {red("\u001B[31m"), white("\u001B[38m");
        private final String value;
        eColor(String val){
            value = val;
        }
        public String getValue() {
            return value;
        }
    }
    /**
     * Конструктор класса {@code BSTree}
     */
    public BSTree() {
        root = null;
        size = 0;
    }
    /**
     * Получить корневой узел дерева
     * @return корневой узел
     */
    public Node getRoot() {
        return root;
    }
    /**
     * Установить корневой узел дерева
     * @param root узел дерева
     */
    public void setRoot(Node root) {
        this.root = root;
    }
    /**
     * Получить размер дерева
     * @return количество узлов дерева
     */
    public int getSize() {
        return size;
    }
    /**
     * Получить кодировку цвета для данного узла
     * @param node узел дерева
     * @return для красного узла будет возвращен строковый код красного цвета, иначе вернется "черный" (white как черный)
     */
    public String getColorStart(Node node) {
        return node.getIsRed() ? eColor.red.getValue() : eColor.white.getValue();
    }
    /**
     * Получить кодировку цвета текста по умолчанию
     * @return строковый код цвета текста по умолчанию (white как черный)
     */
    public String getColorEnd() {
        return eColor.white.getValue();
    }
    /**
     * Добавить узел в дерево с данными
     * @param key целочисленное значение ключа дерева
     * @param data данные как парное значение ключа
     * @return добавленный узел
     */
    public Node add(int key, String data) {
        Node newNode = new Node(key, data);
        newNode.setData(data);
        if(root == null) {
            root = newNode;
            size++;
            return newNode;
        }
        else {
            Node current = root;
            Node parent;
            while(current.getKey() != key) {
                parent = current;
                if(key < current.getKey()) {
                    current = current.getLeftChild();
                    if(current == null) {
                        parent.setLeftChild(newNode);
                        size++;
                        return newNode;
                    }
                }
                else {
                    current = current.getRightChild();
                    if(current == null) {
                        parent.setRightChild(newNode);
                        size++;
                        return newNode;
                    }
                }
            }
            if(current.getKey() == key) {
                current.setData(data);
                return current;
            }
        }
        return null;
    }
    /**
     * Добавить узел в дерево без данных
     * @param key целочисленное значение ключа дерева
     * @return добавленный узел
     */
    public Node add(int key) {
        return add(key, null);
    }
    /**
     * Найти узел дерева
     * @param key целочисленное значение ключа дерева
     * @return найденный узел
     */
    public Node findNode(int key) {
        Node current = getRoot();
        while(current != null && key != current.getKey()) {
            if(key < current.getKey()) {
                current = current.getLeftChild();
            }
            else {
                current = current.getRightChild();
            }
        }
        if(current != null && current.getKey() == key) {
            return current;
        }
        else {
            return null;
        }
    }
    /**
     * Найти узел дерева с расширенным результатом поиска
     * @param key целочисленное значение ключа дерева
     */
    public void findNodeExt(int key) {
        Node node = findNode(key);
        if (node != null) {
            System.out.print(getColorStart(node) + "found: " + node.getKey()+ (node.getIsRed() ? " red" : " black") + getColorEnd() + "; ");
            Node child = node.getLeftChild();
            if (child != null) {
                System.out.print(getColorStart(child) + "Left: " + child.getKey() + (child.getIsRed() ? " red" : " black") + getColorEnd() + "; ");
            }
            child = node.getRightChild();
            if (child != null) {
                System.out.println(getColorStart(child) + "Right: " + child.getKey() + (child.getIsRed() ? " red" : " black") + getColorEnd());
            }
        }
        else {
            System.out.println("not found: " + key);
        }
    }

    /**
     * Поиск максимального значения ключа дерева (с округлением в большую сторону)
     * Поиск осуществляется по правым ссылкам узлов.
     * Поиск останавливается при нахождении первого ключа больше заданного или достижении листового узла
     * @param max заданное значение округления
     * @return значение первого найденного ключа, которое больше заданного значение
     */
    public int getRoundUpMaxKey(int max) {
        Node current = root;
        int nMax = 0;
        while(current != null) {
            nMax = current.getKey();
            if(max > 0 && nMax >= max) {
                return nMax;
            }
            current = current.getRightChild();
        }
        return nMax;
    }
    /**
     * Получить ближайшее округленное (вверх или вниз) бинарное значение
     * @param value целочисленное значение для округления
     * @return ближайшее округленное бинарное значение
     */
    private int getBinaryRound(int value) {
        double log = value;
        log = Math.log(log) / Math.log(2);
        log = (int) Math.round(log);
        return (int) Math.pow(2, log);
    }
    /**
     * Получить округленное бинарное значение количества пробелов (не больше 16384 {@code nMaxBlank})
     * @param max максимальное целочисленное значение для округления.
     *            При задании значения не более 0 производится автоматический расчет бинарного значения округления {@code max} в зависимости от заполнения дерева.
     * @return ближайшее округленное бинарное значение
     */
    public int getMaxBlank(int max) {
        int nMaxBlank = 16384;
        int nBlank = max;
        if(nBlank > nMaxBlank) {
            nBlank = nMaxBlank;
        }
        if(nBlank <= 0) {
            nBlank = getBinaryRound(getRoundUpMaxKey(0));
        }
        else {
            nBlank = getBinaryRound(nBlank);
        }
        return nBlank;
    }
    /**
     * Вывести дерево в стандартный поток вывода (консоль)
     * Поиск осуществляется по правым ссылкам дочерних узлов.
     * Поиск останавливается при нахождениии первого ключа больше заданного
     */
    public void displayTree() {
        displayTree(0);
    }
    /**
     * Вывести дерево в стандартный поток вывода (консоль)
     * @param blank количество пробелов сдвига вправо вершины дерева от начала строки.
     *              Чем больше значение, тем больше растягивается дерево по горизонтали.
     *              При задании значения не более 0 производится автоматический расчет значения {@code blank} в зависимости от заполнения дерева.
     *              Рекомендуется задавать бинарное значение {@code blank} (32, 64, 128, ...) для правильного вывода иерархии дерева.
     *              Небинарное значение будет автоматически преобразовано к ближайшему бинарному значению (вверх или вниз)
     */
    public void displayTree(int blank) {
        Stack global = new Stack();
        global.push(root);
        int nBlank = getMaxBlank(blank);
        boolean isNewRow = false;
        System.out.println("<<< Red-Black tree");
        while(isNewRow == false) {
            isNewRow = true;
            Stack local = new Stack();
            for(int j = 0; j < nBlank; ++j) {
                System.out.print(" ");
            }
            while(global.isEmpty() == false) {
                Node current = (Node) global.pop();
                int len = 0;
                if(current != null) {
                    System.out.print(getColorStart(current) + current.getKey() + getColorEnd());
                    len = Integer.toString(current.getKey()).length();
                    local.push(current.getLeftChild());
                    local.push(current.getRightChild());
                    if (current.getLeftChild() != null || current.getRightChild() != null) {
                        isNewRow = false;
                    }
                }
                else {
                    System.out.print(getColorEnd() + "--" + getColorEnd());
                    len = 2;
                    local.push(null);
                    local.push(null);
                }
                for(int j = 0; j < nBlank * 2 - (len); ++j) {
                    System.out.print(" ");
                }
            }
            System.out.println();
            nBlank /= 2;
            while(local.isEmpty() == false) {
                global.push(local.pop());
            }
        }
        System.out.println(">>>");
    }
    /**
     * Вывести дерево в файл
     * @param path имя файла, в том числе относительный или полный путь, в котором будет сохранено дерево
     * @param charset набор символов кодировки, в котором будет сохранен файл, например, StandardCharsets.UTF_8
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода
     */
    public void displayTree(String path, Charset charset) throws IOException {
        displayTree(path, charset, 0);
    }
    /**
     * Вывести дерево в файл
     * @param path имя файла, в том числе относительный или полный путь, в котором будет сохранено дерево
     * @param charset набор символов кодировки, в котором будет сохранен файл, например, StandardCharsets.UTF_8
     * @param blank количество пробелов сдвига вправо вершины дерева от начала строки.
     *              Чем больше значение, тем больше растягивается дерево по горизонтали.
     *              При задании значения не более 0 производится автоматический расчет значения {@code blank} в зависимости от заполнения дерева.
     *              Рекомендуется задавать бинарное значение {@code blank} (32, 64, 128, ...) для правильного вывода иерархии дерева.
     *              Небинарное значение будет автоматически преобразовано к ближайшему бинарному значению (вверх или вниз)
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода
     */
    public void displayTree(String path, Charset charset, int blank) throws IOException {

        PrintWriter out = new PrintWriter(path, charset);
        Stack global = new Stack();
        global.push(root);
        int nBlank = getMaxBlank(blank);
        boolean isNewRow = false;
        out.println("<<< Red-Black tree");
        while(isNewRow == false) {
            isNewRow = true;
            Stack local = new Stack();
            for(int j = 0; j < nBlank; ++j) {
                out.print(" ");
            }
            while(global.isEmpty() == false) {
                Node current = (Node) global.pop();
                int len = 0;
                if(current != null) {
                    out.print(current.getKey());
                    len = Integer.toString(current.getKey()).length();
                    local.push(current.getLeftChild());
                    local.push(current.getRightChild());
                    if (current.getLeftChild() != null || current.getRightChild() != null) {
                        isNewRow = false;
                    }
                }
                else {
                    out.print("--");
                    len = 2;
                    local.push(null);
                    local.push(null);
                }
                for(int j = 0; j < nBlank * 2 - (len); ++j) {
                    out.print(" ");
                }
            }
            out.println();
            nBlank /= 2;
            while(local.isEmpty() == false) {
                global.push(local.pop());
            }
        }
        out.println(">>>");
        out.flush();
    }
}
