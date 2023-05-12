package widesim.clustering;

import java.util.List;
import widesim.parse.dax.File;
import widesim.computation.Job;
import widesim.computation.Task;

/**
 * The ClusteringInterface for all clustering methods
 */
public interface ClusteringInterface {

    /**
     * set the task list.
     * @param list
     */
    public void setTaskList(List<Task> list);

    /**
     * get job list.
     * @return 
     */
    public List<Job> getJobList();

    /**
     * get task list.
     * @return 
     */
    public List<Task> getTaskList();

    /**
     * the main function.
     */
    public void run();

    /**
     * get all the task files.
     * @return 
     */
    public List<File> getTaskFiles();
}
