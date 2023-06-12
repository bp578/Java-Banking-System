package banking;

public class CommandProcessor {
	private final Bank bank;
	private String[] command;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
	}

	public void run(String commandStr) {
		this.command = commandStr.split(" ");
		String action = command[0];
		if (action.equalsIgnoreCase("create")) {
			runCreateCommand();
		} else if (action.equalsIgnoreCase("deposit")) {
			runDepositCommand();
			bank.retrieveAccount(command[1]).addCommand(commandStr);
		} else if (action.equalsIgnoreCase("withdraw")) {
			runWithdrawCommand();
			bank.retrieveAccount(command[1]).addCommand(commandStr);
		} else if (action.equalsIgnoreCase("transfer")) {
			runTransferCommand();
			bank.retrieveAccount(command[1]).addCommand(commandStr);
			bank.retrieveAccount(command[2]).addCommand(commandStr);
		} else if (action.equalsIgnoreCase("pass")) {
			runPassCommand();
		}
	}

	private void runCreateCommand() {
		if (command[1].equalsIgnoreCase("savings")) {
			bank.addAccount(command[2], new SavingsAccount(Double.parseDouble(command[3])));
		} else if (command[1].equalsIgnoreCase("checking")) {
			bank.addAccount(command[2], new CheckingAccount(Double.parseDouble(command[3])));
		} else if (command[1].equalsIgnoreCase("cd")) {
			bank.addAccount(command[2], new CDAccount(Double.parseDouble(command[3]), Double.parseDouble(command[4])));
		}
	}

	private void runDepositCommand() {
		bank.deposit(command[1], Double.parseDouble(command[2]));
	}

	private void runWithdrawCommand() {
		bank.withdraw(command[1], Double.parseDouble(command[2]));
	}

	private void runTransferCommand() {
		bank.transfer(command[1], command[2], Double.parseDouble(command[3]));
	}

	private void runPassCommand() {
		bank.passTime(Integer.parseInt(command[1]));
	}
}
