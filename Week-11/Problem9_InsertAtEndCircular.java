class Node {
    int data;
    Node next;
    Node(int d) { data = d; }
}

public class Problem9_InsertAtEndCircular {
    static Node insertAtEndCircular(Node head, int value) {
        Node newNode = new Node(value);
        
        if (head == null) {
            newNode.next = newNode;
            return newNode;
        }
        
        Node current = head;
        while (current.next != head) {
            current = current.next;
        }
        
        current.next = newNode;
        newNode.next = head;
        return head;
    }
}
