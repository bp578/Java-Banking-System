public class CreateCommandValidator {
	private String[] command;
	private String action; // arg 1
	private String accountType; // arg 2
	private String accountId; // arg 3
	private String apr; // arg 4
	private String balance; // arg 5 (cd only)

	// All tests on each part of the command must pass for the whole command to pass
	public boolean validate(String commandStr) {
		this.command = commandStr.split(" ");

		if (command.length == 4) {
			this.action = command[0];
			this.accountType = command[1];
			this.accountId = command[2];
			this.apr = command[3];
			return actionIsValid(action) && accountTypeIsValid(accountType) && accountIdIsValid(accountId)
					&& APRisValid(apr);
		} else if (command.length == 5) {
			this.action = command[0];
			this.accountType = command[1];
			this.accountId = command[2];
			this.apr = command[3];
			this.balance = command[4];
			return actionIsValid(action) && accountTypeIsValid(accountType) && accountIdIsValid(accountId)
					&& APRisValid(apr) && balanceIsValid(balance);
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

	private boolean accountIdIsValid(String accountId) {
		// Check length and make sure all characters are numbers
		if (accountId.length() == 8) {
			try {
				Integer.parseInt(accountId);
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}

		return true;
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
