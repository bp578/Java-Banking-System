package banking;

public abstract class Account {
	private final double APR;
	protected String type;
	private double balance;
	private int age;

	// Constructor for Checking and Savings
	public Account(double APR) {
		this.APR = APR;
	}

	// Constructor for CD only
	public Account(double APR, double balance) {
		this.APR = APR;
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double moneyToDeposit) {
		balance += moneyToDeposit;
	}

	public void withdraw(double moneyToWithdraw) {
		if (moneyToWithdraw > balance) {
			balance = 0;
		} else {
			balance -= moneyToWithdraw;
		}

	}

	public void passTime(int months) {
		age += months;
		for (int i = 0; i < months; i++) {
			monthlyAprCalculation();
		}
	}

	public double getAPR() {
		return APR;
	}

	public String getType() {
		return type;
	}

	public int getAge() {
		return age;
	}

	public void monthlyAprCalculation() {
		double interestRate = (APR / 100) / 12;
		double interestEarned = balance * interestRate;
		interestEarned = (double) Math.round(interestEarned * 100) / 100;

		balance += interestEarned;
	}
}
