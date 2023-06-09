package banking;

public class TransferCommandValidator extends CommandValidator {
	public TransferCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String commandStr) {
		parse(commandStr);
		return actionIsValid(argument1, "transfer");
	}
}
