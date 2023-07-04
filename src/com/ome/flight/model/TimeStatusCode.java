package com.ome.flight.model;

public enum TimeStatusCode implements Status<TimeStatusCode> {
	FE("Flight Early"), 
	NI("Next Information"), 
	OT("Flight On Time"), 
	DL("Flight Delayed"), 
	NO("No statusF");
	
	private String definition;
	
	private TimeStatusCode (final String definition) {
		this.definition = definition;
	}

	@Override
	public TimeStatusCode code () {
		return this;
	}

	@Override
	public String definition () {
		return this.definition;
	}
}
