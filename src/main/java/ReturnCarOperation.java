import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
-- Check whether a given car is currently on a trip - error if this is not exactly one row (none : not on a trip, 2+ : database error)
SELECT booking_number as foundNumber 
FROM booking 
WHERE license_plate = '2I3 D9D' 
AND (arrived_to_lat IS NULL OR arrived_to_lon IS NULL);

-- Find lat/lon of the parkingSpot, error if not found
SELECT 
	latitude, longitude
FROM parkingSpot
WHERE name = 'Namur Station';

-- Use the given values to add a currentlyAt
INSERT INTO carcurrentlyat VALUES (
		'2I3 D9D', 
		?latitude, 
		?longitude
)

-- Use the given values to save booking
UPDATE booking SET arrived_to_lat = ?latitude, arrived_to_lon = ?longitude WHERE booking_number = ?foundNumber


@author Ivan M, Yuzhou G, cyyze
*/
public class ReturnCarOperation extends Operation {
	
	public ReturnCarOperation(String shortName, String desc) {
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
	        
	        /**
	         * SELECT booking_number as foundNumber 
				FROM booking 
				WHERE license_plate = '2I3 D9D' 
				AND (arrived_to_lat IS NULL OR arrived_to_lon IS NULL);
	         * SELECT 
				latitude, longitude
				FROM parkingSpot
				WHERE name = 'Namur Station';
				
			   INSERT INTO carcurrentlyat VALUES (
					'2I3 D9D', 
					?latitude, 
					?longitude
				)
	         */
	        
	        String preparedS = "SELECT booking_number as foundNumber FROM booking " + 
	        					"WHERE license_plate = ? AND (arrived_to_lat IS NULL OR arrived_to_lon IS NULL)";
	        //System.out.println(preparedS);
	        String preparedI = "INSERT INTO carcurrentlyat VALUES (" +
	        					"	?" +
	        					",	?" + 
	        					",	?" +
	        					")";
	        		
	        
	        PreparedStatement preparedCid = null;
			try {
				preparedCid = con.prepareStatement(preparedS);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
			
			// Introduce what does this Operation do to the user (first print)
	        System.out.println(this.description);
	        
	        // Request license plate from the customer
			System.out.println("Please enter the car license plate: ");
			String license = scc.nextLine();
			//System.out.println(license);
			
			preparedCid.setString(1, license);
			
			System.out.println(preparedCid);
		        
	        ResultSet result = preparedCid.executeQuery();
	        
	        int count = 0;        
	        ResultSetMetaData rsmd = result.getMetaData();
	        int num;
	        
	        while(result.next()) {
	        	count++;
	        	num = result.getInt(rsmd.getColumnName(1));
	        	System.out.println("\nThe booking number is " + num);
	        }
	        
	        while(count == 0) {
	        	System.out.println("The car you entered does not have a correct booking number! Please try again: ");
	        	license = scc.nextLine();
	        	preparedCid.setString(1, license);
	        	result = preparedCid.executeQuery();
	        	rsmd = result.getMetaData();
	        	
	        	count=0;
	        	while(result.next()) {
		        	count++;
		        	num = result.getInt(rsmd.getColumnName(1));
		        	System.out.println("\nThe booking number is " + num);
		        }
	        }
	        
	        String preparedS2 = "SELECT" + 
	        		"	latitude, longitude " + 
	        		"FROM parkingspot " + 
	        		"WHERE name = ?";
	        try {
				preparedCid = con.prepareStatement(preparedS2);
			} catch (SQLException e) {
				e.printStackTrace();				
			}
	        
	        System.out.println("Please enter the parking spot that you want to place the car:");
	        String spot = scc.nextLine();
	        System.out.println(spot);
	        
	        preparedCid.setString(1, spot);
	        
	        result = preparedCid.executeQuery();
	        rsmd = result.getMetaData();
	        
	        count = 0;        
	        double lat = 0;
	        double lot = 0;
	        
	        while(result.next()) {
	        	count++;
	        	lat = result.getDouble(rsmd.getColumnName(1));
	        	lot = result.getDouble(rsmd.getColumnName(2));
	        	System.out.println("\nThe latitude is: " + lat + ", The longitude is: "+lot);
	        }
	        
	        while(count == 0) {
	        	System.out.println("The parking spot you entered does not exist! Please try again: ");
	        	license = scc.nextLine();
	        	preparedCid.setString(1, license);
	        	result = preparedCid.executeQuery();
	        	rsmd = result.getMetaData();
	        	
	        	count=0;
	        	while(result.next()) {
	        		count++;
		        	lat = result.getDouble(rsmd.getColumnName(1));
		        	lot = result.getDouble(rsmd.getColumnName(2));
		        	System.out.println("\nThe latitude is: " + lat + ", The longitude is: "+lot);
		        }
	        }
	        
	        try {
				preparedCid = con.prepareStatement(preparedI);
	        
	        preparedCid.setString(1, license);
	        preparedCid.setDouble(2, lat);
	        preparedCid.setDouble(3, lot);
	        
	        preparedCid.executeUpdate();
	       
	        } catch (SQLException e) {
				e.printStackTrace();				
			}

	        System.out.println("\nThe car "+ license +" has been placed at latitude: " + lat + ", longitude: " + lot);
	        // Don't forget to close
	        result.close();
	        preparedCid.close();
	        statement.close();
	        con.close();
	}

}
