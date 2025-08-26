package main.core;

/**
 * Abstract class for game Managers, like MapManager, CharacterManager, etc.
 * Managers should hold an internal ManagerState which tracks the state of the Manager.
 * Any class extending Manager should set the ManagerState to {@code INACTIVE} when instantiated.
 */
public abstract class Manager implements Repr<Manager>, Jsonic<Manager> {

    /** Possible internal States of a Manager. */
    public static enum ManagerState {
        /** Manager is initialized, prepared, and ready to recieve messages. */
        ACTIVE,
        /** Manager is uninitialized and not yet ready to recieve messages. @see Manager#init() */
        INACTIVE,
        /** Manager has encountered an error which must be resolved. */
        ERROR;
    }
    
    /** Initialize the Manager, setting the internal ManagerState to ACTIVE when successful. */
    public abstract boolean init();

    /** Get the internal state of the Manager. */
    public abstract ManagerState getState();

    /** Perform cleanup operations on this Manager's internal data, deallocating members and 
     * setting the internal state to {@code INACTIVE}. */
    public abstract boolean cleanup();

}