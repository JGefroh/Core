package com.jgefroh.core;


/**
 * The interface for an {@code Entity}.
 * 
 * <p>
 * An {@code Entity} is merely a container for {@code Components} that describe
 * the same element.
 * </p>
 * 
 * @author 	Joseph Gefroh
 * @see		IComponent
 */
public interface IEntity {

    /**
     * Adds a {@code Component} to this {@code Entity}.
     * @param component	the instance of the component to add
     */
    void addComponent(IComponent component);

    /**
     * Adds the given {@code Component} to this {@code Entity}.
     * @param component	the instance of the component to add
     */
    void add(IComponent component);

    /**
     * Gets the {@code Component} of the passed type from this {@code Entity}.
     * @param type	the Class type of the component to retrieve
     * @return		the component of the passed type saved in this Entity; null
     * 				if the component does not exist
     */
    <T extends IComponent> T getComponent(Class<T> type);

    /**
     * Sets the unique ID of this {@code Entity}.
     * 
     * <p>
     * The {@code ID} should not be used by any other object.
     * </p>
     * 
     * @return	the unique ID of this Entity
     */
    String getID();

    /**
     * Gets the human-readable name of this {@code Entity}.
     * 
     * <p>
     * The {@code name} should not be used to uniquely identify this
     * {@code Entity}. It is purely for legibility.
     * </p>
     * @return	the name of this Entity, if it was given one
     */
    String getName();

    /**
     * Checks the flag that indicates whether this {@code Entity}'s
     * {@code Component} composition has changed. This should be called
     * by any Entity method that changes the component structure. 
     * @return	{@code true} if there was a change; {@code false} otherwise
     */
    boolean hasChanged();

    /**
     * Sets the flag that indicates whether this {@code Entity}'s 
     * {@code Component} composition has changed.
     * @param hasChanged	{@code true} if there was a change; 
     * 						{@code false} otherwise
     */
    void setChanged(boolean hasChanged);

    /**
     * Gets the unique ID of this {@code Entity}.
     * @param id	the unique ID of this Entity
     */
    void setID(String id);

    /**
     * Sets the human-readable name of this {@code Entity}.
     * 
     * <p>
     * The {@code name} should not be used to uniquely identify this
     * {@code Entity}. It is purely for legibility.
     * </p>
     * @param name	the name of this Entity, if it was given one
     */
    void setName(String name);

    /**
     * Removes the {@code Component} of the passed type from this 
     * {@code Entity}, if it exists.
     * 
     * <p>
     * This method should flag this {@code Entity} as having been changed.
     * </p>
     * @param type	the Class type of the component to remove
     */
    <T> void removeComponent(Class<T> type);

    /**
     * Removes all components of this {@code Entity}.
     */
    void removeAllComponents();

}
