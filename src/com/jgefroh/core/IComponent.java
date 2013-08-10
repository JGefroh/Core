package com.jgefroh.core;


/**
 * The interface for a {@code Component}.
 * 
 * <p>
 * A {@code Component} holds all of the data needed for a specific feature
 * or function. The data in a {@code Component} is manipulated by 
 * {@code Systems} through {@code InfoPacks}.
 * </p>
 * 
 * <p>
 * As a rule, {@code Components} should not contain any logic. They should be
 * entirely data. The only methods a {@code Component} should have are
 * accessor methods (getters/setters) to retrieve its data and appropriate
 * constructors and initializers.
 * </p>
 * 
 * @author 	Joseph Gefroh
 * @see		IEntity
 */
public interface IComponent
{
	/**
	 * Initializes this {@code Component} with some default settings.
	 */
	public void init();
	
	/**
	 * Sets the owner of this {@code Component}.
	 * @param owner	the {@code Entity} to which this {@code Component} belongs
	 */
	public void setOwner(final IEntity owner);
	
	/**
	 * Gets the owner of this {@code Component}.
	 * @return 		the {@code Entity} to which this {@code Component} belongs
	 */
	public IEntity getOwner();
}
