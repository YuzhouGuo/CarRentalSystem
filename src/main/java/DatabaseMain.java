import java.util.Scanner;

/**
 * Entrypoint to the database application.
 * 
 * @author Ivan M
 *
 */
public class DatabaseMain {
	static final String INTRO = "Welcome to FakeCars!\n", CONCLUSION = "\nThank you and goodbye!";
	static final Scanner sc = new Scanner(System.in);
	static Operation[] ops;
	
	static void setupOps() {
		ops = new Operation[] {
			// Print the closest parking spot name and lon/lat to a given lon/lat - and birds-eye distance to it in km
			// Modify car and booking to return the car to a given named parking spot
			// Print amount spent by a customer in the last year, by email
			// Open a new membership. Then, you get the list of all previously existing memberships and say for what memberships should all members move to newly created membership
			// Print the names of the customers that rented the last 5 bookings of cars of a particular model, the parking spot where they left it, and current location of that car
		};
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
				ops[selected].run();
			}
		}
		
		// Exit message
		System.out.println(CONCLUSION);
		sc.close();
	}

}
