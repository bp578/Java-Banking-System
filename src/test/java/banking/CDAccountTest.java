package banking;

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

	@Test
	public void apr_calculation_is_correct_after_one_month() {
		CDAccount CDAccount = new CDAccount(2.1, 2000);
		CDAccount.passTime(1);
		double actual = CDAccount.getBalance();

		assertEquals(2014.04, actual);
	}

	@Test
	public void apr_calculation_is_correct_after_multiple_months() {
		CDAccount CDAccount = new CDAccount(2.1, 2000);
		CDAccount.passTime(2);
		double actual = CDAccount.getBalance();

		assertEquals(2028.1699999999998, actual);
	}
}
