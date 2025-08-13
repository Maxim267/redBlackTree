package binarySearchTree;

import utils.calculations.MathUtils;
import utils.constants.AppConstants;
import utils.output.DualOutput;
import utils.output.IntDisplay;
import utils.output.IntOutput;

import java.util.Stack;

/**
 * Двоичное дерево поиска (Binary search tree, BST).
 * Узлы упорядочены в соответствии с естественным порядком их ключей {@code K}.
 * Уникальность узлов по ключу {@code K} поддерживается.
 * @param <K> тип ключей, поддерживаемых этим деревом.
 * @param <V> тип соответствующих ключам данных.
 */
public class BSTree<K extends Comparable<K>, V> implements IntOutput {
    /**
     * Корневой узел дерева.
     */
    private BSNode<K, V> root;

    /**
     * Размер дерева (количество узлов).
     */
    private int size;

    /**
     * Интерфейс вывода в поток двоичного дерева поиска.
     */
    public final IntDisplay out = new IntDisplay(this::display, "<<< BS tree: ", ">>>");

    /**
     * Создает пустое двоичное дерево поиска.
     */
    public BSTree() {
        root = null;
        size = 0;
    }

    /**
     * Получает корневой узел дерева.
     * @return корневой узел.
     */
    public BSNode<K, V> getRoot() {
        return root;
    }

    /**
     * Устанавливает корневой узел дерева.
     * @param root корневой узел.
     */
    public void setRoot(BSNode<K, V> root) {
        this.root = root;
    }

    /**
     * Получает размер дерева.
     * @return количество узлов дерева.
     */
    public int size() {
        return size;
    }

    /**
     * Добавляет узел в дерево с данными.
     * Уникальность элементов по ключу {@code K} поддерживается.
     * @param key значение ключа узла.
     * @param value данные узла.
     * @return добавленный узел.
     */
    public BSNode<K, V> add(K key, V value) {
        return add(key, value, false);
    }

    /**
     * Добавляет узел в дерево с данными.
     * Уникальность элементов по ключу {@code K} поддерживается.
     * @param key значение ключа узла дерева.
     * @param value данные узла дерева.
     * @param isMerge признак объединенного узла.
     * @return добавленный узел.
     */
    public BSNode<K, V> add(K key, V value, boolean isMerge) {
        BSNode<K, V> newNode = new BSNode<>(key, value);
        newNode.setValue(value);
        if(isMerge) {
            newNode.setIsMerge(true);
        }
        if(root == null) {
            root = newNode;
            size++;
            return newNode;
        }
        else {
            BSNode<K, V> current = root;
            BSNode<K, V> parent;
            while(current.getKey() != key) {
                parent = current;
                if(newNode.compareToOther(current) < 0) {
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
            // Обнаружен узел с заданным ключом
            if(current.getKey() == key) {
                // Переписать данные узла
                current.setValue(value);
                return current;
            }
        }
        return null;
    }

    /**
     * Добавляет узел в дерево без данных.
     * Уникальность элементов по ключу {@code K} поддерживается.
     * @param key значение ключа.
     * @return добавленный узел.
     */
    public BSNode<K, V> add(K key) {
        return add(key, null);
    }

    /**
     * Находит узел дерева по заданному ключу.
     * @param key значение ключа.
     * @return найденный узел.
     */
    public BSNode<K, V> findNode(K key) {
        BSNode<K, V> current = getRoot();
        while(current != null && current.compareToOther(key) != 0) {
            if(current.compareToOther(key) > 0) {
                current = current.getLeftChild();
            }
            else {
                current = current.getRightChild();
            }
        }
        if(current != null && current.compareToOther(key) == 0) {
            return current;
        }
        else {
            return null;
        }
    }

    /**
     * Находит узел дерева по заданному ключу с выводом результата поиска.
     * @param key значение ключа узла.
     */
    public void findNodeExt(K key) {
        BSNode<K, V> node = findNode(key);
        if (node != null) {
            System.out.print("Node is found: key = " + node.getKey() + "; value = " + (node.getValue() != null ? node.getValue() : "null") + "; ");
            BSNode<K, V> child = node.getLeftChild();
            if (child != null) {
                System.out.print("Left: " + child.getKey() + "; ");
            }
            child = node.getRightChild();
            if (child != null) {
                System.out.println("Right: " + child.getKey() + "; ");
            }
        }
        else {
            System.out.println("Node is not found: key = " + key + "; ");
        }
    }

    /**
     * Получает оценочную максимальную длину выводимого значения узлов дерева.
     * @return максимальную длину выводимого значения узлов.
     */
    public int getEstimateMaxValue() {
        BSNode<K, V> current = root;
        int nMax = 0;
        while(current != null) {
            String str = current.getKey().toString().replace("\n", AppConstants.PRINT_NEW_ROW)
                    + "/" + (current.getValue() != null ? current.getValue().toString().replace("\n", AppConstants.PRINT_NEW_ROW) : "");
            int len = str.length();
            if(nMax < len) {
                nMax = len;
            }
            current = current.getRightChild();
        }
        return nMax;
    }

    /**
     * Получает количество уровней дерева.
     * @return количество уровней дерева.
     */
    public int getNLevelTree() {
        BSNode<K, V> current = root;
        int nLevel = 0;
        while(current != null) {
            nLevel++;
            current = current.getRightChild();
        }
        return nLevel;
    }


    // DISPLAY

    @Override
    public void display(int blanks, DualOutput out) {
        // Инициатор выводит свои верхнее и нижнее оформления сообщения и отключает их вывод в цепочке объектов.
        String header = out.getHeader() != null ? out.getHeaderOnce() : this.out.getHeader();
        String footer = out.getFooter() != null ? out.getFooterOnce() : this.out.getFooter();

        Stack<BSNode<K, V>> global = new Stack<>();
        global.push(root);
        int nBlank;
        if(blanks > 0) {
            nBlank = MathUtils.getBinaryRound(blanks);
        } else {
            int nLevel = getNLevelTree();
            int nMax = getEstimateMaxValue();
            nLevel = nLevel + (nMax) - 1;
            nBlank = (int) Math.pow(2, nLevel);
        }
        if(AppConstants.MAX_BLANKS > 0 && nBlank > AppConstants.MAX_BLANKS) {
            nBlank = MathUtils.getBinaryRound(AppConstants.MAX_BLANKS);
        }
        boolean isNewRow = true;

        // header
        out.println(header + "(blanks = " + nBlank + "): ");

        while(isNewRow) {
            isNewRow = false;
            Stack<BSNode<K, V>> local = new Stack<>();
            for(int j = 0; j < nBlank; ++j) {
                out.print(" ");
            }
            while(!global.isEmpty()) {
                BSNode<K, V> current = global.pop();
                int len;
                if(current != null) {
                    String value = "";
                    int valLenght = 0;
                    if (current.getValue() != null) {
                        value = current.getValue().toString();
                        if(value.equals(AppConstants.UNIX_NEW_ROW)) {
                            value = AppConstants.PRINT_NEW_ROW;
                        }
                        else {
                            value = "/" + value;
                        }
                        valLenght = value.length();
                    }
                    out.print(current.getKey().toString() + value);
                    len = current.getKey().toString().length() + valLenght;
                    local.push(current.getLeftChild());
                    local.push(current.getRightChild());
                    if (current.getLeftChild() != null || current.getRightChild() != null) {
                        isNewRow = true;
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
            out.println("");
            nBlank /= 2;
            while(!local.isEmpty()) {
                global.push(local.pop());
            }
        }
        // footer
        out.println(footer);
    }
}
