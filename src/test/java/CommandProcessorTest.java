import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {
	Bank bank;
	CommandProcessor commandProcessor;
	String command;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
		command = "";
	}

	// Testing create command
	@Test
	public void account_created_has_correct_id() {
		command = "create savings 12345678 1.2";
		commandProcessor.run(command);
		Account actual = bank.retrieveAccount("12345678");

		assertNotNull(actual);
	}

	@Test
	public void account_created_has_correct_apr() {
		command = "create savings 12345678 1.2";
		commandProcessor.run(command);
		Double actual = bank.retrieveAccount("12345678").getAPR();

		assertEquals(1.2, actual);
	}

	@Test
	public void cd_account_starts_with_correct_balance() {
		command = "create cd 12345678 1.2 500";
		commandProcessor.run(command);

		Double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(500, actual);
	}

	@Test
	public void account_is_of_correct_type() {
		String savingsCommand = "create savings 00000000 1.2";
		String checkingCommand = "create checking 11111111 1.2";
		String cdCommand = "create cd 12345678 1.2 500";
		commandProcessor.run(savingsCommand);
		commandProcessor.run(checkingCommand);
		commandProcessor.run(cdCommand);

		assertTrue(bank.retrieveAccount("00000000") instanceof SavingsAccount);
		assertTrue(bank.retrieveAccount("11111111") instanceof CheckingAccount);
		assertTrue(bank.retrieveAccount("12345678") instanceof CDAccount);
	}

	// Testing deposit command
	@Test
	public void new_account_has_balance_equal_to_amount_deposited() {
		bank.addAccount("12345678", new CheckingAccount(2.5));
		command = "deposit 12345678 100";
		commandProcessor.run(command);
		Double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(100, actual);
	}

	@Test
	public void existing_account_balance_increases_by_amount_deposited() {
		Account account = new CheckingAccount(2.5);
		Double balanceBefore = 1000.0;
		bank.addAccount("12345678", account);
		bank.deposit("12345678", balanceBefore);

		command = "deposit 12345678 100";
		commandProcessor.run(command);
		Double balanceAfter = account.getBalance();
		Double actualDifference = balanceAfter - balanceBefore;

		assertEquals(100, actualDifference);

	}

}
