import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CDAccountTest {
	public static final double APR = 2.5;
	public static final double DEFAULT_BALANCE = 100;

	@Test
	public void balance_is_value_supplied() {
		CDAccount CDAccount = new CDAccount(APR, DEFAULT_BALANCE);
		double actual = CDAccount.getBalance();

		assertEquals(DEFAULT_BALANCE, actual);
	}
}
