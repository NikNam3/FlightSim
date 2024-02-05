package aircraft.display;

import aircraft.Aircraft;
import math.Vec3;
import simobjects.Entity;

import java.util.List;

public abstract class Display extends Entity {
    public Display(int id, Vec3 relPos, Vec3 relRot, List<DisplayElement> displayElements) {
        super(id, relPos, relRot);
        this.displayElements = displayElements;
    }
    /**
     * A List of one to multiple DisplayElements
     */
    private List<DisplayElement> displayElements;
    /**
     * The Aircraft this Display is attached to
     * This is used to get the Data to Display
     */
    protected Aircraft aircraft;
    /**
     * Updates the Display
     */
    public abstract void Update();
    /**
     * Adds a DisplayElement to the Display
     * @param displayElement is the DisplayElement to add
     */
    public void addDisplayElement(DisplayElement displayElement) {
        displayElements.add(displayElement);
    }
    /**
     * Returns a DisplayElement from the Display
     * @param index is the index of the DisplayElement
     * @return the DisplayElement
     */
    protected DisplayElement getDisplayElement(int index) {
        return displayElements.get(index);
    }
}
