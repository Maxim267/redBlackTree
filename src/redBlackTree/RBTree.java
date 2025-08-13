package redBlackTree;

import binarySearchTree.BSNode;
import binarySearchTree.BSTree;
import utils.calculations.MathUtils;
import utils.constants.AppConstants;
import utils.output.DualOutput;
import utils.output.IntDisplay;

import java.util.Stack;

/**
 * Красно-чёрное дерево (red-black tree, RB tree).
 * Нелистовой узел может иметь одного или двух потомков.
 * Листовой узел не имеет потомков.
 * @see <a href="https://ru.wikipedia.org/wiki/%D0%9A%D1%80%D0%B0%D1%81%D0%BD%D0%BE-%D1%87%D1%91%D1%80%D0%BD%D0%BE%D0%B5_%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%BE">Красно-черное дерево</a>
 * В коде пометки "wiki" соответствуют случаям в web-описании, например, "случай 5 wiki".
 * При добавлении узла в RB tree может нарушиться условие: "Оба потомка любого красного узла - черные".
 * Такое нарушенное условие далее в комментариях обозначено как "красная линия".
 * @param <K> тип ключей, поддерживаемых этим деревом.
 * @param <V> тип соответствующих ключам данных.
 */
public class RBTree<K extends Comparable<K>, V> extends BSTree<K, V> {
    /**
     * Перечисление цвета.
     */
    private enum eColor {
        /**
         * Красный цвет.
         */
        RED(AppConstants.COLOR_RED),
        /**
         * Черный цвет.
         */
        BLACK(AppConstants.COLOR_WHITE);

        /**
         * Значение цвета.
         */
        private final String value;

        /**
         * Создает значение цвета.
         */
        eColor(String val) {
            value = val;
        }
        /**
         * Получает значение цвета.
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * Интерфейс вывода в поток красно-черного дерева.
     */
    public final IntDisplay out = new IntDisplay(this::display, "<<< Red-Black tree: ", ">>>");

    /**
     * Создает пустое красно-черное дерево.
     */
    public RBTree() {

    }

    /**
     * Получает код цвета по умолчанию при выводе текста в консоль.
     * @return цвета по умолчанию.
     */
    public String getTextColorDefault() {
        return eColor.BLACK.getValue();
    }

    /**
     * Получает код красного цвета при выводе текста в консоль.
     * @param node узел дерева.
     * @return код красного цвета.
     */
    public String getTextColorNode(BSNode<K, V> node) {
        return node.getIsRed() ? eColor.RED.getValue() : eColor.BLACK.getValue();
    }

    /**
     * Получает символ красного цвета при выводе текста в файл.
     * @param node узел дерева.
     * @return символ красного цвета, например, "*".
     */
    public String getRedSymbolNode(BSNode<K, V> node) {
        return node.getIsRed() ? AppConstants.SYMBOL_RED : "";
    }

