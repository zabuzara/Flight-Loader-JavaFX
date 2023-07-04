package com.ome.flight.model;

import java.util.Map;
import java.util.Objects;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class Flight {
	private String airlineId;
	private String flightNumber;
	private String serviceType;
	private Equipment equipment;
	private FlightStatus flightStatus;
	private Departure departure;
	private Arrival arrival;
	
	/**
	 * Initializes a new instance
	 * @param flightDataMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Flight (final Map<String,Object> flightDataMap) {
		if (flightDataMap == null ||
			!flightDataMap.containsKey("OperatingCarrier") ||
			!flightDataMap.containsKey("Departure") ||
			!flightDataMap.containsKey("Arrival") ||
			!flightDataMap.containsKey("Equipment") ||
			!flightDataMap.containsKey("OperatingCarrier") ||
			!flightDataMap.containsKey("FlightStatus") ||
			!flightDataMap.containsKey("ServiceType"))
			throw new IllegalArgumentException("invalid flight data");
			
		final Map<String,Object> operationCarrierDataMap = (Map) flightDataMap.get("OperatingCarrier");
		if (operationCarrierDataMap.containsKey("AirlineID"))
			this.airlineId = Objects.toString(operationCarrierDataMap.get("AirlineID"));
		if (operationCarrierDataMap.containsKey("FlightNumber"))
			this.flightNumber = Objects.toString(operationCarrierDataMap.get("FlightNumber"));
		if (flightDataMap.containsKey("ServiceType"))
			this.serviceType = Objects.toString(flightDataMap.get("ServiceType"));
		
		this.flightStatus = new FlightStatus((Map) flightDataMap.get("FlightStatus"));
		this.equipment = new Equipment((Map) flightDataMap.get("Equipment"));
		this.departure = new Departure((Map) flightDataMap.get("Departure"));
		this.arrival = new Arrival((Map) flightDataMap.get("Arrival"));
	}

	/**
	 * Returns the airlineId
	 * @return the airlineId
	 */
	public String getAirlineId () { return this.airlineId; }
	
	/**
	 * Returns the flightNumber
	 * @return the flightNumber
	 */
	public String getFlightNumber () { return this.flightNumber; }
	
	/**
	 * Returns the serviceType
	 * @return the serviceType
	 */
	public String getServiceType () { return this.serviceType; }
	
	/**
	 * Returns the equipment
	 * @return the equipment
	 */
	public Equipment getEquipment () { return this.equipment; }
	
	/**
	 * Returns the flightStatus
	 * @return the flightStatus
	 */
	public FlightStatus getFlightStatus () { return this.flightStatus; }
	
	/**
	 * Returns the departure
	 * @return the departure
	 */
	public Departure getDeparture () { return this.departure; }
	
	/**
	 * Returns the arrival
	 * @return the arrival
	 */
	public Arrival getArrival () { return this.arrival; }
}