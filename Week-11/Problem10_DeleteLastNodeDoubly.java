class DNode {
    int data;
    DNode prev, next;
    DNode(int d) { data = d; }
}

public class Problem10_DeleteLastNodeDoubly {
    static DNode deleteLastNodeDoubly(DNode head) {
        if (head == null || head.next == null) return null;
        
        DNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        
        current.prev.next = null;
        return head;
    }
}
