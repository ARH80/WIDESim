package widesim.clustering;

/**
 * ClusteringParameters contains all the parameters used in task clustering
 */
public class ClusteringParameters {

    /**
     * The number of clustered jobs per level. You just need to set one of
     * clusters.num or clusteres.size
     */
    private final int clusters_num;
    /**
     * The size of clustered jobs (=The number of tasks in a clustered job)
     */
    private final int clusters_size;

    /**
     * Supported Clustering Method, by default it is none
     */
    public enum ClusteringMethod {

        HORIZONTAL, VERTICAL, NONE, BLOCK, BALANCED
    }
    /**
     * Used for balanced clustering to tell which specific balanced clustering
     * to use
     */
    private final String code;
    /**
     * Supported Clustering Method, by default it is none
     */
    private final ClusteringMethod method;

    /**
     * Gets the code for balanced clustering Please refer to our balanced
     * clustering paper for details
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the number of clustered jobs per level
     *
     * @return clusters.num
     */
    public int getClustersNum() {
        return clusters_num;
    }

    /**
     * Gets the size of clustered jobs, which is equal to the number of tasks in
     * a job
     *
     * @return clusters.size
     */
    public int getClustersSize() {
        return clusters_size;
    }

    /**
     * Gets the clustering method
     *
     * @return clusters.method
     */
    public ClusteringMethod getClusteringMethod() {
        return method;
    }

    /**
     * Initialize a ClusteringParameters
     *
     * @param cNum, clustes.num
     * @param cSize, clusters.size
     * @param method, clusters.method
     * @param code , balanced clustering code (used for research)
     */
    public ClusteringParameters(int cNum, int cSize, ClusteringMethod method, String code) {
        this.clusters_num = cNum;
        this.clusters_size = cSize;
        this.method = method;
        this.code = code;
    }
}