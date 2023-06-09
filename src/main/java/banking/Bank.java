package banking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Bank {
	private final Map<String, Account> accounts;

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
		this.retrieveAccount(accountID).increaseWithdrawalsMadeThisMonth(1);
	}

	public void passTime(int months) {
		for (int i = 0; i < months; i++) {
			Iterator<Map.Entry<String, Account>> it = accounts.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Account account = (Account) pair.getValue();

				account.incrementAge();
				account.resetWithdrawMadeThisMonth();
				if (account.getBalance() == 0) {
					it.remove();
				} else if (account.getBalance() < 100) {
					account.withdraw(25);
				}
				account.monthlyAprCalculation();
			}
		}
	}

}
