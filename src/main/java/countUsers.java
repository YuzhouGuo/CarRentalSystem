import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * count number of users of a given membership
 * 
 * SELECT count(*) AS num
 * FROM customer
 * WHERE customer.membership_type = ?
 * 
 * @author cyyze
 *
 */
public class countUsers extends Operation {
	public countUsers(String shortName, String desc) {
		this.shortName = shortName;
		this.description = desc;
	}
	
	public void run() throws SQLException {
		 try {
	            DriverManager.registerDriver(new org.postgresql.Driver());
	        } catch (Exception cnfe) {
	            System.out.println("Class not found");
	        }

	        // This is the url you must use for Postgresql.
	        // Note: This url may not valid now !
	        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
	        String usernamestring = "cs421g04";
	        String passwordstring = "wearegroup4";
	        Connection con = DriverManager.getConnection(url, usernamestring, passwordstring);
	        Statement statement = con.createStatement();
	        
	        String preparedS = "SELECT count(*) AS num FROM customer" + 
	        					" WHERE customer.membership_type = ?";
	        //System.out.println(preparedS);
	        
	        PreparedStatement preparedCid = null;
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			// Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
	        
	        // Request license plate from the customer
			System.out.println("Please enter the chosen membership type: ");
			String type = scc.nextLine();
			//System.out.println(license);
			
			preparedCid.setString(1, type);
		        
	        ResultSet result = preparedCid.executeQuery();
	        ResultSetMetaData rsmd = result.getMetaData();
	        
	        int count = 0;
	        while(result.next()) {
	        	count++;
	        	System.out.println("\nThere are " + result.getInt(rsmd.getColumnName(1)) + " users of this type.");
	        }
	        
	        if (count == 0) 
	        	System.out.println("Error with the data");
	        
	        // Don't forget to close
	        result.close();
	        preparedCid.close();
	        statement.close();
	        con.close();
	}
}
