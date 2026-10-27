
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankingSystem {

    static class InsufficientFundsException extends Exception {
        InsufficientFundsException(String msg) {
            super(msg);
        }
    }

    static class Account {
        private final int accNo;
        private final String name;
        private double balance;

        Account(int accNo, String name, double balance) {
            this.accNo = accNo;
            this.name = name;
            this.balance = balance;
        }

        int getAccNo() {
            return accNo;
        }

        String getName() {
            return name;
        }

        double getBalance() {
            return balance;
        }

        void deposit(double amt) {
            if (amt <= 0) {
                throw new IllegalArgumentException(
                    "Amount must be positive");
            }
            balance += amt;
        }

        void withdraw(double amt)
                throws InsufficientFundsException {
            if (amt <= 0) {
                throw new IllegalArgumentException(
                    "Amount must be positive");
            }
            if (amt > balance) {
                throw new InsufficientFundsException(
                    "Insufficient balance");
            }
            balance -= amt;
        }

        @Override
        public String toString() {
            return "Acc No: " + accNo
                + " | Name: " + name
                + " | Balance: " + balance;
        }
    }

    static class Bank {
        private final List<Account> accounts =
            new ArrayList<Account>();

        private int nextAccNo = 1001;

        Account createAccount(String name, double initial) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Name cannot be empty");
            }
            if (initial < 0) {
                throw new IllegalArgumentException(
                    "Initial deposit cannot be negative");
            }

            Account acc = new Account(
                nextAccNo++, name.trim(), initial);
            accounts.add(acc);
            return acc;
        }

        Account find(int accNo) {
            for (Account a : accounts) {
                if (a.getAccNo() == accNo) {
                    return a;
                }
            }
            return null;
        }

        void transfer(int from, int to, double amt)
                throws InsufficientFundsException {
            Account a = find(from);
            Account b = find(to);

            if (a == null || b == null) {
                throw new IllegalArgumentException(
                    "Account not found");
            }
            if (from == to) {
                throw new IllegalArgumentException(
                    "Cannot transfer to the same account");
            }

            a.withdraw(amt);
            b.deposit(amt);
        }

        List<Account> getAll() {
            return accounts;
        }
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(
                "\n===== BANKING MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Transfer");
            System.out.println("6. Show All Accounts");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            try {
                int choice = Integer.parseInt(
                    sc.nextLine().trim());

                switch (choice) {
                    case 1: {
                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Initial deposit: ");
                        double amt = Double.parseDouble(
                            sc.nextLine().trim());

                        Account a = bank.createAccount(
                            name, amt);
                        System.out.println("Created! " + a);
                        break;
                    }

                    case 2: {
                        Account a = getAccount(bank, sc);

                        System.out.print("Amount: ");
                        double amt = Double.parseDouble(
                            sc.nextLine().trim());

                        a.deposit(amt);
                        System.out.println(
                            "Deposited. New balance: "
                            + a.getBalance());
                        break;
                    }

                    case 3: {
                        Account a = getAccount(bank, sc);

                        System.out.print("Amount: ");
                        double amt = Double.parseDouble(
                            sc.nextLine().trim());

                        a.withdraw(amt);
                        System.out.println(
                            "Withdrawn. New balance: "
                            + a.getBalance());
                        break;
                    }

                    case 4:
                        System.out.println(
                            getAccount(bank, sc));
                        break;

                    case 5: {
                        System.out.print("From Acc No: ");
                        int from = Integer.parseInt(
                            sc.nextLine().trim());

                        System.out.print("To Acc No: ");
                        int to = Integer.parseInt(
                            sc.nextLine().trim());

                        System.out.print("Amount: ");
                        double amt = Double.parseDouble(
                            sc.nextLine().trim());

                        bank.transfer(from, to, amt);
                        System.out.println(
                            "Transfer successful.");
                        break;
                    }

                    case 6:
                        if (bank.getAll().isEmpty()) {
                            System.out.println(
                                "No accounts yet.");
                        } else {
                            for (Account a : bank.getAll()) {
                                System.out.println(a);
                            }
                        }
                        break;

                    case 7:
                        System.out.println("Thank you!");
                        sc.close();
                        return;

                    default:
                        System.out.println(
                            "Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                    "Please enter a valid number.");

            } catch (InsufficientFundsException e) {
                System.out.println(
                    "Error: " + e.getMessage());

            } catch (IllegalArgumentException e) {
                System.out.println(
                    "Error: " + e.getMessage());
            }
        }
    }

    private static Account getAccount(
            Bank bank, Scanner sc) {
        System.out.print("Account No: ");

        int no = Integer.parseInt(sc.nextLine().trim());
        Account a = bank.find(no);

        if (a == null) {
            throw new IllegalArgumentException(
                "Account not found");
        }

        return a;
    }
}
