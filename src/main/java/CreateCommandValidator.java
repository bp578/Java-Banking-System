public class CreateCommandValidator {
    private String[] command;
    private String action; // arg 1

    // All tests on each part of the command must pass for the whole command to pass
    public boolean validate(String commandStr) {
        this.command = commandStr.split(" ");
        this.action = command[0];

        return validateAction(action);
    }

    public boolean validateAction(String action) {
        if (action.equalsIgnoreCase("create")) {
            return true;
        } else {
            return false;
        }
    }
}
