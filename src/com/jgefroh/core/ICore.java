package com.jgefroh.core;


import java.util.ArrayList;



/**
 * The interface for the {@code Core}.
 * 
 * <p>
 * {@code Core} is the heart of the game.
 * </p>
 * <p>
 * {@code Core} keeps track of all {@code Entities}, {@code InfoPacks},
 * {@code InfoPackFactories}, and {@code Systems}.
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
 * @see		IInfoPackFactory
 * @see		ISystem
 * @see		IEntity
 * @version 0.1.0
 * @since	20MAY13
 */
public interface ICore
{
	/**
	 * Starts tracking the passed {@code Entity}.
	 * This should also generate the {@code InfoPacks} for the {@code Entity}.
	 * @param entity	the Entity to begin tracking
	 */
	public void addEntity(final IEntity entity);
	
	/**
	 * Starts tracking the passed {@code InfoPack}.
	 * @param entity	the Entity whose components the InfoPack is grouping
	 * @param infoPack	the InfoPack to start tracking
	 */
	public void addInfoPack(final IEntity entity, final IInfoPack infoPack);
	
	/**
	 * Starts using the passed {@code InfoPackFactory} to generate 
	 * {@code infoPacks}.
	 * @param factory	the InfoPackFactory that generates InfoPacks
	 */
	public void addFactory(final IInfoPackFactory factory);
		
	/**
	 * Starts tracking the passed {@code System}.
	 * This should ensure that the {@code System} is started.
	 * @param system	the System to begin tracking
	 * @param priority	the order in which the System should be executed
	 */
	public void addSystem(final ISystem system, final int priority);

	/**
	 * Stops tracking the passed {@code Entity}.
	 * This should ensure that the associated {@code InfoPacks} are removed.
	 * @param entity	the Entity to stop tracking
	 */
	public void removeEntity(final IEntity entity);
	
	/**
	 * Stops tracking the passed {@code InfoPack}.
	 * @param infoPack	the InfoPack to stop tracking
	 */
	public void removeInfoPack(final IInfoPack infoPack);

	/**
	 * Stops tracking the passed {@code System}. 
	 * This should ensure that the {@code System} is stopped.
	 * @param system	the System to stop tracking
	 */
	public void removeSystem(final ISystem system);
	
	/**
	 * Gets {@code InfoPacks} of a specific Class type.
	 * @param t	the Class type of the InfoPacks to retrieve
	 * @return	an ArrayList with only the InfoPacks of the passed type
	 */
	public <T extends IInfoPack> ArrayList<T> getInfoPacksOfType(Class<T> t);
	
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
	public void generateInfoPacks(final IEntity entity);
}
