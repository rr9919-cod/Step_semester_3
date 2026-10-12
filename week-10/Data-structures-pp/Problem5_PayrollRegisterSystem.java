import java.util.*;

public class Problem5_PayrollRegisterSystem {
    static class Payslip {
        String name, type;
        int pay;
        Payslip(String name, String type, int pay) {
            this.name = name; this.type = type; this.pay = pay;
        }
        public String toString() {
            return "Payslip[name=" + name + ", type=" + type + ", pay=" + pay + "]";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Payslip> slips = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            try {
                switch (p[0].toLowerCase()) {
                    case "fulltime":
                        if (p.length >= 3) slips.add(new Payslip(p[1], "FullTime", Integer.parseInt(p[2])));
                        break;
                    case "parttime":
                        if (p.length >= 4) slips.add(new Payslip(p[1], "PartTime",
                                Integer.parseInt(p[2]) * Integer.parseInt(p[3])));
                        break;
                    case "intern":
                        if (p.length >= 3) slips.add(new Payslip(p[1], "Intern", Integer.parseInt(p[2])));
                        break;
                }
            } catch (NumberFormatException ignored) { }
        }

        int total = 0;
        Payslip top = null;
        for (Payslip p : slips) {
            System.out.println(p);
            total += p.pay;
            if (top == null || p.pay > top.pay) top = p;
        }
        System.out.println("Total " + total);
        if (top != null) System.out.println("top earner " + top.name);
        sc.close();
    }
}
