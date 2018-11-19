package com.jpm.trade.reportengine.exception;

/**
 * Represent exception object of Report Engine project.
 *
 * @author Sajadh
 */
public class ReportEngineException extends Exception {

	private static final long serialVersionUID = -127734488231399779L;

	/**
	 * creates a new reportEngine Exception.
	 *
	 * @param message   the message
	 * @param exception the exception
	 */
	public ReportEngineException(final String message, final Exception exception) {
		super(message, exception);
	}

}
