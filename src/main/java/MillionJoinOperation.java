/**
SELECT
	b_info.license_plate,
	customer.name,
	b_info.arrived_to_lat,
	b_info.arrived_to_lon,
	arrived.name as arrived_name,
	currently.*
FROM
	-- b_info partial table
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
*/
public class MillionJoinOperation extends Operation {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
