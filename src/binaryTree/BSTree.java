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
     * Создает новое двоичное дерево поиска {@code BSTree}.
     */
    public BSTree() {
        root = null;
        size = 0;
    }
    /**
     * Получить корневой узел дерева.
     * @return корневой узел.
     */
    public Node getRoot() {
        return root;
    }
    /**
     * Установить корневой узел дерева.
     * @param root узел дерева.
     */
    public void setRoot(Node root) {
        this.root = root;
    }
    /**
     * Получить размер дерева.
     * @return количество узлов дерева.
     */
    public int getSize() {
        return size;
    }
    /**
     * Получить кодировку цвета текста для заданного узла.
     * @param node узел дерева.
     * @return {@code eColor.red} для "красного" узла; {@code eColor.white} для "черного" узла.
     */
    public String getTextColorNode(Node node) {
        return node.getIsRed() ? eColor.red.getValue() : eColor.white.getValue();
    }
    /**
     * Получить кодировку цвета текста по умолчанию.
     * @return строковый код цвета текста по умолчанию {@code eColor.white}.
     */
    public String getTextColorDefault() {
        return eColor.white.getValue();
    }
    /**
     * Добавить узел в дерево с данными.
     * @param key целочисленное значение ключа.
     * @param data строковые данные.
     * @return добавленный узел.
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
     * Добавить узел в дерево без данных.
     * @param key целочисленное значение ключа.
     * @return добавленный узел.
     */
    public Node add(int key) {
        return add(key, null);
    }
    /**
     * Найти узел дерева по заданному ключу.
     * @param key целочисленное значение ключа.
     * @return найденный узел.
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
     * Найти узел дерева по заданному ключу с выводом результата поиска.
     * @param key целочисленное значение ключа.
     */
    public void findNodeExt(int key) {
        Node node = findNode(key);
        if (node != null) {
            System.out.print(getTextColorNode(node) + "found: " + node.getKey()+ (node.getIsRed() ? " red" : " black") + getTextColorDefault() + "; ");
            Node child = node.getLeftChild();
            if (child != null) {
                System.out.print(getTextColorNode(child) + "Left: " + child.getKey() + (child.getIsRed() ? " red" : " black") + getTextColorDefault() + "; ");
            }
            child = node.getRightChild();
            if (child != null) {
                System.out.println(getTextColorNode(child) + "Right: " + child.getKey() + (child.getIsRed() ? " red" : " black") + getTextColorDefault());
            }
        }
        else {
            System.out.println("not found: " + key);
        }
    }

    /**
     * Поиск максимального значения ключа дерева (с округлением в большую сторону) по заданному значению.
     * Поиск осуществляется по правым дочерним узлам.
     * Поиск останавливается при нахождении первого ключа больше заданного или достижении листового узла.
     * При заданном значении 0 поиск останавливается при достижении листового узла.
     * @param value заданное значение округления.
     * @return значение первого найденного ключа больше заданного или значение листового узла, возможно, меньше заданного.
     */
    public int getRoundUpMaxKey(int value) {
        Node current = root;
        int nMax = 0;
        while(current != null) {
            nMax = current.getKey();
            if(value > 0 && nMax >= value) {
                return nMax;
            }
            current = current.getRightChild();
        }
        return nMax;
    }
    /**
     * Получить ближайшее округленное (вверх или вниз) двоичное значение.
     * @param value целочисленное значение для округления.
     * @return ближайшее округленное двоичное значение.
     */
    private int getBinaryRound(int value) {
        double log = value;
        log = Math.log(log) / Math.log(2);
        log = (int) Math.round(log);
        return (int) Math.pow(2, log);
    }
    /**
     * Получить округленное двоичное значение заданного количества пробелов (не больше 16384 {@code nMaxBlank}).
     * @param blanks заданное количество пробелов.
     *               При задании значения 0 (или меньше 0) производится автоматический расчет двоичного значения округления {@code blanks} в зависимости от заполнения дерева.
     * @return ближайшее округленное двоичное значение.
     */
    public int getMaxBlank(int blanks) {
        int nMaxBlank = 16384;
        int nBlank = blanks;
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
     * Вывести дерево в стандартный поток вывода (консоль) с автоматически рассчитанной шириной дерева.
     */
    public void displayTree() {
        displayTree(0);
    }
    /**
     * Вывести дерево в стандартный поток вывода (консоль) с применением заданного количества пробелов влияющих на ширину дерева.
     * @param blanks количество пробелов сдвига дерева и всех его узлов вправо от начала строки, начиная с вершины дерева.
     *              Чем больше значение {@code blanks}, тем больше растягивается дерево по горизонтали.
     *              При задании значения 0 (или меньше 0) производится автоматический расчет значения {@code blanks} в зависимости от заполнения дерева.
     *              Рекомендуется задавать двоичное значение {@code blanks} (32, 64, 128, ...) для правильного вывода иерархии дерева.
     *              Не двоичное значение будет автоматически преобразовано к ближайшему двоичному значению (вверх или вниз).
     */
    public void displayTree(int blanks) {
        Stack<Node> global = new Stack<>();
        global.push(root);
        int nBlank = getMaxBlank(blanks);
        boolean isNewRow = true;
        System.out.println("<<< Red-Black tree");
        while(isNewRow) {
            isNewRow = false;
            Stack<Node> local = new Stack<>();
            for(int j = 0; j < nBlank; ++j) {
                System.out.print(" ");
            }
            while(!global.isEmpty()) {
                Node current = global.pop();
                int len;
                if(current != null) {
                    System.out.print(getTextColorNode(current) + current.getKey() + getTextColorDefault());
                    len = Integer.toString(current.getKey()).length();
                    local.push(current.getLeftChild());
                    local.push(current.getRightChild());
                    if (current.getLeftChild() != null || current.getRightChild() != null) {
                        isNewRow = true;
                    }
                }
                else {
                    System.out.print(getTextColorDefault() + "--" + getTextColorDefault());
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
            while(!local.isEmpty()) {
                global.push(local.pop());
            }
        }
        System.out.println(">>>");
    }
    /**
     * Вывести дерево в файл с автоматически рассчитанной шириной дерева.
     * @param path имя сохраняемого файла, в том числе его относительный или полный путь.
     * @param charset набор символов кодировки, в котором будет сохранен файл, например, StandardCharsets.UTF_8.
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public void displayTree(String path, Charset charset) throws IOException {
        displayTree(path, charset, 0);
    }
    /**
     * Вывести дерево в файл с применением заданного количества пробелов влияющих на ширину дерева.
     * @param path имя сохраняемого файла, в том числе его относительный или полный путь.
     * @param charset набор символов кодировки, в котором будет сохранен файл, например, StandardCharsets.UTF_8.
     * @param blanks количество пробелов сдвига дерева и всех его узлов вправо от начала строки, начиная с вершины дерева.
     *              Чем больше значение {@code blanks}, тем больше растягивается дерево по горизонтали.
     *              При задании значения 0 (или меньше 0) производится автоматический расчет значения {@code blanks} в зависимости от заполнения дерева.
     *              Рекомендуется задавать двоичное значение {@code blanks} (32, 64, 128, ...) для правильного вывода иерархии дерева.
     *              Не двоичное значение будет автоматически преобразовано к ближайшему двоичному значению (вверх или вниз).
     * @throws IOException если при открытии или создании файла произошла ошибка ввода-вывода.
     */
    public void displayTree(String path, Charset charset, int blanks) throws IOException {

        try(PrintWriter out = new PrintWriter(path, charset)) {
            Stack<Node> global = new Stack<>();
            global.push(root);
            int nBlank = getMaxBlank(blanks);
            boolean isNewRow = true;
            out.println("<<< Red-Black tree");
            while (isNewRow) {
                isNewRow = false;
                Stack<Node> local = new Stack<>();
                for (int j = 0; j < nBlank; ++j) {
                    out.print(" ");
                }
                while (!global.isEmpty()) {
                    Node current = global.pop();
                    int len;
                    if (current != null) {
                        out.print(current.getKey());
                        len = Integer.toString(current.getKey()).length();
                        local.push(current.getLeftChild());
                        local.push(current.getRightChild());
                        if (current.getLeftChild() != null || current.getRightChild() != null) {
                            isNewRow = true;
                        }
                    } else {
                        out.print("--");
                        len = 2;
                        local.push(null);
                        local.push(null);
                    }
                    for (int j = 0; j < nBlank * 2 - (len); ++j) {
                        out.print(" ");
                    }
                }
                out.println();
                nBlank /= 2;
                while (!local.isEmpty()) {
                    global.push(local.pop());
                }
            }
            out.println(">>>");
            out.flush();
        }
    }
}
