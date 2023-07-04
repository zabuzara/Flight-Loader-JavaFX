package com.ome.flight.model;

import java.util.Map;
import java.util.Objects;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class TimeStatus {
	private Status<TimeStatusCode> statusCode;
	private String statusDefinition;
	
	/**
	 * Initializes a new instance
	 * @param timeStatusDataMap
	 */
	public TimeStatus (final Map<String,Object> timeStatusDataMap) {
		if (timeStatusDataMap.containsKey("TimeStatus") ||
			timeStatusDataMap.containsKey("Terminal"))
			throw new IllegalArgumentException("invalid timestatus data");
		
		this.statusCode =  TimeStatusCode.valueOf(Objects.toString(timeStatusDataMap.get("Code")));
		this.statusDefinition = this.statusCode.definition();
	}
	
	
	/**
	 * Returns the statusCode
	 * @return the statusCode
	 */
	public Status<TimeStatusCode> getStatusCode () {
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
