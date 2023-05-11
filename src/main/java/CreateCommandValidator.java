public class CreateCommandValidator {
	private String command;

	public CreateCommandValidator() {
	}

	public boolean validate(String command) {
		this.command = command;
		return false;
	}

}
