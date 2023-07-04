package com.ome.flight.model;

import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class Gate {
	private String name = "unknown";
	
	
	/**
	 * Initializes a instance
	 */
	public Gate () {
		this("unknown");
	}
	
	
	/**
	 * Inizializes a new instance
	 * @param gateName
	 */
	public Gate (final String name) {
		this.name = name;
	}
	
	
	/**
	 * Returns the name
	 * @return the name
	 */
	public String getName () {
		return this.name;
	}
}