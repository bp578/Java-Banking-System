package banking;

public class CDAccount extends Account {
	public CDAccount(double APR, double balance) {
		super(APR, balance);
		type = "cd";
	}
}
