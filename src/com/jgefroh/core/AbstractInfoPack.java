package com.jgefroh.core;

import java.util.Iterator;

/**
 * A base InfoPack class that acts as a node, for use with the provided
 * implementation of Core.
 * 
 * 
 * <p>Extend this to create new InfoPack types.</p>
 */
public abstract class AbstractInfoPack implements IInfoPack, Iterable<AbstractInfoPack>
{
	/**The next info pack of this type.*/
	private AbstractInfoPack next;

	/**The previous info pack of this type.*/
	private AbstractInfoPack prev;
	
	/**The next info pack of any type belonging to the owner.*/
	private AbstractInfoPack ownerNext;

	/**The next info pack of any type belonging to the owner.*/
	private AbstractInfoPack ownerPrev;
	
	/**The components making up this */
	private boolean isDirty;
	
	private boolean onHead;

	public abstract boolean checkDirty();

	@Override
	public boolean isDirty()
	{
		return this.isDirty;
	}

	@Override
	public void setDirty(final boolean isDirty)
	{
		this.isDirty = isDirty;
	}
	
	/**
	 * Gets the next InfoPack of the same type as this InfoPack.
	 * @return	the next InfoPack of the same type as this InfoPack
	 */
	public <T extends AbstractInfoPack>T next()
	{
		return (T)this.next;
	}
	
	/**
	 * Gets the previous InfoPack of the same type as this InfoPack.
	 * @return	the previous InfoPack of the same type as this InfoPack
	 */
	public<T extends AbstractInfoPack>T prev()
	{
		return (T)this.prev;
	}
	/**
	 * Gets the next InfoPack belonging to the same owner.
	 * @return	the next InfoPack belonging to the same owner.
	 */
	public AbstractInfoPack ownerNext()
	{
		return this.ownerNext;
	}
	
	/**
	 * Gets the previous InfoPack belonging to the same owner.
	 * @return	the previous infopack
	 */
	public AbstractInfoPack ownerPrev()
	{
		return this.ownerPrev;
	}
	
	/**
	 * Sets the next InfoPack. It must be the same type.
	 * @param next	the InfoPack
	 */
	public void setNext(final AbstractInfoPack next)
	{
		if(next==null || (next.getClass()==this.getClass()))
		{			
			this.next = next;
		}
	}
	
	/**
	 * Sets the previous InfoPack. It must be the same type.
	 * @param prev	the InfoPack
	 */
	public void setPrev(final AbstractInfoPack prev)
	{
		if(prev==null || (prev.getClass()==this.getClass()))
		{			
			this.prev = prev;
		}
	}
	
	/**
	 * Sets the next InfoPack belonging to the same entity.
	 * @param ownerNext	the InfoPack
	 */
	public void setOwnerNext(final AbstractInfoPack ownerNext)
	{
		if(ownerNext==null || (ownerNext.getOwner()==this.getOwner()))
		{			
			this.ownerNext = ownerNext;
		}
	}
	
	/**
	 * Sets the previous InfoPack belonging to the same entity.
	 * @param ownerPrev
	 */
	public void setOwnerPrev(final AbstractInfoPack ownerPrev)
	{
		if(ownerPrev==null || (ownerPrev.getOwner()==this.getOwner()))
		{			
			this.ownerPrev = ownerPrev;
		}
	}
	
	/**
	 * Checks to see if there is a next InfoPack of the same type.
	 * @return	true if there is; false otherwise
	 */
	public boolean hasNext()
	{
		return (this.next!=null) ? true : false;
	}

	/**
	 * Checks to see if there is a previous InfoPack of the same type.
	 * @return	true if there is; false otherwise
	 */
	public boolean hasPrev()
	{
		return (this.prev!=null) ? true : false;
	}
	
	/**
	 * Checks to see if there is a next InfoPack with the same owner.
	 * @return	true if there is; false otherwise
	 */
	public boolean hasOwnerNext()
	{
		return (this.ownerNext!=null) ? true : false;

	}
	
	/**
	 * Checks to see if there is a previous InfoPack with the same owner.
	 * @return	true if there is; false otherwise
	 */
	public boolean hasOwnerPrev()
	{
		return (this.ownerPrev!=null) ? true : false;

	}
	
	@Override
	public Iterator iterator()
	{
		AbstractInfoPack head = this;
		while(head.hasPrev())
		{
			head = head.prev();
		}
		return new AbstractInfoPackIterator(head);
	}

	@Override
	public abstract IInfoPack generate(final IEntity entity);
}
