package widesim.clustering.balancing.metrics;

import java.util.List;
import widesim.clustering.TaskSet;

/**
 * HorizontalRuntimeVariance is the standard deviation of the runtime
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class HorizontalRuntimeVariance implements BalancingMetric {

    /**
     * Returns the standard deviation of runtime
     * @param list taskSets to be checked
     * @return the standard deviation
     */
    @Override
    public double getMetric(List<TaskSet> list) {
        if (list == null || list.size() <= 1) {
            return 0.0;
        }
        long sum = 0;
        for (TaskSet task : list) {
            sum += task.getJobRuntime();
        }
        long mean = sum / list.size();
        sum = 0;
        for (TaskSet task : list) {
            long var = task.getJobRuntime();
            sum += Math.pow((double) (var - mean), 2);
        }
        return Math.sqrt((double) (sum / list.size())) / mean;
    }
}
