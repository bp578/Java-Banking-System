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

	@Test
	public void empty_string_is_invalid() {
		command = "";
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

	// Test each type of command
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

}
