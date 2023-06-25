package Tree;

import java.util.ArrayList;
import java.util.List;

public class BinTree<T extends Comparable<T>> {
    public static void main(String[] args) {
        BinTree<Integer> Tree = new BinTree<>();

        Tree.add(13);
        Tree.add(1);
        Tree.add(20);
        Tree.add(10);
        Tree.add(17);
        Tree.add(8);
        Tree.add(2);
        Tree.add(10);
        Tree.add(28);
        Tree.add(21);
        Tree.add(32);
        Tree.add(15);
        Tree.add(9);
        Tree.add(7);
        Tree.add(5);
        Tree.add(27);
        Tree.add(19);

        Tree.print();

    }
    Node root;
    private class Node { // Класс Node
        T value;
        Color color;
        Node left;
        Node right;

        Node(T _value) { // Конструктор
            this.value = _value;
            left = null;
            right = null;
            color = Color.Red;
        }
    }
    enum Color {Red, Black}

    public boolean add(T value) {   //добавление значения
        if (root == null) {         // проверка на наличие root, если его нет, то создаётся корневая Node и окрашивается в чёрный
            Node newNode = new Node(value);
            root = newNode;
            root.color = Color.Black;
            return true;
        }
        if (addNode(root, value) != null) // добавление Node, если результат addNode не null!!!
            return true;
        return false;
    }

    private Node addNode(Node node, T value) { // добавление Node
        if (node.value.compareTo(value) == 0)   // если добавляемое значение уже есть в дереве, то результат null
            return null;
        if (node.value.compareTo(value) > 0) {  // если добавляемое значение меньше Node, то проверяем наличие node left
            if (node.left == null) {
                node.left = new Node(value);    //если слева пусто, то добавляем значение в node left
                return node.left;
            }
            Node resultNode = addNode(node.left, value); // иначе, создаём resultNode как node left
            node.left = rebalanced(node.left);          // проводим ребалансировку дерева
            return resultNode;
        } else {    // аналогично если значение больше Node, то производим добавление node right
            if (node.right == null) {
                node.right = new Node(value);
                return node.right;
            }
            Node resultNode = addNode(node.right, value);
            node.right = rebalanced(node.right);
            return resultNode;
        }
    }
    public boolean remove(T value) { //удаление значения
        if (!contain(value))    // проверка на наличие значения
            return false;
        Node deleteNode = root; // вводится удаляемая node и предшествующая ей, сначала обе равны root
        Node prevNode = root;
        while (deleteNode != null) { //проход дерева в ширину до поиска нужного значения
            if (deleteNode.value.compareTo(value) == 0) {
                Node currentNode = deleteNode.right; //currentNode приваивает значение правой node от удаляемой
                if (currentNode == null) {      //если там ничего нет и удаляемое значение является корнем дерева, то
                    if (deleteNode == root) {   //новый корень получается от root.left и окрашивается чёрным
                        root = root.left;
                        root.color = Color.Black;
                        return true;
                    }
                    deleteNode = rebalanced(deleteNode); // если не корень, то проводится ребалансировка
                    if (deleteNode.left == null) { // если слева от deletedNode ничего нет, то значение удаляется, т.е.
                        deleteNode = null;         //deletedNode обнуляется
                        return true;
                    }
                    if (prevNode.left == deleteNode) // сдвиг дерева после удаления node путём преопределения связей удаляемого node на предшествующий
                        prevNode.left = deleteNode.left;
                    else
                        prevNode.right = deleteNode.left;
                    return true;
                }
                while (currentNode.left != null)    // если справа от удаляемой node есть ветки,
                    currentNode = currentNode.left; // то делаем ребалансировку
                deleteNode = rebalanced(deleteNode);
                deleteNode.value = currentNode.value;   //значению удаляемой node присваиваем значением текущей
                currentNode = null;                     //текущую node обнуляем
                return true;
            }
            if (prevNode != deleteNode) {               // сдвиг по дереву
                if (prevNode.value.compareTo(value) > 0)
                    prevNode = prevNode.left;
                else
                    prevNode = prevNode.right;
            }
            if (deleteNode.value.compareTo(value) > 0)
                deleteNode = deleteNode.left;
            else
                deleteNode = deleteNode.right;
        }
        return false;
    }
    private boolean contain(T value) { //поиск node проходом в ширину
        Node currentNode = root;
        while (currentNode != null) {
            if (currentNode.value.equals(value))
                return true;
            if (currentNode.value.compareTo(value) > 0)
                currentNode = currentNode.left;
            else
                currentNode = currentNode.right;
        }
        return false;
    }
    private Node rebalanced(Node node) { // ребалансировка Node
        Node result = node;
        boolean needRebalance; // проверка по условиям
        do {
            needRebalance = false;
            //если правая node красная и левая чёрная, ребалансировка правым поворотом
            if (result.right != null && result.right.color == Color.Red
                    && (result.left == null || result.left.color == Color.Black)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            //если левая node красная и левая от левой тоже красная, ребалансировка левым поворотом
            if (result.left != null && result.left.color == Color.Red
                    && result.left.left != null && result.left.left.color == Color.Red) {
                needRebalance = true;
                result = leftSwap(result);
            }
            //если левая и правая node красные, ребалансировка сменой цветов
            if (result.left != null && result.left.color == Color.Red
                    && result.right != null && result.right.color == Color.Red) {
                needRebalance = true;
                colorSwap(result);
            }
        } while (needRebalance);
        return result;
    }
    private void colorSwap(Node node) { //смена цветов
        node.right.color = Color.Black; //node правая и левая окрашиваются в чёрный, а node - в красный
        node.left.color = Color.Black;
        node.color = Color.Red;
    }
    private Node leftSwap(Node node) { //левый поворот
        Node left = node.left;
        Node between = left.right;
        left.right = node;
        node.left = between;
        left.color = node.color;
        node.color = Color.Red;
        return left;
    }
    private Node rightSwap(Node node) { //правый поворот
        Node right = node.right;
        Node between = right.left;
        right.left = node;
        node.right = between;
        right.color = node.color;
        node.color = Color.Red;
        return right;
    }

    private class PrintNode {
        Node node;
        String str;
        int depth;

        public PrintNode() {
            node = null;
            str = " ";
            depth = 0;
        }

        public PrintNode(Node node) {
            depth = 0;
            this.node = node;
            this.str = node.value.toString();
        }
    }
    public void print() {

        int maxDepth = maxDepth() + 3;
        int nodeCount = nodeCount(root, 0);
        int width = 50;//maxDepth * 4 + 2;
        int height = nodeCount * 5;
        List<List<PrintNode>> list = new ArrayList<List<PrintNode>>();
        for (int i = 0; i < height; i++) /*Создание ячеек массива*/ {
            ArrayList<PrintNode> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new PrintNode());
            }
            list.add(row);
        }

