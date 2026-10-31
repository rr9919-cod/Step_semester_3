class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem7_CountNodes {
    static int countNodes(Node head) {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
}
