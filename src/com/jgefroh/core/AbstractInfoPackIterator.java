package com.jgefroh.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of an InfoPack as a node.
 */
public class AbstractInfoPackIterator implements Iterator<AbstractInfoPack>
{
	private AbstractInfoPack head;
	private AbstractInfoPack current;
	private boolean onHead = true;
	
	public AbstractInfoPackIterator(final AbstractInfoPack head)
	{
		this.head = head;
	}
	@Override
	public boolean hasNext()
	{
		if(this.onHead==false)
		{//This is not the very first node...
			if(this.current.hasNext())
			{//If the node has one more after it...
				return true;
			}
		}
		else
		{//If this is the very first node...
			if(this.head!=null)
			{//If there is at least one element...
				return true;
			}
		}
		return false;
	}

	@Override
	public AbstractInfoPack next()
	{
		if(this.onHead==false)
		{
			if(current.hasNext())
			{
				current = current.next();
				return current;
			}
		}
		else
		{
			this.onHead = false;
			this.current = head;
			return this.head;
		}
		throw new NoSuchElementException("No further node.");
	}

	@Override
	public void remove()
	{		
		throw new UnsupportedOperationException("remove() is not supported by this implementation.");
	}
	
}
