package com.ome.flight.model;

import java.util.Map;
import java.util.Objects;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class Arrival {
	private Long scheduledTimeLocal;
	private Long scheduledTimeUTC;
	private Long estimatedTimeLocal = 1000000000000L;
	private Long estimatedTimeUTC = 1000000000000L;
	private Long actualTimeLocal = 1000000000000L;
	private Long actualTimeUTC = 1000000000000L;
	private TimeStatus timeStatus;
	private Airport airport;
	

	/**
	 * Initializes a new instance
	 * @param arrivalDataMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Arrival (final Map<String,Object> arrivalDataMap) {
		if (arrivalDataMap == null ||
			!arrivalDataMap.containsKey("TimeStatus") ||
			!arrivalDataMap.containsKey("AirportCode"))
			throw new IllegalArgumentException("invalid arrival data");
		
		this.timeStatus = new TimeStatus((Map) arrivalDataMap.get("TimeStatus"));
		
		if (arrivalDataMap.containsKey("Terminal"))
			this.airport = new Airport(Objects.toString(arrivalDataMap.get("AirportCode")), (Map) arrivalDataMap.get("Terminal"));	
		else 
			this.airport = new Airport(Objects.toString(arrivalDataMap.get("AirportCode")), null);	

		if (arrivalDataMap.containsKey("ScheduledTimeLocal"))
			this.scheduledTimeLocal = FlightDateTime.localDateTimeToMillis(Objects.toString(((Map) arrivalDataMap.get("ScheduledTimeLocal")).get("DateTime")));
		if (arrivalDataMap.containsKey("ScheduledTimeUTC"))
			this.scheduledTimeUTC = FlightDateTime.utcDateTimeToMillis(Objects.toString(((Map) arrivalDataMap.get("ScheduledTimeUTC")).get("DateTime")));
		if (arrivalDataMap.containsKey("EstimatedTimeLocal"))
			this.estimatedTimeLocal = FlightDateTime.localDateTimeToMillis(Objects.toString(((Map) arrivalDataMap.get("EstimatedTimeLocal")).get("DateTime")));
		if (arrivalDataMap.containsKey("EstimatedTimeUTC"))
			this.estimatedTimeUTC = FlightDateTime.utcDateTimeToMillis(Objects.toString(((Map) arrivalDataMap.get("EstimatedTimeUTC")).get("DateTime")));
		if (arrivalDataMap.containsKey("ActualTimeLocal"))
			this.actualTimeLocal = FlightDateTime.localDateTimeToMillis(Objects.toString(((Map) arrivalDataMap.get("ActualTimeLocal")).get("DateTime")));
		if (arrivalDataMap.containsKey("ActualTimeUTC"))
			this.actualTimeUTC = FlightDateTime.utcDateTimeToMillis(Objects.toString(((Map) arrivalDataMap.get("ActualTimeUTC")).get("DateTime")));

	}


	/**
	 * Returns the scheduledTimeLocal
	 * @return the scheduledTimeLocal
	 */
	public Long getScheduledTimeLocal () {
		return this.scheduledTimeLocal;
	}


	/**
	 * Returns the scheduledTimeUTC
	 * @return the scheduledTimeUTC
	 */
	public Long getScheduledTimeUTC () {
		return this.scheduledTimeUTC;
	}


	/**
	 * Returns the estimatedTimeLocal
	 * @return the estimatedTimeLocal
	 */
	public Long getEstimatedTimeLocal () {
		return this.estimatedTimeLocal;
	}


	/**
	 * Returns the estimatedTimeUTC
	 * @return the estimatedTimeUTC
	 */
	public Long getEstimatedTimeUTC () {
		return this.estimatedTimeUTC;
	}


	/**
	 * Returns the actualTimeLocal
	 * @return the actualTimeLocal
	 */
	public Long getActualTimeLocal () {
		return this.actualTimeLocal;
	}

	
	/**
	 * Returns the actualTimeUTC
	 * @return the actualTimeUTC
	 */
	public Long getActualTimeUTC () {
		return this.actualTimeUTC;
	}


	/**
	 * Returns the timeStatus
	 * @return the timeStatus
	 */
	public TimeStatus getTimeStatus () {
		return this.timeStatus;
	}


	/**
	 * Returns the airport
	 * @return the airport
	 */
	public Airport getAirport () {
		return this.airport;
	}
}