package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandValidatorTest {
	PassTimeCommandValidator passCommandValidator;
	Bank bank;
	String command;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		passCommandValidator = new PassTimeCommandValidator(bank);
		command = "";
	}

	// Testing first argument (action)
	@Test
	public void missing_pass_is_invalid() {
		command = "12";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);

	}

	@Test
	public void pass_is_case_insensitive() {
		command = "pAsS 12";
		boolean actual = passCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void typo_in_pass_is_invalid() {
		command = "pas 12";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Testing second argument (time)
	@Test
	public void missing_time_is_invalid() {
		command = "pass";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void time_must_be_a_number() {
		command = "pass two";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void time_cannot_be_a_double() {
		command = "pass 5.7";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void time_cannot_be_0() {
		command = "pass 0";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void time_cannot_be_more_than_60() {
		command = "pass 61";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void time_can_be_60() {
		command = "pass 60";
		boolean actual = passCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void time_can_be_between_1_and_60() {
		command = "pass 1";
		boolean actual = passCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void more_than_two_arguments_is_invalid() {
		command = "pass 1 2";
		boolean actual = passCommandValidator.validate(command);

		assertFalse(actual);
	}

}
