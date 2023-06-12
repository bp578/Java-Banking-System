package banking;

public class SavingsAccount extends Account {

	public SavingsAccount(double APR) {
		super(APR);
		type = "Savings";
		maximumWithdrawalsPerMonth = 1;
		maximumWithdrawalAmount = 1000;
	}

}
