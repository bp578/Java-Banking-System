package banking;

public class CreateCommandValidator extends CommandValidator {
	public CreateCommandValidator(Bank bank) {
		super(bank);
	}

	// All tests on each part of the command must pass for the whole command to pass
	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 4) {
			return actionIsValid(argument1, "create") && accountTypeIsValid(argument2) && accountIdIsValid(argument3)
					&& APRisValid(argument4) && !accountExists(argument3);
		} else if (command.length == 5) {
			return actionIsValid(argument1, "create") && accountTypeIsValid(argument2) && accountIdIsValid(argument3)
					&& APRisValid(argument4) && balanceIsValid(argument5) && !accountExists(argument3);
		} else {
			return false;
		}

	}

	private boolean accountTypeIsValid(String accountType) {
		if (accountTypeIsCheckingOrSavings(accountType) && command.length == 4) {
			return true;
		} else {
			return accountType.equalsIgnoreCase("cd") && command.length == 5;
		}

	}

	private boolean APRisValid(String aprStr) {
		return positiveDoubleIsWithinLimit(aprStr, 10);
	}

	private boolean balanceIsValid(String balanceStr) {
		if (amountIsADouble(balanceStr)) {
			Double balance = Double.parseDouble(balanceStr);
			return balance > 0 && balance <= 10000;
		} else {
			return false;
		}
	}

	private boolean accountTypeIsCheckingOrSavings(String accountType) {
		return (accountType.equalsIgnoreCase("savings") || accountType.equalsIgnoreCase("checking"));
	}

}
