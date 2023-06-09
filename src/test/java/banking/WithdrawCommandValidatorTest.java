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
	public void can_withdraw_more_than_account_balance() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		bank.deposit("12345678", 1);
		command = "withdraw 12345678 100";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Savings account testing
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
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_withdraw_less_than_1000_from_savings_account() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		command = "withdraw 12345678 999.99";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_withdraw_from_savings_account_more_than_once_per_month() {
		bank.addAccount("12345678", new SavingsAccount(2.5));
		bank.deposit("12345678", 1000);
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
		bank.addAccount("12345678", new SavingsAccount(0));
		bank.deposit("12345678", 500);
		bank.withdraw("12345678", 100);
		command = "withdraw 12345678 500";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_still_withdraw_after_minimum_balance_fee() {
		bank.addAccount("12345678", new SavingsAccount(0));
		bank.deposit("12345678", 50);
		bank.passTime(1);
		command = "withdraw 12345678 100";

		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Checking account testing
	@Test
	public void cannot_withdraw_more_than_400_from_checking_account() {
		bank.addAccount("12345678", new CheckingAccount(0));
		command = "withdraw 12345678 400.01";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);

	}

	@Test
	public void can_withdraw_exactly_400_from_checking_account() {
		bank.addAccount("12345678", new CheckingAccount(0));
		command = "withdraw 12345678 400";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_withdraw_less_than_400_from_checking_account() {
		bank.addAccount("12345678", new CheckingAccount(0));
		command = "withdraw 12345678 399.99";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_withdraw_multiple_times_per_month_from_checking_account() {
		bank.addAccount("12345678", new CheckingAccount(0));
		bank.deposit("12345678", 1000);
		bank.withdraw("12345678", 100);
		bank.withdraw("12345678", 100);
		bank.withdraw("12345678", 100);
		command = "withdraw 12345678 100";

		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	// CD account testing
	@Test
	public void cannot_withdraw_from_new_cd_account() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void cannot_withdraw_from_cd_account_after_less_than_12_months() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(11);
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_withdraw_from_cd_account_after_exactly_12_months() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(12);
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_withdraw_from_cd_account_after_more_than_12_months() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(13);
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void amount_withdrawn_cannot_be_less_than_balance() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(12);
		command = "withdraw 12345678 999.99";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_withdrawn_can_be_equal_to_balance() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(12);
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void amount_withdrawn_can_be_greater_than_balance() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(12);
		command = "withdraw 12345678 1001";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_withdraw_from_cd_account_more_than_once() {
		bank.addAccount("12345678", new CDAccount(0, 1000));
		bank.passTime(12);
		bank.withdraw("12345678", 1000);
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Simple scenarios
	@Test
	public void withdrawing_from_empty_bank_is_invalid() {
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void withdrawing_using_correct_id_is_valid() {
		bank.addAccount("12345678", new SavingsAccount(0));
		command = "withdraw 12345678 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void withdrawing_using_incorrect_id_is_invalid() {
		bank.addAccount("12345678", new SavingsAccount(0));
		command = "withdraw 00000000 1000";
		boolean actual = withdrawCommandValidator.validate(command);

		assertFalse(actual);
	}

}
