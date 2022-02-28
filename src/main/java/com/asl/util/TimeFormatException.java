package com.asl.util;

/**
 * @author Zubayer Ahamed
 * @since 09-May-2020
 *
 */
public class TimeFormatException extends Exception {

	private static final long serialVersionUID = 3775124978394079634L;

	/**
	 * Creates a new instance of <code>TimeFormatException</code> without detail
	 * message.
	 */
	public TimeFormatException() {
	}

	/**
	 * Constructs an instance of <code>TimeFormatException</code> with the specified
	 * detail message.
	 * 
	 * @param msg Detail message.
	 */
	public TimeFormatException(String msg) {
		super(msg);
	}
}
