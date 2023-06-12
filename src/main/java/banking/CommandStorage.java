package banking;

import java.util.ArrayList;
import java.util.List;

public class CommandStorage {
	List<String> invalidCommands = new ArrayList<>();

	public List<String> getInvalidCommands() {
		return invalidCommands;
	}

	public void addInvalidCommand(String command) {
		invalidCommands.add(command);
	}

}
