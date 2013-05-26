package com.jgefroh.core;

/**
 * Interface for the {@code InfoPackFactory}.
 * 
 * <p>
 * The {@code InfoPackFactory} is used by {@code Core} to generate
 * {@code InfoPacks} for an {@code Entity} that has the proper 
 * {@code components}. 
 * </p>
 * 
 * @author Joseph Gefroh
 * @version 0.1.0
 * @since 0.1.0
 */
public interface IInfoPackFactory
{
	/**
	 * Creates an {@code InfoPack} based on the {@code components} the passed 
	 * {@code Entity} has.
	 * 
	 * <p>
	 * {@code Core} will automatically call this method when it begins tracking
	 * an {@code Entity} or when it notices that an {@code Entity} has changed.
	 * </p>
	 * @param entity	the entity for which this InfoPack is being generated
	 * @return			the generated InfoPack
	 */
	public <T extends IInfoPack>T generate(final IEntity entity);
}
