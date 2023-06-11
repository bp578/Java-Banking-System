package banking;

public class CommandValidator {
	protected Bank bank;
	protected String[] command;
	protected String argument1;
	protected String argument2;
	protected String argument3;
	protected String argument4;
	protected String argument5;

	CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String commandStr) {
		parse(commandStr);
		if (argument1 != null) {
			return delegateChildValidator(commandStr);
		} else {
			return false;
		}
	}

	public void parse(String commandStr) {
		this.command = commandStr.split(" ");
		switch (command.length) {
		case 2:
			this.argument1 = command[0];
			this.argument2 = command[1];
			break;
		case 3:
			this.argument1 = command[0];
			this.argument2 = command[1];
			this.argument3 = command[2];
			break;
		case 4:
			this.argument1 = command[0];
			this.argument2 = command[1];
			this.argument3 = command[2];
			this.argument4 = command[3];
			break;
		case 5:
			this.argument1 = command[0];
			this.argument2 = command[1];
			this.argument3 = command[2];
			this.argument4 = command[3];
			this.argument5 = command[4];
			break;
		}
	}

	public String getArgument1() {
		return argument1;
	}

	private boolean delegateChildValidator(String commandStr) {
		switch (argument1.toLowerCase()) {
		case "create":
			CreateCommandValidator createCommandValidator = new CreateCommandValidator(bank);
			return createCommandValidator.validate(commandStr);
		case "deposit":
			DepositCommandValidator depositCommandValidator = new DepositCommandValidator(bank);
			return depositCommandValidator.validate(commandStr);
		case "withdraw":
			WithdrawCommandValidator withdrawCommandValidator = new WithdrawCommandValidator(bank);
			return withdrawCommandValidator.validate(commandStr);
		case "transfer":
			TransferCommandValidator transferCommandValidator = new TransferCommandValidator(bank);
			return transferCommandValidator.validate(commandStr);
		case "pass":
			PassTimeCommandValidator passTimeCommandValidator = new PassTimeCommandValidator(bank);
			return passTimeCommandValidator.validate(commandStr);
		default:
			return false;
		}
	}

	// Common functions used by child validators

	protected boolean actionIsValid(String actualAction, String expectedAction) {
		return actualAction.equalsIgnoreCase(expectedAction);
	}

	protected boolean accountIdIsValid(String accountId) {
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

	protected boolean accountExists(String accountId) {
		return bank.retrieveAccount(accountId) != null;
	}

}
