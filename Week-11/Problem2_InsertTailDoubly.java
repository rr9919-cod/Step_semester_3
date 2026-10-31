class DNode {
    int data;
    DNode prev, next;
    DNode(int d) { data = d; }
}

public class Problem2_InsertTailDoubly {
    static DNode insertAtTailDoubly(DNode head, int value) {
        DNode newNode = new DNode(value);
        if (head == null) return newNode;
        
        DNode current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
        newNode.prev = current;
        return head;
    }
}
