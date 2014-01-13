package com.jgefroh.core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The heart of the Core framework.
 * 
 * 
 * <p>
 * It also provides timing methods and very basic message passing for
 * inter-system communication.
 * </p>
 * 
 * @author 	Joseph Gefroh
 * @see     IInfoPack
 * @see     IComponent
 */
public class Core {

    //////////////////////////////////////////////////
    // Fields
    //////////////////////////////////////////////////
    
    /**Holds the info packs, organized by type.*/
    private Map<Class<? extends IInfoPack>, IInfoPack> infoPacksByType;

    /**Holds the systems.*/
    private List<ISystem> systems;

    /**Holds the entities, sorted by ID.*/
    private Map<String, IEntity> entitiesByID;

    /**Holds the entities, sorted by ID.*/
    private Map<Class<? extends IInfoPack>, List<IEntity>> entitiesByPack;

    /**Holds a list of message handlers stored by message type.*/
    private Map<Class<? extends IMessage>, List<IMessageHandler<? extends IMessage>>> handlersByMessage;

    /**The last ID that was assigned to an entity.*/
    private long lastID;

    /**The time, in NS.*/
    private long timer;

    /**Used to provide accurate timing.*/
    private long timeLastChecked;

    /**FLAG: Indicates whether Core should pause execution or not.*/
    private boolean isPaused; //TODO: Implement

    /**The level of detail in debug messages.*/
    private Level debugLevel = Level.INFO;

