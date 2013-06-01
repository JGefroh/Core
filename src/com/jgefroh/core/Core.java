package com.jgefroh.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * An implementation of the {@code ICore} interface.
 * 
 * @author 	Joseph Gefroh
 * @see		ICore
 * @version 0.4.0
 * @since	0.1.0
 */
public class Core implements ICore
{
	//////////
	// DATA
	//////////
	//TODO: Switch to LinkedLists?
	 //TODO: Get rid of UncheckedCast warnings w/o suppressing them.
	/**Holds all of the InfoPacks associated with an entity.*/
	private HashMap<IEntity, ArrayList<IInfoPack>> infoPacks;
	
	/**Holds all of the entities in the game.*/
	private ArrayList<IEntity> entities;
	
	/**Holds all of the systems in the game.*/
	private ArrayList<ISystem> systems;	
	
	/**Holds all of the critical systems in the game.*/
	private ArrayList<ISystem> criticalSystems;
	
	/**Holds all of the InfoPack factories.*/
	private ArrayList<IInfoPackFactory> packFactories;
	
	/**Holds all of the used IDs*/
	private ArrayList<String> usedIDs;
	
	private HashMap<String, ArrayList<ISystem>> subscribers;
	
	/**Logger for debugging.*/
	private final static Logger LOGGER 
		= Logger.getLogger(Core.class.getName());
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.FINE;
	
	/**Flag that indicates whether Core is paused or not.*/
	private boolean isPaused = false;
	
	/**The last ID assigned to an Entity.*/
	private long lastID = 0;
	
	/**The amount of time the simulation has been running (unpaused), in ns.*/
	private long timer = 0;
	
