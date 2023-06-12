package banking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {
	CommandValidator commandValidator;
	String command;
	Bank bank;

	@BeforeEach
	public void setUp() {
		command = "";
		bank = new Bank();
		commandValidator = new CommandValidator(bank);
	}

	public void addAccounts() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new SavingsAccount(0));

		bank.deposit("00000000", 1000);
		bank.deposit("11111111", 1000);
	}

	@Test
	public void empty_string_is_invalid() {
		command = "";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void non_existent_action_is_invalid() {
		command = "delete 12345678";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void action_is_parsed_correctly() {
		command = "create savings 12345678 0.2";
		commandValidator.parse(command);
		String actual = commandValidator.getArgument1();

		assertEquals("create", actual);

	}

	// Test each type of command with a sample valid command and a sample invalid
	// command
	@Test
	public void valid_create_command_is_valid() {
		command = "create savings 12345678 0.2";
		boolean actual = commandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void invalid_create_command_is_invalid() {
		command = "create svingsa 1298381923dsd 11";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void valid_deposit_command_is_valid() {
		addAccounts();
		command = "deposit 00000000 100";
		boolean actual = commandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void invalid_deposit_command_is_invalid() {
		addAccounts();
		command = "deposit 00000000 9999999999999999999";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void valid_withdraw_command_is_valid() {
		addAccounts();
		command = "withdraw 00000000 300";
		boolean actual = commandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void invalid_withdraw_command_is_invalid() {
		addAccounts();
		command = "withdraw 00000000 999999999999999999999999";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void valid_transfer_command_is_valid() {
		addAccounts();
		command = "transfer 00000000 11111111 200";
		boolean actual = commandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void invalid_transfer_command_is_invalid() {
		addAccounts();
		command = "transfer 00000000 11111111 9999999999999999999999";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void valid_pass_command_is_valid() {
		command = "pass 12";
		boolean actual = commandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void invalid_pass_command_is_invalid() {
		command = "pass 99999999";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

}
