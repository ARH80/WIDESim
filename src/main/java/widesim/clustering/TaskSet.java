package widesim.clustering;

import java.util.ArrayList;
import java.util.List;
import widesim.computation.Task;

/**
 * TaskSet is a group of tasks used only in clustering
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class TaskSet {

    /**
     * the task list.
     */
    private final List<Task> taskList;
    /**
     * the parent list of this taskset.
     */
    private final List<TaskSet> parentList;
    /**
     * the child list of this taskset.
     */
    private final List<TaskSet> childList;
    /**
     * the check point.
     */
    public boolean hasChecked;
    /**
     * the impact factor.
     */
    private double impactFactor;

    /**
     * Initialize a TaskSet object
     */
    public TaskSet() {
        this.taskList = new ArrayList<>();
        this.parentList = new ArrayList<>();
        this.childList = new ArrayList<>();
        this.hasChecked = false;
        this.impactFactor = 0.0;
    }

    /**
     * Gets the impact factor
     *
     * @return impact factor
     */
    public double getImpactFactor() {
        return this.impactFactor;
    }

    /**
     * Sets the impact factor
     *
     * @param factor the impact factor
     */
    public void setImpactFafctor(double factor) {
        this.impactFactor = factor;
    }

    /**
     * Gets the parent list
     *
     * @return parent list
     */
    public List<TaskSet> getParentList() {
        return this.parentList;
    }

    /**
     * Gets the child list
     *
     * @return the child list
     */
    public List<TaskSet> getChildList() {
        return this.childList;
    }

    /**
     * Gets task list
     *
     * @return task list
     */
    public List<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * Adds a task to this taskSet
     *
     * @param task to be added
     */
    public void addTask(Task task) {
        this.taskList.add(task);
    }

    /**
     * Adds a list of task to this taskSet
     *
     * @param list to be added
     */
    public void addTask(List<Task> list) {
        this.taskList.addAll(list);
    }

    /**
     * Gets the job runtime of this taskSet (sum of task runtime)
     *
     * @return job runtime
     */
    public long getJobRuntime() {
        long runtime = 0;
        for (Task task : taskList) {
            runtime += task.getCloudletLength();
        }
        return runtime;
    }
}
