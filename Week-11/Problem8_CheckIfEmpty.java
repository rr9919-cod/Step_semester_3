class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem8_CheckIfEmpty {
    static boolean checkIfEmpty(Node head) {
        return head == null;
    }
}
