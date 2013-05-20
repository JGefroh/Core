package com.jgefroh.core;

/**
 * Interface for the InfoPack factory.
 * TODO: Make this unnecessary and automatic.
 * 
 * @author Joseph Gefroh
 */
public interface IInfoPackFactory
{
	public <T extends IInfoPack>T generate(final IEntity entity);
}
