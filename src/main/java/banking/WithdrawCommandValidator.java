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
					&& amountIsValid(argument3);
		} else {
			return false;
		}

	}

	private boolean amountIsValid(String amountStr) {
		try {
			double amount = Double.parseDouble(amountStr);
			String type = getAccountType(argument2);
			Account account = bank.retrieveAccount(argument2);
			switch (type) {
			case "savings":
				if (account.getWithdrawsMadeThisMonth() < account.getMaximumWithdrawalsPerMonth()) {
					return amount >= 0 && amount <= account.maximumWithdrawalAmount;
				} else {
					return false;
				}
			case "checking":
				return amount >= 0 && amount <= account.maximumWithdrawalAmount;
			case "cd":
				return (account.getAge() >= 12) && (amount >= account.getBalance());
			default:
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private String getAccountType(String accountId) {
		return bank.retrieveAccount(accountId).getType();
	}

}
