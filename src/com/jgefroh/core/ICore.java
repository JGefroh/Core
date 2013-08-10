package com.jgefroh.core;

import java.util.Iterator;

/**
 * The interface for the {@code Core}.
 * 
 * <p>
 * {@code Core} is the heart of the game.
 * </p>
 * <p>
 * {@code Core} keeps track of all {@code Entities}, {@code InfoPacks},
 * and {@code Systems}.
 * </p>
 * <p>
 * It monitors {@code Entities} for changes in their composition of 
 * {@code Components}, and automatically generates appropriate 
 * {@code InfoPacks}.
 * </p>
 * <p>
 * It calls methods that perform work in {@code Systems}. It also provides
 * accessor methods so that {@code Systems} can retrieve the
 * {@code InfoPacks} that are relevant to them.
 * </p>
 * 
 * 
 * @author 	Joseph Gefroh
 * @see 	IEntity
 * @see		IInfoPack
 * @see		ISystem
 * @see		IEntity
 */
public interface ICore
{
	/**
	 * Starts tracking the passed {@code Entity}.
	 * This should also generate the {@code InfoPacks} for the {@code Entity}.
	 * This should also generate a unique ID for the {@code Entity}.
	 * @param entity	the Entity to begin tracking
	 */
	public <T extends IEntity> void addEntity(final T entity);
	
	/**
	 * Starts using the passed {@code InfoPack} to generate 
	 * {@code infoPacks}.
	 * @param factory	the InfoPack to use to generate more of that type
	 */
	public <T extends IInfoPack> void addFactory(final T factory);
		
	/**
	 * Starts tracking the passed {@code System} as a non-critical system.
	 * This should ensure that the {@code System} is started.
	 * @param system	the System to begin tracking
	 */
	public <T extends ISystem> void addSystem(final T system);
	
	/**
	 * Starts tracking the passed {@code System}.
	 * @param system		the System to begin tracking
	 * @param isCritical	true if the System should always run, false otherwise;
	 */
	public <T extends ISystem> void addSystem(final T system, final boolean isCritical);
	
	/**
	 * Stops tracking the passed {@code Entity}.
	 * This should ensure that the associated {@code InfoPacks} are removed.
	 * @param entity	the Entity to stop tracking
	 */
	public <T extends IEntity> void removeEntity(final IEntity entity);
	
	/**
	 * Stops tracking the {@code Entity} with the passed id.
	 * This should ensure that the associated {2code InfoPacks} are removed.
	 * @param id	the ID of the Entity to stop tracking
	 */
	public void removeEntity(final String id);
	
	/**
	 * Returns the {@code Entity} with the specific ID.
	 * @param id	the unique ID of the Entity to retrieve
	 * @return		the Entity that  has the ID;null if no Entity was found
	 */
	public <T extends IEntity>T getEntityWithID(final String id);
	
	/**
	 * Returns a generated ID that is guaranteed to be unique.
	 * @return	the unique ID
	 */
	public String generateID();
	
	/**
	 * Stops tracking all {@code Entities}.
	 */
	public void removeAllEntities();
	
	/**
	 * Stops tracking all entities with the given component.
	 * @param	type	the component that removed entities should have
	 */
	public <T extends IComponent> void removeEntitiesWith(final Class<T> type);

	/**
	 * Stops tracking the passed {@code System}. 
	 * This should ensure that the {@code System} is stopped.
	 * @param system	the System to stop tracking
	 */
	public <T extends ISystem> void removeSystem(final T system);
	
	/**
	 * Stops tracking all {@code Systems}.
	 */
	public void removeAllSystems();
	
	/**
	 * Gets {@code InfoPacks} of a specific Class type.
	 * @param t	the Class type of the InfoPacks to retrieve
	 * @return	an Iterator over a collection of all matching InfoPacks
	 */
	public <T extends IInfoPack> Iterator<T> getInfoPacksOfType(Class<T> t);
	
	/**
	 * Gets a specific {@code InfoPack} from the passed {@code Entity}.
	 * @param entity	the Entity associated with the desired {@code InfoPack}
	 * @param type		the Class type of the InfoPack to retrieve
	 * @return			the InfoPack of the passed type;
	 * 					returns {@code null} if it does not exist
	 * 					
	 */
	public <T extends IInfoPack>T getInfoPackFrom(final IEntity entity, 
													final Class<T> type);
	
	/**
	 * Gets a specific {@code InfoPack} from the {@code Entity} with the ID.
	 * @param id	the unique ID of the Entity to retrieve the pack from
	 * @param type	the type of InfoPack to retrieve
	 * @return		the InfoPack if found; null otherwise
	 */
	public <T extends IInfoPack>T getInfoPackFrom(final String id, 
													final Class<T> type);
	/**
	 * Gets a {@code System} that is being tracked by {@code Core}.
	 * @param t	the Class type of the System whose reference to return
	 * @return	the System of the desired type
	 * 			returns {@code null} if it does not exist
	 */
	public <T extends ISystem> T getSystem(Class<T> t);
	
	/**
	 * Performs the work in all of the {@code Systems} tracked by {@code Core}.
	 * This should ensure that {@code InfoPacks} are valid and generated.
	 */
	public void work();
	
	/**
	 * Generates all of the appropriate {@code InfoPacks} for the given 
	 * {@code Entity}. 
	 * @param entity	the Entity for whom the InfoPacks are being generated
	 */
	public <T extends IEntity> void generateInfoPacks(final T entity);
}
