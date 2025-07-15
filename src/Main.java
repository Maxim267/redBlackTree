import redBlackTree.RBTree;

import java.io.IOException;
import java.time.LocalDateTime;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;

public class Main {
    public static void main(String[] args)  throws IOException {

        // Создать красно-черное дерево
        RBTree tree = new RBTree();

        // Время начала обработки
        System.out.println("time1: " + LocalDateTime.now());

        // Выбрать тип генерации значений ключа:
        //   I - на возрастание
        //  II - на убывание
        // III - случайные

        // I - на возрастание
        int n = 35;
        for(int j = 1; j <= n; ++j) {
            tree.add(j);
            if(j % 100000 == 0) {
                System.out.println(j);
            }
        }

        // II - на убывание
//        int n = 35;
//        for(int j = n; j > 0; --j) {
//            tree.add(j);
//            if(j % 100000 == 0) {
//                System.out.println(j);
//            }
//        }

        // III - случайные
//        int n = 35;
//        int j = 0;
//        Map<Integer, Integer> map = new HashMap();
//        while(j < n) {
//            int val = (int) (java.lang.Math.random() * n);
//            if (!map.containsKey(val)) {
//                map.put(val, 0);
//                tree.add(val);
//                j++;
//                if(j % 100000 == 0) {
//                    System.out.println(j);
//                }
//            }
//        }

        System.out.println("size of the RB tree: " + tree.getSize());
        System.out.println("time2: " + LocalDateTime.now());

        // Вывод в файл иерархии красно-черного дерева (без цвета)
        // Рекомендуется при сохранении большого дерева вызывать displayTree с параметром blank более 16384 (убрать ограничение в коде)
        // что позволит нижним уровням дерева выводится без искажения, но верхнии уровни будем сильно растянуты
//         tree.displayTree("Tree.txt", StandardCharsets.UTF_8, 0);

        // Вывод в консоль иерархии красно-черного дерева (с цветом)
        // Не рекомендуется выводить в консоль дерево с более чем 1000 узлов (тогда blank должен быть увеличен: 8192, 16384), иначе вывод происходит с искажением,
        // Также консоль должна поддерживать вывод длинных строк.
        tree.displayTree(0);

        // Проверить на наличие красных линий (их не должно быть)
        // Не рекомендуется использовать этот рекурсивный метод для огромных деревьев, чтобы не получить ошибку переполнения стека
        tree.alarmRedLines(tree.getRoot(), 0);

        // Время окончания обработки
        System.out.println(LocalDateTime.now());

        // Поиск узла
//        tree.findNodeExt(30);
//        System.out.println("time3: " + LocalDateTime.now());
    }
}