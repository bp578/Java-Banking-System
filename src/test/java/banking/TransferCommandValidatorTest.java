package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferCommandValidatorTest {
	TransferCommandValidator transferCommandValidator;
	Bank bank;
	String command;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		command = "";
		transferCommandValidator = new TransferCommandValidator(bank);
	}

	// Tests dealing with first argument (action)
	@Test
	public void missing_transfer_is_invalid() {
		command = "00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void first_argument_must_be_transfer() {
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void typo_in_transfer_is_invalid() {
		command = "trainzf3r 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void transfer_is_case_insensitive() {
		command = "TrAnSfeR 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}
}
