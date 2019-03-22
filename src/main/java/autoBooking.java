import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;
import java.util.Date;

/**
 * A trigger that asks the user to enter an valid email, and automatically book a car according to his membership and favorite cars;
 * If his favorite car is not available, then the app will book a car with lowest price instead.
 * 
 *output the customer's membership type and his favorite car, report an error if the provided email is not in our customer list
 * SELECT customer.membership, customerFavorites.license_plate FROM 
 * customer, customerFavorites
 * WHERE customer.email = ?
 * AND customer.email = customerFavorites.email
 * 
 * SELECT license_plate, arrived_to_lat, arrived_to_lon 
 * FROM booking WHERE license_plate IN ((SELECT license_plate
 * 			FROM booking 
 * 			WHERE arrived_to_lat IS NOT NULL
 * 			AND arrived_to_lon IS NOT NULL) EXCEPT ((SELECT license_plate
 * 			FROM booking
 * 		    WHERE arrived_to_lat IS NULL
 *  			AND arrived_to_lon IS NULL)）
 * AND license_plate = ?
 * 
 * if output is 0 row, print a message telling "no available cars currently"
 * else add a new booking information
 * 
 * INSERT INTO booking VALUES(DEFAULT, ?transaction_num, ?license_plate, ?email, ?left_from_lat, ?left_from_lon);
 */

public class autoBooking extends Operation{

	public autoBooking (String shortName, String desc) {
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
				System.out.println("Sorry, the user you enterred is not in our member list, please try again: ");
				email = scc.nextLine();
				for(int i=email.length(); i<254; i++) {
		        	email = email + " ";
				}
				for(String temp: emaillist) {
					if(temp.equals(email)) exists = true;
				}
			}
			
