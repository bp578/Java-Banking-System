package banking;

public class WithdrawCommandValidator extends CommandValidator {
	WithdrawCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 3) {
			return actionIsValid(argument1, "withdraw") && accountIdIsValid(argument2) && amountIsValid(argument3);
		} else {
			return false;
		}

	}

	private boolean amountIsValid(String amountStr) {
		try {
			double amount = Double.parseDouble(amountStr);
			String type = getAccountType(argument2);
			if (type.equals("savings")) {
				if (!bank.retrieveAccount(argument2).wasWithdrawnThisMonth()) {
					return amount >= 0 && amount <= 1000;
				} else {
					return false;
				}

			} else if (type.equals("checking")) {
				return amount >= 0 && amount <= 400;
			} else {
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
