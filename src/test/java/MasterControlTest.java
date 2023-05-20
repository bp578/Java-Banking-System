import org.junit.jupiter.api.BeforeEach;

public class MasterControlTest {
	MasterControl masterControl;

	@BeforeEach
	public void setUp() {
		Bank bank = new Bank();
		masterControl = new MasterControl(new);
	}

}
