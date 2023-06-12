package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {
	CommandStorage commandStorage;

	@BeforeEach
	public void setUp() {
		commandStorage = new CommandStorage();
	}

	@Test
	public void command_storage_starts_with_no_commands() {
		List<String> actual = commandStorage.getInvalidCommands();

		assertTrue(actual.isEmpty());
	}

	@Test
	public void invalid_commands_stored_equals_commands_added() {
		commandStorage.addInvalidCommand("create cheking 123456789 1.0");
		commandStorage.addInvalidCommand("deps0T 1234 5000000 abc");

		assertEquals(2, commandStorage.getInvalidCommands().size());

	}

	@Test
	public void invalid_commands_added_are_correct() {
		commandStorage.addInvalidCommand("create cheking 123456789 1.0");
		commandStorage.addInvalidCommand("deps0T 1234 5000000 abc");

		assertEquals("create cheking 123456789 1.0", commandStorage.getInvalidCommands().get(0));
		assertEquals("deps0T 1234 5000000 abc", commandStorage.getInvalidCommands().get(1));
	}

}
