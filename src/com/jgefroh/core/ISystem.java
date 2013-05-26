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
	 */
	public void work();
	
	/**
	 * Sets the flag that marks this {@code System} as not ready.
	 * This method is called automatically by {@code Core} when it stops 
	 * tracking this {@code System}.
	 */
	public void stop();
	
	/**
	 * Tells this {@code System} to do something.
	 * This is an experimental feature to test cross-system communication.
	 */
	//public String send(final String message);
	
	/**
	 * Gets the name of this {@code System}.
	 * This is an experimental feature to test cross-system communication.
	 * It facilitates obtaining a reference to a {@code System} without knowing
	 * the exact class name.
	 */
	//public void getName(final String name);
}
