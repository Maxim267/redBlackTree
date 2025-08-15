import redBlackTree.RBTree;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

// Пример использования деревьев
class TestDebug {

    // Вывод итоговой информации
    public static void Info(int first, int last, long start, long finish, String key, int size) {
        // debug: диапазон; значении(коде) ключа корневой вершины; ожидаемое значение кода ключа на вершине оптимального дерева
        System.out.println("Range: " + first + " - " + last + "; Root key = " + key + "; expected middle: " + ((first + last) / 2));
        System.out.println("Formation time of a RB tree: " + (finish - start) / 1_000_000 + " мс");
        System.out.println("Size of the RB tree: " + size);
    }

    // I - на возрастание строковых ключей
    public static RBTree<String, Integer> IncreasingStringKey(int count) {
        System.out.println("Красно-черное дерево со строковыми ключами на возрастание:");

        // Создать красно-черное дерево со СТРОКОВЫМ ключом

        RBTree<String, Integer> tree = new RBTree<>();

        // Время начала обработки
        long start = System.nanoTime();

        // Используя заданный набор символов UTF_16
        Charset charset = StandardCharsets.UTF_16;
        CharsetEncoder encoder = charset.newEncoder();
        // int first = 65; // Начать с "A"
        int first = 127900; // Кодовая точка Символа, например, эмодзи (emoji)
        int last = first + count - 1;
        IntStream.rangeClosed(first, last)
                .filter(cp -> {
                    try {
                        return encoder.canEncode(Character.toString(cp));
                    } catch (IllegalArgumentException e2) {
                        return false;
                    }
                })
                // Добавить строковый ключ (символ по кодовой точке int) и данные (порядковый номер символа в цикле forEach, начиная с 1)
                .forEach(cp -> tree.add(String.valueOf(Character.toChars(cp)), cp - first + 1));

        // Время окончания обработки
        long finish = System.nanoTime();

        TestDebug.Info(first, last, start, finish, tree.getRoot().getKey() + " (" + tree.getRoot().getKey().codePoints().toArray()[0] + ")", tree.size());

        return tree;

    }

    // II - на возрастание целочисленных ключей
    public static RBTree<Integer, String> IncreasingIntKey(int count) {
        System.out.println("\nКрасно-черное дерево с целочисленными ключами на возрастание:");

        RBTree<Integer, String> tree = new RBTree<>();

        // Время начала обработки
        long start = System.nanoTime();

        for(int j = 1; j <= count; ++j) {
            // Добавить ключ
            tree.add(j);
            // if(j % 1_000_000 == 0) {
            // System.out.println(j);
            // }
        }

        // Время окончания обработки
        long finish = System.nanoTime();

        TestDebug.Info(1, count, start, finish, tree.getRoot().getKey().toString(), tree.size());

        return tree;
    }

    // III - на убывание целочисленных ключей.
    public static RBTree<Integer, String> DecreasingIntKey(int count) {
        System.out.println("\nКрасно-черное дерево с целочисленными ключами на убывание:");

        RBTree<Integer, String> tree = new RBTree<>();

        // Время начала обработки
        long start = System.nanoTime();

        for(int j = count; j > 0; --j) {
            // Добавить ключ
            tree.add(j);
        }

        // Время окончания обработки
        long finish = System.nanoTime();

        TestDebug.Info(1, count, start, finish, tree.getRoot().getKey().toString(), tree.size());

        return tree;
    }

    // IV - случайные целочисленные ключи
    public static RBTree<Integer, String> RandomIntKey(int count) {
        System.out.println("\nКрасно-черное дерево с случайными целочисленными ключами:");

        RBTree<Integer, String> tree = new RBTree<>();

        // Время начала обработки
        long start = System.nanoTime();

        int j = 0;
        Map<Integer, Integer> map = new HashMap<>();
        while(j < count) {
            int val = (int) (java.lang.Math.random() * count) + 1;
            if (!map.containsKey(val)) {
                map.put(val, 0);
                // Добавить ключ
                tree.add(val);
                j++;
            }
        }

        // Время окончания обработки
        long finish = System.nanoTime();

        TestDebug.Info(1, count, start, finish, tree.getRoot().getKey().toString(), tree.size());

        return tree;
    }
}

public class Main {
    public static void main(String[] args) {
        // Пример обработки параметризованных деревьев RBTree<K, V>

        // I - возрастание строковых ключей.
        RBTree<String, Integer> tree = TestDebug.IncreasingStringKey(19);
        tree.out.display(0);

        // При выводе в файл ключ красного узла предваряется символом "*" (используется AppConstants.SYMBOL_RED)
        // tree.out.display(0, "file.txt");

        // Поиск узла по строковому ключу (кодовой точке int)
        // tree.findNodeExt(String.valueOf(Character.toChars(2 + (127900 - 1)))); // 10 - порядковый номер символа в дереве; (127900 - 1): offset in IncreasingStringKey

        // Проверить на наличие красных линий (их не должно быть).
        // Не рекомендуется использовать этот рекурсивный метод для огромных деревьев, чтобы не получить ошибку переполнения стека.
        // tree.alarmRedLines(tree.getRoot(), 0);

        // II - возрастание целочисленных ключей.
        RBTree<Integer, String> tree2 = TestDebug.IncreasingIntKey(35);
        tree2.out.display(0);
        // tree2.out.display(0, "file2.txt");
        // tree2.findNodeExt(2);

        // III - убывание целочисленных ключей.
        RBTree<Integer, String> tree3 = TestDebug.DecreasingIntKey(35);
        tree3.out.display(0);
        // tree3.out.display(0, "file3.txt");
        // tree3.findNodeExt(2);

        // IV - случайные целочисленные ключи
        RBTree<Integer, String> tree4 = TestDebug.RandomIntKey(35);
        tree4.out.display(0);
        // tree4.out.display(0, "file4.txt");
        // tree4.findNodeExt(2);

    }
}