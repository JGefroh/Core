package com.jgefroh.core;


import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Provides an easy way to create an instance-specific logger.
 * 
 * This is purely for convenience and to reduce unnecessary code duplication.
 * @author Joseph Gefroh
 */
public class LoggerFactory
{	
	//////////
	// DATA
	//////////
	
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
