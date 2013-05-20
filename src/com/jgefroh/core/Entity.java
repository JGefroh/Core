package com.jgefroh.core;

import java.util.HashMap;


/**
 * A default implementation of the IEntity interface.
 * @author Joseph Gefroh
 * @version 0.1.0
 * @since	16MAY13
 */
public class Entity implements IEntity
{
	//////////
	// DATA
	//////////
	/**Holds the components that belong to this entity.*/
	private HashMap<Class, IComponent> components;	//Uses class type to store
	
	/**Flag that shows whether the entity's state has changed.*/
	private boolean hasChanged = false;
	
	/**The non-unique name of the entity.*/
	private String name;
	
	
	//////////
	// INIT
	//////////
	public Entity()
	{
		components = new HashMap<Class, IComponent>();
		hasChanged = true;
	}
	
	
	//////////
	// GETTERS
	//////////
	/**
	 * Get the component stored in the hashmap.
	 * @param type	Type is the key used to store the components in the hashmap.
	 * @return		the component that fits the type.
	 */
	@Override
	public <T>T getComponent(Class<T> type)
	{
		T t = (T)components.get(type);
		if(t!=null)
		{
			return t;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Return the name of the entity.
	 * @return	the name of the entity
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Return the flag that indicates whether the entity's component makeup
	 * has changed or not.
	 * @return true if it has changed, false otherwise
	 */
	@Override
	public boolean hasChanged()
	{
		return hasChanged;
	}
	
	//////////
	// SETTERS
	//////////
	
	/**
	 * Set the flag that indicates whether the entity's component makeup
	 * has changed.
	 * @param	hasChanged set to true if the entity has changed, false if not.
	 */
	@Override
	public void setChanged(final boolean hasChanged)
	{
		this.hasChanged = hasChanged;
	}
	
	/**
	 * Set the name of the entity.
	 * @param name	the non-unique name of the entity
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}
	
	//////////
	// SYSTEM METHODS
	//////////
	/**
	 * Add a component of any type
	 * @param type
	 * @param component
	 */
	@Override
	public <T extends IComponent> void addComponent(Class<T> type, T component)
	{
		components.put(type, (IComponent)component);
		hasChanged = true;
	}
	
	@Override
	public <T> void removeComponent(Class<T> type)
	{
		if(components.get(type)!=null)
		{
			components.remove(type);
		}
		hasChanged = true;
	}
}
