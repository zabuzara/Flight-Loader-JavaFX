package com.ome.flight.model;

import java.util.Map;
import java.util.Objects;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class Equipment {
	private String aircraftCode = "unknown";
	private String aircraftRegistration = "unknown";
	
	
	/**
	 * Initializes a new instance
	 * @param equipmentDataMap
	 */
	public Equipment (final Map<String,Object> equipmentDataMap) {
		if (equipmentDataMap != null && equipmentDataMap.containsKey("AircraftCode"))
			this.aircraftCode = Objects.toString(equipmentDataMap.get("AircraftCode"));
		if (equipmentDataMap != null && equipmentDataMap.containsKey("AircraftRegistration"))
			this.aircraftRegistration = Objects.toString(equipmentDataMap.get("AircraftRegistration"));
	}


	/**
	 * Returns the aircraftCode
	 * @return the aircraftCode
	 */
	public String getAircraftCode () {
		return this.aircraftCode;
	}


	/**
	 * Returns the aircraftRegistration
	 * @return the aircraftRegistration
	 */
	public String getAircraftRegistration () {
		return this.aircraftRegistration;
	}
}
