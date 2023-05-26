package banking;

public class SavingsAccount extends Account {
	public SavingsAccount(double APR) {
		super(APR);
		type = "savings";
	}
}
