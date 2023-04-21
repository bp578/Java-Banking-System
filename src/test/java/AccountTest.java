import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Tests common behaviors of checking and savings accounts
public class AccountTest {
	public static final int MONEY_TO_DEPOSIT = 100;
	public static final int MONEY_TO_WITHDRAW = 25;
	public static final double APR = 2.5;
	Account savingsAccount;
	Account checkingAccount;

	@BeforeEach
	public void setUp() {
		savingsAccount = new SavingsAccount(APR);
		checkingAccount = new CheckingAccount(APR);
	}

	@Test
	public void checking_and_savings_account_starts_with_0_balance() {
		double actualSavingsBalance = savingsAccount.getBalance();
		double actualCheckingBalance = checkingAccount.getBalance();

		assertEquals(0, actualSavingsBalance);
		assertEquals(0, actualCheckingBalance);
	}

	@Test
	public void APR_is_supplied_APR() {
		double actual = savingsAccount.getAPR();
		assertEquals(APR, actual);
	}

	@Test
	public void balance_increases_by_amount_deposited() {
		double balanceBefore = savingsAccount.getBalance();
		savingsAccount.deposit(MONEY_TO_DEPOSIT);

		double balanceAfter = savingsAccount.getBalance();
		double actual = balanceAfter - balanceBefore;

		assertEquals(MONEY_TO_DEPOSIT, actual);

	}

	@Test
	public void depositing_twice_increases_balance_properly() {
		double balanceBefore = savingsAccount.getBalance();
		savingsAccount.deposit(MONEY_TO_DEPOSIT);
		savingsAccount.deposit(MONEY_TO_DEPOSIT);

		double balanceAfter = savingsAccount.getBalance();
		double actual = balanceAfter - balanceBefore;

		assertEquals(MONEY_TO_DEPOSIT * 2, actual);
	}

	@Test
	public void balance_decreases_by_amount_withdrawn() {
		savingsAccount.deposit(MONEY_TO_DEPOSIT);
		double balanceBefore = savingsAccount.getBalance();
		savingsAccount.withdraw(MONEY_TO_WITHDRAW);

		double balanceAfter = savingsAccount.getBalance();
		double actual = balanceBefore - balanceAfter;

		assertEquals(MONEY_TO_WITHDRAW, actual);
	}

	@Test
	public void withdrawing_twice_decreases_balance_properly() {
		savingsAccount.deposit(MONEY_TO_DEPOSIT);
		double balanceBefore = savingsAccount.getBalance();
		savingsAccount.withdraw(MONEY_TO_WITHDRAW);
		savingsAccount.withdraw(MONEY_TO_WITHDRAW);

		double balanceAfter = savingsAccount.getBalance();
		double actual = balanceBefore - balanceAfter;

		assertEquals(MONEY_TO_WITHDRAW * 2, actual);

	}

	@Test
	public void withdrawing_cannot_reduce_balance_below_zero() {
		savingsAccount.withdraw(MONEY_TO_WITHDRAW);
		double actual = savingsAccount.getBalance();

		assertEquals(0, actual);
	}

}
