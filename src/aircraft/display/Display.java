package aircraft.display;

import aircraft.Aircraft;

import java.util.List;

public abstract class Display {
    public Display(List<DisplayElement> displayElements) {
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
