import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private final int accountNumber;
    private double balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
            System.out.println("Пополнение " + amount + " на счет " + accountNumber + ". Новый баланс: " + balance);
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                System.out.println("Снятие " + amount + " с счета " + accountNumber + ". Новый баланс: " + balance);
                return true;
            } else {
                System.out.println("Недостаточно средств на счете " + accountNumber);
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Lock getLock() {
        return lock;
    }
}

class ConcurrentBank {
    private final ConcurrentHashMap<Integer, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(int accountNumber, double initialBalance) {
        BankAccount account = new BankAccount(accountNumber, initialBalance);
        accounts.put(accountNumber, account);
        return account;
    }

    public boolean transfer(int fromAccount, int toAccount, double amount) {
        if (!accounts.containsKey(fromAccount) || !accounts.containsKey(toAccount) || fromAccount == toAccount) {
            return false;
        }

        BankAccount acc1 = accounts.get(fromAccount);
        BankAccount acc2 = accounts.get(toAccount);

        // Блокировка в порядке возрастания номера счета для избежания deadlock
        Lock firstLock = acc1.getAccountNumber() < acc2.getAccountNumber() ? acc1.getLock() : acc2.getLock();
        Lock secondLock = acc1.getAccountNumber() < acc2.getAccountNumber() ? acc2.getLock() : acc1.getLock();

        firstLock.lock();
        secondLock.lock();
        try {
            if (acc1.withdraw(amount)) {
                acc2.deposit(amount);
                System.out.println("Перевод " + amount + " со счета " + fromAccount + " на счет " + toAccount);
                return true;
            }
            return false;
        } finally {
            secondLock.unlock();
            firstLock.unlock();
        }
    }

    public double getTotalBalance() {
        return accounts.values().stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
    }
}

public class BankSimulation {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();
        bank.createAccount(1, 1000);
        bank.createAccount(2, 2000);

        Thread t1 = new Thread(() -> bank.transfer(1, 2, 500));
        Thread t2 = new Thread(() -> bank.transfer(2, 1, 300));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Общий баланс: " + bank.getTotalBalance());
    }
}