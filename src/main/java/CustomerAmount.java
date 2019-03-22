import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

// An operation to print amount spent by a customer in the last year, by email

/**

SELECT 
	SUM(mship.price_per_month) AS spent
FROM 
	membership mship
	JOIN memberPayment mp 
	ON mp.type = mship.type
	AND mp.transaction_num IN (
		SELECT 
			p.transaction_num
		FROM payment p
		WHERE paid_by = ?
		AND date >= (NOW() - interval '1 year')
	)
;

 */
public class CustomerAmount extends Operation {
	
	public CustomerAmount(String shortName, String desc) {
		this.shortName = shortName;
		this.description = desc;
	}

	@Override
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
	        
	        String preparedS = "SELECT " + 
		 			   "SUM(mship.price_per_month) AS spent " + 
		 			   "FROM " + 
		 			   "membership mship " + 
		 			   "JOIN memberPayment mp " + 
		 			   "ON mp.type = mship.type " + 
		 			   "AND mp.transaction_num IN ( " + 
		 			   "	SELECT " + 
		 			   "		p.transaction_num" + 
		 			   "	FROM payment p" + 
		 			   "	WHERE paid_by = ?" + 
		 			   "	AND date >= (NOW() - interval '1 year')" + 
		 			   ")";
	        System.out.println(preparedS);
	        
	        PreparedStatement preparedCid = null;
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			// Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
	        
	        // Request email from the customer
			System.out.println("Please enter your email: ");
			String email = scc.nextLine();
			System.out.println(email);
			
			preparedCid.setString(1, email);
		        
	        ResultSet result = preparedCid.executeQuery();
	        ResultSetMetaData rsmd = result.getMetaData();
	        
	        while(result.next()) {
	        	System.out.println("\nThe amount of money you spent from last year is " + result.getDouble(rsmd.getColumnName(1)));
	        }	       	  
	        
	        // Don't forget to close
	        result.close();
	        preparedCid.close();
	        statement.close();
	        con.close();
	}

}
