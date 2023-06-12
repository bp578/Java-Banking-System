package banking;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
	protected final double APR;
	private final List<String> transactionHistory = new ArrayList<>();
	protected String type;
	protected double balance;
	protected int maximumWithdrawalsPerMonth;
	protected double maximumWithdrawalAmount;
	private int age;
	private int withdrawsMadeThisMonth = 0;

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

	public double withdraw(double moneyToWithdraw) {
		double balanceToReturn;
		if (moneyToWithdraw > balance) {
			balanceToReturn = balance;
			balance = 0;
		} else {
			balanceToReturn = moneyToWithdraw;
			balance -= moneyToWithdraw;
		}
		return balanceToReturn;

	}

	// To be used when passing months on an individual account
	public void passTime(int months) {
		age += months;
		withdrawsMadeThisMonth = 0;
		for (int i = 0; i < months; i++) {
			monthlyAprCalculation();
		}
	}

	// Bank uses this in conjunction with monthlyAprCalculation()
	public void incrementAge() {
		age = age + 1;
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

	public int getWithdrawsMadeThisMonth() {
		return withdrawsMadeThisMonth;
	}

	public void resetWithdrawMadeThisMonth() {
		withdrawsMadeThisMonth = 0;
	}

	public int getMaximumWithdrawalsPerMonth() {
		return maximumWithdrawalsPerMonth;
	}

	public void increaseWithdrawalsMadeThisMonth(int val) {
		withdrawsMadeThisMonth += val;
	}

	public List<String> getTransactionHistory() {
		return transactionHistory;
	}

	public void addCommand(String command) {
		transactionHistory.add(command);
	}
}
