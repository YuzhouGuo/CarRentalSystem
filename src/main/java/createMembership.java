import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * create a new membership type, and insert it into Table Membership
 * ask the user for a group of members of a specific type of membership
 * and change all of them to have the new membership type
 * 
 * SELECT * FROM membership;
 * INSERT INTO membership VALUES (
 * 		?type
 * 		?description
 * 		?price_per_month
 * )
 * UPDATE customer
 * SET membership_type = ?
 * WHERE membership_type = ?
 * 
 * @author cyyze
 *
 */
public class createMembership extends Operation{
	public createMembership(String shortName, String desc) {
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
	        
	        String prepared1 = "SELECT * FROM membership";
	        String preparedS = "INSERT INTO membership VALUES (" + 
	        				" 	?" + 
	        				", 	?" + 
	        				", 	?" + 
	        				")";
	        //System.out.println(preparedS);
	        
	        PreparedStatement preparedCid = null;
	        
	        // Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
	        
	        try {
				preparedCid = con.prepareStatement(prepared1);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
	        ArrayList<String> list= new ArrayList<String>();
	        System.out.println("We have these different types of membership: ");
	        ResultSet result = preparedCid.executeQuery();
	        ResultSetMetaData rsmd = result.getMetaData();
	        
	        while(result.next()) {
	        	String temp = result.getString(rsmd.getColumnName(1));
	        	list.add(temp);
	        	//System.out.println("\n" + temp);
	        }
	        
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
			System.out.println("Please enter the new membership's name: ");
			String membership = scc.nextLine();
			//System.out.println(license);
			boolean exist = false;
			for(String temp: list) {
				if(temp.equals(membership))
					exist = true;
			}
			
			while(exist) {
				System.out.println("Error! The membership is already in our list, please try again: ");
				membership = scc.nextLine();
				exist = false;
				for(String temp: list) {
					if(temp.equals(membership))
						exist = true;
				}
			}
			
			System.out.println("Now please enter a brief description of the new membership type: ");
			String desc = scc.nextLine();
			System.out.println("Please enter the price per month for this type of membership, your input should be a double value: ");
			boolean valid = false;
			double price = 0.0;
			while (!valid) {
				try {
					price = Double.parseDouble(scc.nextLine());
					valid = true;
				} catch(NumberFormatException e) {
					System.out.println("Your input is invalid, please try again");
					scc.nextLine();
				}
			}
			preparedCid.setString(1, membership);
			preparedCid.setString(2, desc);
			preparedCid.setDouble(3, price);
		        
	        preparedCid.executeUpdate(); // use update for insertion
	        
	        System.out.println("The new membership is successfully inserted.");
	        
	        String preparedS2 = "UPDATE customer" + 
	        					" SET membership_type = ?" + 
	        					" WHERE membership_type = ?"; 
	        try {
				preparedCid = con.prepareStatement(preparedS2);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
	        System.out.println("Please enter the membership type of the user group, that you want to change: ");
	        String type2 = scc.nextLine();
	        String tempType = type2;
	        
	        for(int i=tempType.length(); i<40; i++) {
	        	tempType = tempType + " ";
			}
	        
	        exist = false;
			for(String temp: list) {
				if(tempType.equals(temp))
					exist = true;
			}
			
			while(!exist) {
				System.out.println("Error! The membership is not in our list, please try again: ");
				type2 = scc.nextLine();
				tempType = type2;
		        
		        for(int i=tempType.length(); i<40; i++) {
		        	tempType = tempType + " ";
				}
		        
				exist = false;
				for(String temp: list) {
					if(tempType.equals(temp))
						exist = true;
				}
			}
			
			preparedCid.setString(1, membership);
			preparedCid.setString(2, type2);
	        
	        preparedCid.executeUpdate();
	        
	        String preparedS3 = "SELECT * FROM customer";
	        try {
				preparedCid = con.prepareStatement(preparedS3);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
	        result = preparedCid.executeQuery();
	        rsmd = result.getMetaData();
	        
	        System.out.println("Now our customers' information has been updated: ");
	        while(result.next()) {
	        	System.out.println("\n" + rsmd.getColumnName(1) + ":   " + result.getString(rsmd.getColumnName(1)) +
	        			"\n" + rsmd.getColumnName(2) + ":            " + result.getString(rsmd.getColumnName(2)) + 
	        			"\n" + rsmd.getColumnName(3) + ":  " + result.getString(rsmd.getColumnName(3)) +
	        			"\n" + rsmd.getColumnName(4) + ":  " + result.getString(rsmd.getColumnName(4)) +
	        			"\n" + rsmd.getColumnName(5) + ":    " + result.getString(rsmd.getColumnName(5)) +
	        			"\n" + rsmd.getColumnName(6) + ":    " + result.getString(rsmd.getColumnName(6)));
	        }
	        
	        // Don't forget to close
	        result.close();
	        preparedCid.close();
	        statement.close();
	        con.close();
	}
}