    /**Logger for debug purposes.*/
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass(), debugLevel);


    public Core() {
        infoPacksByType = new HashMap<Class<? extends IInfoPack>, IInfoPack>();
        systems = new ArrayList<ISystem>();
        entitiesByID = new HashMap<String, IEntity>();
        entitiesByPack = new HashMap<Class<? extends IInfoPack>, List<IEntity>>();
        handlersByMessage = new HashMap<Class<? extends IMessage>, List<IMessageHandler<? extends IMessage>>>();
        this.timeLastChecked = System.nanoTime();
        LOGGER.log(Level.INFO, "Core initialized.");
    }

    /**
     * Begins tracking the passed {@code IEntity}.
     * 
     * 
     * <br />An ID is generated for entities without a preset ID.
     * <br />IInfoPacks are updated with this entity.
     * @param entity
     */
    public void addEntity(final IEntity entity) {
        if (entity == null || entitiesByID.get(entity.getID()) != null) {
            LOGGER.log(Level.WARNING, "Entity was not added.");
            return;
        }

        if (entity.getID() == null) {
            //If the entity does not have an ID...
            entity.setID(generateID());
        }

        entitiesByID.put(entity.getID(), entity);
        updateInfoPacks(entity); //Generate info packs for this entity.
    }

    /**
     * Begins tracking the passed {@code IInfoPack} type.
     * @param newPack	the pack to track
     */
    public <T extends IInfoPack> void addInfoPack(final T newPack) {
        infoPacksByType.put(newPack.getClass(), newPack);
    }

    /**
     * Begins tracking the passed System.
     * @param system	the system to track
     */
    public void addSystem(final ISystem system) {
        if (system == null || systems.contains(system) == true) {
            LOGGER.log(Level.WARNING, "Couldn't add system: " + system);
            return;
        }

        systems.add(system);
        system.start();
    }

    /**
     * Convenience method.
     * @param system	the system to add
     */
    public void add(final ISystem system) {
        addSystem(system);
    }

    /**
     * Convenience method.
     * @param entity	the entity to add
     */
    public void add(final IEntity entity) {
        addEntity(entity);
    }

    /**
     * Convenience method.
     * @param pack	the InfoPack to add
     */
    public void add(final IInfoPack pack) {
        addInfoPack(pack);
    }

    /**
     * Returns the entity with the passed ID.
     * @param id	the ID of the entity to retrieve
     * @return		the entity with the ID if found; null otherwise
     */
    public IEntity getEntityWithID(final String id) {
        return entitiesByID.get(id);
    }

    /**
     * Gets all entities that can use the passed IInfoPack type.
     * @param type	the type of IInfoPack that the entities can use
     * @return	an iterator with the IEntity objects that can use the pack
     */
    public Iterator<IEntity> getEntitiesWithPack(final Class<? extends IInfoPack> type) {
        List<IEntity> entities = entitiesByPack.get(type);

        if (entities == null) {
            return Collections.<IEntity> emptyList().iterator();
        }

        return entities.iterator();
    }

    /**
     * Gets the IInfoPack of the passed type ready to be used on an entity with the passed ID.
     * @param id	the ID of the entity
     * @param type	the type of IInfoPack
     * @return	an IInfoPack ready to be used with the entity if found; null otherwise
     */
    public <T extends IInfoPack> T getInfoPackFrom(final String id, Class<T> type) {
        IEntity entity = entitiesByID.get(id);
        IInfoPack pack = infoPacksByType.get(type);

        if (entity != null && pack != null) {
            if (pack.setEntity(entity)) {
                return (T) pack;
            }
        }
        return null;
    }

    /**
     * Gets a new info pack of the passed type, if found.
     * @param type  the type of info pack to get
     * @return  the new info pack if found; null otherwise
     */
    public <T extends IInfoPack> T getInfoPackOfType(Class<T> type) {
        IInfoPack pack = infoPacksByType.get(type);
        
        if (pack == null) {
            return null;
        }
        return ((T) pack).create(type);
    }

    /**
     * Gets the ISystem.
     * @param type	the type of ISystem to get
     * @return  the system if found; null otherwise
     */
    public <T extends ISystem> T getSystem(final Class<T> type) {
        Iterator<ISystem> sysIter = systems.iterator();

        while (sysIter.hasNext()) {
            ISystem system = sysIter.next();

            if (system.getClass() == type) {
                return (T) system;
            }
        }
        return null;
    }

    /**
     * Updates all tracked IInfoPack with the given entity.
     * 
     * 
     * The entity will be tracked by any IInfoPack objects that it can use
     * @param entity	the entity to update the packs with
     */
    public void updateInfoPacks(final IEntity entity) {
        if (entity == null) {
            LOGGER.log(Level.WARNING, "Can't update packs for null entity; skipping.");
            return;
        }

        Iterator<IInfoPack> packs = infoPacksByType.values().iterator();

        while (packs.hasNext()) {
            IInfoPack pack = packs.next();
            boolean hasProperComponents = pack.checkComponents(entity);

            if (!hasProperComponents) {//If entity doesn't meet pack requirements
                disallowEntityToUsePack(entity, pack.getClass());
            }
            else {
                allowEntityToUsePack(entity, pack.getClass());
            }
        }
        entity.setChanged(false);
    }

    /**
     * Generates a previously ungenerated ID.
     * @return	a unique ID
     */
    public String generateID() {
        this.lastID += 1;
        return this.lastID + "";
    }

    /**
     * Stops tracking the passed Entity.
     * @param entity	the entity to stop tracking
     */
    public void removeEntity(final IEntity entity) {
        if (entity == null) {
            return;
        }
        entity.removeAllComponents();
        updateInfoPacks(entity);
        entitiesByID.remove(entity.getID());
    }

    /**
     * Stops tracking the entity with the passed id.
     * @param id    the id of the entity to stop tracking
     */
    public void removeEntity(final String id) {
        removeEntity(entitiesByID.get(id));
    }

    /**
     * Stops tracking all entities.
     */
    public void removeAllEntities() {
        entitiesByID.clear();
        entitiesByPack.clear();
    }

    /**
     * Removes all entities with the passed component type.
     * @param type	the type of component
     */
    public <T extends IComponent> void removeEntitiesWith(final Class<T> type) {
        Collection<IEntity> entitiesWithComponent = entitiesByID.values();

        for (IEntity each : entitiesWithComponent) {
            if (each.getComponent(type) != null) {
                removeEntity(each); //May cause concurrent modification issues.
            }
        }
    }

    /**
     * Removes a system.
     * @param system	the system to remove
     */
    public void removeSystem(final ISystem system) {
        systems.remove(system);
    }

    /**
     * Removes all systems.
     */
    public void removeAllSystems() {
        systems.clear();
    }

    /**
     * Executes the systems added to Core.
     */
    public void work() {
        Iterator<ISystem> sysIter = systems.iterator();
        updateTimer();
        long now = now();

        Collection<IEntity> entities = entitiesByID.values();

        for (IEntity each : entities) {
            if (each.hasChanged()) {
                updateInfoPacks(each);
                each.setChanged(false);
            }
        }

        while (sysIter.hasNext()) {
            ISystem system = sysIter.next();
            if (system.isRunning() && now - system.getLast() > system.getWait()) {
                system.setLast(now);
                system.work(now);
            }
        }
    }

    /**
     * Adds the entity to the pack's list of approved entities.
     * @param entity    the entity to approve
     * @param packType  the pack type to set
     */
    private <T extends IInfoPack> void allowEntityToUsePack(final IEntity entity, final Class<T> packType) {
        if (entity == null) {
            return;
        }
        List<IEntity> entities = entitiesByPack.get(packType);

        if (entities == null) {
            entities = new ArrayList<IEntity>();
            entitiesByPack.put(packType, entities);
        }

        if (!entities.contains(entity)) {
            entities.add(entity);
        }
    }

    /**
     * Removes the entity from the pack type's list of approved entities.
     * @param entity    the entity to remove
     * @param packType  the pack type to set
     */
    public <T extends IInfoPack> void disallowEntityToUsePack(final IEntity entity, final Class<T> packType) {
        List<IEntity> entities = entitiesByPack.get(packType);
        if (entities == null) {
            return;
        }
        entity.setChanged(true);
    }

    /**
     * Removes all entities allowed to use the passed pack type.
     * @param packType  the type of pack
     */
    public <T extends IInfoPack> void clearEntitiesAllowedToUsePack(final Class<T> packType) {
        List<IEntity> entities = entitiesByPack.get(packType);

        if (entities != null) {
            entities.clear();
        }
    }

    //////////////////////////////////////////////////
    // Timer
    //////////////////////////////////////////////////
    /**
     * Gets the current time of the simulation, in ms.
     * @return	the current time, in ms
     */
    public long now() {
        return this.timer / 1000000;
    }

    /**
     * Update the timer.
     */
    private void updateTimer() {
        long now = System.nanoTime();
        if (isPaused == false) {
            long timePassed = now - this.timeLastChecked;
            this.timer += timePassed;
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
    public boolean isTime(final long now, final long last, final long waitTime) {
        if (now - last >= waitTime) {
            return true;
        }
        return false;
    }

    /**
     * Sets the flag that indicates non-critical systems are paused.
     * @param isPaused	true if paused;false otherwise
     */
    public void setPaused(final boolean isPaused) {
        this.isPaused = isPaused;
    }

    /**
     * Returns the flag that indicates that non-critical systems are paused.
     * @return	true if paused; false otherwise
     */
    public boolean isPaused() {
        return this.isPaused;
    }


    //////////////////////////////////////////////////
    // Message Passing
    //////////////////////////////////////////////////
    
    /**
     * Registers a handler for messages of the specified type.
     * @param type  the class type of message to look for
     * @param handler   the handler of the message
     */
    public <T extends IMessage>void addHandler(final Class<T> type, final IMessageHandler<T> handler) {
        List<IMessageHandler<? extends IMessage>> handlers = handlersByMessage.get(type);
        
        if (handlers == null) {
            handlers = new ArrayList<IMessageHandler<? extends IMessage>>();
            handlersByMessage.put(type, handlers);
        }
        
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }
    
    /**
     * Sends the passed message to all interested systems.
     * @param message   the message to send
     */
    public <T extends IMessage> void send(final T message) {
        List<IMessageHandler<? extends IMessage>> handlers = handlersByMessage.get(message.getClass());
        
        if (handlers != null) {
            for (IMessageHandler<? extends IMessage> handler : handlers) {
                ((IMessageHandler<T>) handler).onMessageReceived(message);
            }
        }
    }
    
    /**
     * Sets the debug level of Core.
     * @param level	the Level to set
     */
    public void setDebugLevel(final Level level) {
        LOGGER.log(Level.ALL, "Debug level set to: " + level);
        this.LOGGER.setLevel(level);
    }
}
