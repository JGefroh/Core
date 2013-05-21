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
	/**
	 * Constructor to instantiate an empty {@code Entity}.
	 */
	public Entity()
	{
		components = new HashMap<Class, IComponent>();
		hasChanged = true;
	}
	
	//////////
	// GETTERS
	//////////
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
	
	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public boolean hasChanged()
	{
		return hasChanged;
	}
	
	
	//////////
	// SETTERS
	//////////
	@Override
	public void setChanged(final boolean hasChanged)
	{
		this.hasChanged = hasChanged;
	}
	
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}
	
	//////////
	// SYSTEM METHODS
	//////////
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
