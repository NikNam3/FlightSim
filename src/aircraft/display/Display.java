package aircraft.display;

import aircraft.Aircraft;
import math.Vec3;
import simobjects.Entity;

import java.util.List;

public abstract class Display extends Entity {
    public Display(Vec3 relPos, Vec3 relRot) {
        super(relPos, relRot);
    }
    /**
     * A List of the DisplayElements
     * The List is in display order from back to front meaning the first Element with index 0 is the Background
     * and the last Element is the Foreground
     */
    private List<DisplayElement> displayElements;
    /**
     * The Aircraft this Display is attached to
     * This is used to get the Data to Display
     */
    protected Aircraft aircraft;
    /**
     * Updates the Display and all Display elements
     * Gets Overridden by every DisplayType
     */
    public abstract void Update();
    /**
     * Adds a DisplayElement to the Display
     * @param displayElement is the DisplayElement to add
     */
    public void addDisplayElement(DisplayElement displayElement) {
        displayElements.add(displayElement); // Safes the DisplayElement inside this Display for later use

        displayElement.setParent(this); // Handles the Parent Child Relationship considering the Position and Rotation of the Display and its Elements
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
