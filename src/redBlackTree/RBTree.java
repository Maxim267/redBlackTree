package redBlackTree;

import binaryTree.Node;
import binaryTree.BSTree;

/**
 * Красно-чёрное дерево (red-black tree, RB tree).
 * Нелистовой узел может иметь одного или двух потомков.
 * Листовой узел не имеет потомков.
 * см. web-описание RB tree: <a href="https://ru.wikipedia.org/wiki/%D0%9A%D1%80%D0%B0%D1%81%D0%BD%D0%BE-%D1%87%D1%91%D1%80%D0%BD%D0%BE%D0%B5_%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%BE">Красно-чёрное_дерево</a>
 * см. в коде пометки соответствующие случаям в web-описании как "wiki", например, "случай 5 wiki".
 */
public class RBTree extends BSTree {
    /**
     * Создает новое красно-черное дерево {@code RBTree}
     */
    public RBTree() {

    }
    /**
     * Рекурсивно вывести информацию с пометкой "red:" всех обнаруженных узлов дерева, входящих в красные линии.
     * В дереве не должно быть красных линий (два красных узла рядом: красный родитель имеет соседнего красного потомка).
     * При правильной структуре дерева этот метод не должен выводить информацию с пометкой "red:".
     * @param node узел, с которого начинается рекурсивный поиск красных линий.
     * @param nRed значение, в котором аккумулируется количество непрерывно найденных красных соседних узлов.
     */
    public void alarmRedLines(Node node, int nRed) {
        if(node == null) {
            return;
        }
        int red = node.getIsRed() ? 1 : 0;
        nRed += red;
        if(nRed > 1 ) {
            System.out.println(getTextColorNode(node) + "red: " + node.getKey() + getTextColorDefault());
        }
        if(nRed > 1 || red == 0) {
            nRed = 0;
        }
        alarmRedLines(node.getLeftChild(), nRed);
        alarmRedLines(node.getRightChild(), nRed);
    }
    /**
     * Добавить узел в дерево.
     * @param key целочисленное значение ключа дерева.
     * @param data строковые данные как парное значение ключа.
     * @return добавленный узел.
     */
    @Override
    public Node add(int key, String data) {
        Node result;
        Node current;
        Node parent;
        Node grand;
        Node grand2;
        Node grand3;

        int nRed;
        current = getRoot();
        parent = null;
        grand = null;
        grand2 = null;
        nRed = 0;
        while (current != null && current.getKey() != key) {
            grand3 = grand2;
            grand2 = grand;
            grand = parent;
            parent = current;
            setColorsTriangle(current);
            if (current.getIsRed()) {
                nRed++;
            } else {
                nRed = 0;
            }
            if (nRed == 2) {
                // Обработка ДО добавления узла
                checkColorsBeforeInsert(parent, grand, grand2, grand3);
            }
            if (key < current.getKey()) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }

        if(current == null) {
            // Добавление узла
            result = super.add(key, data);
        }
        else {
            result = current;
        }
        // Обработка ПОСЛЕ добавления узла
        checkColorsAfterInsert(result, parent, grand, grand2);

        return result;
    }
    /**
     * Проверить и при необходимости внести изменения в окраску узлов дерева ДО добавления узла.
     * @param parent узел - ближайший родственник узла, который может образовывать красную линию (от которой необходимо избавиться ее поворотом).
     * @param grand узел - родственник предыдущего родственника.
     * @param grand2 узел - родственник предыдущего родственника.
     * @param grand3 узел - родственник предыдущего родственника.
     */
    public void checkColorsBeforeInsert(Node parent, Node grand, Node grand2, Node grand3) {
        turnRedLine(parent, grand, grand2, grand3);
    }
    /**
     * Проверить и при необходимости внести изменения в окраску узлов дерева ПОСЛЕ добавления узла.
     * @param parent узел - ближайший родственник узла, который может образовывать красную линию (от которой необходимо избавиться ее поворотом).
     * @param grand узел - родственник предыдущего родственника.
     * @param grand2 узел - родственник предыдущего родственника.
     * @param grand3 узел - родственник предыдущего родственника.
     */
    public void checkColorsAfterInsert(Node parent, Node grand, Node grand2, Node grand3) {
        turnRedLine(parent, grand, grand2, grand3);
        setColorsTriangle(grand);
        correctOppositeSide(grand);
    }
    /**
     * Проверить и при необходимости внести изменения в окраску узлов на стороне дерева противоположной относительно корневой вершины стороне поворота красной линии.
     * @param node узел - который может образовать красную линию.
     */
    public void correctOppositeSide(Node node) {
        if(node == null) {
            return;

        }
        int key = node.getKey();
        Node current;
        Node parent;
        Node grand;
        Node grand2;
        Node grand3;

        current = getRoot();
        parent = null;
        grand = null;
        grand2 = null;
        while (current != null) {
            grand3 = grand2;
            grand2 = grand;
            grand = parent;
            parent = current;
            if(current.getKey() == key) {
                turnRedLine(current, grand, grand2, grand3);
                break;
            }
            if (key < current.getKey()) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }
    }
    /**
     * Переключить цвета узлов в треугольнике, нарушающих правило RDTree: Оба потомка каждого красного узла — чёрные.
     * @param parent верхний узел (вершина) треугольника, у которого обязательно должны быть два дочерних узла (нижние вершины).
     */
    public void setColorsTriangle(Node parent) {
        if(parent == null) {
            return;
        }

        // Получить потомков верхнего узла треугольника (нижние вершины)
        Node childLeft = parent.getLeftChild();
        Node childRight = parent.getRightChild();

        // Цвета переключаются только для треугольника
        if(childLeft == null || childRight == null) {
            return;
        }
        // и только с двумя красными нижними вершинами
        if(!childLeft.getIsRed() || !childRight.getIsRed()) {
            return;
        }

        // Корневой верхний всегда черный (случай 1 wiki)
        // Некорневой верхний - красный (случай 2 wiki)
        parent.setIsRed(parent != getRoot());
        // Потомки всегда черные (случай 3 wiki)
        childLeft.setIsRed(false);
        childRight.setIsRed(false);
    }
    /**
     * Повернуть красную линию.
     * @param node узел, который, возможно, входит в красную линию.
     * @param parent узел - ближайший родственник узла node, который, возможно, также входит в красную линию.
     * @param grand узел - родственник предыдущего родственника.
     * @param grand2 узел - родственник предыдущего родственника.
     */
    public void turnRedLine(Node node, Node parent, Node grand, Node grand2) {
        if(grand == null || node == null) {
            return;
        }

        // Красный узел принадлежит красному родителю
        if(node.getIsRed() && parent.getIsRed()) {
            // Родитель левый
            if(parent == grand.getLeftChild()) {

                // Правый поворот левой красной линии (N + P) с внешним внуком (N)

                // ---- G
                // -- P
                // N

                // Связь красных узлов обнаружена как левая
                if(node == parent.getLeftChild()){

                    // <<< Поворот вправо красной линии (N + P) (случай 5 wiki)
                    grand.setLeftChild(parent.getRightChild());
                    if(grand == getRoot()) {
                        setRoot(parent);
                    }
                    if(grand2 != null) {
                        if (grand == grand2.getRightChild()) {
                            grand2.setRightChild(parent);
                        } else {
                            grand2.setLeftChild(parent);
                        }
                    }
                    parent.setRightChild(grand);
                    grand.setIsRed(true);
                    parent.setIsRed(false);
                    // >>>
                }

                // Правый поворот левой красной линии (N + P) с внутренним внуком (N)

                //  ------ G
                //  -- P
                //  ---- N

                // Связь красных узлов обнаружена как правая
                else {

                    // <<< Поворот влево красной линии (N + P) (случай 4 wiki)
                    grand.setLeftChild(node);
                    parent.setRightChild(node.getLeftChild());
                    node.setLeftChild(parent);
                    // >>>

                    // <<< Поворот вправо красной линии (P + N) (случай 5 wiki)
                    grand.setLeftChild(node.getRightChild());
                    if(grand == getRoot()) {
                        setRoot(node);
                    }
                    if(grand2 != null) {
                        if (grand == grand2.getRightChild()) {
                            grand2.setRightChild(node);
                        } else {
                            grand2.setLeftChild(node);
                        }
                    }
                    node.setRightChild(grand);
                    grand.setIsRed(true);
                    node.setIsRed(false);
                    // >>>
                }
            }

            // Родитель правый
            else {

                // Левый поворот правой красной линии (N + P) с внешним внуком (N)

                // G
                // ---- P
                // ------ N

                // Связь красных узлов обнаружена как правая
                if(node == parent.getRightChild()){

                    // <<< Поворот влево красной линии (P + N) (случай 5 wiki)
                    grand.setRightChild(parent.getLeftChild());
                    if(grand == getRoot()) {
                        setRoot(parent);
                    }
                    if(grand2 != null) {
                        if (grand == grand2.getRightChild()) {
                            grand2.setRightChild(parent);
                        } else {
                            grand2.setLeftChild(parent);
                        }
                    }
                    parent.setLeftChild(grand);
                    grand.setIsRed(true);
                    parent.setIsRed(false);
                    // >>>
                }
                // Связь красных узлов обнаружена как левая
                else {

                    // Левый поворот правой красной линии (N + P) с внутренним внуком (N)

                    // G
                    // ---- P
                    // -- N
                    // 11.left = 12

                    // <<< Поворот вправо красной линии (N + P) (случай 4 wiki)
                    grand.setRightChild(node);
                    parent.setLeftChild(node.getRightChild());
                    node.setRightChild(parent);
                    // >>>

                    // <<< Поворот влево красной линии (P + N) (случай 5 wiki)
                    grand.setRightChild(node.getLeftChild());
                    if (grand == getRoot()) {
                        setRoot(node);
                    }
                    if(grand2 != null) {
                        if (grand == grand2.getRightChild()) {
                            grand2.setRightChild(node);
                        } else {
                            grand2.setLeftChild(node);
                        }
                    }
                    node.setLeftChild(grand);
                    grand.setIsRed(true);
                    node.setIsRed(false);
                    // >>>
                }
            }
        }
    }

}
