package widesim.entity;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import widesim.computation.Job;
import widesim.computation.Task;
import widesim.core.Constants;

import java.util.ArrayList;
import java.util.List;


//        import org.workflowsim.clustering.BasicClustering;
//        import org.workflowsim.clustering.BlockClustering;
//        import org.workflowsim.clustering.HorizontalClustering;
//        import org.workflowsim.clustering.VerticalClustering;
//        import org.workflowsim.clustering.balancing.BalancedClustering;
//        import org.workflowsim.utils.ClusteringParameters;
//        import org.workflowsim.utils.Parameters;
//        import org.workflowsim.utils.Parameters.ClassType;
//        import org.workflowsim.utils.ReplicaCatalog;


public final class ClusteringEngine extends SimEntity {

    protected List< Task> taskList;

    protected List<Job> jobList;

    protected List<? extends Task> taskSubmittedList;

    protected List<? extends Task> taskReceivedList;

    protected int cloudletsSubmitted;

    protected BasicClustering engine;

    private final int workflowEngineId;

    private final WorkflowEngine workflowEngine;


    public ClusteringEngine(String name, int schedulers) throws Exception {
        super(name);
        setJobList(new ArrayList<>());
        setTaskList(new ArrayList<>());
        setTaskSubmittedList(new ArrayList<>());
        setTaskReceivedList(new ArrayList<>());

        cloudletsSubmitted = 0;
        this.workflowEngine = new WorkflowEngine(schedulers);
        this.workflowEngineId = this.workflowEngine.getId();
    }

    public int getWorkflowEngineId() {
        return this.workflowEngineId;
    }

    public WorkflowEngine getWorkflowEngine() {
        return this.workflowEngine;
    }

    public void submitTaskList(List<Task> list) {
        getTaskList().addAll(list);
    }

    protected void processClustering() {

        ClusteringParameters params = Parameters.getClusteringParameters();
        switch (params.getClusteringMethod()) {

            case HORIZONTAL:
                // if clusters.num is set in configuration file
                if (params.getClustersNum() != 0) {
                    this.engine = new HorizontalClustering(params.getClustersNum(), 0);
                } // if clusters.size is set in configuration file
                else if (params.getClustersSize() != 0) {
                    this.engine = new HorizontalClustering(0, params.getClustersSize());
                }
                break;

            case VERTICAL:
                int depth = 1;
                this.engine = new VerticalClustering(depth);
                break;

            case BLOCK:
                this.engine = new BlockClustering(params.getClustersNum(), params.getClustersSize());
                break;

            case BALANCED:
                this.engine = new BalancedClustering(params.getClustersNum());
                break;

            default:
                this.engine = new BasicClustering();
                break;
        }
        engine.setTaskList(getTaskList());
        engine.run();
        setJobList(engine.getJobList());
    }

    protected void processDatastaging() {

        List<FileItem> list = this.engine.getTaskFiles();

        Job job = new Job(getJobList().size(), 110);

        List<FileItem> fileList = new ArrayList<>();
        for (FileItem file : list) {

            if (file.isRealInputFile(list)) {
                ReplicaCatalog.addFileToStorage(file.getName(), Parameters.SOURCE);
                fileList.add(file);
            }
        }
        job.setFileList(fileList);
        job.setClassType(ClassType.STAGE_IN.value);

        job.setDepth(0);
        job.setPriority(0);

        job.setUserId(getWorkflowEngine().getSchedulerId(0));


        for (Job cJob : getJobList()) {

            if (cJob.getParentList().isEmpty()) {
                cJob.addParent(job);
                job.addChild(cJob);
            }
        }
        getJobList().add(job);
    }


    @Override
    public void processEvent(SimEvent ev) {

        switch (ev.getTag()) {
            case Constants.MsgTag.JOB_SUBMIT:
                List list = (List) ev.getData();
                setTaskList(list);
                processClustering();
                processDatastaging();
                sendNow(this.workflowEngineId, Constants.MsgTag.JOB_SUBMIT, getJobList());
                break;
            case CloudSimTags.END_OF_SIMULATION:
                shutdownEntity();
                break;
            default:
                processOtherEvent(ev);
                break;
        }
    }

    protected void processOtherEvent(SimEvent ev) {
        if (ev == null) {
            Log.printLine(getName() + ".processOtherEvent(): " + "Error - an event is null.");
            return;
        }

        Log.printLine(getName() + ".processOtherEvent(): "
                + "Error - event unknown by this DatacenterBroker.");
    }

    protected void finishExecution() {
        //sendNow(getId(), CloudSimTags.END_OF_SIMULATION);
    }

    @Override
    public void shutdownEntity() {
        Log.printLine(getName() + " is shutting down...");
    }

    @Override
    public void startEntity() {
        Log.printLine(getName() + " is starting...");
//        schedule(getId(), 0, WorkflowSimTags.START_SIMULATION);
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTaskList() {
        return (List<Task>) taskList;
    }


    public List<Job> getJobList() {
        return jobList;
    }


    protected void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }


    protected void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTaskSubmittedList() {
        return (List<Task>) taskSubmittedList;
    }


    protected void setTaskSubmittedList(List<Task> taskSubmittedList) {
        this.taskSubmittedList = taskSubmittedList;
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTaskReceivedList() {
        return (List<Task>) taskReceivedList;
    }

    protected void setTaskReceivedList(List<Task> taskReceivedList) {
        this.taskReceivedList = taskReceivedList;
    }
}
