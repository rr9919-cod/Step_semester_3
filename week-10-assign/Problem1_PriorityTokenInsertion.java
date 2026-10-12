import java.util.Scanner;

public class Problem1_PriorityTokenInsertion {
    static class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    static class MyList {
        Node head;

        void addFirst(int token) {
            Node n = new Node(token);
            n.next = head;
            head = n;
        }

        void addLast(int token) {
            Node n = new Node(token);
            if (head == null) {
                head = n;
                return;
            }
            Node cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = n;
        }

        void insertAt(int index, int token) {
            if (index < 0) throw new IndexOutOfBoundsException("Invalid index");
            if (index == 0) {
                addFirst(token);
                return;
            }
            Node cur = head;
            for (int i = 0; cur != null && i < index - 1; i++) cur = cur.next;
            if (cur == null) throw new IndexOutOfBoundsException("Invalid index");
            Node n = new Node(token);
            n.next = cur.next;
            cur.next = n;
        }

        void printList() {
            Node cur = head;
            while (cur != null) {
                System.out.print(cur.data + " -> ");
                cur = cur.next;
            }
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        MyList list = new MyList();
        // Example from assignment:
        list.addLast(101);
        list.addLast(102);
        list.addLast(103);
        list.addFirst(100);
        list.addLast(104);
        list.insertAt(2, 150);
        list.printList();
    }
}
