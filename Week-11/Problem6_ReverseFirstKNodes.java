class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem6_ReverseFirstKNodes {
    static Node reverseFirstKNodes(Node head, int k) {
        if (head == null || k <= 1) return head;
        
        Node current = head, prev = null, next = null;
        int count = 0;
        
        while (current != null && count < k) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
            count++;
        }
        
        if (head != null) {
            head.next = current;
        }
        
        return prev;
    }
}
