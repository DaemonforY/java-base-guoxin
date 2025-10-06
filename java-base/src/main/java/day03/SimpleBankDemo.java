/*
 * 3. 线程同步与安全
 * 模拟银行账户转账，使用同步方法确保数据一致性
 * 初始余额1000， 10次存款 10元， 10次取款10元
 * 使用了synchronized， 最终余额是 1000
 */


package day03;

public class SimpleBankDemo {

    static class BankAccount {
        private int balance;

        public BankAccount(int initialBalance) {
            this.balance = initialBalance;
        }

        public synchronized void deposit(int amount) {
            balance += amount;
        }

        public synchronized void withdraw(int amount) {
            balance -= amount;
        }

        public synchronized int getBalance() {
            return balance;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(1000);

        Thread depositor = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                account.deposit(10);
            }
        }, "depositor"
        );

        Thread withdrawer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                account.withdraw(10);
            }
        }, "withdrawer"
        );

        depositor.start();
        withdrawer.start();

        depositor.join();
        withdrawer.join();

        System.out.println("最终余额：" + account.getBalance());
    }
}