        list.get(height / 2).set(0, new PrintNode(root));
        list.get(height / 2).get(0).depth = 0;

        for (int j = 0; j < width; j++)  /*Принцип заполнения*/ {
            for (int i = 0; i < height; i++) {
                PrintNode currentNode = list.get(i).get(j);
                if (currentNode.node != null) {
                    currentNode.str = currentNode.node.value.toString();
                    if (currentNode.node.left != null) {
                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.left;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;

                    }
                    if (currentNode.node.right != null) {
                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.right;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
                    }

                }
            }
        }
        for (int i = 0; i < height; i++) /*Чистка пустых строк*/ {
            boolean flag = true;
            for (int j = 0; j < width; j++) {
                if (list.get(i).get(j).str != " ") {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.remove(i);
                i--;
                height--;
            }
        }

        for (var row : list) {
            for (var item : row) {
                System.out.print(item.str + " ");
            }
            System.out.println();
        }
    }
    private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) {
        if (i2 > i) // Идём вниз
        {
            while (i < i2) {
                i++;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "\\";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        } else {
            while (i > i2) {
                i--;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "/";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        }
    }
    public int maxDepth() {
        return maxDepth2(0, root);
    }
    private int maxDepth2(int depth, Node node) {
        depth++;
        int left = depth;
        int right = depth;
        if (node.left != null)
            left = maxDepth2(depth, node.left);
        if (node.right != null)
            right = maxDepth2(depth, node.right);
        return left > right ? left : right;
    }
    private int nodeCount(Node node, int count) {
        if (node != null) {
            count++;
            return count + nodeCount(node.left, 0) + nodeCount(node.right, 0);
        }
        return count;
    }
}
