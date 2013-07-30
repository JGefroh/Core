package com.jgefroh.core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * An implementation of the {@code ICore} interface using nodes.
 * 
 * @author 	Joseph Gefroh
 * @see		ICore
 * @version 0.1.0
 * @since 27JUL13
 */
public class Core implements ICore
{
	/**Holds the info packs, organized by type.*/
	private HashMap<Class<? extends AbstractInfoPack>, AbstractInfoPack> infoPacks;
	
	/**Holds the info packs, organized by owning entity.*/
	private HashMap<IEntity, AbstractInfoPack> entityPacks;
	
	/**Holds the factories that generate the InfoPacks.*/
	private ArrayList<IInfoPackFactory> factories;
	
	/**Holds the systems.*/
	private ArrayList<ISystem> systems;
	
	/**Holds the entities, sorted by ID.*/
	private HashMap<String, IEntity> entitiesByID;
	
	/**Holds a list of systems subscribed to different message IDs*/
	private HashMap<String, ArrayList<ISystem>> subscribers;
	
	/**The last ID that was assigned to an entity.*/
	private long lastID;
	
	/**The time, in NS.*/
	private long timer;
	
	/**Used to provide accurate timing.*/
	private long timeLastChecked;
	
	/**FLAG: Indicates whether Core should pause execution or not.*/
	private boolean isPaused;	//TODO: Implement
	
	/**Logger for debug purposes.*/
	private final Logger LOGGER 
		= LoggerFactory.getLogger(this.getClass(), Level.OFF);
	
	/**The level of detail in debug messages.*/
	private Level debugLevel = Level.ALL;
	
	public Core()
	{
		infoPacks = new HashMap<Class<? extends AbstractInfoPack>, AbstractInfoPack>();
		entityPacks = new HashMap<IEntity, AbstractInfoPack>();
		factories = new ArrayList<IInfoPackFactory>();
		systems = new ArrayList<ISystem>();
		entitiesByID = new HashMap<String, IEntity>();
		subscribers = new HashMap<String, ArrayList<ISystem>>();
		this.timeLastChecked = System.nanoTime();
	}
	
	@Override
	public void addEntity(final IEntity entity)
	{
		if(entity!=null&&entitiesByID.get(entity)==null)
		{//If the entity is not already tracked...
			if(entity.getID()==null)
			{//If the entity does not have an ID...
				entity.setID(generateID());
			}
			entitiesByID.put(entity.getID(), entity);
			LOGGER.log(Level.FINER, "Added entity (" + entity + ")"
					+ " | " + entity.getName() +" (ID: " +  entity.getID() + ")");
			generateInfoPacks(entity); //Generate info packs for this entity.
		}
	}

	@Override
	public <T extends IInfoPack> void addInfoPack(final T newPack)
	{
		AbstractInfoPack addMe = (AbstractInfoPack)newPack;

		if(addMe!=null&&addMe.checkDirty()==false)
		{
			AbstractInfoPack head = infoPacks.get(addMe.getClass());
			
			if(head!=null)
			{//There is already an info pack of this type...
				AbstractInfoPack nextPack = head;
				
				while(nextPack!=null)
				{//Check to see if the info pack already exists.
					if(nextPack==addMe)
					{
						return;	//Found same copy, do nothing.
					}
					nextPack = nextPack.next();
				}
				addMe.setNext(head);	//Set the old head as next of new pack
				head.setPrev(addMe);	//Set the new pack as previous of old.
				infoPacks.put(addMe.getClass(), addMe); //Set new pack as head
			}
			else
			{//There is no info pack of this type already.
				infoPacks.put(addMe.getClass(), addMe);	//Set new pack as head
			}
			
			
			AbstractInfoPack ptrPack = entityPacks.get(addMe.getOwner());
			if(ptrPack==null)
			{//This is the first info pack the owner has
				entityPacks.put(addMe.getOwner(), addMe);
			}
			else
			{//The owner of the info pack has other info packs...
				addMe.setOwnerNext(ptrPack);
				ptrPack.setOwnerPrev(addMe);
				entityPacks.put(addMe.getOwner(),  addMe);
			}
		}
	}
	
	@Override
	public <T extends IInfoPackFactory>void addFactory(final T factory)
	{
		if(factory!=null && factories.contains(factory)==false)
		{			
			factories.add(factory);
		}
	}

	@Override
	public void addSystem(final ISystem system)
	{
		if(system!=null && systems.contains(system)==false)
		{			
			system.start();
			systems.add(system);
		}
	}

	@Override
	public void addSystem(final ISystem system, final boolean isCritical)
	{
		addSystem(system);
	}
	
	/**
	 * Convenience method.
	 * @param system	the system to add
	 */
	public void add(final ISystem system)
	{
		addSystem(system);
	}

	/**
	 * Convenience method.
	 * @param factory	the factory to add
	 */
	public void add(final IInfoPackFactory factory)
	{
		addFactory(factory);
	}

	/**
	 * Convenience method.
	 * @param entity	the entity to add
	 */
	public void add(final IEntity entity)
	{
		addEntity(entity);
	}

	/**
	 * Convenience method.
	 * @param pack	the InfoPack to add
	 */
	public void add(final IInfoPack pack)
	{
		addInfoPack(pack);
	}

	@Override
	public IEntity getEntityWithID(final String id)
	{
		return entitiesByID.get(id);
	}

	@Override
	public <T extends IInfoPack> Iterator<T> getInfoPacksOfType(Class<T> t)
	{
		AbstractInfoPack head = infoPacks.get(t);
		if(head!=null)
		{
			return head.iterator();			
		}
		return (Iterator<T>) new AbstractInfoPackIterator(null);
	}

