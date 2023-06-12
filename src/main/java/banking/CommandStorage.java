package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandStorage {
	Bank bank;
	List<String> transactionHistory = new ArrayList<>();
	List<String> invalidCommands = new ArrayList<>();

	public CommandStorage(Bank bank) {
		this.bank = bank;
	}

	public List<String> getInvalidCommands() {
		return invalidCommands;
	}

	public void addInvalidCommand(String command) {
		invalidCommands.add(command);
	}

	public List<String> getTransactionHistory() {
		finalizeTransactionHistory();
		return transactionHistory;
	}

	public void finalizeTransactionHistory() {
		transactionHistory = new ArrayList<>();
		for (Map.Entry<String, Account> entry : bank.getAccounts().entrySet()) {
			String state = getAccountState(entry.getKey());
			transactionHistory.add(state);
			transactionHistory.addAll(bank.retrieveAccount(entry.getKey()).getTransactionHistory());
		}
	}

	private String getAccountState(String accountID) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
		Account account = bank.retrieveAccount(accountID);
		String type = account.getType();
		String balance = decimalFormat.format(account.getBalance());
		String apr = decimalFormat.format(account.getAPR());

		return String.format("%s %s %s %s", type, accountID, balance, apr);
	}
}
