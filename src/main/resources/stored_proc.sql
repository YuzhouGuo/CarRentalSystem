\d

SELECT * FROM car WHERE mileage > 200000;
SELECT * FROM carcurrentlyat WHERE license_plate IN (
    SELECT license_plate FROM car WHERE mileage > 200000);

DROP FUNCTION retireCars(INT);

CREATE OR REPLACE FUNCTION retireCars(maxMileage INT)
RETURNS TABLE (license_plate CHAR(8)) AS $$

DECLARE
    v_car RECORD;

    cur_car CURSOR FOR SELECT mileage, car.license_plate FROM car;

BEGIN
    CREATE TABLE IF NOT EXISTS retiredCar (
        license_plate CHAR(8) NOT NULL,
	    car_make CHAR(40) NOT NULL,
	    model_name CHAR(40) NOT NULL,
	    mileage INTEGER DEFAULT 0,
	    gas NUMERIC(4, 1) DEFAULT 0,
	    PRIMARY KEY (license_plate),
	    FOREIGN KEY (car_make, model_name) REFERENCES model
    );

    OPEN cur_car;

    LOOP

        FETCH cur_car INTO v_car;
        EXIT WHEN NOT FOUND;
        IF (v_car.mileage > maxMileage) THEN 
            INSERT INTO retiredCar (
                SELECT * FROM car WHERE car.license_plate = v_car.license_plate
            );
            DELETE FROM carcurrentlyat WHERE carcurrentlyat.license_plate = v_car.license_plate;
            UPDATE booking SET license_plate = NULL WHERE booking.license_plate = v_car.license_plate;
            DELETE FROM car WHERE CURRENT OF cur_car;
        END IF;
    END LOOP;
    CLOSE cur_car;

    RETURN QUERY SELECT retiredCar.license_plate FROM retiredCar;
END; $$

LANGUAGE 'plpgsql';

-- Retires cars have more than 200,000 mileage
SELECT retireCars(200000);

\d

SELECT * FROM retiredcar;

SELECT * FROM car WHERE mileage > 200000;
