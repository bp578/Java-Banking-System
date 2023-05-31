package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Tests common behaviors of checking and savings accounts
public class AccountTest {
	public static final int MONEY_TO_DEPOSIT = 100;
	public static final int MONEY_TO_WITHDRAW = 25;
	public static final double APR = 3;
	Account savingsAccount;

	@BeforeEach
	public void setUp() {
		savingsAccount = new SavingsAccount(APR);
	}

	@Test
	public void APR_is_supplied_APR() {
		double actual = savingsAccount.getAPR();
		assertEquals(APR, actual);
	}

	// Testing deposit
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

	// Testing withdraw
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

	@Test
	public void withdrawing_more_than_account_balance_reduces_balance_to_zero() {
		savingsAccount.deposit(MONEY_TO_DEPOSIT);
		savingsAccount.withdraw(MONEY_TO_DEPOSIT + 1);
		double actual = savingsAccount.getBalance();

		assertEquals(0, actual);
	}

	// Testing pass time
	@Test
	public void age_starts_at_0() {
		int actual = savingsAccount.getAge();

		assertEquals(0, actual);

	}

	@Test
	public void age_increases_by_months_passed() {
		savingsAccount.passTime(1);
		int actual = savingsAccount.getAge();

		assertEquals(1, actual);

	}

	@Test
	public void APR_is_calculated_properly_after_one_month() {
		savingsAccount.deposit(1000);
		savingsAccount.passTime(1);
		double actual = savingsAccount.getBalance();

		assertEquals(1002.5, actual);

	}

	@Test
	public void APR_is_calculated_properly_after_multiple_months() {
		savingsAccount.deposit(1000);
		savingsAccount.passTime(3);
		double actual = savingsAccount.getBalance();

		assertEquals(1007.52, actual);

	}
}
