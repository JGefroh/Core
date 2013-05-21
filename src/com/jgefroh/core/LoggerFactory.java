package com.jgefroh.core;


import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Provides an easy way to create an instance-specific logger for debugging.
 * 
 * <p>
 * This is purely for convenience and to reduce unnecessary code duplication.
 * </p>
 * 
 * @author Joseph Gefroh
 * @version 0.1.0
 * @since 20MAY13
 */
public abstract class LoggerFactory
{	
	//////////
	// DATA
	//////////
	/**
	 * Returns a Logger object for the class and the specified debug level.
	 * @param type			the Class type of the object the Logger is watching
	 * @param debugLevel	the Level of the debug messages that are printed	
	 * @return				a Logger with the given settings
	 */
	public static Logger getLogger(final Class type, final Level debugLevel)
	{
		Logger LOGGER = Logger.getLogger(type.getName());
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(debugLevel);
		LOGGER.addHandler(ch);
		LOGGER.setLevel(debugLevel);
		LOGGER.setUseParentHandlers(false);
		return LOGGER;
	}
}
