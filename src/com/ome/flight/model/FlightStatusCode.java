package com.ome.flight.model;

public enum FlightStatusCode implements Status<FlightStatusCode> {
	DV("Flight Diverted"), 
	CD("Flight Cancelled"), 
	DP("Flight Departed"), 
	LD("Flight Landed"), 
	RT("Flight Rerouted"), 
	NA("No status");
	
	private String definition;
	
	private FlightStatusCode (final String definition) {
		this.definition = definition;
	}

	@Override
	public FlightStatusCode code () {
		return this;
	}

	@Override
	public String definition () {
		return this.definition;
	}
}
