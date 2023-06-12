package banking;

public class CheckingAccount extends Account {
	public CheckingAccount(double APR) {
		super(APR);
		type = "Checking";
		maximumWithdrawalAmount = 400;
	}
}
