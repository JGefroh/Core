package com.jgefroh.core;


import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * System that implements defaults.
 * 
 * 
 * @author Joseph Gefroh
 */
public abstract class AbstractSystem implements ISystem
{
	//////////
	// DATA
	//////////
	
	/**Flag that shows whether the system is running or not.*/
	@SuppressWarnings("unused")
	private boolean isRunning;
	
	/**The time to wait between executions of the system.*/
	private long waitTime;
	
	/**The time this System was last executed, in ms.*/
	private long last;
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.INFO;
	
	/**Logger for debug purposes.*/
	private final Logger LOGGER 
		= LoggerFactory.getLogger(this.getClass(), Level.ALL);


	/////////
	// ISYSTEM INTERFACE
	/////////
	@Override
	public abstract void init();
	
	@Override
	public void start()
	{
		isRunning = true;
	}

	@Override
	public abstract void work(final long now);

	@Override
	public void stop()
	{
		isRunning = false;
	}
	
	@Override
	public long getWait()
	{
		return this.waitTime;
	}

	@Override
	public long	getLast()
	{
		return this.last;
	}
	
	@Override
	public void setWait(final long waitTime)
	{
		this.waitTime = waitTime;
	}
	
	@Override
	public void setLast(final long last)
	{
		this.last = last;
	}
	
	@Override
	public abstract void recv(final String id, final String... message);
	/////////
	// SYSTEM METHODS
	/////////
	
	/**
	 * Sets the debug level of this {@code System}.
	 * @param level	the Level to set
	 */
	public void setDebugLevel(final Level level)
	{
		this.LOGGER.setLevel(level);
	}
}
