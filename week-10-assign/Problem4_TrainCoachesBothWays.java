public class Problem4_TrainCoachesBothWays {
    static class Node {
        String name;
        Node prev, next;
        Node(String name) { this.name = name; }
    }

    static class DoublyList {
        Node head, tail;

        void addLast(String name) {
            Node n = new Node(name);
            if (head == null) {
                head = tail = n;
            } else {
                tail.next = n;
                n.prev = tail;
                tail = n;
            }
        }

        boolean remove(String name) {
            Node cur = head;
            while (cur != null && !cur.name.equals(name)) cur = cur.next;
            if (cur == null) return false;

            if (cur.prev != null) cur.prev.next = cur.next;
            else head = cur.next;

            if (cur.next != null) cur.next.prev = cur.prev;
            else tail = cur.prev;

            return true;
        }

        void printForward() {
            Node cur = head;
            System.out.print("Forward: ");
            while (cur != null) {
                System.out.print(cur.name);
                if (cur.next != null) System.out.print(" <-> ");
                cur = cur.next;
            }
            System.out.println();
        }

        void printBackward() {
            Node cur = tail;
            System.out.print("Backward: ");
            while (cur != null) {
                System.out.print(cur.name);
                if (cur.prev != null) System.out.print(" <-> ");
                cur = cur.prev;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        DoublyList train = new DoublyList();
        String[] coaches = {"Engine", "A1", "B1", "B2", "Guard"};
        for (String coach : coaches) train.addLast(coach);

        train.remove("B1");
        train.printForward();
        train.printBackward();
    }
}
