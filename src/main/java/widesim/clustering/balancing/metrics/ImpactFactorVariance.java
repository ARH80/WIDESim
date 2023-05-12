package widesim.clustering.balancing.metrics;

import java.util.List;
import widesim.clustering.TaskSet;

/**
 * ImpactFactorVariance is the standard deviation of their impact factors
 * 
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class ImpactFactorVariance implements BalancingMetric {

    /**
     * Returns the standard deviation of their impact factors
     * @param list the TaskSets to be checked
     * @return the standard deviation
     */
    @Override
    public double getMetric(List<TaskSet> list) {
        if (list == null || list.size() <= 1) {
            return 0.0;
        }
        double sum = 0;
        for (TaskSet task : list) {
            sum += task.getImpactFactor();

        }
        double mean = sum / list.size();
        //Log.printLine("sum: " + sum );
        sum = 0.0;
        for (TaskSet task : list) {
            double var = task.getImpactFactor();
            sum += Math.pow(var - mean, 2);
        }
        return Math.sqrt(sum / list.size());
    }
}
