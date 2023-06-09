package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandValidatorTest {
	String command;
	WithdrawCommandValidator withdrawCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		command = "";
		bank = new Bank();
		withdrawCommandValidator = new WithdrawCommandValidator(bank);

	}

	// Tests dealing with the first argument (action)
	@Test
	public void missing_withdraw_is_invalid() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "12345678 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void first_argument_must_be_withdraw() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void typo_in_withdraw_is_invalid() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "wihtdrw 12345678 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void withdraw_is_case_insensitive() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "wItHdrAw 12345678 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Tests dealing with the second argument (account ID)
	@Test
	public void missing_ID_is_invalid() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void ID_must_be_a_number() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw abc123!* 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void ID_cannot_be_less_than_8_characters() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 1234567 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void ID_cannot_be_more_than_8_characters() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 123456789 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Tests dealing with the 3rd argument (amount)
	@Test
	public void missing_amount_is_invalid() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_must_be_a_number() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 abc";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_can_be_decimal() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 100.55";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void amount_cannot_be_negative() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 -1";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_can_be_zero() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 0";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_withdraw_more_than_1000_from_savings_account() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 1000.1";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_withdraw_exactly_1000_from_savings_account() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 1000.1";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void cannot_withdraw_from_savings_account_more_than_once_per_month() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		bank.withdraw("12345678", 100);
		command = "withdraw 12345678 500";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_withdraw_from_savings_account_after_one_month_has_passed() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		bank.deposit("12345678", 500);
		bank.withdraw("12345678", 100);
		bank.passTime(1);
		command = "withdraw 12345678 500";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_withdraw_from_savings_account_after_many_months_has_passed() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		bank.deposit("12345678", 500);
		bank.withdraw("12345678", 100);
		bank.passTime(60);
		command = "withdraw 12345678 500";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_withdraw_twice_from_new_savings_account_after_time_has_already_passed() {
		bank.passTime(1);
		bank.addAccount("12345678", new SavingsAccount(2.5));
		bank.deposit("12345678", 500);
		bank.withdraw("12345678", 100);
		command = "withdraw 12345678 500";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

}
