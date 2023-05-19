import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {
	CommandValidator commandValidator;

	@BeforeEach
	public void setUp() {
		commandValidator = new CommandValidator();
	}

	@Test
	public void empty_string_is_invalid() {
		String command = "";
		boolean actual = commandValidator.validate(command);

		assertFalse(actual);
	}

	@Test
	public void action_is_parsed_correctly() {
		String command = "create savings 12345678 0.2";
		commandValidator.parse(command);
		String actual = commandValidator.getArgument1();

		assertEquals("create", actual);

	}

}
