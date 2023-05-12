package widesim.clustering.balancing.methods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import widesim.computation.Task;
import widesim.clustering.TaskSet;

/**
 * HorizontalRuntimeBalancing is a method that merges task so as to balance job runtime
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class HorizontalRandomClustering extends BalancingMethod {

    /**
     * Initialize a HorizontalRuntimeBalancing object
     * @param levelMap the level map
     * @param taskMap the task map
     * @param clusterNum the clustes.num
     */
    public HorizontalRandomClustering(Map levelMap, Map taskMap, int clusterNum) {
        super(levelMap, taskMap, clusterNum);
    }

    /**
     * The main function
     */
    @Override
    public void run() {
        Map<Integer, List<TaskSet>> map = getLevelMap();
        for (List<TaskSet> taskList : map.values()) {
            /**The reason why we don shuffle is very complicated. */
            long seed = System.nanoTime();
            Collections.shuffle(taskList, new Random(seed));
            seed = System.nanoTime();
            Collections.shuffle(taskList, new Random(seed));

            if (taskList.size() > getClusterNum()) {
                List<TaskSet> jobList = new ArrayList<>();
                for (int i = 0; i < getClusterNum(); i++) {
                    jobList.add(new TaskSet());
                }
                int index = 0;
                for (TaskSet set : taskList) {
                    //MinHeap is required 
                    TaskSet job = (TaskSet) jobList.get(index);
                    index ++ ;
                    if(index == getClusterNum()){
                        index = 0;
                    }
                    job.addTask(set.getTaskList());
                    //update dependency
                    for (Task task : set.getTaskList()) {
                        getTaskMap().put(task, job);//this is enough
                    }

                }
                taskList.clear();
            } else {
                //do nothing since 
            }

        }
    }
    
}
