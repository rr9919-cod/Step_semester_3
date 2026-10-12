public class Problem5_RoundRobinGameTurns {
    static class Node {
        String name;
        Node next;
        Node(String name) { this.name = name; }
    }

    static class CircularList {
        Node tail;

        void addLast(String name) {
            Node n = new Node(name);
            if (tail == null) {
                tail = n;
                tail.next = tail;
            } else {
                n.next = tail.next;
                tail.next = n;
                tail = n;
            }
        }

        void printTurns(int turns) {
            if (tail == null || turns <= 0) return;
            Node current = tail.next; // head
            for (int i = 0; i < turns; i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(current.name);
                current = current.next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CircularList players = new CircularList();
        players.addLast("Asha");
        players.addLast("Ravi");
        players.addLast("Neha");
        players.printTurns(7); // Asha Ravi Neha Asha Ravi Neha Asha
    }
}
