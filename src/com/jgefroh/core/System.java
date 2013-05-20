package com.jgefroh.core;


import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * This is a default implementation of a system. It does not do anything.
 * @author Joseph Gefroh
 */
public class System implements ISystem
{	
	//////////
	// DATA
	//////////
	/**A reference to the core engine controlling this system.*/
	private Core core;
	
	/**Flag that shows whether the system is running or not.*/
	private boolean isRunning;
	
	/**Logger for debug purposes.*/
	private final static Logger LOGGER 
		= Logger.getLogger(System.class.getName());
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.FINE;
	
	private String name;
	
	//////////
	// INIT
	//////////
	
	/**
	 * Initialize the Logger with default settings.
	 */
	private void initLogger()
	{
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(debugLevel);
		LOGGER.addHandler(ch);
		LOGGER.setLevel(debugLevel);
		LOGGER.setUseParentHandlers(false);
	}
	
	
	//////////
	// ISYSTEM INTERFACE
	//////////
	@Override
	public void init()
	{
		initLogger();
		isRunning = true;
	}
	
	@Override
	public void start()
	{
		LOGGER.log(Level.INFO, "System started.");
		isRunning = true;
	}

	@Override
	public void work()
	{		
		if(isRunning)
		{			
		}
	}

	@Override
	public void stop()
	{
		LOGGER.log(Level.INFO, "System stopped.");
		isRunning = false;
	}

	
	
	//////////
	// SYSTEM METHODS
	//////////
}
