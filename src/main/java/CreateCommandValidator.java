public class CreateCommandValidator {
	private String[] command;
	private String action; // arg 1
	private String accountType; // arg 2

	// All tests on each part of the command must pass for the whole command to pass
	public boolean validate(String commandStr) {
		this.command = commandStr.split(" ");

		// Check if the argument count is valid before continuing
		if (command.length == 4) {
			this.action = command[0];
			this.accountType = command[1];
		} else {
			return false;
		}

		if (actionIsValid(action) && accountTypeIsValid(accountType)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean actionIsValid(String action) {
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
}
