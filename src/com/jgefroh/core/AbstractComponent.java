package com.jgefroh.core;

import com.jgefroh.core.IComponent;
import com.jgefroh.core.IEntity;

/**
 * @author Joseph Gefroh
 */
public abstract class AbstractComponent implements IComponent
{
	//////////
	// DATA
	//////////
	/**The owner of this component.*/
	private IEntity owner;
	
	//////////
	// INIT
	//////////
	@Override
	public abstract void init();

	//////////
	// GETTERS
	//////////
	@Override
	public IEntity getOwner()
	{
		return this.owner;
	}
	
	//////////
	// SETTERS
	//////////
	@Override
	public void setOwner(final IEntity owner)
	{
		this.owner = owner;
	}
}
