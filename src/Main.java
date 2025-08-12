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

    // Сравнить формирование деревьев с целыми и строковыми ключами
    public static void CompareTreeTypes() {
        System.out.println("Сравнить красно-черные деревья с разными типами ключей: Integer; String:");

        // Создать красно-черное дерево с ЦЕЛОЧИСЛЕННЫМ ключом
        RBTree<Integer, String> tree = new RBTree<>();

        // Время начала обработки
        long start = System.nanoTime();

        // Общий диапазон для обоих типов деревьев
        int cnt = 16;

        int offSet = 127866; // Символ эмодзи (emoji)
        for (int j = 1; j <= cnt; ++j) {
            // Добавить ключ и данные
            tree.add(j, String.valueOf(Character.toChars((j - 1) + offSet)));
        }

        // Время окончания обработки
        long finish = System.nanoTime();

        TestDebug.Info(1, cnt, start, finish, tree.getRoot().getKey().toString(), tree.size());

        tree.out.display(0);
        // При выводе в файл используется AppConstants.SYMBOL_RED: "*" - символ красного цвета узла, например, "*123".
        // tree.out.display(0, "AAA.txt");


        // Создать красно-черное дерево со СТРОКОВЫМ ключом

        RBTree<String, Integer> tree2 = new RBTree<>();

        // Время начала обработки
        start = System.nanoTime();

        // Используя заданный набор символов UTF_16
        Charset charset = StandardCharsets.UTF_16;
        CharsetEncoder encoder = charset.newEncoder();
        int first = 65; // Начать с "A"
        int last = first + cnt - 1;
        IntStream.rangeClosed(first, last)
                .filter(cp -> {
                    try {
                        return encoder.canEncode(Character.toString(cp));
                    } catch (IllegalArgumentException e2) {
                        return false;
                    }
                })
                // // Добавить ключ и данные
                .forEach(cp -> tree2.add(String.valueOf(Character.toChars(cp)), cp));

        // Время окончания обработки
        finish = System.nanoTime();

        TestDebug.Info(first, last, start, finish, tree2.getRoot().getKey() + " (" + tree2.getRoot().getKey().codePoints().toArray()[0] + ")", tree2.size());

        tree2.out.display(0);
        // При выводе в файл используется AppConstants.SYMBOL_RED: "*" - символ красного цвета узла, например, "*A".
        //tree2.out.display(0, "BBB.txt");

    }

    //   I - на возрастание ключей
    public static RBTree<Integer, String> IncreasingKeyTree(int count) {
        System.out.println("\nКрасно-черное дерево с целыми ключами на возрастание:");

        RBTree<Integer, String> tree = new RBTree<>();

        // Время начала обработки
        long start = System.nanoTime();

        for(int j = 1; j <= count; ++j) {
            // Добавить ключ
            tree.add(j);
            if(j % 1_000_000 == 0) {
                System.out.println(j);
            }
        }

        // Время окончания обработки
        long finish = System.nanoTime();

        TestDebug.Info(1, count, start, finish, tree.getRoot().getKey().toString(), tree.size());

        return tree;
    }

    // II - на убывание ключей.
    public static RBTree<Integer, String> DecreasingKeyTree(int count) {
        System.out.println("\nКрасно-черное дерево с целыми ключами на убывание:");

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

    // III - случайные целые ключи
    public static RBTree<Integer, String> RandomKeyTree(int count) {
        System.out.println("\nКрасно-черное дерево с случайными целыми ключами:");

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
    public static void main(String[] args)  throws IOException {
        // Пример обработки параметризованных деревьев RBTree<K, V>

        // O Сравнение формирования деревьев с целыми и строковыми ключами (с данными)
        TestDebug.CompareTreeTypes();


        // Разный тип генерации значений ключа:

        // I - последовательное возрастание целых ключей.
        int count = 35;
        RBTree<Integer, String> tree = TestDebug.IncreasingKeyTree(count);
        tree.out.display(0);
        // При выводе в файл используется AppConstants.SYMBOL_RED: "*" - символ красного цвета узла, например, "*123".
        // tree.out.display(0, "file1.txt");

        // II - последовательное убывание целых ключей.
        RBTree<Integer, String> tree2 = TestDebug.DecreasingKeyTree(35);
        tree2.out.display(0);
        // tree2.out.display(0, "file2.txt");

        // III - случайные целые ключи
        RBTree<Integer, String> tree3 = TestDebug.RandomKeyTree(35);
        tree3.out.display(64);
        // tree3.out.display(64, "file3.txt");
    }
}