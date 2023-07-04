SET CHARACTER SET utf8mb4;
DROP SCHEMA IF EXISTS flight;
CREATE SCHEMA IF NOT EXISTS flight DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE flight ;

-- Error function
DROP FUNCTION IF EXISTS flight.GET_ERROR_MESSAGE;
DELIMITER //
CREATE FUNCTION flight.GET_ERROR_MESSAGE (errorCode INT) RETURNS VARCHAR(255)
NO SQL
BEGIN
    CASE 
		WHEN errorCode=1 THEN RETURN "Invalid parameter.";
		ELSE RETURN "Unknown Error";
	END CASE;
END ;
//
DELIMITER ;

-- IS_EMPTY function
DROP FUNCTION IF EXISTS flight.IS_EMPTY;
DELIMITER //
CREATE FUNCTION flight.IS_EMPTY (argument VARCHAR(255)) RETURNS BOOLEAN
NO SQL
BEGIN 
	RETURN LENGTH(TRIM(argument)) = 0;
END ;
//
DELIMITER ;

-- Equipment table
DROP TABLE IF EXISTS flight.Equipment;
CREATE TABLE flight.Equipment (
    equipmentIdentity BIGINT NOT NULL AUTO_INCREMENT,
    aircraftCode VARCHAR(45) NOT NULL,
    aircraftRegistration VARCHAR(45),
    PRIMARY KEY (equipmentIdentity)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Flight table
DROP TABLE IF EXISTS flight.Flight;
CREATE TABLE IF NOT EXISTS flight.Flight (
    flightIdentity BIGINT NOT NULL AUTO_INCREMENT,
    equipmentReference BIGINT NOT NULL,
    airlineId VARCHAR(16) NOT NULL,
    flightNumber VARCHAR(20) NOT NULL,
    serviceType VARCHAR(45) NOT NULL,
    PRIMARY KEY (flightIdentity),
    FOREIGN KEY (equipmentReference) REFERENCES flight.Equipment (equipmentIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- FlightStatus table
DROP TABLE IF EXISTS flight.FlightStatus;
CREATE TABLE IF NOT EXISTS flight.FlightStatus (
    flightStatusIdentity BIGINT NOT NULL AUTO_INCREMENT,
    statusCode ENUM("CD","DV", "DP", "LD", "RT", "NA") NOT NULL,
    statusDefinition VARCHAR(16) NOT NULL,
    PRIMARY KEY (flightStatusIdentity)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

INSERT INTO flight.FlightStatus VALUES 
(null, "CD", "Flight Cancelled"),
(null, "DP", "Flight Departed"),
(null, "LD", "Flight Landed"),
(null, "RT", "Flight Rerouted"),
(null, "DV", "Flight Diverted"),
(null, "NA", "No status");

-- FlightFlightStatusAssociation table
DROP TABLE IF EXISTS flight.FlightFlightStatusAssociation;
CREATE TABLE IF NOT EXISTS flight.FlightFlightStatusAssociation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    flightReference BIGINT NOT NULL,
    flightStatusReference BIGINT NOT NULL,
    creationTimestamp BIGINT NOT NULL,   
	PRIMARY KEY (id),
    FOREIGN KEY (flightReference) REFERENCES flight.Flight (flightIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (flightStatusReference) REFERENCES flight.FlightStatus (flightStatusIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Airport table
DROP TABLE IF EXISTS flight.Airport;
CREATE TABLE IF NOT EXISTS flight.Airport (
    airportIdentity BIGINT NOT NULL AUTO_INCREMENT,
    airportUsage ENUM ("CIVIL", "MILITARY") NOT NULL,
    iataCode CHAR(3) NOT NULL,
    icaoCode VARCHAR(45),
    city VARCHAR(128) NOT NULL,
    state VARCHAR(128),
    airportName VARCHAR(255),
    PRIMARY KEY (airportIdentity)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Terminal table
DROP TABLE IF EXISTS flight.Terminal;
CREATE TABLE IF NOT EXISTS flight.Terminal (
    terminalIdentity BIGINT NOT NULL AUTO_INCREMENT,
    airportReference BIGINT NOT NULL,
    terminalName VARCHAR(45),
	PRIMARY KEY (terminalIdentity),
	FOREIGN KEY (airportReference) REFERENCES flight.Airport (airportIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Gate table
DROP TABLE IF EXISTS flight.Gate;
CREATE TABLE IF NOT EXISTS flight.Gate (
    gateIdentity BIGINT NOT NULL AUTO_INCREMENT,
	terminalReference BIGINT NOT NULL,
    gateName VARCHAR(45),
	PRIMARY KEY (gateIdentity),
	FOREIGN KEY (terminalReference) REFERENCES flight.Terminal (terminalIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Departure table
DROP TABLE IF EXISTS flight.Departure;
CREATE TABLE IF NOT EXISTS flight.Departure ( 
    departureIdentity BIGINT NOT NULL AUTO_INCREMENT,
    flightReference BIGINT NOT NULL,
    scheduledTimeLocal BIGINT NOT NULL,
    scheduledTimeUTC BIGINT NOT NULL,
    estimatedTimeLocal BIGINT,
    estimatedTimeUTC BIGINT,
    actualTimeLocal BIGINT,
    actualTimeUTC BIGINT,
    PRIMARY KEY(departureIdentity),
    FOREIGN KEY (flightReference) REFERENCES flight.Flight (flightIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Arrival table
DROP TABLE IF EXISTS flight.Arrival;
CREATE TABLE IF NOT EXISTS flight.Arrival (
    arrivalIdentity BIGINT NOT NULL AUTO_INCREMENT,
    flightReference BIGINT NOT NULL,
    scheduledTimeLocal BIGINT NOT NULL,
    scheduledTimeUTC BIGINT NOT NULL,
    estimatedTimeLocal BIGINT,
    estimatedTimeUTC BIGINT,
    actualTimeLocal BIGINT,
    actualTimeUTC BIGINT,
	PRIMARY KEY(arrivalIdentity),
    FOREIGN KEY (flightReference) REFERENCES flight.Flight (flightIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- DepartureAirportAssociation table
DROP TABLE IF EXISTS flight.DepartureGateAssociation;
CREATE TABLE IF NOT EXISTS flight.DepartureGateAssociation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    departureReference BIGINT NOT NULL,
    gateReference BIGINT NOT NULL,
    creationTimestamp BIGINT NOT NULL,   
	PRIMARY KEY(id),
    FOREIGN KEY (departureReference) REFERENCES flight.Departure (departureIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (gateReference) REFERENCES flight.Gate (gateIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- ArrivalAirportAssociation table
DROP TABLE IF EXISTS flight.ArrivalGateAssociation;
CREATE TABLE IF NOT EXISTS flight.ArrivalGateAssociation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    arrivalReference BIGINT NOT NULL,
    gateReference BIGINT NOT NULL,
    creationTimestamp BIGINT NOT NULL,   
	PRIMARY KEY(id),
    FOREIGN KEY (arrivalReference) REFERENCES flight.Arrival (arrivalIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (gateReference) REFERENCES flight.Gate (gateIdentity) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- TimeStatus table
DROP TABLE IF EXISTS flight.TimeStatus;
CREATE TABLE IF NOT EXISTS flight.TimeStatus (
    timeStatusIdentity BIGINT NOT NULL AUTO_INCREMENT,
    statusCode ENUM("FE", "NI", "OT", "DL", "NO") NOT NULL,
    statusDefinition VARCHAR(16),
    PRIMARY KEY(timeStatusIdentity)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

INSERT INTO flight.TimeStatus VALUES 
(null, "FE", "Flight Early"),
(null, "NI", "Next Information"),
(null, "OT", "Flight On Time"),
(null, "DL", "Flight Delayed"),
(null, "NO", "No status");

-- ArrivalTimeStatusAssociation table
DROP TABLE IF EXISTS flight.ArrivalTimeStatusAssociation;
CREATE TABLE IF NOT EXISTS flight.ArrivalTimeStatusAssociation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    arrivalReference BIGINT NOT NULL,
    timeStatusReference BIGINT NOT NULL,
    creationTimestamp BIGINT NOT NULL,
    FOREIGN KEY (arrivalReference) REFERENCES flight.Arrival (arrivalIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (timeStatusReference) REFERENCES flight.TimeStatus (timeStatusIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- DepartureTimeStatusAssociation table
DROP TABLE IF EXISTS flight.DepartureTimeStatusAssociation;
CREATE TABLE IF NOT EXISTS flight.DepartureTimeStatusAssociation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    departureReference BIGINT NOT NULL,
    timeStatusReference BIGINT NOT NULL,
    creationTimestamp BIGINT NOT NULL,
    FOREIGN KEY (departureReference) REFERENCES flight.Departure (departureIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (timeStatusReference) REFERENCES flight.TimeStatus (timeStatusIdentity) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- INSERT_FLIGHT Procedure
DROP PROCEDURE IF EXISTS flight.PROCESS_FLIGHT;
DELIMITER //
CREATE PROCEDURE flight.PROCESS_FLIGHT
(
/*Equipemnt*/			aircraftCode VARCHAR(45), aircraftRegistration VARCHAR(45),
/*Flight*/				airlineId VARCHAR(16), flightNumber VARCHAR(20), serviceType VARCHAR(45),
/*FlightStatus*/ 		flightStatusCode CHAR(2), flightStatusDefinition VARCHAR(16),
/*DepartureAirport*/	departureAirportUsage VARCHAR(8), departureIataCode CHAR(3), departureIcaoCode VARCHAR(45), departureCity VARCHAR(128), departureState VARCHAR(128), departureAirportName VARCHAR(255),
/*ArrivalAirport*/		arrivalAirportUsage VARCHAR(8), arrivalIataCode CHAR(3), arrivalIcaoCode VARCHAR(45), arrivalCity VARCHAR(128), arrivalState VARCHAR(128), arrivalAirportName VARCHAR(255),
/*DepartureTerminal*/	departureGate VARCHAR(45), departureTerminal VARCHAR(45),
/*Departure*/			departureScheduledTimeLocal BIGINT, departureScheduledTimeUTC BIGINT, departureEstimatedTimeLocal BIGINT, departureEstimatedTimeUTC BIGINT, departureActualTimeLocal BIGINT, departureActualTimeUTC BIGINT,
/*ArrivalTerminal*/		arrivalGate VARCHAR(45), arrivalTerminal VARCHAR(45),
/*Arrival*/				arrivalScheduledTimeLocal BIGINT, arrivalScheduledTimeUTC BIGINT, arrivalEstimatedTimeLocal BIGINT, arrivalEstimatedTimeUTC BIGINT, arrivalActualTimeLocal BIGINT, arrivalActualTimeUTC BIGINT,
/*DepartureTimeStatus*/	departureTimeStatusCode CHAR(2), departureTimeStatusDefinition VARCHAR(16),
/*ArrivalTimeStatus*/	arrivalTimeStatusCode CHAR(2), arrivalTimeStatusDefinition VARCHAR(16)
) 
processFlight:BEGIN
	DECLARE departureAirportExists TINYINT DEFAULT 0;
	DECLARE arrivalAirportExists TINYINT DEFAULT 0;
	DECLARE departureTerminalExists TINYINT DEFAULT 0;
	DECLARE arrivalTerminalExists TINYINT DEFAULT 0;
	DECLARE departureGateExists TINYINT DEFAULT 0;
	DECLARE arrivalGateExists TINYINT DEFAULT 0;
	DECLARE equipmentExists TINYINT DEFAULT 0;
	DECLARE flightExists TINYINT DEFAULT 0;
	DECLARE arrivalExists TINYINT DEFAULT 0;
	DECLARE departureExists TINYINT DEFAULT 0;
   		
	DECLARE departure_airport_primary_key BIGINT DEFAULT 0;
	DECLARE arrival_airport_primary_key BIGINT DEFAULT 0;
	DECLARE departure_terminal_primary_key BIGINT DEFAULT 0;
	DECLARE arrival_terminal_primary_key BIGINT DEFAULT 0;
	DECLARE departure_gate_primary_key BIGINT DEFAULT 0;
	DECLARE arrival_gate_primary_key BIGINT DEFAULT 0;
	DECLARE departure_primary_key  BIGINT DEFAULT 0;
	DECLARE arrival_primary_key BIGINT DEFAULT 0;
	DECLARE flight_status_primary_key BIGINT DEFAULT 0;
	DECLARE equipment_primary_key BIGINT DEFAULT 0;
	DECLARE flight_primary_key BIGINT DEFAULT 0;
	DECLARE flight_flightstatus_association_primary_key BIGINT DEFAULT 0;
	DECLARE departure_gate_association_primary_key BIGINT DEFAULT 0;
	DECLARE arrival_gate_association_primary_key BIGINT DEFAULT 0;
	DECLARE departure_timestatus_association_primary_key BIGINT DEFAULT 0;
	DECLARE arrival_timestatus_association_primary_key BIGINT DEFAULT 0;
    
	IF 
		(LENGTH(aircraftCode) > 45 OR IS_EMPTY(aircraftRegistration) OR LENGTH(aircraftRegistration) > 45) OR
		(LENGTH(airlineId) > 16  OR IS_EMPTY(flightNumber) OR LENGTH(flightNumber) > 20 OR IS_EMPTY(serviceType) OR LENGTH(serviceType) > 45) OR
		(LENGTH(flightStatusCode) <> 2 OR flightStatusCode NOT IN("DV", "CD", "DP", "LD", "RT", "NA") OR IS_EMPTY(flightStatusDefinition) OR LENGTH(flightStatusDefinition) > 16) OR
		(IS_EMPTY(departureAirportUsage) OR LENGTH(departureAirportUsage) > 8 OR departureAirportUsage NOT IN("CIVIL", "MILITARY") OR LENGTH(departureIataCode) <> 3 OR LENGTH(departureIcaoCode) > 45 OR IS_EMPTY(departureCity) OR LENGTH(departureCity) > 128 OR IS_EMPTY(departureState) OR LENGTH(departureState) > 128 OR IS_EMPTY(departureAirportName) OR LENGTH(departureAirportName) > 255) OR
		(IS_EMPTY(departureGate) OR LENGTH(departureGate) > 45 OR IS_EMPTY(departureTerminal) OR LENGTH(departureTerminal) > 45) OR
		(departureScheduledTimeLocal = NULL OR LENGTH(departureScheduledTimeLocal) < 10 OR departureScheduledTimeUTC = NULL OR LENGTH(departureScheduledTimeUTC) < 10 OR (departureEstimatedTimeLocal = NULL OR LENGTH(departureEstimatedTimeLocal) < 10) OR (departureEstimatedTimeUTC = NULL OR LENGTH(departureEstimatedTimeUTC) < 10) OR (departureActualTimeLocal = NULL OR LENGTH(departureActualTimeLocal) < 10) OR (departureActualTimeUTC = NULL OR LENGTH(departureActualTimeUTC) < 10)) OR
		(LENGTH(departureTimeStatusCode) <> 2 OR departureTimeStatusCode NOT IN("FE", "NI", "OT", "DL", "NO") OR IS_EMPTY(departureTimeStatusDefinition) OR LENGTH(departureTimeStatusDefinition) > 16) OR
		(IS_EMPTY(arrivalAirportUsage) OR LENGTH(arrivalAirportUsage) > 8 OR arrivalAirportUsage NOT IN("CIVIL", "MILITARY") OR LENGTH(arrivalIataCode) <> 3 OR LENGTH(arrivalIcaoCode) > 45 OR IS_EMPTY(arrivalCity) OR LENGTH(arrivalCity) > 128 OR IS_EMPTY(arrivalState) OR LENGTH(arrivalState) > 128 OR IS_EMPTY(arrivalAirportName) OR LENGTH(arrivalAirportName) > 255) OR
		(IS_EMPTY(arrivalGate) OR LENGTH(arrivalGate) > 45 OR IS_EMPTY(arrivalTerminal) OR LENGTH(arrivalTerminal) > 45) OR
		(arrivalScheduledTimeLocal = NULL OR LENGTH(arrivalScheduledTimeLocal) < 10 OR arrivalScheduledTimeUTC = NULL OR LENGTH(arrivalScheduledTimeUTC) < 10 OR (arrivalEstimatedTimeLocal = NULL OR LENGTH(arrivalEstimatedTimeLocal) < 10) OR (arrivalEstimatedTimeUTC = NULL OR LENGTH(arrivalEstimatedTimeUTC) < 10) OR (arrivalActualTimeLocal = NULL OR LENGTH(arrivalActualTimeLocal) < 10) OR (arrivalActualTimeUTC = NULL OR LENGTH(arrivalActualTimeUTC) < 10)) OR
		(LENGTH(arrivalTimeStatusCode) <> 2 OR arrivalTimeStatusCode NOT IN("FE", "NI", "OT", "DL", "NO") OR IS_EMPTY(arrivalTimeStatusDefinition) OR LENGTH(arrivalTimeStatusDefinition) > 16)
	THEN
		SELECT flight.GET_ERROR_MESSAGE(1) AS "message";
    	ELSE
		SET SESSION TRANSACTION ISOLATION LEVEL  READ UNCOMMITTED;
		START TRANSACTION;
		
		-- Departure airport Exists check
		SELECT COUNT(*) INTO @departureAirportExists FROM flight.Airport 
        WHERE 
			flight.Airport.iataCode = departureIataCode AND
			flight.Airport.city = departureCity AND
			departureIataCode != "unknown" AND
			departureCity != "unknown";

		IF @departureAirportExists = 0 
		THEN 
			-- insert in Airport Table
			INSERT INTO flight.Airport VALUES (null, departureAirportUsage, departureIataCode, departureIcaoCode, departureCity, departureState, departureAirportName);
			SELECT LAST_INSERT_ID() INTO @departure_airport_primary_key;
		ELSE 
			-- get primary id from Airport
			SELECT flight.Airport.airportIdentity INTO @departure_airport_primary_key FROM flight.Airport 
			WHERE 
				flight.Airport.iataCode = departureIataCode AND
		    	flight.Airport.city = departureCity AND
		    	departureIataCode != "unknown" AND
				departureCity != "unknown";
		END IF;
		
		IF @departure_airport_primary_key = 0
		THEN
			ROLLBACK;            
			SELECT "rolledback Nr.0" AS "message";
			LEAVE processFlight;
		END IF;
        
        -- Arrival airport Exists check
		SELECT COUNT(*) INTO @arrivalAirportExists FROM flight.Airport 
        WHERE 
			flight.Airport.iataCode = arrivalIataCode AND
			flight.Airport.city = arrivalCity AND
	    	arrivalIataCode != "unknown" AND
			arrivalCity != "unknown";

		IF @arrivalAirportExists = 0 
		THEN 
			-- insert in Airport Table
			INSERT INTO flight.Airport VALUES (null, arrivalAirportUsage, arrivalIataCode, arrivalIcaoCode, arrivalCity, arrivalState, arrivalAirportName);
			SELECT LAST_INSERT_ID() INTO @arrival_airport_primary_key;
		ELSE 
			-- get primary id from Airport
			SELECT flight.Airport.airportIdentity INTO @arrival_airport_primary_key FROM flight.Airport 
			WHERE 
				flight.Airport.iataCode = arrivalIataCode AND
			    flight.Airport.city = arrivalCity AND
		    	arrivalIataCode != "unknown" AND
				arrivalCity != "unknown";
		END IF;

		IF @arrival_airport_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.1" AS "message";
			LEAVE processFlight;
		END IF;
		
		-- Terminal Exists check
		SELECT COUNT(*) INTO @departureTerminalExists FROM flight.Terminal 
		WHERE 
			flight.Terminal.terminalName = departureTerminal AND
			flight.Terminal.airportReference = @departure_airport_primary_key;

		IF @departureTerminalExists = 0 
		THEN 
			-- insert in Terminal Table
			INSERT INTO flight.Terminal VALUES (null, @departure_airport_primary_key, departureTerminal);
			SELECT LAST_INSERT_ID() INTO @departure_terminal_primary_key;
		ELSE 
			-- get primary id from Terminal
			SELECT flight.Terminal.terminalIdentity INTO @departure_terminal_primary_key FROM flight.Terminal 
			WHERE 
				flight.Terminal.terminalName = departureTerminal AND
		    	flight.Terminal.airportReference = @departure_airport_primary_key;
		END IF;

		IF @departure_terminal_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.2" AS "message";
			LEAVE processFlight;
		END IF;
		
		-- Terminal Exists check
		SELECT COUNT(*) INTO @arrivalTerminalExists FROM flight.Terminal 
		WHERE 
			flight.Terminal.terminalName = arrivalTerminal AND
			flight.Terminal.airportReference = @arrival_airport_primary_key;

		IF @arrivalTerminalExists = 0 
		THEN 
			-- insert in Terminal Table
			INSERT INTO flight.Terminal VALUES (null, @arrival_airport_primary_key, arrivalTerminal);
			SELECT LAST_INSERT_ID() INTO @arrival_terminal_primary_key;
			
		ELSE 
			-- get primary id from Terminals
			SELECT flight.Terminal.terminalIdentity INTO @arrival_terminal_primary_key FROM flight.Terminal 
			WHERE 
				flight.Terminal.terminalName = arrivalTerminal AND
		    	flight.Terminal.airportReference = @arrival_airport_primary_key;
		END IF;

		IF @arrival_terminal_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.3" AS "message";
			LEAVE processFlight;
		END IF;
		
		-- Gate Exists check
		
		SELECT COUNT(*) INTO @departureGateExists FROM flight.Gate 
		WHERE 
			flight.Gate.gateName = departureGate AND
			flight.Gate.terminalReference = @departure_terminal_primary_key;

		IF @departureGateExists = 0 
		THEN 
			-- insert in Gate Table
			INSERT INTO flight.Gate VALUES (null, @departure_terminal_primary_key, departureGate);
			SELECT LAST_INSERT_ID() INTO @departure_gate_primary_key;
		ELSE 
			-- get primary id from Gate
			SELECT flight.Gate.gateIdentity INTO @departure_gate_primary_key FROM flight.Gate 
			WHERE 
				flight.Gate.gateName = departureGate AND
		    	flight.Gate.terminalReference = @departure_terminal_primary_key;
		END IF;	

		IF @departure_gate_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.4" AS "message";
			LEAVE processFlight;
		END IF;
			
		-- Gate Exists check
		SELECT COUNT(*) INTO @arrivalGateExists FROM flight.Gate 
		WHERE 
			flight.Gate.gateName = arrivalGate AND
			flight.Gate.terminalReference = @arrival_terminal_primary_key;

		IF @arrivalGateExists = 0 
		THEN 
			-- insert in Gate Table
			INSERT INTO flight.Gate VALUES (null, @arrival_terminal_primary_key, arrivalGate);
			SELECT LAST_INSERT_ID() INTO @arrival_gate_primary_key;
		ELSE 
			-- get primary id from Gate
			SELECT flight.Gate.gateIdentity INTO @arrival_gate_primary_key FROM flight.Gate 
			WHERE 
				flight.Gate.gateName = arrivalGate AND
		    	flight.Gate.terminalReference = @arrival_terminal_primary_key;
		END IF;

		IF @arrival_gate_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.5" AS "message";
			LEAVE processFlight;
		END IF;	
			
		-- get primary id from flightstatus
		SELECT flight.FlightStatus.flightStatusIdentity INTO @flight_status_primary_key FROM flight.FlightStatus 
		WHERE 
			flight.FlightStatus.statusCode = flightStatusCode AND
	    	flight.FlightStatus.statusDefinition = flightStatusDefinition;

		IF @flight_status_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.6" AS "message";
			LEAVE processFlight;
		END IF;	
		
		-- Equipment Exists check
		SELECT COUNT(*) INTO @equipmentExists FROM flight.Equipment 
		WHERE 
			flight.Equipment.aircraftCode = aircraftCode AND
			flight.Equipment.aircraftRegistration = aircraftRegistration;

		IF @equipmentExists = 0 
		THEN 
			-- insert in Equipment Table
			INSERT INTO flight.Equipment VALUES (null, aircraftCode, aircraftRegistration);
			SELECT LAST_INSERT_ID() INTO @equipment_primary_key;
		ELSE 
			-- get primary id from Equipment
			SELECT flight.Equipment.equipmentIdentity INTO @equipment_primary_key FROM flight.Equipment 
			WHERE 
				flight.Equipment.aircraftCode = aircraftCode AND
				flight.Equipment.aircraftRegistration = aircraftRegistration;
		END IF;

		IF @equipment_primary_key = 0
		THEN
			ROLLBACK;
			SELECT "rolledback Nr.7" AS "message";
			LEAVE processFlight;
		END IF;	
		
		-- Flight Exists check
		SELECT COUNT(*) INTO @flightExists 
		FROM 
			flight.Flight, flight.Arrival, flight.Departure
		WHERE 
			(flight.Flight.flightIdentity = flight.Arrival.flightReference AND
			flight.Arrival.scheduledTimeLocal = arrivalScheduledTimeLocal AND
			flight.Arrival.scheduledTimeUTC = arrivalScheduledTimeUTC) AND
			(flight.Flight.flightIdentity = flight.Departure.flightReference AND
			flight.Departure.scheduledTimeLocal = departureScheduledTimeLocal AND
			flight.Departure.scheduledTimeUTC = departureScheduledTimeUTC);
		
		IF @flightExists = 0 
		THEN 
			-- insert in Flight Table
			INSERT INTO flight.Flight VALUES (null, @equipment_primary_key, airlineId, flightNumber, serviceType);
			SELECT LAST_INSERT_ID() INTO @flight_primary_key;
			
			-- inesrt in FlightFlightStatusAssociation
			INSERT INTO flight.FlightFlightStatusAssociation VALUES(null, @flight_primary_key, @flight_status_primary_key, UNIX_TIMESTAMP());
			SELECT LAST_INSERT_ID() INTO @flight_flightstatus_association_primary_key;
		
			-- insert in Departure
			INSERT INTO flight.Departure VALUES(null, @flight_primary_key, departureScheduledTimeLocal, departureScheduledTimeUTC, IFNULL(departureEstimatedTimeLocal, 0), IFNULL(departureEstimatedTimeUTC,0), IFNULL(departureActualTimeLocal,0), IFNULL(departureActualTimeUTC,0));
			SELECT LAST_INSERT_ID() INTO @departure_primary_key;
			
			-- insert in Arrival
			INSERT INTO flight.Arrival VALUES(null, @flight_primary_key, arrivalScheduledTimeLocal, arrivalScheduledTimeUTC, IFNULL(arrivalEstimatedTimeLocal,0), IFNULL(arrivalEstimatedTimeUTC,0), IFNULL(arrivalActualTimeLocal,0), IFNULL(arrivalActualTimeUTC,0));
			SELECT LAST_INSERT_ID() INTO @arrival_primary_key;
			
			-- inesrt in DepartureGateAssociation
			INSERT INTO flight.DepartureGateAssociation VALUES(null, @departure_primary_key, @departure_gate_primary_key, UNIX_TIMESTAMP());
			SELECT LAST_INSERT_ID() INTO @departure_gate_association_primary_key;
			
			-- inesrt in ArrivalGateAssociation
			INSERT INTO flight.ArrivalGateAssociation VALUES(null, @arrival_primary_key, @arrival_gate_primary_key, UNIX_TIMESTAMP());
			SELECT LAST_INSERT_ID() INTO @arrival_gate_association_primary_key;
			
			-- inesrt in DepartureTimeStatusAssociation
			INSERT INTO flight.DepartureTimeStatusAssociation VALUES(null, @departure_primary_key, (SELECT timeStatusIdentity FROM flight.TimeStatus WHERE flight.TimeStatus.statusCode <=> departureTimeStatusCode), UNIX_TIMESTAMP());
			SELECT LAST_INSERT_ID() INTO @departure_timestatus_association_primary_key;
			
			-- inesrt in ArrivalTimeStatusAssociation
			INSERT INTO flight.ArrivalTimeStatusAssociation VALUES(null, @arrival_primary_key, (SELECT timeStatusIdentity FROM flight.TimeStatus WHERE flight.TimeStatus.statusCode <=> arrivalTimeStatusCode), UNIX_TIMESTAMP());
			SELECT LAST_INSERT_ID() INTO @arrival_timestatus_association_primary_key;
		
			IF 
				@flight_primary_key = 0 OR
				@flight_flightstatus_association_primary_key = 0 OR
				@departure_primary_key = 0 OR
				@arrival_primary_key = 0 OR
				@departure_gate_association_primary_key = 0 OR
				@arrival_gate_association_primary_key = 0 OR
				@departure_timestatus_association_primary_key = 0 OR
				@arrival_timestatus_association_primary_key = 0
			THEN
				ROLLBACK;
				SELECT "rolledback Nr.8" AS "message";
				LEAVE processFlight;
			END IF;	
		
		ELSE 
			-- get primary id from Flight
			SELECT flight.Flight.flightIdentity INTO @flight_primary_key FROM flight.Flight, flight.Equipment
			WHERE 
				flight.Flight.airlineId = airlineId AND
				flight.Flight.flightNumber = flightNumber AND
				flight.Equipment.equipmentIdentity = flight.Flight.equipmentReference;
			
			-- update FlightStatus if necessary
			UPDATE flight.FlightFlightStatusAssociation 
			SET flight.FlightFlightStatusAssociation.flightStatusReference = (SELECT flight.FlightStatus.flightStatusIdentity FROM flight.FlightStatus WHERE flight.FlightStatus.statusCode = flightStatusCode) 
			WHERE 
				flight.FlightFlightStatusAssociation.flightReference = @flight_primary_key AND
				flight.FlightFlightStatusAssociation.flightStatusReference = (SELECT flight.FlightStatus.flightStatusIdentity FROM flight.FlightStatus WHERE flight.FlightStatus.statusCode = flightStatusCode);
			
			-- update Departure if necessary
			UPDATE flight.Departure 
			SET flight.Departure.estimatedTimeLocal = departureEstimatedTimeLocal,
				flight.Departure.estimatedTimeUTC = departureEstimatedTimeUTC,
				flight.Departure.actualTimeLocal = departureActualTimeLocal,
				flight.Departure.actualTimeUTC = departureActualTimeUTC
			WHERE 
				flight.Departure.flightReference = @flight_primary_key AND
				flight.Departure.scheduledTimeLocal = departureScheduledTimeLocal AND
				flight.Departure.scheduledTimeUTC = departureScheduledTimeUTC;
				
			-- update DepartureTimeStatusAssociation if necessary
			UPDATE flight.DepartureTimeStatusAssociation 
			SET flight.DepartureTimeStatusAssociation.timeStatusReference = (SELECT flight.TimeStatus.timeStatusIdentity FROM flight.TimeStatus WHERE flight.TimeStatus.statusCode = departureTimeStatusCode) 
			WHERE
				flight.DepartureTimeStatusAssociation.departureReference = (SELECT flight.Departure.departureIdentity FROM flight.Departure WHERE flight.Departure.flightReference = @flight_primary_key);
				
			-- update Arrival if necessary
			UPDATE flight.Arrival 
			SET flight.Arrival.estimatedTimeLocal = arrivalEstimatedTimeLocal,
				flight.Arrival.estimatedTimeUTC = arrivalEstimatedTimeUTC,
				flight.Arrival.actualTimeLocal = arrivalActualTimeLocal,
				flight.Arrival.actualTimeUTC = arrivalActualTimeUTC
			WHERE 
				flight.Arrival.flightReference = @flight_primary_key AND
				flight.Arrival.scheduledTimeLocal = arrivalScheduledTimeLocal AND
				flight.Arrival.scheduledTimeUTC = arrivalScheduledTimeUTC;
				
			-- update ArrivalTimeStatusAssociation if necessary
			UPDATE flight.ArrivalTimeStatusAssociation 
			SET flight.ArrivalTimeStatusAssociation.timeStatusReference = (SELECT flight.TimeStatus.timeStatusIdentity FROM flight.TimeStatus WHERE flight.TimeStatus.statusCode = arrivalTimeStatusCode) 
			WHERE
				flight.ArrivalTimeStatusAssociation.arrivalReference = (SELECT flight.Arrival.arrivalIdentity FROM flight.Arrival WHERE flight.Arrival.flightReference = @flight_primary_key);
		END IF;
					
		COMMIT;
		SELECT "commited" AS "message";	
	END IF;
END ;
//
DELIMITER ;

-- DELETE_ROWS_FROM_ALL_TABLES Procedure
DROP PROCEDURE IF EXISTS flight.DELETE_ROWS_FROM_ALL_TABLES;
DELIMITER //
CREATE PROCEDURE flight.DELETE_ROWS_FROM_ALL_TABLES ()
BEGIN
	DELETE FROM flight.FlightFlightStatusAssociation WHERE id >= 0;
	DELETE FROM flight.DepartureTimeStatusAssociation WHERE id >= 0;
	DELETE FROM flight.ArrivalTimeStatusAssociation WHERE id >= 0;
	DELETE FROM flight.DepartureGateAssociation WHERE id >= 0;
	DELETE FROM flight.ArrivalGateAssociation WHERE id >= 0;
	DELETE FROM flight.Departure WHERE departureIdentity >= 0;
	DELETE FROM flight.Arrival WHERE arrivalIdentity >= 0;
	DELETE FROM flight.Gate WHERE gateIdentity >= 0;
	DELETE FROM flight.Terminal WHERE terminalIdentity >= 0;
	DELETE FROM flight.Airport WHERE airportIdentity >= 0;
	DELETE FROM flight.Flight WHERE flightIdentity >= 0;
	DELETE FROM flight.Equipment WHERE equipmentIdentity >= 0;
END ;
//
DELIMITER ;

-- SHOW_ALL_ROWS_FROM_TABLES Procedure
DROP PROCEDURE IF EXISTS flight.SHOW_ALL_ROWS_FROM_TABLES;
DELIMITER //
CREATE PROCEDURE flight.SHOW_ALL_ROWS_FROM_TABLES ()
BEGIN
	SELECT * FROM flight.Equipment;
	SELECT * FROM flight.Flight;
	SELECT * FROM flight.FlightStatus;
	SELECT * FROM flight.FlightFlightStatusAssociation;
	SELECT * FROM flight.DepartureGateAssociation;
	SELECT * FROM flight.ArrivalGateAssociation;
	SELECT * FROM flight.Airport;
	SELECT * FROM flight.Terminal;
	SELECT * FROM flight.Gate;
	SELECT * FROM flight.Departure;
	SELECT * FROM flight.Arrival;
	SELECT * FROM flight.TimeStatus;
	SELECT * FROM flight.DepartureTimeStatusAssociation;
	SELECT * FROM flight.ArrivalTimeStatusAssociation;
END ;
//
DELIMITER ;

CREATE USER IF NOT EXISTS 'flight'@'localhost' IDENTIFIED BY "e214da75c5915050e47f5779ed5a2063c8b00508903879c1cd7c788bdb4feaa1";
GRANT EXECUTE ON PROCEDURE flight.PROCESS_FLIGHT TO 'flight'@'localhost';