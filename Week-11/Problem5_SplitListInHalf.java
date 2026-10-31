class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem5_SplitListInHalf {
    static Node splitListInHalf(Node head) {
        if (head == null || head.next == null) return null;
        
        Node slow = head, fast = head;
        Node prev_of_slow = null;
        
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            prev_of_slow = slow;
            slow = slow.next;
        }
        
        if (fast != null) { // odd number of nodes
            prev_of_slow = slow;
            slow = slow.next;
        }
        
        if (prev_of_slow != null) {
            prev_of_slow.next = null;
        }
        
        return slow;
    }
}
