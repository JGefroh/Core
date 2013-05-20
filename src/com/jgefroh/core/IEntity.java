package com.jgefroh.core;


/**
 * Defines an interface for an entity, which is simply a collection of
 * components.
 * @author Joseph Gefroh
 * @version 0.1.0
 * @since	16MAY13
 */
public interface IEntity
{
	public <T extends IComponent> void addComponent(final Class<T> type, 
			final T component);
	public <T>T getComponent(final Class<T> type);
	public <T> void removeComponent(final Class<T> type);
	public void setChanged(final boolean hasChanged);
	public boolean hasChanged();
	public String getName();
	public void setName(final String name);

}
