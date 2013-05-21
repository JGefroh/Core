package com.jgefroh.core;

//TODO: Remove
/**
 * Interface for an {@code InfoPack}.
 * 
 * <p>
 * An {@code InfoPack} is intended to provide a {@code System} access 
 * to data held in a {@code component} via accessor methods 
 * (getters/setters). They group together one or more {@code Components}
 * that contain data needed by a {@code System}.
 * <br>
 * <br>
 * It is also intended to control whether a {@code System} will process an 
 * {@code Entity} through its presence or lack of presence.
 * </p>
 * <p>
 * {@code Core} automatically calls a method to generate an {@code InfoPack} 
 * within its accompanying {@code IInfoPackFactory}.
 * </p>
 * 
 * @author 	Joseph Gefroh
 * @see		Core
 * @see		IComponent
 * @see		IInfoPackFactory
 * @see		ISystem
 * @version	0.1.0
 * @since 	20MAY13
 */
public interface IInfoPack
{
	/**
	 * Returns the {@code Entity} associated with this {@code InfoPack}.
	 * @return	the {@code Entity} that is considered the owner of this
	 * 			{@code InfoPack}
	 * @since 	0.1.0
	 */
	public IEntity getOwner();

	/**
	 * Checks to see if this {@code InfoPack} is invalid or unreliable.
	 * 
	 * Validity is determined by whether the {@code Entity} has the
	 * {@code components} this {@code InfoPack} requires.
	 * @return	{@code true} if this {@code InfoPack} is unreliable;
	 * 			{@code false} otherwise
	 * @since 	0.1.0
	 */
	public boolean isDirty();
	
	
	/**
	 * Sets the flag that indicates this {@code InfoPack} is invalid.
	 * @param isDirty	{@code true} if this {@code InfoPack} 
	 * 					is unreliable; {@code false} otherwise
	 * @since 0.1.0
	 */
	public void setDirty(final boolean isDirty);
}
