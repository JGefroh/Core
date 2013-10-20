package com.jgefroh.core;


/**
 * Interface for an {@code InfoPack}.
 * 
 * <p>
 * An {@code InfoPack} is intended to provide a {@code System} access 
 * to data held in a {@code component} via accessor methods 
 * (getters/setters). They group together one or more related {@code Components}
 * that contain data needed by a {@code System}.
 * <br>
 * <br>
 * It is also intended to control whether a {@code System} will process an 
 * {@code Entity} through its presence or lack of presence.
 * </p>
 * <p>
 * {@code Core} automatically calls the generate(entity) method to generate an 
 * {@code InfoPack}.
 * </p>
 * 
 * @author 	Joseph Gefroh
 * @see		Core
 * @see		IComponent
 * @see		ISystem
 */
public interface IInfoPack {

    /**
     * Sets the entity that the info pack is currently pointing to.
     * @param entity    the entity to point to
     */
    public void setCurrent(final IEntity entity);

    /**
     * Checks to see if the entity has the required components to use this info pack.
     * @param entity    the entity to check
     * @return  true if the entity can use the pack; false otherwise
     */
    public boolean checkComponents(final IEntity entity);

    /**
     * Points this adapter towards the passed entity and its components.
     * @param entity    the entity to point at
     * @return  true if successfully pointed; false otherwise
     */
    public boolean setEntity(final IEntity entity);

    /**
     * Gets a reference to the ntity currently being pointed to.
     * @return  the entity being pointed to
     */
    public IEntity getEntity();

    /**
     * Creates a new instance of this object.
     * @param type  the type of object to create
     * @return      the created object; null if unsucessful
     */
    public <T extends IInfoPack> T create(final Class<T> type);
}
