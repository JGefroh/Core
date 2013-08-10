package com.jgefroh.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that allows for AbstractInfoPack nodes to be traversed according
 * to their same type.
 * 
 * 
 * Note that this Iterator will rewind to the head of any linked list.
 * Make sure there are no cycles or it will rewind forever.
 * @author Joseph Gefroh
 */
public class AbstractInfoPackIterator implements Iterator<AbstractInfoPack>
{
	private AbstractInfoPack head;
	private AbstractInfoPack current;
	private boolean onHead = true;
	
	public AbstractInfoPackIterator(final AbstractInfoPack head)
	{
		this.head = head;
		if(head!=null)
		{
			while(head.hasPrev())
			{
				this.head = head.prev();
			}
		}
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
