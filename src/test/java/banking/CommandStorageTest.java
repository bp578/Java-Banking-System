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
		List<String> actual = commandStorage.getCommands();

		assertTrue(actual.isEmpty());
	}

	@Test
	public void commands_stored_equals_commands_added() {
		commandStorage.add("create cheking 123456789 1.0");
		commandStorage.add("deps0T 1234 5000000 abc");

		assertEquals(2, commandStorage.getCommands().size());

	}

	@Test
	public void commands_added_are_correct() {
		commandStorage.add("create cheking 123456789 1.0");
		commandStorage.add("deps0T 1234 5000000 abc");

		assertEquals("create cheking 123456789 1.0", commandStorage.getCommands().get(0));
		assertEquals("deps0T 1234 5000000 abc", commandStorage.getCommands().get(1));
	}
}
