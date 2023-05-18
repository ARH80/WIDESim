package widesim.clustering.balancing.metrics;

import java.util.List;
import widesim.clustering.TaskSet;

/**
 * Every balancing metric should implement this interface
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public interface BalancingMetric {
    /** Gets the metric value.
     * @param list
     * @return  */
    public double getMetric(List<TaskSet> list);
}