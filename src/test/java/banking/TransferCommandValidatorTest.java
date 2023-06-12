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
	@Test
	public void missing_to_id_is_invalid() {
		addAccounts();
		command = "transfer 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void to_id_must_exist_in_bank() {
		bank = new Bank();
		bank.addAccount("00000000", new CheckingAccount(0));
		command = "transfer 00000000 22222222 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void to_id_can_be_checking() {
		addAccounts(); // Both accounts are checking by default
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void to_id_can_be_savings() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new SavingsAccount(0));
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void to_id_cannot_be_cd() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new CDAccount(0, 1000));
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Tests dealing with fourth argument (amount)
	@Test
	public void missing_amount_is_invalid() {
		addAccounts();
		command = "transfer 00000000 11111111";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_must_be_a_number() {
		addAccounts();
		command = "transfer 00000000 11111111 abc123!*";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_can_be_decimal() {
		addAccounts();
		command = "transfer 00000000 11111111 100.55";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void amount_cannot_be_negative() {
		addAccounts();
		command = "transfer 00000000 11111111 -1";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void amount_can_be_zero() {
		addAccounts();
		command = "transfer 00000000 11111111 0";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_transfer_from_account_with_zero_balance() {
		addAccounts();
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void amount_can_be_greater_than_from_account_balance() {
		addAccounts();
		bank.deposit("00000000", 100);
		command = "transfer 00000000 11111111 150";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	// Make sure all deposit and withdrawal rules apply

	// Testing all withdrawal rules on from account
	@Test
	public void cannot_transfer_from_savings_account_more_than_once_per_month() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 1000);
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.transfer("00000000", "11111111", 100);
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void cannot_transfer_from_savings_account_if_a_withdrawal_was_made_in_the_same_month() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 1000);
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.withdraw("00000000", 100);
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_transfer_from_savings_account_again_after_one_month() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 1000);
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.transfer("00000000", "11111111", 100);
		bank.passTime(1);
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void can_transfer_from_savings_account_again_after_multiple_months() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 1000);
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.transfer("00000000", "11111111", 100);
		bank.passTime(2);
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void cannot_transfer_from_savings_account_twice_if_time_passes_before_account_has_created() {
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.deposit("11111111", 100);
		bank.passTime(1);
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 1000);
		bank.transfer("00000000", "11111111", 100);
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_still_transfer_from_savings_account_after_minimum_balance_fee() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 50);
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.deposit("11111111", 100);
		bank.passTime(1);
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_transfer_more_than_1000_from_savings_account() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.deposit("11111111", 2000);
		command = "transfer 00000000 11111111 1000.01";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void can_transfer_exactly_1000_from_savings_account() {
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.deposit("11111111", 2000);
		bank.addAccount("00000000", new SavingsAccount(0));
		command = "transfer 00000000 11111111 1000";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void can_transfer_less_than_1000_from_savings_account() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.deposit("11111111", 2000);
		command = "transfer 00000000 11111111 999";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void cannot_transfer_more_than_400_from_checking_account() {
		addAccounts(); // Both accounts are checking by default
		bank.deposit("00000000", 1000);
		command = "transfer 00000000 11111111 400.001";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);

	}

	@Test
	public void can_transfer_less_than_400_from_checking_account() {
		addAccounts(); // Both accounts are checking by default
		bank.deposit("00000000", 1000);
		command = "transfer 00000000 11111111 399.99";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void can_transfer_exactly_400_from_checking_account() {
		addAccounts(); // Both accounts are checking by default
		bank.deposit("00000000", 1000);
		command = "transfer 00000000 11111111 400";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void can_transfer_multiple_times_per_month_from_checking_account() {
		addAccounts(); // Both accounts are checking by default
		bank.deposit("00000000", 1000);
		bank.transfer("00000000", "11111111", 100);
		bank.transfer("00000000", "11111111", 100);
		command = "transfer 00000000 11111111 400";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);

	}

	@Test
	public void cannot_transfer_from_cd_account_even_if_withdrawing_from_it_is_valid() {
		WithdrawCommandValidator withdrawCommandValidator = new WithdrawCommandValidator(bank);
		bank.addAccount("00000000", new CDAccount(0, 1000));
		bank.addAccount("11111111", new CheckingAccount(0));
		bank.deposit("11111111", 100);
		bank.passTime(12);
		String cdWithdrawalCommand = "withdraw 00000000 1000";
		String cdTransferCommand = "transfer 00000000 11111111 1000";

		assertTrue(withdrawCommandValidator.validate(cdWithdrawalCommand));
		assertFalse(transferCommandValidator.validate(cdTransferCommand));
	}

	// Testing all deposit rules on from account
	@Test
	public void cannot_transfer_more_than_2500_to_savings_account() {
		bank.addAccount("00000000", new CheckingAccount(0));
		bank.addAccount("11111111", new SavingsAccount(0));
		command = "transfer 00000000 11111111 2500.01";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void cannot_transfer_more_than_1000_to_checking_account() {
		addAccounts();
		command = "transfer 00000000 11111111 1000.01";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	// Other scenarios
	@Test
	public void both_accounts_can_be_savings() {
		bank.addAccount("00000000", new SavingsAccount(0));
		bank.deposit("00000000", 1000);
		bank.addAccount("11111111", new SavingsAccount(0));
		command = "transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void both_accounts_cannot_be_CD() {
		bank.addAccount("00000000", new CDAccount(0, 1000));
		bank.addAccount("11111111", new CDAccount(0, 1000));
		command = "transfer 00000000 11111111 1000";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void cannot_transfer_to_the_same_account() {
		addAccounts();
		command = "transfer 00000000 00000000 0";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void command_can_end_with_a_space() {
		addAccounts();
		command = "transfer 00000000 11111111 100 ";
		boolean actual = transferCommandValidator.validate(command);

		assertTrue(actual);
	}

	@Test
	public void command_cannot_start_with_a_space() {
		addAccounts();
		command = " transfer 00000000 11111111 100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void command_cannot_have_extra_spaces_in_the_middle() {
		addAccounts();
		command = " transfer  00000000   11111111  100";
		boolean actual = transferCommandValidator.validate(command);

		assertFalse(actual);
	}

}
