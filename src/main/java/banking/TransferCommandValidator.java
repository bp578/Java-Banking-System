package banking;

public class TransferCommandValidator extends CommandValidator {
	public TransferCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		if (command.length == 4) {
			return actionIsValid(argument1, "transfer") && withdrawalOnFromAccountIsValid(argument2, argument4)
					&& depositOnToAccountIsValid(argument3, argument4) && bothAccountsAreUnique(argument2, argument3);
		} else {
			return false;
		}

	}

	private boolean withdrawalOnFromAccountIsValid(String fromAccountID, String amount) {
		WithdrawCommandValidator validator = new WithdrawCommandValidator(bank);
		String command = "withdraw " + fromAccountID + " " + amount;
		return validator.validate(command) && !bank.retrieveAccount(fromAccountID).getType().equalsIgnoreCase("cd");
	}

	private boolean depositOnToAccountIsValid(String toAccountID, String amount) {
		DepositCommandValidator validator = new DepositCommandValidator(bank);
		String command = "deposit " + toAccountID + " " + amount;
		return validator.validate(command);
	}

	private boolean bothAccountsAreUnique(String fromAccountID, String toAccountID) {
		return bank.retrieveAccount(fromAccountID) != bank.retrieveAccount(toAccountID);
	}

}
