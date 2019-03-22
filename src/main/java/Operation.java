import java.sql.SQLException;
import java.util.Scanner;

/**
 * A general class containing methods and variables we want all menu options to share.
 * 
 * @author Ivan M
 *
 */
public abstract class Operation {
	// The main loop will call this on the selected operation
	public abstract void run() throws SQLException;
	
	// The name displayed in the menu
	protected String shortName;
	// A longer description, for example could be used as first print in run()
	protected String description;
	// A Scanner to receive info from customer
	protected static final Scanner scc = new Scanner(System.in);
}
