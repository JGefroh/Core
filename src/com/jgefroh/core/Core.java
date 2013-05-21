package com.jgefroh.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * An implementation of the {@code ICore} interface.
 * 
 * @author 	Joseph Gefroh
 * @see		ICore
 * @version 1.1
 * @since	20MAY13
 */
public class Core implements ICore
{
	//////////
	// DATA
	//////////
	//TODO: Use iterator for concurrent modification of ALs.
	/**Holds all of the InfoPacks associated with an entity.*/
	private HashMap<IEntity, ArrayList<IInfoPack>> infoPacks;
	
	/**Holds all of the entities in the game.*/
	private ArrayList<IEntity> entities;
	
	/**Holds all of the systems in the game.*/
	private ArrayList<ISystem> systems;	
	
	/**Holds all of the InfoPack factories.*/
	private ArrayList<IInfoPackFactory> packFactories;
	
	/**Logger for debugging.*/
	private final static Logger LOGGER 
		= Logger.getLogger(Core.class.getName());
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.OFF;
	
	
	//////////
	// INIT
	//////////
	/**
	 * Initializes the Logger's settings.
	 */
	private void initLogger()
	{
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(debugLevel);
		LOGGER.addHandler(ch);
		LOGGER.setLevel(debugLevel);
		LOGGER.setUseParentHandlers(false);
	}
	
	/**
	 * Create a new Core object.
	 */
	public Core()
	{
		//TODO: Change to LinkedLists later on for simplicity.
		initLogger();
		infoPacks = new HashMap<IEntity, ArrayList<IInfoPack>>();
		entities = new ArrayList<IEntity>();
		systems = new ArrayList<ISystem>();
		packFactories = new ArrayList<IInfoPackFactory>();
		LOGGER.log(Level.INFO, "Core started.");
	}
	
	@Override
	public void addEntity(final IEntity entity)
	{
		if(entity!=null&&entities.contains(entity)==false)
		{//if the entity exists and it is not already being tracked...
			LOGGER.log(Level.FINER, "Tracking entity: " + entity);
			entities.add(entity);
			generateInfoPacks(entity);
		}
	}
	
	@Override
	public void addInfoPack(final IEntity entity, final IInfoPack infoPack)
	{	
		if(infoPack!=null&&infoPacks.containsValue(infoPack)==false)
		{//If the infopack is not already being tracked
			LOGGER.log(Level.FINEST, "Tracking " + infoPack + " belonging to: " 
						+ entity);
			ArrayList<IInfoPack> entityPacks = infoPacks.get(entity);
			if(entityPacks!=null)
			{//If an arraylist already exists for the info pack
				entityPacks.add(infoPack);	//Add the info pack to the arraylist
			}
			else
			{//If not...
				//Construct an array list.
				entityPacks = new ArrayList<IInfoPack>();
				entityPacks.add(infoPack);
				infoPacks.put(entity, entityPacks);
			}
		}
	}
	
	@Override
	public void addFactory(final IInfoPackFactory factory)
	{
		if(factory!=null&&packFactories.contains(factory)==false)
		{			
			LOGGER.log(Level.FINE, "Adding factory: " + factory);
			packFactories.add(factory);
		}
	}

	@Override
	public void addSystem(final ISystem system, final int priority)
	{
		if(system!=null&&systems.contains(system)==false)
		{//If the system exists and is not already being tracked...
			LOGGER.log(Level.FINE, "Adding system: " + system 
						+ " with priority: " + priority);
			system.start();
			if(priority>=0)
			{
				systems.add(priority, system);
			}
			else
			{
				systems.add(system);
			}
		}
	}

	@Override
	public void removeEntity(final IEntity entity)
	{
		LOGGER.log(Level.FINER, "Untracking entity: " + entity);
		if(entity!=null)
		{
			entities.remove(entity);
			infoPacks.remove(entity);
		}
	}
	
	@Override
	public void removeInfoPack(final IInfoPack infoPack)
	{
		if(infoPack!=null)
		{
			ArrayList<IInfoPack> entityPacks = 
					infoPacks.get(infoPack.getOwner());
			if(entityPacks!=null)
			{
				LOGGER.log(Level.FINEST, "Untracking infoPack " + infoPack 
						+ "belonging to: " + infoPack.getOwner());
				entityPacks.remove(infoPack);
			}
		}
	}

	@Override
	public void removeSystem(final ISystem system)
	{
		if(system!=null)
		{
			LOGGER.log(Level.FINE, "Untracking system: " + system);
			system.stop();
			systems.remove(system);
		}
	}
	
	@Override
	public <T extends IInfoPack> ArrayList<T> getInfoPacksOfType(Class<T> t)
	{
		ArrayList<T> packs = new ArrayList<T>();
		Set<IEntity> entitySet = infoPacks.keySet();

		for(IEntity each:entitySet)
		{//For each entity tracked...
			//...get the packs the entity "owns"
			ArrayList<IInfoPack> entityPacks = infoPacks.get(each);
			
			for(IInfoPack entityPack:entityPacks)
			{//For each pack the entity owns....
				if(entityPack.getClass()==t)
				{//If it is an instance of the desired type, grab it.
					packs.add((T)entityPack);
				}
			}
		}
		return packs;
	}

	@Override
	public <T extends IInfoPack>T getInfoPackFrom(final IEntity entity, 
													final Class<T> type)
	{
		ArrayList<IInfoPack> packs = infoPacks.get(entity);
		if(packs!=null)
		{
			for(IInfoPack each:packs)
			{
				if(each.getClass()==type)
				{
					return (T)each;
				}
			}
		}
		return null;
	}

	@Override
	public <T extends ISystem> T getSystem(Class<T> t)
	{
		for(ISystem each:systems)
		{
			if(each.getClass()==t)
			{
				return (T)each;
			}
		}
		return null;
	}
	
	@Override
	public void work()
	{
		for(IEntity each:entities)
		{
			if(each.hasChanged())
			{
				generateInfoPacks(each);
			}
		}
		
		for(ISystem system:systems)
		{
			system.work();
		}
	}
	
	@Override
	public void generateInfoPacks(final IEntity entity)
	{
		if(entity!=null)
		{
			LOGGER.log(Level.FINEST, "Generating infoPacks for: " + entity);
			infoPacks.remove(entity);
			for(IInfoPackFactory each:packFactories)
			{
				IInfoPack pack = each.generate(entity);
				if(pack!=null&&pack.isDirty()==false)
				{					
					addInfoPack(entity, pack);
				}
			}
			entity.setChanged(false);
		}
	}
	
	/**
	 * Sets the granularity of the debug messages that are printed.
	 * @param debugLevel	the Level of messages that should be printed
	 */
	public void setDebugLevel(final Level debugLevel)
	{
		this.debugLevel = debugLevel;
	}
	
	/**
	 * Gets the current granularity of debug messages that are printed.
	 * @return	the current logging Level of messages that are printed
	 */
	public Level getDebugLevel()
	{
		return this.debugLevel;
	}
}
