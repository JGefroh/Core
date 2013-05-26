package com.jgefroh.core;

import java.util.HashMap;


/**
 * A default implementation of the IEntity interface.
 * @author Joseph Gefroh
 * @version 0.2.0
 * @since	0.1.0
 */
public class Entity implements IEntity
{
	//////////
	// DATA
	//////////
	//TODO: Get rid of class cast warnings without suppressing them.
	/**Holds the components that belong to this entity.*/
	private HashMap<Class<? extends IComponent>, IComponent> components;
	//TODO: Research pitfalls of Class as key - supposedly causes memory leaks
	
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
		init();
	}
	
	/**
	 * Constructor to instantiate an {@code Entity} with the given name.
	 * @param name	the human-readable, non-unique name of the entity
	 */
	public Entity(final String name)
	{
		setName(name);
	}
	
	private void init()
	{
		this.components = new HashMap<Class<? extends IComponent>, IComponent>();
		this.hasChanged = true;
	}
	//////////
	// GETTERS
	//////////
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IComponent>T getComponent(Class<T> type)
	{
		T t = (T)components.get(type);
		if(t!=null)
		{
			return t;
		}
		else
		{//Returns null anyways :X
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
	public void addComponent(final IComponent component)
	{
		components.put(component.getClass(), component);
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
