import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
	        
	        String prepared1 = "SELECT distinct email FROM customer";
	        
	        PreparedStatement preparedCid = null;
	        
			try {
				preparedCid = con.prepareStatement(prepared1);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			ArrayList<String> emaillist = new ArrayList<String>();
			ResultSet result = preparedCid.executeQuery();
		    ResultSetMetaData rsmd = result.getMetaData();
	        
	        while(result.next()) {
	        	emaillist.add(result.getString(rsmd.getColumnName(1)));
	        }
			
			// Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
	        
	        // Request email from the customer
			System.out.println("Please enter your email: ");
			String email = scc.nextLine();
			
			for(int i=email.length(); i<254; i++) {
	        	email = email + " ";
			}
			
			boolean exists = false;
			for(String temp: emaillist) {
				if(temp.equals(email)) exists = true;
			}
			
			while(!exists) {
				System.out.println("Sorry, the user you entered is not in our member list, please try again: ");
				email = scc.nextLine();
				for(int i=email.length(); i<254; i++) {
		        	email = email + " ";
				}
				for(String temp: emaillist) {
					if(temp.equals(email)) exists = true;
				}
			}
			
			System.out.println("You're requesting the amount spent by customer: " + email);
			
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			preparedCid.setString(1, email);
		        
	        result = preparedCid.executeQuery();
	        rsmd = result.getMetaData();
	        
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
