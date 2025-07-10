package binaryTree;

/**
 * Узел двоичного дерева
 */
public class Node {
    /**
     * Ключевое поле  {@code iData}
     */
    private int iData;
    /**
     * Поле данных {@code sData}
     */
    private String sData;
    /**
     * Поле красно-черного цвета {@code isRed}
     */
    private boolean isRed;
    /**
     * Поля ссылок на дочерние узлы {@code leftChild} и {@code rightChild}
     */
    private Node leftChild;
    private Node rightChild;
    public Node(int key) {
        iData = key;
        sData = null;
    }
    public Node(int key, String data) {
        iData = key;
        sData = data;
        isRed = true;
    }
    @Override
    public String toString() {
        return Integer.toString(getKey());
    }
    public void setKey(int key) {
        iData = key;
    }
    public int getKey() {
        return iData;
    }
    public String getData() {
        return sData;
    }
    public void setData(String sdt) {
        sData = sdt;
    }
    public Node getLeftChild() {
        return leftChild;
    }
    public void setLeftChild(Node left) {
        leftChild = left;
    }
    public Node getRightChild() {
        return rightChild;
    }
    public void setRightChild(Node right) {
        rightChild = right;
    }
    public boolean getIsRed() {
        return isRed;
    }
    public void setIsRed(boolean red) {
        isRed = red;
    }

}
