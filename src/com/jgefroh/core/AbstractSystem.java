package com.jgefroh.core;


import java.util.HashMap;


/**
 * An abstract system that provides basic functionality.
 * 
 * 
 * <p>Extend this {@code System} to make it easier to write {@code Systems}.</p>
 * 
 * @author Joseph Gefroh
 */
public abstract class AbstractSystem implements ISystem
{

	//////////
	// DATA
	//////////
	
	/**Flag that shows whether the system is running or not.*/
	private boolean isRunning;
	
	/**The time to wait between executions of the system.*/
	private long waitTime;
	
	/**The time this System was last executed, in ms.*/
	private long last;


	/////////
	// ISYSTEM INTERFACE
	/////////
	@Override
	public void init()
	{
	}
	
	@Override
	public void start()
	{
		isRunning = true;
	}

	@Override
	public void work(final long now)
	{
	}

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
	public void recv(final String id, final String... message)
	{
		
	}
	
	@Override
	public void recv(final String id, final HashMap<String, String> map)
	{	
	}
	
	@Override
	public boolean isRunning()
	{
		return this.isRunning;
	}
	
	/////////
	// SYSTEM METHODS
	/////////
}
