public abstract class Account {
	protected String type;
	private double balance;
	private double APR;

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
		if (moneyToWithdraw >= balance) {
			balance = 0;
		} else {
			balance -= moneyToWithdraw;
		}

	}

	public double getAPR() {
		return APR;
	}

	public String getType() {
		return type;
	}
}
