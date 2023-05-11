import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class CreateCommandValidatorTest {
	@Test
	public void empty_string_is_invalid() {
		String command = "";
		CreateCommandValidator createCommandValidator = new CreateCommandValidator();

		boolean actual = createCommandValidator(command);

		assertFalse(actual);
	}

}
