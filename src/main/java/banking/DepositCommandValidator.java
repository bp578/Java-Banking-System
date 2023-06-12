package banking;

public class DepositCommandValidator extends CommandValidator {
	public DepositCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 3) {
			return actionIsValid(argument1, "deposit") && accountIdIsValid(argument2) && accountExists(argument2)
					&& amountIsADouble(argument3) && depositAmountIsValid(argument3);
		} else {
			return false;
		}
	}

	private boolean depositAmountIsValid(String amountStr) {
		double amount = Double.parseDouble(amountStr);
		String type = getAccountType(argument2);
		if (type.equals("Savings")) {
			return amount >= 0 && amount <= 2500;
		} else if (type.equals("Checking")) {
			return amount >= 0 && amount <= 1000;
		} else {
			return false;
		}

	}

	private String getAccountType(String accountId) {
		return bank.retrieveAccount(accountId).getType();
	}

}
