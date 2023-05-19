public class CreateCommandValidator extends CommandValidator {
	// All tests on each part of the command must pass for the whole command to pass
	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 4) {
			return actionIsValid(argument1) && accountTypeIsValid(argument2) && accountIdIsValid(argument3)
					&& APRisValid(argument4);
		} else if (command.length == 5) {
			return actionIsValid(argument1) && accountTypeIsValid(argument2) && accountIdIsValid(argument3)
					&& APRisValid(argument4) && balanceIsValid(argument5);
		} else {
			return false;
		}

	}

	private boolean actionIsValid(String action) {
		return action.equalsIgnoreCase("create");
	}

	private boolean accountTypeIsValid(String accountType) {
		if ((accountType.equalsIgnoreCase("savings") || accountType.equalsIgnoreCase("checking"))
				&& command.length == 4) {
			return true;
		} else if (accountType.equalsIgnoreCase("cd") && command.length == 5) {
			return true;
		} else {
			return false;
		}

	}

	private boolean APRisValid(String aprStr) {
		try {
			Double APR = Double.parseDouble(aprStr);
			return APR >= 0 && APR <= 10;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean balanceIsValid(String balanceStr) {
		try {
			double balance = Double.parseDouble(balanceStr);
			return balance > 0 && balance <= 10000;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
