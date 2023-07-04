package com.ome.flight.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import com.ome.flight.tools.Copyright;

@Copyright(year = 2022, holders = "Omid Malekzadeh Eshtajarani")
public abstract class FlightDateTime {

	/**
	 * Converts the localDateTime to milliseconds
	 * @param localDateTime
	 * @return the milliseconds
	 */
	static public Long localDateTimeToMillis (final String localDateTime) {
		if (localDateTime != null & localDateTime.equals("null")) return null;
		return Timestamp.valueOf(LocalDateTime.parse(localDateTime)).getTime();
	}
	
	
	/**
	 * Converts the utcDateTime {@code UTC} to milliseconds
	 * @param utcDateTime
	 * @return the milliseconds
	 */
	static public Long utcDateTimeToMillis (final String utcDateTime) {
		if (utcDateTime != null & utcDateTime.equals("null")) return null;
		return Timestamp.valueOf(OffsetDateTime.parse(utcDateTime).toZonedDateTime().toLocalDateTime()).getTime();
	}
	
	
	/**
	 * Converts the milliseconds to localDateTime  
	 * @param millis 
	 * @return the string of localDateTime
	 */
	static public String millisToLocalDateTime (final long millis) {
		final Instant instant = Instant.ofEpochMilli(millis);
		final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		return zonedDateTime.toLocalDateTime().toString();
	} 
	
	
	/**
	 * Converts the milliseconds to zonedDateTime {@code UTC}  
	 * @param millis 
	 * @return the string of zonedDateTime
	 */
	static public String millisToUtcDateTime (final long millis) {
		final Instant instant = Instant.ofEpochMilli(millis);
		final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
		final ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		return zonedDateTime.format(formatter);
	} 
}