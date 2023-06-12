package banking;

public class WithdrawCommandValidator extends CommandValidator {
	WithdrawCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 3) {
			return actionIsValid(argument1, "withdraw") && accountIdIsValid(argument2) && accountExists(argument2)
					&& amountIsADouble(argument3) && withdrawalAmountIsValid(argument3);
		} else {
			return false;
		}

	}

	private boolean withdrawalAmountIsValid(String amountStr) {
		double amount = Double.parseDouble(amountStr);
		String type = getAccountType(argument2);
		Account account = bank.retrieveAccount(argument2);

		if (type.equalsIgnoreCase("savings")) {
			return savingsAccountWithdrawalIsValid(account, amount);
		} else if (type.equalsIgnoreCase("checking")) {
			return amount >= 0 && amount <= account.maximumWithdrawalAmount;
		} else if (type.equalsIgnoreCase("cd")) {
			return (account.getAge() >= 12) && (amount >= account.getBalance());
		}

		return false;
	}

	private String getAccountType(String accountId) {
		return bank.retrieveAccount(accountId).getType();
	}

	private boolean savingsAccountWithdrawalIsValid(Account account, double amount) {
		if (account.getWithdrawsMadeThisMonth() < account.getMaximumWithdrawalsPerMonth()) {
			return amount >= 0 && amount <= account.maximumWithdrawalAmount;
		} else {
			return false;
		}
	}

}
