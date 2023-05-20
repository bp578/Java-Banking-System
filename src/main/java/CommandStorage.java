import java.util.ArrayList;
import java.util.List;

public class CommandStorage {
	List<String> commands = new ArrayList<>();

	public List<String> getCommands() {
		return commands;
	}

	public void add(String command) {
		commands.add(command);
	}
}
