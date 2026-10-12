import java.util.*;

public class Problem1_BankAccountWithdrawalSystem {
    static class Account {
        String id;
        int balance;
        Account(String id, int balance) { this.id = id; this.balance = balance; }
        String withdraw(int amount) {
            return "Withdrawal is not supported";
        }
    }

    static class Savings extends Account {
        Savings(String id, int balance) { super(id, balance); }
        @Override String withdraw(int amount) {
            if (balance - amount < 1000) return id + " rejected: minimum balance 1000";
            balance -= amount;
            return id + " balance " + balance;
        }
    }

    static class Current extends Account {
        Current(String id, int balance) { super(id, balance); }
        @Override String withdraw(int amount) {
            if (balance - amount < -5000) return id + " rejected: overdraft limit 5000";
            balance -= amount;
            return id + " balance " + balance;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Account> accounts = new LinkedHashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            try {
                if (p[0].equalsIgnoreCase("Savings") && p.length >= 3) {
                    accounts.put(p[1], new Savings(p[1], Integer.parseInt(p[2])));
                } else if (p[0].equalsIgnoreCase("Current") && p.length >= 3) {
                    accounts.put(p[1], new Current(p[1], Integer.parseInt(p[2])));
                } else if (p[0].equalsIgnoreCase("WITHDRAW") && p.length >= 3) {
                    Account a = accounts.get(p[1]);
                    if (a == null) System.out.println("Account not found");
                    else System.out.println(a.withdraw(Integer.parseInt(p[2])));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount");
            }
        }
        sc.close();
    }
}
