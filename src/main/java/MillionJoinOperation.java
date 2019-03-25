import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
SELECT
	b_info.license_plate,
	customer.name,
	b_info.arrived_to_lat,
	b_info.arrived_to_lon,
	arrived.name as arrived_name,
	currently
FROM
	(SELECT 
		b.license_plate,
		b.email,
		b.arrived_to_lat,
		b.arrived_to_lon
	FROM 
		car c 
	JOIN 
		booking b
		ON c.license_plate = b.license_plate
		AND car_make = 'Volvo'
		AND model_name = 'C30'
	ORDER BY booking_number DESC
	LIMIT 5) AS b_info
JOIN
	customer
	ON customer.email = b_info.email
LEFT OUTER JOIN
	carcurrentlyat cat
	ON cat.license_plate = b_info.license_plate
JOIN
	parkingSpot arrived
	ON b_info.arrived_to_lat = arrived.latitude
	AND b_info.arrived_to_lon = arrived.longitude
JOIN
	parkingSpot currently
	ON cat.latitude = currently.latitude
	AND cat.longitude = currently.longitude
	
	
	@author Ivan M, Yuzhou G
*/

public class MillionJoinOperation extends Operation {
	
	public MillionJoinOperation(String shortName, String desc) {
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
	        
	        String preparedS = "SELECT\n" + 
	        		"	b_info.license_plate,\n" + 
	        		"	customer.name,\n" + 
	        		"	b_info.arrived_to_lat,\n" + 
	        		"	b_info.arrived_to_lon,\n" + 
	        		"	arrived.name as arrived_name,\n" + 
	        		"	currently\n" + 
	        		"FROM\n" + 
	        		"\n" + 
	        		"	(SELECT \n" + 
	        		"		b.license_plate,\n" + 
	        		"		b.email,\n" + 
	        		"		b.arrived_to_lat,\n" + 
	        		"		b.arrived_to_lon\n" + 
	        		"	FROM \n" + 
	        		"		car c \n" + 
	        		"	JOIN \n" + 
	        		"		booking b\n" + 
	        		"		ON c.license_plate = b.license_plate\n" + 
	        		"		AND car_make = ?\n" + 
	        		"		AND model_name = ?\n" + 
	        		"	ORDER BY booking_number DESC\n" + 
	        		"	LIMIT 5) AS b_info\n" + 
	        		"JOIN\n" + 
	        		"	customer\n" + 
	        		"	ON customer.email = b_info.email\n" + 
	        		"LEFT OUTER JOIN\n" + 
	        		"	carcurrentlyat cat\n" + 
	        		"	ON cat.license_plate = b_info.license_plate\n" + 
	        		"JOIN\n" + 
	        		"	parkingSpot arrived\n" + 
	        		"	ON b_info.arrived_to_lat = arrived.latitude\n" + 
	        		"	AND b_info.arrived_to_lon = arrived.longitude\n" + 
	        		"JOIN\n" + 
	        		"	parkingSpot currently\n" + 
	        		"	ON cat.latitude = currently.latitude\n" + 
	        		"	AND cat.longitude = currently.longitude";
	        
	        String prepared1 = "SELECT distinct car_make FROM car";
	        String prepared2 = "SELECT distinct model_name FROM car";
	        
	        PreparedStatement preparedCid = null;
	        
	        // Add in all the car_make and model_name into two associated arraylists
			try {
				preparedCid = con.prepareStatement(prepared1);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			ArrayList<String> modellist = new ArrayList<String>();
			ArrayList<String> carlist = new ArrayList<String>();
			ResultSet result = preparedCid.executeQuery();
		    ResultSetMetaData rsmd = result.getMetaData();
	        
	        while(result.next()) {
	        	carlist.add(result.getString(rsmd.getColumnName(1)));
	        }
	        
	        try {
				preparedCid = con.prepareStatement(prepared2);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
	        result = preparedCid.executeQuery();
		    rsmd = result.getMetaData();
	        
	        while(result.next()) {
	        	modellist.add(result.getString(rsmd.getColumnName(1)));
	        }
			
			// Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
	        
	        System.out.println("Please enter the car name: ");
	        String car = scc.nextLine();
			
			for(int i=car.length(); i<40; i++) {
				car = car + " ";
			}
			
			boolean exists = false;
			for(String temp: carlist) {
				if(temp.equals(car)) exists = true;
			}
			
			while(!exists) {
				System.out.println("Sorry, the car you entered is not in our car list, please try again: ");
				car = scc.nextLine();
				for(int i=car.length(); i<40; i++) {
					car = car + " ";
				}
				for(String temp: carlist) {
					if(temp.equals(car)) exists = true;
				}
			}
			
			System.out.println("You're requesting information of the car: " + car);
	        
	        System.out.println("Please enter the model name: ");
	        String model = scc.nextLine();
			
			for(int i=model.length(); i<40; i++) {
				model = model + " ";
			}
			
			//using the same boolean variable
			exists = false;
			for(String temp: modellist) {
				if(temp.equals(model)) exists = true;
			}
			
			while(!exists) {
				System.out.println("Sorry, the model you entered is not in our car list, please try again: ");
				model = scc.nextLine();
				for(int i=model.length(); i<40; i++) {
					model = model + " ";
				}
				for(String temp: modellist) {
					if(temp.equals(model)) exists = true;
				}
			}
			
			System.out.println("You're requesting information of the model: " + model);
			
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			// Set these two variables inside the query
			preparedCid.setString(1, car);
			preparedCid.setString(2, model);
			
			//System.out.println(preparedCid);
		        
	        result = preparedCid.executeQuery();
	        rsmd = result.getMetaData();
	        
	        while(result.next()) {
	        	System.out.println("\n" + rsmd.getColumnName(1) + ":   " + result.getString(rsmd.getColumnName(1)) +
	        			"\n" + rsmd.getColumnName(2) + ":            " + result.getString(rsmd.getColumnName(2)) + 
	        			"\n" + rsmd.getColumnName(3) + ":  " + result.getString(rsmd.getColumnName(3)) +
	        			"\n" + rsmd.getColumnName(4) + ":  " + result.getString(rsmd.getColumnName(4)) +
	        			"\n" + rsmd.getColumnName(5) + ":    " + result.getString(rsmd.getColumnName(5)) +
	        			"\n" + rsmd.getColumnName(6) + ":       " + result.getString(rsmd.getColumnName(6)));
	        }	       	  
	        
	        // Don't forget to close
	        result.close();
	        preparedCid.close();
	        statement.close();
	        con.close();
	}

}