	@Override
	public <T extends IInfoPack> T getInfoPackFrom(final IEntity entity, final Class<T> type)
	{
		//Get the first InfoPack belonging to the entity, if any.
		AbstractInfoPack head = entityPacks.get(entity);
		
		while(head!=null)
		{//While there are nodes to search..
			if(head.getClass()==type)
			{//If found, return
				return (T)head;
			}
			head = head.ownerNext();
		}
		return null;
	}

	@Override
	public <T extends IInfoPack> T getInfoPackFrom(String id, Class<T> type)
	{
		IEntity entity = entitiesByID.get(id);
		return getInfoPackFrom(entity, type);
	}

	@Override
	public <T extends ISystem> T getSystem(final Class<T> t)
	{
		Iterator<ISystem> sysIter = systems.iterator();
		
		while(sysIter.hasNext())
		{
			ISystem system = sysIter.next();
			
			if(system.getClass()==t)
			{
				return (T)system;
			}
		}
		return null;
	}
	
	@Override
	public void generateInfoPacks(final IEntity entity)
	{
		if(entity!=null)
		{
			LOGGER.log(Level.FINER, "Generated packs for (" + entity + ")"
					+ " | " + entity.getName() +" (ID: " +  entity.getID() + ")");
			removeAllInfoPacksFrom(entity);
			AbstractInfoPack pack = entityPacks.get(entity);

			for(IInfoPackFactory each:factories)
			{//For each type of factory...
				addInfoPack(each.generate(entity));
			}
		}
		entity.setChanged(false);
	}
	
	@Override
	public String generateID()
	{
		this.lastID+=1;
		return this.lastID+"";
	}

	@Override
	public void removeEntity(final IEntity entity)
	{
		if(entity==null)
		{
			return;
		}
		
		//Get the packs that belong to the entity.
		AbstractInfoPack pack = entityPacks.get(entity);
		while(pack!=null)
		{//While the entity still has packs...
			removeInfoPack(pack);
			pack = pack.ownerNext();
		}
		entity.removeAllComponents();	//This shouldn't be necessary.
		entitiesByID.remove(entity.getID());
	}
	
	@Override
	public void removeEntity(final String id)
	{
		removeEntity(entitiesByID.get(id));
	}

	@Override
	public void removeAllEntities()
	{
		entitiesByID.clear();
		infoPacks.clear();
		entityPacks.clear();
	}

	@Override
	public <T extends IComponent> void removeEntitiesWith(final Class<T> type)
	{
		Collection<IEntity> entitiesWithComponent = entitiesByID.values();
		
		for(IEntity each:entitiesWithComponent)
		{
			if(each.getComponent(type)!=null)
			{				
				removeEntity(each);	//May cause concurrent modification issues.
			}
		}
	}

	/**
	 * Stops tracking the info pack.
	 * @param pack	the InfoPack to stop tracking
	 */
	private void removeInfoPack(final AbstractInfoPack pack)
	{
		if(pack!=null)
		{
			//Remove from list of packs of same type...
			if(pack.hasPrev()==false)
			{//If the pack is the head...
				if(pack.hasNext())
				{//Set the next pack, if any, to the head...
					infoPacks.put(pack.getClass(), pack.next());
					pack.next().setPrev(null);
				}
				else
				{//Remove the pack since it is by itself
					infoPacks.remove(pack.getClass());
				}
			}
			else
			{//Remove itself from the linked list.
				pack.prev().setNext(pack.next());	//Remove self from previous
				if(pack.hasNext())
				{
					pack.next().setPrev(pack.prev());	//Remove self from next
				}
			}
			
			//Remove from list of packs of same entity
			if(pack.hasOwnerPrev()==false)
			{
				if(pack.hasOwnerNext())
				{//If this pack is the head...
					//Set the next pack, if any, to the head.
					entityPacks.put(pack.getOwner(), pack.ownerNext());
					pack.ownerNext().setOwnerPrev(null);
				}
				else
				{//Alone
					entityPacks.remove(pack.getOwner());
				}
			}
			else
			{
				pack.ownerPrev().setOwnerNext(pack.ownerNext()); //Remove self from previous
				if(pack.hasOwnerNext())
				{//Remove self from next
					pack.ownerNext().setOwnerPrev(pack.ownerPrev());
				}
			}
			pack.setDirty(true);
			pack.getOwner().setChanged(true);
		}
	}

	/**
	 * Removes all InfoPacks associated with a specific entity.
	 * @param entity
	 */
	private void removeAllInfoPacksFrom(final IEntity entity)
	{
		AbstractInfoPack pack = entityPacks.get(entity);
		

		while(pack!=null&&pack.hasOwnerPrev())
		{
			pack = pack.ownerPrev();	//This should never enter the loop.
		}
		
		while(pack!=null)
		{
			removeInfoPack(pack);
			pack = pack.ownerNext();
		}
		entity.setChanged(true);
	}
	@Override
	public void removeSystem(final ISystem system)
	{
		systems.remove(system);
	}

	@Override
	public void removeAllSystems()
	{
		systems.clear();
	}

	@Override
	public void work()
	{
		Iterator<ISystem> sysIter = systems.iterator();
		updateTimer();
		long now = now();
		
		Collection<IEntity> entities = entitiesByID.values();
		
		for(IEntity each:entities)
		{
			if(each.hasChanged())
			{
				generateInfoPacks(each);
				each.setChanged(false);
			}
		}
		
		while(sysIter.hasNext())
		{
			ISystem system = sysIter.next();
			if(now-system.getLast()>system.getWait())
			{	
				system.setLast(now);
				system.work(now);
			}
		}
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
