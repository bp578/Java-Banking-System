package banking;

public class CDAccount extends Account {
	public CDAccount(double APR, double balance) {
		super(APR, balance);
		type = "Cd";
	}

	@Override
	public void monthlyAprCalculation() {

		for (int i = 0; i < 4; i++) {
			double interestRate = (APR / 100) / 12;
			double interestEarned = balance * interestRate;
			interestEarned = (double) Math.round(interestEarned * 100) / 100;
			System.out.println(interestEarned);
			balance += interestEarned;
			System.out.println(balance);
		}

	}
}
