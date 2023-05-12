package widesim.clustering;

import java.util.ArrayList;

/**
 * This data structure AbstractArrayList is used in clustering alone It is
 * better than ArrayList since it associates with depth information
 */
public class AbstractArrayList {

    /**
     * The task list.
     */
    private final ArrayList taskList;
    /**
     * The depth of these tasks.
     */
    private final int depth;

    /**
     * Initialize AbstractArrayList
     *
     * @param taskList the task list
     * @param depth the level of these tasks
     */
    public AbstractArrayList(ArrayList taskList, int depth) {
        this.taskList = taskList;
        this.hasChecked = false;
        this.depth = depth;
    }

    /**
     * Gets the task list
     *
     * @return task list
     */
    public ArrayList getArrayList() {
        return this.taskList;
    }

    /**
     * Gets the depth of these tasks
     *
     * @return depth
     */
    public int getDepth() {
        return this.depth;
    }
    /**
     * A check point.
     */
    public boolean hasChecked;
}