    /**
     * Рекурсивно выводит информацию с пометкой "red:" узлов дерева, входящих в "красные линии".
     * Наличие "красных линий" свидетельствует о некорректной структуре дерева.
     * При правильной структуре дерева этот метод не должен выводить информацию с пометкой "red:".
     * @param node узел, с которого начинается рекурсивный поиск "красных линий".
     * @param nRed значение, в котором аккумулируется количество непрерывно найденных красных соседних узлов.
     */
    public void alarmRedLines(BSNode<K, V> node, int nRed) {
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
     * Добавляет узел в дерево.
     * @param key ключ узла дерева.
     * @param value данные узла дерева.
     * @return добавленный узел.
     */
    @Override
    public BSNode<K, V> add(K key, V value ) {
        BSNode<K, V> result;
        BSNode<K, V> current;
        BSNode<K, V> parent;
        BSNode<K, V> grand;
        BSNode<K, V> grand2;
        BSNode<K, V> grand3;

        int nRed;
        current = getRoot();
        parent = null;
        grand = null;
        grand2 = null;
        nRed = 0;
        while (current != null && current.compareToOther(key) != 0) {
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
            if (current.compareToOther(key) > 0) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }

        if(current == null) {
            // Добавление узла
            result = super.add(key, value);
        }
        else {
            result = current;
        }
        // Обработка ПОСЛЕ добавления узла
        checkColorsAfterInsert(result, parent, grand, grand2);

        return result;
    }

    /**
     * Проверяет и при необходимости вносит изменения в окраску узлов дерева ДО добавления узла.
     * @param parent узел - ближайший родственник узла.
     * @param grand узел - родственник предыдущего родственника.
     * @param grand2 узел - родственник предыдущего родственника.
     * @param grand3 узел - родственник предыдущего родственника.
     */
    public void checkColorsBeforeInsert(BSNode<K, V> parent, BSNode<K, V> grand, BSNode<K, V> grand2, BSNode<K, V> grand3) {
        turnRedLine(parent, grand, grand2, grand3);
    }

    /**
     * Проверяет и при необходимости вносит изменения в окраску узлов дерева ПОСЛЕ добавления узла.
     * @param parent узел - ближайший родственник узла.
     * @param grand узел - родственник предыдущего родственника.
     * @param grand2 узел - родственник предыдущего родственника.
     * @param grand3 узел - родственник предыдущего родственника.
     */
    public void checkColorsAfterInsert(BSNode<K, V> parent, BSNode<K, V> grand, BSNode<K, V> grand2, BSNode<K, V> grand3) {
        turnRedLine(parent, grand, grand2, grand3);
        setColorsTriangle(grand);
        correctOppositeSide(grand);
    }

    /**
     * Проверить и при необходимости внести изменения в окраску узлов на стороне дерева
     * противоположной стороне поворота "красной линии" относительно корневой вершины.
     * @param node узел, который может образовать "красную линию".
     */
    public void correctOppositeSide(BSNode<K, V> node) {
        if(node == null) {
            return;

        }
        K key = node.getKey();
        BSNode<K, V> current;
        BSNode<K, V> parent;
        BSNode<K, V> grand;
        BSNode<K, V> grand2;
        BSNode<K, V> grand3;

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
            if (current.compareToOther(key) > 0) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }
    }
    /**
     * Переключает цвета узлов в треугольнике для исправления "красных линий".
     * Треугольник представлен верхним узлом (верхней вершиной) и двумя его потомками (нижними вершинами).
     * @param parent верхний узел треугольника.
     */
    public void setColorsTriangle(BSNode<K, V> parent) {
        if(parent == null) {
            return;
        }

        // Получить потомков верхнего узла треугольника (нижние вершины)
        BSNode<K, V> childLeft = parent.getLeftChild();
        BSNode<K, V> childRight = parent.getRightChild();

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
     * Поворачивает "красную линию" при ее наличии.
     * @param node узел, который, возможно, входит в "красную линию".
     * @param parent узел - ближайший родственник узла node, который, возможно, также входит в "красную линию".
     * @param grand узел - родственник предыдущего родственника.
     * @param grand2 узел - родственник предыдущего родственника.
     */
    public void turnRedLine(BSNode<K, V> node, BSNode<K, V> parent, BSNode<K, V> grand, BSNode<K, V> grand2) {
        if(grand == null || node == null) {
            return;
        }

        // Красный узел принадлежит красному родителю
        if(node.getIsRed() && parent.getIsRed()) {
            // Родитель левый
            if(parent == grand.getLeftChild()) {

                // Правый поворот левой "красной линии' (N + P) с внешним внуком (N)

                // ---- G
                // -- P
                // N

                // Связь красных узлов обнаружена как левая
                if(node == parent.getLeftChild()){

                    // <<< Поворот вправо "красной линии" (N + P) (случай 5 wiki)
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

                // Правый поворот левой "красной линии" (N + P) с внутренним внуком (N)

                //  ------ G
                //  -- P
                //  ---- N

                // Связь красных узлов обнаружена как правая
                else {

                    // <<< Поворот влево "красной линии" (N + P) (случай 4 wiki)
                    grand.setLeftChild(node);
                    parent.setRightChild(node.getLeftChild());
                    node.setLeftChild(parent);
                    // >>>

                    // <<< Поворот вправо "красной линии" (P + N) (случай 5 wiki)
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

                // Левый поворот правой "красной линии" (N + P) с внешним внуком (N)

                // G
                // ---- P
                // ------ N

                // Связь красных узлов обнаружена как правая
                if(node == parent.getRightChild()){

                    // <<< Поворот влево "красной линии" (P + N) (случай 5 wiki)
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

                    // Левый поворот правой "красной линии" (N + P) с внутренним внуком (N)

                    // G
                    // ---- P
                    // -- N

                    // <<< Поворот вправо "красной линии" (N + P) (случай 4 wiki)
                    grand.setRightChild(node);
                    parent.setLeftChild(node.getRightChild());
                    node.setRightChild(parent);
                    // >>>

                    // <<< Поворот влево "красной линии" (P + N) (случай 5 wiki)
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

    /**
     * Рассчитывает позиционирование дерева, влияющее на получение оптимальной ширины вывода дерева.
     * @return количество пробелов позиционирования дерева.
     */
    private int getCalcBlanks(DualOutput out) {
        int nLevel = getNLevelTree();
        int nMax = getEstimateMaxValue();
        if(out.getUseFile()) {
            nMax = nMax + AppConstants.SYMBOL_RED.length();
        }
        if(out.getUseFile()) {
            // при выводе в файл
            nLevel = nLevel + (nMax) / 3 - 1;
        }
        else {
            // при выводе в консоль
            nLevel = nLevel + (nMax) / 2 - 1;
        }
        return (int) Math.pow(2, nLevel);
    }


    // DISPLAY

    @Override
    public void display(int blanks, DualOutput out) {
        // Инициатор выводит свои верхнее и нижнее оформления сообщения и отключает их вывод в цепочке объектов.
        String header = out.getHeader() != null ? out.getHeaderOnce() : this.out.getHeader();
        String footer = out.getFooter() != null ? out.getFooterOnce() : this.out.getFooter();

        Stack<BSNode<K, V>> global = new Stack<>();
        global.push(getRoot());
        int nBlank;
        if(blanks > 0) {
            nBlank = MathUtils.getBinaryRound(blanks);
        } else {
            // Расчет позиционирования
            nBlank = getCalcBlanks(out);
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
                    String sRed = "";
                    if(out.getUseFile()) {
                        sRed = getRedSymbolNode(current);
                        out.print(sRed + current.getKey().toString() + value);
                    }
                    else {
                        out.print(getTextColorNode(current) + current.getKey().toString() + value + getTextColorDefault());
                    }
                    len = current.getKey().toString().length() + valLenght + sRed.length();
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
