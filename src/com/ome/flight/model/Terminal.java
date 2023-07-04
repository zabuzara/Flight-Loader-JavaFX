package com.ome.flight.model;

import java.util.Map;
import java.util.Objects;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public class Terminal {
	private String name = "unknown";
	private Gate gate;

	
	/**
	 * Initializes a instance
	 */
	public Terminal () {
		this(null);
	}
	
	
	/**
	 * Initializes a instance
	 * @param terminalDataMap
	 */
	public Terminal (final Map<String,Object> terminalDataMap) {
		if (terminalDataMap != null && terminalDataMap.containsKey("Name")) {
			this.name = Objects.toString(terminalDataMap.get("Name"));
		}
		
		if (terminalDataMap != null && terminalDataMap.containsKey("Gate")) {
			this.gate = new Gate(Objects.toString(terminalDataMap.get("Gate")));
		} else {
			this.gate = new Gate();
		}
	}

	
	/**
	 * Returns the name
	 * @return the name
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Returns the gate
	 * @return the gate
	 */
	public Gate getGate () {
		return this.gate;
	}
}
