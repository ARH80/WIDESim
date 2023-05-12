package widesim.clustering.balancing.metrics;

import java.util.List;
import widesim.clustering.TaskSet;

/**
 * The PipelineRuntimeVariance is the standard deviation of the runtime of the pipelines
 * 
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class PipelineRuntimeVariance implements BalancingMetric {

    /**
     * Returns the standard deviation of the pipeline runtime
     * @param list TaskSets to be checked
     * @return the standard deviation
     */
    @Override
    public double getMetric(List<TaskSet> list) {
        if (list == null || list.size() <= 1) {
            return 0.0;
        }
        double[] rv = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            TaskSet set = (TaskSet) list.get(i);
            rv[i] = getPipelineSum(set);
            //Log.printLine(rv[i]);
        }
        double sum = 0.0;
        for (int i = 0; i < list.size(); i++) {
            sum += rv[i];
        }
        double mean = sum / list.size();
        sum = 0.0;
        for (int i = 0; i < list.size(); i++) {
            sum += Math.pow(rv[i] - mean, 2);
        }
        if (mean == 0.0) {
            return 0.0;
        }
        return Math.sqrt(sum / list.size()) / mean;
    }

    /**
     * Calculates the pipeline runtime
     * @param task TaskSet
     * @return the sum of the Task runtime
     */
    private double getPipelineSum(TaskSet task) {
        double sum = 0.0;
        if (task == null) {
            return sum;
        }
        sum += task.getJobRuntime();
        while (task.getChildList().size() == 1) {
            TaskSet kid = task.getChildList().get(0);
            if (kid.getParentList().size() == 1) {
                sum += kid.getJobRuntime();
            } else {
                break;
            }
            task = kid;
        }
        return sum;
    }
}
