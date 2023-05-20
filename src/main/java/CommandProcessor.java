public class CommandProcessor {
	private Bank bank;
	private String[] command;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
	}

	public void run(String commandStr) {
		this.command = commandStr.split(" ");
		String action = command[0];
		switch (action.toLowerCase()) {
		case "create":
			runCreateCommand();
			break;
		case "deposit":
			runDepositCommand();
			break;
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
}
