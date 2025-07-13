package binaryTree;

/**
 * Узел двоичного дерева
 */
public class Node {
    /**
     * Поле целочисленного значения ключа
     */
    private int iData;
    /**
     * Поле строковых данных
     */
    private String sData;
    /**
     * Поле красно-черного цвета (true - "красный"; false - "черный")
     */
    private boolean isRed;
    /**
     * Поле ссылки на левый дочерний узел
     */
    private Node leftChild;
    /**
     * Поле ссылки на правый дочерний узел
     */
    private Node rightChild;
    /**
     * Конструктор класса {@code Node}
     * @param key целочисленное значение ключа дерева
     */
    public Node(int key) {
        iData = key;
        sData = null;
    }
    /**
     * Конструктор класса {@code Node}
     * @param key целочисленное значение ключа дерева
     * @param data строковые данные как парное значение ключа
     */
    public Node(int key, String data) {
        iData = key;
        sData = data;
        isRed = true;
    }
    @Override
    public String toString() {
        return Integer.toString(getKey());
    }
    /**
     * Установить значение ключа узла
     * @param key значение ключа узла
     */
    public void setKey(int key) {
        iData = key;
    }
    /**
     * Получить значение ключа узла
     * @return значение ключа текущего узла
     */
    public int getKey() {
        return iData;
    }
    /**
     * Получить данные по ключу
     * @return данные по заданному ключу
     */
    public String getData() {
        return sData;
    }
    /**
     * Установить данные узла
     * @param data данные узла
     */
    public void setData(String data) {
        sData = data;
    }
    /**
     * Получить ссылку на левый дочерний узел
     * @return узел
     */
    public Node getLeftChild() {
        return leftChild;
    }
    /**
     * Установить левый дочерний узел
     * @param left узел
     */
    public void setLeftChild(Node left) {
        leftChild = left;
    }
    /**
     * Получить ссылку на правый дочерний узел
     * @return узел
     */
    public Node getRightChild() {
        return rightChild;
    }
    /**
     * Установить левый дочерний узел
     * @param right узел
     */
    public void setRightChild(Node right) {
        rightChild = right;
    }
    /**
     * Получить цвет узла
     * @return true: "красный"; false: "черный"
     */
    public boolean getIsRed() {
        return isRed;
    }
    /**
     * Установить цвет узла
     * @param red true: "красный"; false: "черный"
     */
    public void setIsRed(boolean red) {
        isRed = red;
    }

}
