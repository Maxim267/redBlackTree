package binaryTree;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Stack;

/**
 * Двоичное дерево поиска (binary search tree, BST)
 */
public class BSTree {
    /**
     * Корневой узел дерева {@code root}
     */
    private Node root;
    /**
     * Размер дерева (количество узлов) {@code size}
     */
    private int size;
    /**
     * Перечисление цветов: {@code red}, {@code white}
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
    public BSTree() {
        root = null;
        size = 0;
    }
    public Node getRoot() {
        return root;
    }
    public void setRoot(Node root) {
        this.root = root;
    }
    public int getSize() {
        return size;
    }
    /**
     * Получить кодировку цвета, используемую при выводе текста в консоль: {@code getColorStart}, {@code getColorEnd}
     */
    public String getColorStart(Node node) {
        return node.getIsRed() ? eColor.red.getValue() : eColor.white.getValue();
    }
    public String getColorEnd() {
        return eColor.white.getValue();
    }
    /**
     * Добавить узел в дерево {@code add} с непустыми данными
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
     * Добавить узел в дерево {@code add} с пустыми данными
     * @param key целочисленное значение ключа дерева
     * @return добавленный узел
     */
    public Node add(int key) {
        return add(key, null);
    }
    /**
     * Найти узел дерева {@code findNode}
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
     * Найти узел дерева {@code findNodeExt} - расширенный метод с выводом в консоль результата
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
     * Поиск осуществляется по правым ссылкам дочерних узлов.
     * Поиск останавливается при нахождении первого ключа больше заданного
     * @param max
     *      *     Заданное значение округления
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
     *              Значение <= 0 - автоматический расчет бинарного значения округления {@code max} в зависимости от заполнения дерева.
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
     *              Значение <= 0 - автоматический расчет значения {@code blank} в зависимости от заполнения дерева.
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
     * @param charset Набор символов кодировки, в котором будет сохранен файл, например, StandardCharsets.UTF_8
     */
    public void displayTree(String path, Charset charset) throws IOException {
        displayTree(path, charset, 0);
    }
    /**
     * Вывести дерево в файл
     * @param path имя файла, в том числе относительный или полный путь, в котором будет сохранено дерево
     * @param charset Набор символов кодировки, в котором будет сохранен файл, например, StandardCharsets.UTF_8
     * @param blank количество пробелов сдвига вправо вершины дерева от начала строки.
     *              Чем больше значение, тем больше растягивается дерево по горизонтали.
     *              Значение <= 0 - автоматический расчет значения {@code blank} в зависимости от заполнения дерева.
     *              Рекомендуется задавать бинарное значение {@code blank} (32, 64, 128, ...) для правильного вывода иерархии дерева.
     *              Небинарное значение будет автоматически преобразовано к ближайшему бинарному значению (вверх или вниз)
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