			try {
				preparedCid = con.prepareStatement("SELECT membership_type FROM customer WHERE email = ?");
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			preparedCid.setString(1, email);
			
			String membership = "";
			result = preparedCid.executeQuery();
	        rsmd = result.getMetaData();
			if(result.next()) {
				membership = result.getString(rsmd.getColumnName(1));
			}
			
			String[] membershiplist = new String[6];
			membershiplist[0] = "basic";
			membershiplist[1] = "premium";
			membershiplist[2] = "silver";
			membershiplist[3] = "company";
			membershiplist[4] = "silverForOneMonth";
			membershiplist[5] = "premiumForOneMonth";
			
			double discount;
			if(membership.contains(membershiplist[0])) {
				discount = 0.95;
			} else if (membership.contains(membershiplist[1])) {
				discount = 0.80;
			} else if (membership.contains(membershiplist[2])) {
				discount = 0.85;
			} else if (membership.contains(membershiplist[3])) {
				discount = 0.80;
			} else if (membership.contains(membershiplist[4])) {
				discount = 0.85;
			} else if (membership.contains(membershiplist[5])) {
				discount = 0.80;
			} else {
				discount = 1;
			}

			String preparedS = "SELECT " +
				"customer.membership_type, customerFavorites.license_plate FROM " +
				"customer, customerFavorites" +
				" WHERE customer.email = ?" +
				" AND customer.email = customerFavorites.email "
				+ "LIMIT 1";

			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			preparedCid.setString(1, email);
		        
	        result = preparedCid.executeQuery();
	        rsmd = result.getMetaData();
	        
	        String license = "";
	        int count = 0;
	        while(result.next()) {
	        	count++;
	        	license = result.getString(rsmd.getColumnName(2));
	        }	       	  
	        
	        String prepared2 = "SELECT license_plate, arrived_to_lat, arrived_to_lon" + 
	        				" FROM booking WHERE license_plate IN ((SELECT license_plate" +
	        				" 			FROM booking" + 
	        				" 			WHERE arrived_to_lat IS NOT NULL" +
	        				" 			AND arrived_to_lon IS NOT NULL) " +
							"    EXCEPT (SELECT license_plate" +
							"			FROM  booking" +
							"			WHERE arrived_to_lat IS NULL" +
							"           AND arrived_to_lon IS NULL))" +
	        				" AND license_plate = ?";
	        
	        if (count != 1) {
	        	System.out.println("Database error!");
	        	result.close();
		        preparedCid.close();
		        statement.close();
		        con.close();
	        	return;
	        }
	        try {
				preparedCid = con.prepareStatement(prepared2);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
	       	preparedCid.setString(1, license);
	       	
	       	result = preparedCid.executeQuery();
	       	rsmd = result.getMetaData();
		        
	       	double lat;
	       	double lon;
	       	double price;
		    if (result.next()) {
		        lat = result.getDouble(rsmd.getColumnName(2));
		        lon = result.getDouble(rsmd.getColumnName(3));
		        String prepared3 = "SELECT model.price_for_rent FROM "
		        		+ "car, model "
		        		+ "WHERE car.car_make = model.car_make "
		        		+ "AND car.model_name = model.model_name "
		        		+ "AND car.license_plate = ?";
		        
		        try {
					preparedCid = con.prepareStatement(prepared3);
				} catch (SQLException e) {
					e.printStackTrace();				
				}
		       	
		        preparedCid.setString(1, license);
		        
		       	result = preparedCid.executeQuery();
		       	rsmd = result.getMetaData();
		       	
		       	if(result.next()) {
		       		price = result.getDouble(rsmd.getColumnName(1));
		       	} else {
		       		System.out.println("Database Error");
		       		result.close();
			        preparedCid.close();
			        statement.close();
			        con.close();
		       		return;
		       	}
		       	
		        System.out.println("We find a car for you, here is the information: ");
		        System.out.println("\n" + "License" + ":   " + license +
        			"\n" + "Position: latitude" + ":            " + lat + 
        			"\n" + "Position: longitude" + ":  " + lon +
        			"\n" + "Your fee" + ":  " + price*discount);
		        
		        Random rand = new Random();
		        int accountNum = rand.nextInt((9999-1000)+1)+1000;
		        String prepared5 = "INSERT INTO payment VALUES (DEFAULT, ?, ?, ?)";
		        try {
					preparedCid = con.prepareStatement(prepared5);
				} catch (SQLException e) {
					e.printStackTrace();				
				}
		        preparedCid.setString(1, email);
		        preparedCid.setInt(2, accountNum);
		        preparedCid.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
		        
		        preparedCid.executeUpdate();

				String prepared6 = "SELECT currval(pg_get_serial_sequence('payment','transaction_num'))";

				preparedCid = null;

				try {
					preparedCid = con.prepareStatement(prepared6);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				result = preparedCid.executeQuery();
				rsmd = result.getMetaData();

				int latest_seq_num = 0;
				if(result.next()) {
					latest_seq_num = result.getInt(rsmd.getColumnName(1));
				}

		        String prepared4 = "INSERT INTO booking VALUES(DEFAULT, ?, ?, ?, ?, ?)";
		       
		        try {
					preparedCid = con.prepareStatement(prepared4);
				} catch (SQLException e) {
					e.printStackTrace();				
				}

		        preparedCid.setInt(1, latest_seq_num);
		        preparedCid.setString(2, license);
		        preparedCid.setString(3, email);
		        preparedCid.setDouble(4, lat);
		        preparedCid.setDouble(5, lon);
		        			        
		       	preparedCid.executeUpdate();
		    } else {
		    	String prepared3 = "SELECT booking.license_plate, booking.arrived_to_lat, booking.arrived_to_lon, table1.price_for_rent " +
		    			"FROM booking,"
		    			+ "(SELECT car.license_plate, model.price_for_rent FROM "
		    				+ "car, model "
		    				+ "WHERE car.car_make = model.car_make "
		    				+ "AND car.model_name = model.model_name"
		    			+ ") AS table1 "
		    			+ "WHERE booking.license_plate IN ((SELECT license_plate" +
						" 			FROM booking" +
						" 			WHERE arrived_to_lat IS NOT NULL" +
						" 			AND arrived_to_lon IS NOT NULL) " +
						"    EXCEPT (SELECT license_plate" +
						"			FROM  booking" +
						"			WHERE arrived_to_lat IS NULL" +
						"           AND arrived_to_lon IS NULL))"
		    			+ "AND booking.license_plate = table1.license_plate "
		    			+ "ORDER BY table1.price_for_rent";
		    	
		    	System.out.println("Sorry your favorite car is not available, we will automatically choose the cheapest one available for you.");
		    	
		    	try {
					preparedCid = con.prepareStatement(prepared3);
				} catch (SQLException e) {
					e.printStackTrace();				
				}
		       	
		       	result = preparedCid.executeQuery();
		       	rsmd = result.getMetaData();
		       	
		        if (result.next()) {
		        	license = result.getString(rsmd.getColumnName(1));
			        lat = result.getDouble(rsmd.getColumnName(2));
			        lon = result.getDouble(rsmd.getColumnName(3));
			        price = result.getDouble(rsmd.getColumnName(4));
			        
			        System.out.println("We find a car for you, here is the information: ");
			        System.out.println("\n" + rsmd.getColumnName(1) + ":   " + license +
	        			"\n" + rsmd.getColumnName(2) + ":            " + lat + 
	        			"\n" + rsmd.getColumnName(3) + ":  " + lon +
	        			"\n" + rsmd.getColumnName(4) + ":  " + price*discount);

					Random rand = new Random();
					int accountNum = rand.nextInt((9999-1000)+1)+1000;
					String prepared5 = "INSERT INTO payment VALUES (DEFAULT, ?, ?, ?)";
					try {
						preparedCid = con.prepareStatement(prepared5);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					preparedCid.setString(1, email);
					preparedCid.setInt(2, accountNum);
					preparedCid.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

					preparedCid.executeUpdate();

					String prepared6 = "SELECT currval(pg_get_serial_sequence('payment','transaction_num'))";

					preparedCid = null;

					try {
						preparedCid = con.prepareStatement(prepared6);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					result = preparedCid.executeQuery();
					rsmd = result.getMetaData();

					int latest_seq_num = 0;
					if(result.next()) {
						latest_seq_num = result.getInt(rsmd.getColumnName(1));
					}

					String prepared4 = "INSERT INTO booking VALUES(DEFAULT, ?, ?, ?, ?, ?)";

					try {
						preparedCid = con.prepareStatement(prepared4);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					preparedCid.setInt(1, latest_seq_num);
					preparedCid.setString(2, license);
					preparedCid.setString(3, email);
					preparedCid.setDouble(4, lat);
					preparedCid.setDouble(5, lon);

					preparedCid.executeUpdate();

			    } else {
			    	System.out.println("We have searched all through our database, but there is no car available, please try next time.");
			    	result.close();
			        preparedCid.close();
			        statement.close();
			        con.close();
			    	return;
			    }
		    }
	        
	        // Don't forget to close
	        result.close();
	        preparedCid.close();
	        statement.close();
	        con.close();
	}
	
}
