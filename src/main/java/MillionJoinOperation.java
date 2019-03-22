import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

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
	        		"		AND car_make = 'Volvo'\n" + 
	        		"		AND model_name = 'C30'\n" + 
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
	        
	        PreparedStatement preparedCid = null;
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			// Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
			
			//System.out.println(preparedCid);
		        
	        ResultSet result = preparedCid.executeQuery();
	        ResultSetMetaData rsmd = result.getMetaData();
	        
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
