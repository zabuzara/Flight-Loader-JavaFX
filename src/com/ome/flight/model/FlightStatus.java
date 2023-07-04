package com.ome.flight.model;

import java.util.Map;
import java.util.Objects;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class FlightStatus {
	private Status<FlightStatusCode> statusCode;
	private String statusDefinition;
	
	
	/**
	 * Intializes a new instance
	 * @param flightStatusDataMap
	 */
	public FlightStatus (final Map<String,Object> flightStatusDataMap) {
		if (flightStatusDataMap == null || 
			!flightStatusDataMap.containsKey("Code") || 
			!flightStatusDataMap.containsKey("Definition"))
			throw new IllegalArgumentException("invalid flightstatus data");
		this.statusCode = FlightStatusCode.valueOf(Objects.toString(flightStatusDataMap.get("Code")));
		this.statusDefinition = this.statusCode.definition();
	}


	/**
	 * Returns the statusCode
	 * @return the statusCode
	 */
	public Status<FlightStatusCode> getStatusCode () {
		return this.statusCode;
	}


	/**
	 * Returns the statusDefinition
	 * @return the statusDefinition
	 */
	public String getStatusDefinition () {
		return this.statusDefinition;
	}
}