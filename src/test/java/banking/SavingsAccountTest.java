package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SavingsAccountTest {
	@Test
	public void savings_account_starts_with_zero_balance() {
		SavingsAccount savingsAccount = new SavingsAccount(2.5);

		assertEquals(0, savingsAccount.getBalance());
	}
}
