package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {
	CommandStorage commandStorage;
	Bank bank;
	CommandProcessor commandProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		commandStorage = new CommandStorage(bank);
		commandProcessor = new CommandProcessor(bank);
	}

	@Test
	public void command_storage_starts_with_no_commands() {
		List<String> actual = commandStorage.getInvalidCommands();

		assertTrue(actual.isEmpty());
	}

	// Invalid command storage
	@Test
	public void invalid_commands_stored_equals_commands_added() {
		commandStorage.addInvalidCommand("create cheking 123456789 1.0");
		commandStorage.addInvalidCommand("deps0T 1234 5000000 abc");

		assertEquals(2, commandStorage.getInvalidCommands().size());

	}

	@Test
	public void invalid_commands_added_are_correct() {
		commandStorage.addInvalidCommand("create cheking 123456789 1.0");
		commandStorage.addInvalidCommand("deps0T 1234 5000000 abc");

		assertEquals("create cheking 123456789 1.0", commandStorage.getInvalidCommands().get(0));
		assertEquals("deps0T 1234 5000000 abc", commandStorage.getInvalidCommands().get(1));
	}

	// Final transaction history
	@Test
	public void transaction_history_starts_empty() {
		int actual = commandStorage.getTransactionHistory().size();

		assertEquals(0, actual);
	}

	@Test
	public void transaction_history_includes_account_state() {
		bank.addAccount("12345678", new CheckingAccount(0));

		assertEquals(1, commandStorage.getTransactionHistory().size());
	}

	@Test
	public void transaction_history_includes_correct_account_states_in_order() {
		bank.addAccount("11111111", new CheckingAccount(1));
		bank.addAccount("22222222", new SavingsAccount(2));
		bank.addAccount("33333333", new CDAccount(3, 1000));

		assertEquals("Checking 11111111 0.00 1.00", commandStorage.getTransactionHistory().get(0));
		assertEquals("Savings 22222222 0.00 2.00", commandStorage.getTransactionHistory().get(1));
		assertEquals("Cd 33333333 1000.00 3.00", commandStorage.getTransactionHistory().get(2));
	}

	@Test
	public void account_state_is_removed_after_it_is_closed() {
		bank.addAccount("11111111", new CheckingAccount(1));
		bank.passTime(1);

		assertEquals(0, commandStorage.getTransactionHistory().size());
	}

	@Test
	public void order_of_account_states_is_correct_after_one_is_closed() {
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.addAccount("22222222", new SavingsAccount(0));
		bank.deposit("22222222", 300);
		bank.addAccount("33333333", new CDAccount(0, 1000));
		bank.passTime(1);

		assertEquals("Savings 22222222 300.00 0.00", commandStorage.getTransactionHistory().get(0));
		assertEquals("Cd 33333333 1000.00 0.00", commandStorage.getTransactionHistory().get(1));

	}

	@Test
	public void command_history_is_added_after_account_state() {
		commandProcessor.run("create checking 12345678 2");
		commandProcessor.run("deposit 12345678 400");

		assertEquals(2, commandStorage.getTransactionHistory().size());
	}

	@Test
	public void command_history_is_deleted_along_with_account() {
		commandProcessor.run("create checking 12345678 2");
		commandProcessor.run("withdraw 12345678 0");
		commandProcessor.run("pass 1");

		assertEquals(0, commandStorage.getTransactionHistory().size());
	}

	@Test
	public void command_history_is_in_correct_order() {
		commandProcessor.run("create checking 12345678 0");
		commandProcessor.run("deposit 12345678 400");
		commandProcessor.run("withdraw 12345678 0");

		assertEquals("Checking 12345678 400.00 0.00", commandStorage.getTransactionHistory().get(0));
		assertEquals("deposit 12345678 400", commandStorage.getTransactionHistory().get(1));
		assertEquals("withdraw 12345678 0", commandStorage.getTransactionHistory().get(2));
	}

	@Test
	public void command_history_is_in_correct_order_for_two_accounts() {
		commandProcessor.run("create checking 12345678 0");
		commandProcessor.run("create savings 11111111 0");

		commandProcessor.run("withdraw 11111111 0"); // Should be last
		commandProcessor.run("deposit 12345678 400"); // Should be first

		assertEquals("Checking 12345678 400.00 0.00", commandStorage.getTransactionHistory().get(0));
		assertEquals("deposit 12345678 400", commandStorage.getTransactionHistory().get(1));
		assertEquals("Savings 11111111 0.00 0.00", commandStorage.getTransactionHistory().get(2));
		assertEquals("withdraw 11111111 0", commandStorage.getTransactionHistory().get(3));
	}

}
