package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CheckingAccountTest {
	@Test
	public void checking_account_starts_with_zero_balance() {
		CheckingAccount checkingAccount = new CheckingAccount(2.5);

		assertEquals(0, checkingAccount.getBalance());
	}
}
