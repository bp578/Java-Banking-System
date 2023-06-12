package banking;

public class PassTimeCommandValidator extends CommandValidator {
	PassTimeCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 2) {
			return actionIsValid(command[0], "pass") && timeIsValid(command[1]);
		} else {
			return false;
		}
	}

	private boolean timeIsValid(String time) {
		try {
			int months = Integer.parseInt(time);
			return (months > 0) && (months <= 60);
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
