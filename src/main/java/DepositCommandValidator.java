public class DepositCommandValidator extends CommandValidator {
	public DepositCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 3) {
			return actionIsValid(argument1, "deposit") && accountIdIsValid(argument2) && accountExists(argument2)
					&& amountIsValid(argument3);
		} else {
			return false;
		}
	}

	private boolean amountIsValid(String amountStr) {
		try {
			double amount = Double.parseDouble(amountStr);
			String type = getAccountType(argument2);
			if (type.equals("savings")) {
				return amount >= 0 && amount <= 2500;
			} else if (type.equals("checking")) {
				return amount >= 0 && amount <= 1000;
			} else {
				return false;
			}

		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean accountExists(String accountId) {
		return bank.retrieveAccount(accountId) != null;
	}

	private String getAccountType(String accountId) {
		return bank.retrieveAccount(accountId).getType();
	}

}
