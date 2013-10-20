package com.jgefroh.core;

import com.jgefroh.core.IComponent;
import com.jgefroh.core.IEntity;

/**
 * A base implementation of IComponent for convenience.
 * 
 * <p>Extend this to create your own components.</p>
 * @author Joseph Gefroh
 */
public abstract class AbstractComponent implements IComponent {

    //////////////////////////////////////////////////
    // Fields
    //////////////////////////////////////////////////
    /**The owner of this component.*/
    private IEntity owner;


    //////////////////////////////////////////////////
    // Getters
    //////////////////////////////////////////////////
    
    @Override
    public IEntity getOwner() {
        return this.owner;
    }

    
    //////////////////////////////////////////////////
    // Setters
    //////////////////////////////////////////////////
    
    @Override
    public void setOwner(final IEntity owner) {
        this.owner = owner;
    }
}
