/*
 * EventManager.java
 * Steven LaGoy
 * Created: 10 December 2024 at 8:21 AM
 * Modified: 26 August 2025
 */

package main.core.politics;

import core.JSONObject;
import main.core.Manager;

public class EventManager extends Manager {

    private ManagerState currentState;

    public EventManager() {
        currentState = ManagerState.INACTIVE;
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    @Override
    public boolean init() {
        boolean successFlag = true;
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        return successFlag;
    }

    @Override
    public ManagerState getState() {
        return currentState;
    }

    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        if (!successFlag) currentState = ManagerState.ERROR;
        return successFlag;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }

    @Override
    public Manager fromJson(JSONObject json) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }

    /*
     * EVENTS
     * Scripted / Scheduled:
     * - Primary and Caucus Conventions
     * - National Conventions
     * - Federal and popular holidays:
     *   - New Years Day
     *   - Martin Luther King Jr. Day
     *   - Presidents' Day
     *   - Memorial Day
     *   - Flag Day
     *   - Juneteenth
     *   - Independence Day
     *   - Labor Day
     *   - Day of Commemoration (9/11)
     *   - Columbus / Indigenous Peoples' Day
     *   - Veterans Day
     *   - Thanksgiving Day
     *   - Christmas Day
     * - Debates
     * 
     * Incidental / Random:
     * 
     */

    
}
