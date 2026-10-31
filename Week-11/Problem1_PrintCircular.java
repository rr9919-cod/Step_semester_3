class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem1_PrintCircular {
    static void printCircular(Node head) {
        if (head == null) {
            System.out.println();
            return;
        }
        Node current = head;
        do {
            System.out.print(current.data + " ");
            current = current.next;
        } while (current != head);
        System.out.println();
    }
}
