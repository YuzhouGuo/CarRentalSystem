/**
-- Check whether a given car is currently on a trip - error if this is not exactly one row (none : not on a trip, 2+ : database error)
SELECT booking_number as foundNumber FROM booking WHERE license_plate = '2I3 D9D' AND (arrived_to_lat IS NULL OR arrived_to_lon IS NULL);

-- Find lat/lon of the parkingspot, error if not found
SELECT 
	latitude, longitude
FROM parkingspot
WHERE name = 'Namur Station';

-- Use the given values to add a currentlyAt
INSERT INTO carcurrentlyat VALUES (
		'2I3 D9D', 
		?latitude, 
		?longitude
)

-- Use the given values to save booking
UPDATE booking SET arrived_to_lat = ?latitude, arrived_to_lon = ?longitude WHERE booking_number = ?foundNumber
*/
public class ReturnCarOperation extends Operation {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
