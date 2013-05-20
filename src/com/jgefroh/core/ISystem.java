package com.jgefroh.core;

/**
 * The interface for Systems used by the Core.
 * @author Joseph Gefroh
 * @version 0.1.0
 * @since	16MAY13
 */
public interface ISystem
{
	/**
	 * Initialize the system with default values.
	 * This should be called when the system is created.
	 */
	public void init();
	
	/**
	 * Mark the system as runnable.
	 * This is called automatically when the Core begins tracking the system.
	 */
	public void start();
	
	/**
	 * Execute the system's work if runnable.
	 * This is called automatically every time the Core does work.
	 */
	public void work();
	
	/**
	 * Mark the system as not runnable.
	 * This is called automatically when the Core stops tracking the system.
	 */
	public void stop();
	
	/**
	 * Tell the system to do something. A response can also be received.
	 * This is an experimental feature to test cross-system communication.
	 */
	//public String send(final String message);
	
	/**
	 * Get the name of the system.
	 * This is an experimental feature to test cross-system communication.
	 * It facilitates obtaining a reference to a system without knowing
	 * the exact class name.
	 */
	//public void getName(final String name);
}
