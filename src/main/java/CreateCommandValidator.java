public class CreateCommandValidator {
	private String[] command;
	private String action; // arg 1
	private String accountType; // arg 2
	private String accountId;

	// All tests on each part of the command must pass for the whole command to pass
	public boolean validate(String commandStr) {
		this.command = commandStr.split(" ");

		// Check if the argument count is valid before continuing
		if (command.length == 4) {
			this.action = command[0];
			this.accountType = command[1];
			this.accountId = command[2];
		} else {
			return false;
		}

		if (actionIsValid(action) && accountTypeIsValid(accountType) && accountIdIsValid(accountId)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean actionIsValid(String action) {
		if (action.equalsIgnoreCase("create")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean accountTypeIsValid(String accountType) {
		if (accountType.equalsIgnoreCase("savings") || accountType.equalsIgnoreCase("checking")
				|| accountType.equalsIgnoreCase("cd")) {
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

}
