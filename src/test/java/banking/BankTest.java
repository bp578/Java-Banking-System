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
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
	}

	@Test
	public void bank_starts_with_no_accounts() {
		assertTrue(bank.getAccounts().isEmpty());
	}

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

	@Test
	public void correct_account_retrieved() {
		Account account = new CheckingAccount(APR);
		bank.addAccount(ACCOUNT_ID_1, account);
		assertEquals(account, bank.retrieveAccount(ACCOUNT_ID_1));
	}

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

}
