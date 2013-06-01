package com.jgefroh.core;

/**
 * The interface for a {@code System}.
 * 
 * <p>
 * A {@code System} provides a specific piece of functionality by providing the
 * logic that manipulates the data in certain {@code Components}.
 * </p>
 * 
 * <p>
 * {@code Systems} use {@code InfoPacks} in order to access the data that
 * specific {@code Components} contain. {@code Systems} should attempt to
 * minimize their dependencies on other {@code Systems} and focus on providing
 * a single, small piece of functionality as part of an overall "assembly line"
 * of {@code Systems}.
 * </p>
 * 
 * @author 	Joseph Gefroh
 * @see		IInfoPack
 * @version 0.1.0
 * @since	0.1.0
 */
public interface ISystem
{
	/**
	 * Initializes this {@code System} with default values.
	 */
	public void init();
	
	/**
	 * Sets the flag that marks this {@code System} as ready.
	 * This method is called automatically by {@code Core} when it begins
	 * tracking this {@code System}.
	 */
	public void start();
	
	/**
	 * Executes this {@code System's} work if it is ready.
	 * This method is called automatically by {@code Core}.
	 * @param now the current time
	 */
	public void work(final long now);
	
	/**
	 * Sets the flag that marks this {@code System} as not ready.
	 * This method is called automatically by {@code Core} when it stops 
	 * tracking this {@code System}.
	 */
	public void stop();
	
	/**
	 * Sets the number of ms this {@code System} will wait between executions.
	 * @param waitTime the number of ms to wait before doing work
	 */
	public void setWait(final long waitTime);
	
	/**
	 * Sets the time this {@code System} was last run.
	 * @param	last	the time this System was last run, in ms
	 */
	public void setLast(final long last);
	
	/**
	 * Gets the number of ms this {@code System} between executions.
	 */
	public long getWait();
	
	/**
	 * Gets the time, in ms, this {@code System} was last executed.
	 * @return	the time, in ms, of last execution
	 */
	public long getLast();
	
	/**
	 * Sends a message to this {@code System}.
	 * @param message	the message to send
	 */
	public void recv(final String id, final String... message);
}
