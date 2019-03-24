import java.sql.SQLException;
import java.util.Scanner;

/**
 * Entrypoint to the database application.
 * 
 * @author Ivan M, Yuzhou G
 *
 */
public class DatabaseMain {
	static final String INTRO = "Welcome to FakeCars!\n", CONCLUSION = "\nThank you and goodbye!";
	static final Scanner sc = new Scanner(System.in);
	static Operation[] ops;
	
	// TODO
	// Print the closest parking spot name and lon/lat to a given lon/lat - and birds-eye distance to it in km
	//
	// Modify car and booking to return the car to a given named parking spot
	//
	// Print amount spent by a customer in the last year, by email (Done)
	//
	// Open a new membership. 
	// Then, you get the list of all previously existing memberships 
	// and say for what memberships should all members move to newly created membership
	//
	// Print the names of the customers that rented the last 5 bookings of cars of a particular model, 
	// the parking spot where they left it, and current location of that car (Done)
	
	static void setupOps() {
		ops = new Operation[6];
		String CAshort = "Money spent last year";
		String CAlong = "This is an operation to print amount spent by a customer in the last year, by email";
		ops[0] = new CustomerAmount(CAshort, CAlong); //over
		String Mshort = "Million Join Operation";
		String Mlong = "This is an operation to print the names of the customers that rented the last 5 bookings of cars of a particular model"
						+ " the parking spot where they left it, and current location of that car";
		ops[1] = new MillionJoinOperation(Mshort, Mlong); //over
		String Rshort = "Return Car Operation";
		String Rlong = "Return the location of a specified car";
		ops[2] = new ReturnCarOperation(Rshort, Rlong); 
		String Memshort = "Create Membership";
		String Memlong = "create a new membership type, and insert it into Table Membership";
		ops[3] = new createMembership(Memshort, Memlong); //over
		String cshort = "Count users of a membership";
		String clong = "This is an operation to count number of users of a given membership";
		ops[4] = new countUsers(cshort, clong); //over
		String auto = "Automatic booking system";
		String autolong = "This operation asks the user to enter a valid email, then will automatically book a car based on the member's favorite car and price of all the available cars."
				+ "\n Will report an error if there is no matching car available.";
		ops[5] = new autoBooking(auto, autolong);
	}

	public static void main(String[] args) {
		// Setup our operations array
		setupOps();
		
		// Introduction message
		System.out.println(INTRO);
		
		boolean loopAgain = true; // This could theoretically be transformed into an Operation
		// Loop until quit
		while(loopAgain) {
			// Print options + quit
			System.out.println("\nChoose an option: ");
			for(int i = 0; i < ops.length; i++)
				System.out.printf("  [%d] %s\n", i, ops[i].shortName);
			System.out.printf("  [-1] Quit\n");
			
			// Let user select an option
			int selected;
			try {
				selected = Integer.parseInt(sc.nextLine()); // Make sure to get full line
				if(selected < -1 || selected >= ops.length) 
					throw new Exception();
			} catch(Exception e) {
				System.out.println("There was an error in your input, please try again.");
				continue;
			}
			
			// Execute selected option or mark loopAgain as false if option was quit
			if(selected == -1) {
				loopAgain = false;
			} else {
				try {
					ops[selected].run();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Exit message
		System.out.println(CONCLUSION);
		sc.close();
	}

}
