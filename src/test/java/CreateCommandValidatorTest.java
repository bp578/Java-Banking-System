import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandValidatorTest {
	String command;
	CreateCommandValidator createCommandValidator;

	@BeforeEach
	public void setUp() {
		command = "";
		createCommandValidator = new CreateCommandValidator();
	}

	@Test
	public void empty_string_is_invalid() {
		command = "";
		boolean actual = createCommandValidator.validate(command);

		assertFalse(actual);
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
		String cdCommand = "create cd 12345678 0.1";

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

}
