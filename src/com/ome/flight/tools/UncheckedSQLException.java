package com.ome.flight.tools;

import java.sql.SQLException;

 
/**
 * Unchecked exception wrapper for SQL exceptions.
 * @author Sascha Baumeister
 */
public class UncheckedSQLException extends RuntimeException {
	static private final long serialVersionUID = 1L;

	public UncheckedSQLException () {
		super();
	}

	@SuppressWarnings("exports")
	public UncheckedSQLException (final String message, final SQLException cause) {
		super(message, cause);
	}

	public UncheckedSQLException (final String message) {
		super(message);
	}

	@SuppressWarnings("exports")
	public UncheckedSQLException (final SQLException cause) {
		super(cause);
	}


	@SuppressWarnings("exports")
	@Override
	public SQLException getCause () {
		return (SQLException) super.getCause();
	}


	@Override
	public Throwable initCause (Throwable cause) {
		if (!(cause instanceof SQLException)) throw new IllegalArgumentException();
		return super.initCause(cause);
	}
}