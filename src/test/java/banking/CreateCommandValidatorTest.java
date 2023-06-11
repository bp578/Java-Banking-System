package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandValidatorTest {
	String command;
	CreateCommandValidator createCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		command = "";
		bank = new Bank();
		createCommandValidator = new CreateCommandValidator(bank);
	}

	// Tests dealing with the first argument (action)
	@Test
	public void missing_create_is_invalid() {
		command = "savings 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void first_argument_must_be_create() {
		command = "create savings 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void first_argument_is_case_insensitive() {
		command = "CrEAtE savings 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void typo_in_first_argument_is_invalid() {
		command = "creta savings 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Tests dealing with the second argument (account type)
	@Test
	public void missing_account_type_is_invalid() {
		command = "create 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void second_argument_must_be_an_account_type() {
		String savingsCommand = "create savings 12345678 0.1";
		String checkingCommand = "create checking 12345678 0.1";
		String cdCommand = "create cd 12345678 0.1 500";

		assertTrue(createCommandValidator.validate(savingsCommand));
		assertTrue(createCommandValidator.validate(checkingCommand));
		assertTrue(createCommandValidator.validate(cdCommand));

	}

	@Test
	public void second_argument_is_case_insensitive() {
		String command = "create SaVinGs 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void typo_in_second_argument_is_invalid() {
		String command = "create svngs 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);

	}

	// Tests dealing with the 3rd argument (account ID)
	@Test
	public void missing_ID_is_invalid() {
		String command = "create savings 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void third_argument_must_be_an_ID() {
		String command = "create savings 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void ID_must_be_a_number() {
		String command = "create savings 12EA56T8 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void ID_cannot_be_less_than_8_characters() {
		String command = "create savings 1234567 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void ID_cannot_be_more_than_8_characters() {
		String command = "create savings 123456789 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Tests dealing with the fourth argument (APR)
	@Test
	public void missing_APR_is_invalid() {
		String command = "create savings 12345678";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void fourth_argument_must_be_APR() {
		String command = "create savings 12345678 0.1";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void APR_must_be_a_number() {
		String command = "create savings 12345678 abc";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void APR_can_be_int() {
		String command = "create savings 12345678 5";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void APR_can_be_0() {
		String command = "create savings 12345678 0";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void APR_cannot_be_less_than_0() {
		String command = "create savings 12345678 -1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void APR_cannot_be_greater_than_10() {
		String command = "create savings 12345678 11";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Tests dealing with the fifth argument (balance) CD accounts only
	@Test
	public void missing_balance_is_invalid() {
		String command = "create cd 12345678 1.2";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void adding_balance_to_cd_account_is_valid() {
		String command = "create cd 12345678 1.2 500";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void balance_must_be_a_number() {
		String command = "create cd 12345678 1.2 abc";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void adding_balance_to_savings_or_checking_account_is_invalid() {
		String savingsCommand = "create savings 12345678 1.2 500";
		String checkingCommand = "create checking 12345678 1.2 500";

		assertFalse(createCommandValidator.validate(savingsCommand));
		assertFalse(createCommandValidator.validate(checkingCommand));
	}

	@Test
	public void balance_cannot_be_0() {
		String command = "create cd 12345678 1.2 0";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void balance_cannot_be_negative() {
		String command = "create cd 12345678 1.2 -1";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void balance_cannot_be_greater_than_10000() {
		String command = "create cd 12345678 1.2 10001";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Simple scenarios
	@Test
	public void creating_account_in_empty_bank_is_valid() {
		String command = "create savings 12345678 1.2";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void creating_account_with_unique_id_is_valid() {
		String command = "create savings 12345678 1.2";
		Account otherAccount = new SavingsAccount(1.5);
		bank.addAccount("11111111", otherAccount);
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void creating_account_with_same_id_as_another_account_is_invalid() {
		String command = "create savings 12345678 1.2";
		Account otherAccount = new SavingsAccount(1.5);
		bank.addAccount("12345678", otherAccount);
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);

	}

	@Test
	public void can_create_account_with_same_id_as_a_previous_account_if_previous_account_closed() {
		bank.addAccount("12345678", new CheckingAccount(0));
		bank.passTime(1);
		String command = "create savings 12345678 1.2";
		boolean actual = createCommandValidator.validate(command);

		assertTrue(actual);
	}

}
