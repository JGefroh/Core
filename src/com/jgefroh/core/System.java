package com.jgefroh.core;


import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * This is an abstract implementation of a {@code System}.
 * 
 * @author Joseph Gefroh
 * @version 0.1.1
 * @since 	20MAY13
 */
public abstract class System implements ISystem
{	
	//////////
	// DATA
	//////////
	/**A reference to the core engine controlling this system.*/
	private Core core;
	
	/**Flag that shows whether the system is running or not.*/
	private boolean isRunning;
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.FINE;
	
	/**Logger for debug purposes.*/
	private final Logger LOGGER 
		= LoggerFactory.getLogger(this.getClass(), debugLevel);
	
	private String name;
	
	
	//////////
	// ISYSTEM INTERFACE
	//////////
	/**
	 * Creates an instance of this {@code System}.
	 * @param core	a reference to the Core controlling this system
	 */
	public System(final Core core)
	{
		this.core = core;
	}
	
	@Override
	public void init()
	{
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
