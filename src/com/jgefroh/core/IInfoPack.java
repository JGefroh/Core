package com.jgefroh.core;

//TODO: Remove
/**
 * Provides an interface to deal with sending data to a system
 * @author Joseph Gefroh
 * @version 0.1.0
 * @since	16MAY13
 */
public interface IInfoPack
{
	/**
	 * Return the entity associated with the InfoPack.
	 * @return	the entity that is considered the owner of the InfoPack
	 */
	public IEntity getOwner();

	/**
	 * Check to see if the InfoPack is invalid or unreliable.
	 * @return	true if the InfoPack is unreliable, false otherwise
	 */
	public boolean isDirty();
	
	
	/**
	 * Set the flag that indicates the InfoPack is invalid.
	 * @param isDirty	set to true if the InfoPack is unreliable, false if OK
	 */
	public void setDirty(final boolean isDirty);
}
