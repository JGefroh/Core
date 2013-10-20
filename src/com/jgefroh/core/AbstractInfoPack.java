package com.jgefroh.core;


/**
 * An IInfoPack that acts as an adapter.
 * 
 * <p>Extend this to create new InfoPack types.</p>
 * @author Joseph Gefroh
 */
public abstract class AbstractInfoPack implements IInfoPack {

    /**The entity that is currently being pointed to.*/
    private IEntity current;

    @Override
    public IEntity getEntity() {
        return this.current;
    }

    @Override
    public void setCurrent(final IEntity current) {
        this.current = current;
    }

    @Override
    public <T extends IInfoPack> T create(final Class<T> type) {
        try {
            return type.newInstance();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public abstract boolean setEntity(final IEntity entity);

    @Override
    public abstract boolean checkComponents(final IEntity entity);
}
