class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem4_DeleteNthNode {
    static Node deleteNthNode(Node head, int n) {
        if (head == null) return null;
        if (n == 1) return head.next;
        
        Node current = head;
        for (int i = 1; i < n - 1 && current != null; i++) {
            current = current.next;
        }
        
        if (current != null && current.next != null) {
            current.next = current.next.next;
        }
        return head;
    }
}
