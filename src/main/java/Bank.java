import java.util.HashMap;
import java.util.Map;

public class Bank {
	private Map<String, Account> accounts;

	Bank() {
		accounts = new HashMap<>();
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void addAccount(String accountID, Account account) {
		accounts.put(accountID, account);
	}

	public Account retrieveAccount(String accountID) {
		return accounts.get(accountID);
	}

	public void deposit(String accountID, double moneyToDeposit) {
		this.retrieveAccount(accountID).deposit(moneyToDeposit);
	}

	public void withdraw(String accountID, double moneyToWithdraw) {
		this.retrieveAccount(accountID).withdraw(moneyToWithdraw);
	}
}
