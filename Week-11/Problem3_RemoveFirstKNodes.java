class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem3_RemoveFirstKNodes {
    static Node removeFirstKNodes(Node head, int k) {
        if (head == null || k <= 0) return head;
        while (head != null && k > 0) {
            head = head.next;
            k--;
        }
        return head;
    }
}
