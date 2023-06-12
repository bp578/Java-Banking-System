package banking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {
	Bank bank;
	CommandProcessor commandProcessor;
	String command;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		command = "";
	}

	private void addAccounts() {
		bank.addAccount("12345678", new CheckingAccount(0));
		bank.addAccount("00000000", new CheckingAccount(0));
	}

	// Testing create command
	@Test
	public void account_created_has_correct_id() {
		command = "create savings 12345678 1.2";
		commandProcessor.run(command);
		Account actual = bank.retrieveAccount("12345678");

		assertNotNull(actual);
	}

	@Test
	public void account_created_has_correct_apr() {
		command = "create savings 12345678 1.2";
		commandProcessor.run(command);
		Double actual = bank.retrieveAccount("12345678").getAPR();

		assertEquals(1.2, actual);
	}

	@Test
	public void cd_account_starts_with_correct_balance() {
		command = "create cd 12345678 1.2 500";
		commandProcessor.run(command);

		Double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(500, actual);
	}

	@Test
	public void account_is_of_correct_type() {
		String savingsCommand = "create savings 00000000 1.2";
		String checkingCommand = "create checking 11111111 1.2";
		String cdCommand = "create cd 12345678 1.2 500";
		commandProcessor.run(savingsCommand);
		commandProcessor.run(checkingCommand);
		commandProcessor.run(cdCommand);

		assertTrue(bank.retrieveAccount("00000000") instanceof SavingsAccount);
		assertTrue(bank.retrieveAccount("11111111") instanceof CheckingAccount);
		assertTrue(bank.retrieveAccount("12345678") instanceof CDAccount);
	}

	// Testing deposit command
	@Test
	public void new_account_has_balance_equal_to_amount_deposited() {
		bank.addAccount("12345678", new CheckingAccount(2.5));
		command = "deposit 12345678 100";
		commandProcessor.run(command);
		Double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(100, actual);
	}

	@Test
	public void existing_account_balance_increases_by_amount_deposited() {
		Account account = new CheckingAccount(2.5);
		Double balanceBefore = 1000.00;
		bank.addAccount("12345678", account);
		bank.deposit("12345678", balanceBefore);

		command = "deposit 12345678 100";
		commandProcessor.run(command);
		Double balanceAfter = account.getBalance();
		Double actualDifference = balanceAfter - balanceBefore;

		assertEquals(100, actualDifference);

	}

	@Test
	public void money_is_deposited_to_correct_account() {
		bank.addAccount("12345678", new CheckingAccount(2.5));
		bank.addAccount("00000000", new SavingsAccount(0));
		commandProcessor.run("deposit 12345678 100");

		assertEquals(0, bank.retrieveAccount("00000000").getBalance());
		assertEquals(100, bank.retrieveAccount("12345678").getBalance());
	}

	// Testing withdrawal command
	@Test
	public void existing_account_balance_decreases_by_amount_withdrawn() {
		bank.addAccount("12345678", new CheckingAccount(2.5));
		bank.deposit("12345678", 400);
		double balanceBefore = bank.retrieveAccount("12345678").getBalance();
		commandProcessor.run("withdraw 12345678 50");
		double balanceAfter = bank.retrieveAccount("12345678").getBalance();

		assertEquals(-50, balanceAfter - balanceBefore);
	}

	@Test
	public void withdrawal_on_account_with_zero_balance_does_not_change_balance() {
		bank.addAccount("12345678", new CheckingAccount(2.5));
		commandProcessor.run("withdraw 12345678 50");

		assertEquals(0, bank.retrieveAccount("12345678").getBalance());
	}

	@Test
	public void withdrawal_on_account_with_existing_balance_cannot_be_negative() {
		bank.addAccount("12345678", new CheckingAccount(2.5));
		bank.deposit("12345678", 100);
		commandProcessor.run("withdraw 12345678 150");

		assertEquals(0, bank.retrieveAccount("12345678").getBalance());
	}

	@Test
	public void balance_decreases_on_correct_account() {
		addAccounts();
		bank.deposit("12345678", 400);
		bank.deposit("00000000", 100);
		double balanceBefore = bank.retrieveAccount("12345678").getBalance();
		commandProcessor.run("withdraw 12345678 50");
		double balanceAfter = bank.retrieveAccount("12345678").getBalance();

		assertEquals(-50, balanceAfter - balanceBefore);
		assertEquals(100, bank.retrieveAccount("00000000").getBalance());
	}

	// Testing transfer command
	@Test
	public void from_account_balance_decreases_by_amount_transferred() {
		addAccounts();
		bank.deposit("12345678", 400);
		commandProcessor.run("transfer 12345678 00000000 100");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(300, actual);
	}

	@Test
	public void to_account_balance_increases_by_amount_transferred() {
		addAccounts();
		bank.deposit("12345678", 400);
		bank.deposit("00000000", 400);
		commandProcessor.run("transfer 12345678 00000000 100");
		double actual = bank.retrieveAccount("00000000").getBalance();

		assertEquals(500, actual);
	}

	@Test
	public void to_account_balance_starting_at_zero_is_equal_to_amount_transferred_after_transfer() {
		addAccounts();
		bank.deposit("12345678", 400);
		commandProcessor.run("transfer 12345678 00000000 100");
		double actual = bank.retrieveAccount("00000000").getBalance();

		assertEquals(100, actual);
	}

	@Test
	public void from_account_balance_cannot_be_negative_after_transfer() {
		addAccounts();
		bank.deposit("12345678", 300);
		commandProcessor.run("transfer 12345678 00000000 400");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(0, actual);
	}

	@Test
	public void to_account_balance_increases_by_amount_actually_withdrawn() {
		addAccounts();
		bank.deposit("12345678", 300);
		commandProcessor.run("transfer 12345678 00000000 400");
		double actual = bank.retrieveAccount("00000000").getBalance();

		assertEquals(300, actual);
	}

	// Testing pass command
	@Test
	public void age_of_one_account_is_correct_after_time_passes() {
		addAccounts();
		bank.deposit("12345678", 300);
		commandProcessor.run("pass 1");
		int actual = bank.retrieveAccount("12345678").getAge();

		assertEquals(1, actual);
	}

	@Test
	public void bank_closes_account_with_zero_balance_after_one_month() {
		bank.addAccount("12345678", new CheckingAccount(0));
		commandProcessor.run("pass 1");
		int actual = bank.getAccounts().size();

		assertEquals(0, actual);
	}

	@Test
	public void bank_charges_minimum_balance_fee_after_one_month() {
		addAccounts();
		bank.deposit("12345678", 25);
		commandProcessor.run("pass 1");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(0, actual);
	}

	@Test
	public void APR_calculation_is_correct() {
		bank.addAccount("12345678", new CheckingAccount(1));
		bank.deposit("12345678", 100);
		commandProcessor.run("pass 2");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(100.16, actual);

	}

	// Testing transaction history on accounts
	@Test
	public void deposit_command_is_added_to_accounts_transaction_history() {
		bank.addAccount("12345678", new CheckingAccount(0));
		commandProcessor.run("deposit 12345678 0");
		int actual = bank.retrieveAccount("12345678").getTransactionHistory().size();

		assertEquals(1, actual);
	}

	@Test
	public void correct_deposit_command_is_added_to_accounts_transaction_history() {
		bank.addAccount("12345678", new CheckingAccount(0));
		commandProcessor.run("deposit 12345678 0");
		String actual = bank.retrieveAccount("12345678").getTransactionHistory().get(0);

		assertEquals("deposit 12345678 0", actual);
	}

	@Test
	public void withdraw_command_is_added_to_accounts_transaction_history() {
		bank.addAccount("12345678", new CheckingAccount(0));
		commandProcessor.run("withdraw 12345678 0");
		int actual = bank.retrieveAccount("12345678").getTransactionHistory().size();

		assertEquals(1, actual);
	}

	@Test
	public void correct_withdraw_command_is_added_to_accounts_transaction_history() {
		bank.addAccount("12345678", new CheckingAccount(0));
		commandProcessor.run("withdraw 12345678 0");
		String actual = bank.retrieveAccount("12345678").getTransactionHistory().get(0);

		assertEquals("withdraw 12345678 0", actual);
	}

	@Test
	public void transfer_command_is_added_to_both_accounts_transaction_history() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new CheckingAccount(0));
		commandProcessor.run("transfer 00000000 11111111 0");

		assertEquals(1, bank.retrieveAccount("00000000").getTransactionHistory().size());
		assertEquals(1, bank.retrieveAccount("11111111").getTransactionHistory().size());
	}

	@Test
	public void correct_transfer_command_is_added_to_both_accounts_transaction_history() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new CheckingAccount(0));
		commandProcessor.run("transfer 00000000 11111111 0");

		assertEquals("transfer 00000000 11111111 0", bank.retrieveAccount("00000000").getTransactionHistory().get(0));
		assertEquals("transfer 00000000 11111111 0", bank.retrieveAccount("11111111").getTransactionHistory().get(0));

	}

	@Test
	public void create_command_is_not_added_to_accounts_transaction_history() {
		commandProcessor.run("create checking 12345678 2");
		int actual = bank.retrieveAccount("12345678").getTransactionHistory().size();

		assertEquals(0, actual);
	}

	@Test
	public void pass_command_is_not_added_to_accounts_transaction_history() {
		commandProcessor.run("create cd 12345678 2 2000");
		commandProcessor.run("pass 3");
		int actual = bank.retrieveAccount("12345678").getTransactionHistory().size();

		assertEquals(0, actual);
	}

}
