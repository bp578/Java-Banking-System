package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandValidatorTest {
	String command;
	Bank bank;
	DepositCommandValidator depositCommandValidator;
	Account savingsAccount;
	Account checkingAccount;
	Account cdAccount;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		savingsAccount = new SavingsAccount(2.5);
		checkingAccount = new CheckingAccount(2.5);
		cdAccount = new CDAccount(2.5, 1000);
		depositCommandValidator = new DepositCommandValidator(bank);
		command = "";
	}

	// Test first argument (action)
	@Test
	public void missing_deposit_is_invalid() {
		bank.addAccount("12345678", savingsAccount);
		command = "12345678 500";

		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void typo_in_deposit_is_invalid() {
		bank.addAccount("12345678", savingsAccount);
		command = "dposit 12345678 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void first_argument_is_case_insensitive() {
		bank.addAccount("12345678", savingsAccount);
		command = "DePOsIt 12345678 500";
		boolean actual = depositCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Test second argument (account id)
	@Test
	public void missing_account_id_is_invalid() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void id_cannot_be_more_than_8_digits() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 123456789 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void id_cannot_be_less_than_8_digits() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 1234567 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void id_must_contain_numbers_only() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 17TEL05A-+* 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Test third argument (amount)
	@Test
	public void missing_amount_is_invalid() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_must_be_a_number() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 abc123dsa";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_can_be_0() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 0";
		boolean actual = depositCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void amount_cannot_be_negative() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 -1";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void cannot_deposit_over_2500_into_savings_account() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 2501";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_deposit_2500_into_savings_account() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 2500";
		boolean actual = depositCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_deposit_over_1000_into_checking_account() {
		bank.addAccount("12345678", checkingAccount);
		command = "deposit 12345678 1001";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_deposit_1000_into_checking_account() {
		bank.addAccount("12345678", checkingAccount);
		command = "deposit 12345678 1000";
		boolean actual = depositCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_deposit_into_cd_account() {
		bank.addAccount("12345678", cdAccount);
		command = "deposit 12345678 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);

	}

	// Simple scenarios
	@Test
	public void deposit_in_empty_bank_is_invalid() {
		command = "deposit 12345678 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void deposit_with_correct_id_is_valid() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 500";
		boolean actual = depositCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void deposit_with_incorrect_id_is_valid() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 11111111 500";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);

	}

	@Test
	public void command_can_end_with_a_space() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit 12345678 100 ";
		boolean actual = depositCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void command_cannot_start_with_a_space() {
		bank.addAccount("12345678", savingsAccount);
		command = " deposit 12345678 100";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void command_cannot_have_extra_spaces_in_the_middle() {
		bank.addAccount("12345678", savingsAccount);
		command = "deposit  12345678  100";
		boolean actual = depositCommandValidator.validate(command);

		assertFalse(actual);
	}

}