	/**The time the timer was last checked, in ns.*/
	private long timeLastChecked;
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
		criticalSystems = new ArrayList<ISystem>();
		packFactories = new ArrayList<IInfoPackFactory>();
		subscribers = new HashMap<String, ArrayList<ISystem>>();
		this.timeLastChecked = System.nanoTime();
		LOGGER.log(Level.INFO, "Core started.");
	}
	
	@Override
	public void addEntity(final IEntity entity)
	{
		if(entity!=null&&entities.contains(entity)==false)
		{//if the entity exists and it is not already being tracked...
			LOGGER.log(Level.FINER, "Tracking entity: " + entity);
			entity.setID(generateID());
			entities.add(entity);
			generateInfoPacks(entity);
		}
	}
	
	@Override
	public void addInfoPack(final IInfoPack infoPack)
	{	
		if(infoPack!=null&&infoPacks.containsValue(infoPack)==false)
		{//If the infopack is not already being tracked
			LOGGER.log(Level.FINEST, "Tracking " + infoPack + " belonging to: " 
						+ infoPack.getOwner());
			ArrayList<IInfoPack> entityPacks = infoPacks.get(infoPack.getOwner());
			if(entityPacks!=null)
			{//If an arraylist already exists for the info pack
				entityPacks.add(infoPack);	//Add the info pack to the arraylist
			}
			else
			{//If not...
				//Construct an array list.
				entityPacks = new ArrayList<IInfoPack>();
				entityPacks.add(infoPack);
				infoPacks.put(infoPack.getOwner(), entityPacks);
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
	public void addSystem(final ISystem system)
	{
		if(system!=null&&systems.contains(system)==false)
		{//If the system exists and is not already being tracked...
			LOGGER.log(Level.FINE, "Adding system: " + system);
			system.start();
			systems.add(system);
		}
	}
	
	@Override
	public void addSystem(final ISystem system, final boolean isCritical)
	{
		if(isCritical==true
				&&system!=null
				&&criticalSystems.contains(system)==false)
		{
			criticalSystems.add(system);
		}
		addSystem(system);
	}
	
	/**
	 * Adds a System to Core.
	 * This is a convenience method.
	 * @see Core#addSystem(ISystem)
	 */
	public void add(final ISystem system)
	{
		addSystem(system);
	}
	
	/**
	 * Adds a System to Core.
	 * This is a convenience method.
	 * @see Core#add(ISystem, boolean)
	 */
	public void add(final ISystem system, final boolean isCritical)
	{
		addSystem(system, isCritical);
	}
	
	/**
	 * Adds an Entity to Core.
	 * This is a convenience method.
	 * @see Core#add(IEntity)
	 */
	public void add(final IEntity entity)
	{
		addEntity(entity);
	}
	
	/**
	 * Adds an IInfoPackFactory to Core.
	 * This is a convenience method.
	 * @see Core#addFactory(IInfoPackFactory)
	 */
	public void add(final IInfoPackFactory factory)
	{
		addFactory(factory);
	}
	
	/**
	 * Adds an InfoPack to Core.
	 * This is a convenience method.
	 * @see Core#addInfoPack(IInfoPack)
	 */
	public void add(final IInfoPack infoPack)
	{
		addInfoPack(infoPack);
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
	public void removeEntity(final String id)
	{
		removeEntity(getEntityWithID(id));
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
			criticalSystems.remove(system);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IInfoPack> Iterator<T> getInfoPacksOfType(Class<T> t)
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
		return packs.iterator();
	}

	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
		updateTimer();
		for(IEntity each:entities)
		{
			if(each.hasChanged())
			{
				generateInfoPacks(each);
			}
		}
		
		if(isPaused)
		{
			for(ISystem system:criticalSystems)
			{
				long now = now();
				if(isTime(now, system.getWait(), system.getLast()))
				{
					system.setLast(now);
					system.work(now);
				}
			}
		}
		else
		{
			for(ISystem system:systems)
			{
				long now = now();

				if(isTime(now, system.getWait(), system.getLast()))
				{
					system.setLast(now);
					system.work(now);
				}
			}
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
					addInfoPack(pack);
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

	@Override
	public void removeAllEntities()
	{
		this.infoPacks.clear();
		this.entities.clear();
	}

	@Override
	public <T extends IComponent> void removeEntitiesWith(Class<T> type)
	{
		Iterator<IEntity> iter = entities.iterator();
		while(iter.hasNext())
		{
			IEntity each = iter.next();
			if(each.getComponent(type)!=null)
			{
				iter.remove();
			}
		}
	}

	@Override
	public void removeAllSystems()
	{
		this.systems.clear();
	}
	
	@Override
	public String generateID()
	{
		lastID+=1;
		return lastID+"";
	}

	@Override
	public <T extends IInfoPack> T getInfoPackFrom(String id, Class<T> type)
	{
		IEntity entity = getEntityWithID(id);
		if(entity!=null)
		{			
			return getInfoPackFrom(entity, type);
		}
		return null;
	}
	
	@Override
	public IEntity getEntityWithID(final String id)
	{
		//used by getInfoPackFrom(String, Class<T>)
		Iterator<IEntity> iter = entities.iterator();
		while(iter.hasNext())
		{
			IEntity each = iter.next();
			if(each.getID().equals(id))
			{
				return each;
			}
		}
		return null;
	}

	//////////
	// UTILITY
	//////////
	/**
	 * Gets the current time of the simulation, in ms.
	 * @return	the current time, in ms
	 */
	public long now()
	{
		return this.timer/1000000;
	}
	
	/**
	 * Update the timer.
	 */
	private void updateTimer()
	{
		long now = System.nanoTime();
		if(isPaused==false)
		{
			long timePassed = now-this.timeLastChecked;
			this.timer+=timePassed;
		}
		timeLastChecked = now;
	}
	
	/**
	 * Checks to see if the desired amount of time has passed.
	 * @param now		the current time, in ms
	 * @param last		the time of last execution, in ms
	 * @param waitTime	the time to wait, in ms
	 * @return			true if the wait time has passed; false otherwise
	 */
	public boolean isTime(final long now, final long last, final long waitTime)
	{
		if(now-last>=waitTime)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the flag that indicates non-critical systems are paused.
	 * @param isPaused	true if paused;false otherwise
	 */
	public void setPaused(final boolean isPaused)
	{
		this.isPaused = isPaused;
	}
	
	/**
	 * Returns the flag that indicates that non-critical systems are paused.
	 * @return	true if paused;false otherwise
	 */
	public boolean isPaused()
	{
		return this.isPaused;
	}
	
	/**
	 * Sends a message to all interested {@code Systems}.
	 * @param id		the ID of the message
	 * @param message	the message
	 */
	public void send(final String id, final String... message)
	{
		LOGGER.log(Level.FINER, "Sending: " + id + " | " + message);
		if(subscribers.containsKey(id))
		{
			Iterator<ISystem> systems = subscribers.get(id).iterator();
			while(systems.hasNext())
			{
				systems.next().recv(id, message);
			}
		}
	}
	
	/**
	 * Marks the {@code System} as interested in messages of a given type.
	 * @param system		the System that is interested
	 * @param messageID		the message ID the system is interested in
	 */
	public void setInterested(final ISystem system, final String messageID)
	{
		LOGGER.log(Level.FINE, system + " interested in: " + messageID);

		if(system!=null&&messageID!=null)
		{		
			if(subscribers.containsKey(messageID))
			{//If another system has already expressed interest in the message
				ArrayList<ISystem> systems = subscribers.get(messageID);
				if(systems.contains(system)==false)
				{
					//Add it to the list if it is not already in there.
					systems.add(system);
				}
			}
			else
			{
				//Else if the key is genuinely new...
				ArrayList<ISystem> systems = new ArrayList<ISystem>();
				systems.add(system);
				subscribers.put(messageID, systems);
			}
		}
	}
	
	/**
	 * Marks the {@code System} as uninterested in messages of a given type.
	 * @param system		the System that is uninterested
	 * @param messageID		the message ID the system is uninterested in
	 */
	public void setUninterested(final ISystem system, final String messageID)
	{
		LOGGER.log(Level.FINE, system + " uninterested in: " + messageID);
		if(system!=null&&messageID!=null)
		{
			if(subscribers.containsKey(messageID))
			{
				ArrayList<ISystem> systems = subscribers.get(messageID);
				systems.remove(system);
			}
		}
	}
}
