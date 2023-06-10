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

	public void addAccounts() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new CheckingAccount(0));
	}

	// Tests dealing with first argument (action)
	@Test
	public void missing_transfer_is_invalid() {
		addAccounts();
		command = "00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void first_argument_must_be_transfer() {
		addAccounts();
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void typo_in_transfer_is_invalid() {
		addAccounts();
		command = "trainzf3r 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void transfer_is_case_insensitive() {
		addAccounts();
		command = "TrAnSfeR 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Tests dealing with second argument (from ID)
	@Test
	public void missing_from_id_is_invalid() {
		addAccounts();
		command = "transfer 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void from_id_must_exist_in_bank() {
		bank = new Bank();
		bank.addAccount("11111111", new CheckingAccount(0));
		command = "transfer 22222222 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void from_id_can_be_checking() {
		addAccounts(); // Both accounts are checking by default
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void from_id_can_be_savings() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.addAccount("11111111", new CheckingAccount(0));
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void from_id_cannot_be_cd() {
		bank.addAccount("00000000", new CDAccount(0, 1000));
		bank.addAccount("11111111", new CheckingAccount(0));
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Tests dealing with third argument (to ID)

}
