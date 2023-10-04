package widesim.computation;

public class WorkflowState {
    private double energy;
    private double startTime;
    private double endTime;

    public WorkflowState() {
        this.energy = 0;
        this.startTime = 0;
        this.endTime = 0;
    }

    public WorkflowState(double energy, double startTime, double endTime) {
        this.energy = energy;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter and Setter for energy
    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    // Getter and Setter for startTime
    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    // Getter and Setter for endTime
    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WorkflowState{" +
                "energy=" + energy +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

