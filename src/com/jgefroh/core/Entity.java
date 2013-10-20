package com.jgefroh.core;

import java.util.HashMap;


/**
 * A default implementation of the IEntity interface.
 * @author Joseph Gefroh
 */
public class Entity implements IEntity {

    //////////////////////////////////////////////////
    // Fields
    //////////////////////////////////////////////////
    
    /**Holds the components that belong to this entity.*/
    private HashMap<Class<? extends IComponent>, IComponent> components;
    //TODO: Research pitfalls of Class as key - supposedly causes memory leaks

    /**Flag that shows whether the entity's state has changed.*/
    private boolean hasChanged = true;

    /**The non-unique name of this Entity.*/
    private String name;

    /**The unique ID of this Entity.*/
    private String id;


    //////////////////////////////////////////////////
    // Constructor
    //////////////////////////////////////////////////
    
    /**
     * Constructor to instantiate an empty {@code Entity}.
     */
    public Entity() {
    }

    /**
     * Constructor to instantiate an {@code Entity} with the given name.
     * @param name	the human-readable, non-unique name of the entity
     */
    public Entity(final String name) {
        setName(name);
    }


    //////////////////////////////////////////////////
    // Getters
    //////////////////////////////////////////////////
    @SuppressWarnings("unchecked")
    @Override
    public <T extends IComponent> T getComponent(Class<T> type) {
        if (components != null) {
            T t = (T) components.get(type);
            if (t != null) {
                return t;
            }
            else {//Returns null anyways :X
                return null;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public String getID() {
        return this.id;
    }

    //////////////////////////////////////////////////
    // Setters
    //////////////////////////////////////////////////
    
    @Override
    public void setChanged(final boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setID(final String id) {
        this.id = id;
    }


    //////////////////////////////////////////////////
    // Methods
    //////////////////////////////////////////////////
    @Override
    public void addComponent(final IComponent component) {
        if (components == null) {
            this.components = new HashMap<Class<? extends IComponent>, IComponent>();
        }
        if (component != null) {
            component.setOwner(this);
            components.put(component.getClass(), component);
            hasChanged = true;
        }
    }

    @Override
    public void add(final IComponent component) {
        addComponent(component);
    }

    @Override
    public <T> void removeComponent(Class<T> type) {
        if (components != null && components.get(type) != null) {
            components.remove(type);
            hasChanged = true;
        }
    }

    public void removeAllComponents() {
        this.components = null;
        this.hasChanged = true;
    }
}
