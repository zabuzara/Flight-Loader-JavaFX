package com.ome.flight.model;

import java.util.Map;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class Airport {
	static public enum Usage {CIVIL, MILITARY}	
	private Usage usage = Usage.CIVIL;	
	private String iata;
	private String icao = "unknown";
	private String city  = "unknown";
	private String state = "unknown";
	private String name  = "unknown";
	private Terminal terminal;
	
	
	/**
	 * Initializes a new instance
	 * @param airportCode
	 * @param terminalDataMap
	 */
	public Airport (final String airportCode, final Map<String,Object> terminalDataMap) {
		if (airportCode == null) 
			throw new IllegalArgumentException("invalid airport data");
		if (terminalDataMap != null)
			this.terminal = new Terminal(terminalDataMap);
		else 
			this.terminal = new Terminal();
		
		if (Airports.getAirport(airportCode) == null) {
			this.iata = airportCode;
		} else {
			this.iata = Airports.getAirport(airportCode).iata();
			this.icao = Airports.getAirport(airportCode).icao();
			this.city = Airports.getAirport(airportCode).city();
			this.state = Airports.getAirport(airportCode).state();
			this.name = Airports.getAirport(airportCode).airportName();
		}
	}


	/**
	 * Returns the usage
	 * @return the usage
	 */
	public Usage getUsage () {
		return this.usage;
	}


	/**
	 * Returns the iata
	 * @return the iata
	 */
	public String getIata () {
		return this.iata;
	}


	/**
	 * Returns the icao
	 * @return the icao
	 */
	public String getIcao () {
		return this.icao;
	}


	/**
	 * Returns the city
	 * @return the city
	 */
	public String getCity () {
		return this.city;
	}
	
	
	/**
	 * Sets the city
	 * @param city
	 */
	public void setCity (final String city) {
		this.city = city;
	}


	/**
	 * Returns the state
	 * @return the state
	 */
	public String getState () {
		return this.state;
	}


	/**
	 * Returns the name
	 * @return the name
	 */
	public String getName () {
		return this.name;
	}

	
	/**
	 * Sets the name
	 * @param name
	 */
	public void setName (final String name) {
		this.name = name;
	}

	/**
	 * Returns the terminal
	 * @return the terminal
	 */
	public Terminal getTerminal () {
		return this.terminal;
	}
}