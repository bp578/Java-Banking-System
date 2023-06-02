package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {
	public static final double APR = 2.5;
	public static final String ACCOUNT_ID_1 = "12345678";
	public static final String ACCOUNT_ID_2 = "87654321";
	public static final double MONEY_TO_DEPOSIT = 1000;
	public static final double MONEY_TO_WITHDRAW = 50;
	public static final double DEFAULT_CD_BALANCE = 1000;
	public static final int MONTHS_TO_PASS = 5;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
	}

	@Test
	public void bank_starts_with_no_accounts() {
		assertTrue(bank.getAccounts().isEmpty());
	}

	// Testing addAccount()
	@Test
	public void bank_has_one_account_when_an_account_is_added() {
		bank.addAccount(ACCOUNT_ID_1, new CheckingAccount(APR));
		assertEquals(1, bank.getAccounts().size());
	}

	@Test
	public void bank_has_two_accounts_when_two_accounts_are_added() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.addAccount(ACCOUNT_ID_2, new CDAccount(APR, DEFAULT_CD_BALANCE));
		assertEquals(2, bank.getAccounts().size());
	}

	// Testing retrieveAccount()
	@Test
	public void correct_account_retrieved() {
		Account account = new CheckingAccount(APR);
		bank.addAccount(ACCOUNT_ID_1, account);
		assertEquals(account, bank.retrieveAccount(ACCOUNT_ID_1));
	}

	// Testing deposit
	@Test
	public void deposit_on_correct_account() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.deposit(ACCOUNT_ID_1, MONEY_TO_DEPOSIT);

		double actualBalance = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(MONEY_TO_DEPOSIT, actualBalance);

	}

	@Test
	public void depositing_twice_works_as_expected() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.deposit(ACCOUNT_ID_1, MONEY_TO_DEPOSIT);
		bank.deposit(ACCOUNT_ID_1, MONEY_TO_DEPOSIT);

		double actualBalance = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(MONEY_TO_DEPOSIT * 2, actualBalance);
	}

	// Testing withdraw
	@Test
	public void withdraw_on_correct_account() {
		bank.addAccount(ACCOUNT_ID_1, new CDAccount(APR, DEFAULT_CD_BALANCE));
		bank.withdraw(ACCOUNT_ID_1, MONEY_TO_WITHDRAW);

		double actualBalance = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		// Take into account if money withdrawn is greater than balance
		if (MONEY_TO_WITHDRAW >= DEFAULT_CD_BALANCE) {
			assertEquals(0, actualBalance);
		} else {
			assertEquals(DEFAULT_CD_BALANCE - MONEY_TO_WITHDRAW, actualBalance);
		}

	}

	@Test
	public void withdrawing_twice_works_as_expected() {
		bank.addAccount(ACCOUNT_ID_1, new CDAccount(APR, DEFAULT_CD_BALANCE));
		bank.withdraw(ACCOUNT_ID_1, MONEY_TO_WITHDRAW);
		bank.withdraw(ACCOUNT_ID_1, MONEY_TO_WITHDRAW);

		double actualBalance = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		// Take into account if money withdrawn is greater than balance
		if (MONEY_TO_WITHDRAW * 2 >= DEFAULT_CD_BALANCE) {
			assertEquals(0, actualBalance);
		} else {
			assertEquals(DEFAULT_CD_BALANCE - (MONEY_TO_WITHDRAW * 2), actualBalance);
		}

	}

	// Testing pass time
	@Test
	public void age_of_one_account_is_correct_after_time_passes() {
		bank.addAccount(ACCOUNT_ID_1, new CDAccount(APR, DEFAULT_CD_BALANCE));
		bank.passTime(MONTHS_TO_PASS);
		int actual = bank.retrieveAccount(ACCOUNT_ID_1).getAge();

		assertEquals(MONTHS_TO_PASS, actual);
	}

	@Test
	public void new_account_age_should_be_zero_when_added_after_time_passes() {
		bank.passTime(MONTHS_TO_PASS);
		bank.addAccount(ACCOUNT_ID_1, new CDAccount(APR, DEFAULT_CD_BALANCE));
		int actual = bank.retrieveAccount(ACCOUNT_ID_1).getAge();

		assertEquals(0, actual);
	}

	@Test
	public void older_account_has_higher_age_than_new_account_after_time_passes() {
		bank.addAccount(ACCOUNT_ID_1, new CDAccount(APR, DEFAULT_CD_BALANCE));
		bank.passTime(MONTHS_TO_PASS);
		bank.addAccount(ACCOUNT_ID_2, new CDAccount(APR, DEFAULT_CD_BALANCE));
		int olderAccountAge = bank.retrieveAccount(ACCOUNT_ID_1).getAge();
		int newerAccountAge = bank.retrieveAccount(ACCOUNT_ID_2).getAge();

		assertTrue(olderAccountAge > newerAccountAge);

	}

	@Test
	public void passing_time_by_one_month_properly_calculates_apr_for_one_account() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(1));
		bank.deposit(ACCOUNT_ID_1, 100);
		bank.passTime(1);

		double actual = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(100.08, actual);
	}

	@Test
	public void passing_time_by_multiple_months_properly_calculates_apr_for_one_account() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(1));
		bank.deposit(ACCOUNT_ID_1, 100);
		bank.passTime(2);

		double actual = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(100.16, actual);
	}

	@Test
	public void bank_closes_account_with_zero_balance_after_one_month() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.passTime(1);
		int actual = bank.getAccounts().size();

		assertEquals(0, actual);
	}

	@Test
	public void bank_closes_two_accounts_with_zero_balance_after_one_month() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.addAccount(ACCOUNT_ID_2, new SavingsAccount(APR));
		bank.passTime(1);
		int actual = bank.getAccounts().size();

		assertEquals(0, actual);
	}

	@Test
	public void bank_closes_one_account_with_zero_balance_but_leave_other_accounts_with_more_balance() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.addAccount(ACCOUNT_ID_2, new CDAccount(APR, DEFAULT_CD_BALANCE));
		bank.passTime(1);
		int actual = bank.getAccounts().size();

		assertEquals(1, actual);
	}

	@Test
	public void bank_deducts_25_from_account_with_less_than_100_balance() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(0));
		bank.deposit(ACCOUNT_ID_1, 99);
		bank.passTime(1);
		double actual = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(74, actual);
	}

	@Test
	public void bank_does_not_deduct_25_from_account_with_exactly_100_balance() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(0));
		bank.deposit(ACCOUNT_ID_1, 100);
		bank.passTime(1);
		double actual = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(100, actual);
	}

	@Test
	public void bank_charges_minimum_balance_fee_from_correct_account() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(0));
		bank.deposit(ACCOUNT_ID_1, 50);
		bank.addAccount(ACCOUNT_ID_2, new CDAccount(0, DEFAULT_CD_BALANCE));
		bank.passTime(1);
		double savingsBalance = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();
		double cdBalance = bank.retrieveAccount(ACCOUNT_ID_2).getBalance();

		assertEquals(25, savingsBalance);
		assertEquals(DEFAULT_CD_BALANCE, cdBalance);
	}

	@Test
	public void bank_eventually_closes_account_with_less_than_100_balance() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(0));
		bank.deposit(ACCOUNT_ID_1, 99);
		bank.passTime(5);
		int actual = bank.getAccounts().size();

		assertEquals(0, actual);
	}

	@Test
	public void balance_cannot_be_negative_from_minimum_balance_fees() {
		bank.addAccount(ACCOUNT_ID_1, new SavingsAccount(APR));
		bank.deposit(ACCOUNT_ID_1, 24);
		bank.passTime(1);
		double actual = bank.retrieveAccount(ACCOUNT_ID_1).getBalance();

		assertEquals(0, actual);
	}

}
