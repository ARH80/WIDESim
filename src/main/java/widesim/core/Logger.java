package widesim.core;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerVm;
import com.jakewharton.fliptables.FlipTable;
import widesim.computation.Task;
import widesim.computation.Workflow;
import widesim.computation.TaskState.State;
import widesim.entity.FogDevice;
import widesim.failure.FailureParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Logger {
    public static void log(String tag, String formatted, Object... args) {
        System.out.printf("[%.2f]|[%s]: ", CloudSim.clock(), tag);

        System.out.printf(formatted, args);

        System.out.println();
    }

    public static void printResult(int cycle, List<Task> tasks, Map<Integer, Integer> vmToFogDevice, List<FogDevice> fogDevices) {
        ArrayList<String> headersList = new ArrayList<>();
        headersList.add("Task ID");
        headersList.add("Workflow ID");
        headersList.add("On Vm");
        if (FailureParameters.getFailureGeneratorMode() != FailureParameters.FTCFailure.FAILURE_NONE) {
            headersList.add("Failed Histories");
        }
        headersList.add("Start Execution Time");
        headersList.add("End Execution Time");
        headersList.add("Duration");
        String[] headers = headersList.toArray(new String[0]);
        String[][] data = new String[tasks.size()][headers.length];

        AtomicReference<Double> endExecutionTime = new AtomicReference<>(0.0);
        AtomicReference<Integer> totalFailures = new AtomicReference<>(0);

        IntStream.range(0, tasks.size()).forEach(index -> {
            Task task = tasks.get(index);
            State state = task.getTaskState().getState(cycle);
//            String fogDeviceName = "";
//            for (FogDevice fogDevice: fogDevices) {
//                if (fogDevice.getId() == vmToFogDevice.get(task.getVmId())) {
//                    fogDeviceName = fogDevice.getName();
//                }
//            }

            totalFailures.set(totalFailures.get() + task.getFailedExecutions().size());

            if (FailureParameters.getFailureGeneratorMode() != FailureParameters.FTCFailure.FAILURE_NONE) {
                data[index] = new String[]{
                        task.getTaskId() + "",
                        task.getWorkflowId() + "",
                        //                    fogDeviceName,
                        task.getVmId() + "",
                        task.getFailedExecutions().stream().map(num -> String.format("%.2f", num)).collect(Collectors.joining(", ")) + "",
                        //                    state.enterBrokerWaitingQueue + "",
                        //                    state.exitBrokerWaitingQueue + "",
                        //                    state.enterFogDeviceWaitingQueue + "",
                        //                    state.exitFogDeviceWaitingQueue + "",
                        String.format("%.2f", state.startExecutionTime),
                        String.format("%.2f", state.endExecutionTime),
                        String.format("%.2f", state.endExecutionTime - state.startExecutionTime),
                };
            } else {
                data[index] = new String[]{
                        task.getTaskId() + "",
                        task.getWorkflowId() + "",
                        task.getVmId() + "",
                        String.format("%.2f", state.startExecutionTime),
                        String.format("%.2f", state.endExecutionTime),
                        String.format("%.2f", state.endExecutionTime - state.startExecutionTime),
                };
            }

            if (endExecutionTime.get() < state.endExecutionTime) {
                endExecutionTime.set(state.endExecutionTime);
            }
        });

        System.out.println(FlipTable.of(headers, data));
        System.out.println("End Execution Time: " + endExecutionTime);
        if (FailureParameters.getFailureGeneratorMode() != FailureParameters.FTCFailure.FAILURE_NONE) {
            System.out.println("Total Failures: " + totalFailures);
        }
    }

    public static void printResultWorkflow(int cycle, List<Workflow> workflows, List<Vm> vmList) {
        ArrayList<String> headersList = new ArrayList<>();
        headersList.add("Workflow ID");
        headersList.add("Start Execution Time");
        headersList.add("End Execution Time");
        headersList.add("Duration");
        headersList.add("Energy");
        String[] headers = headersList.toArray(new String[0]);
        String[][] data = new String[workflows.size()][headers.length];

        IntStream.range(0, workflows.size()).forEach(index -> {
            Workflow workflow = workflows.get(index);
            AtomicReference<Double> startExecutionTime = new AtomicReference<>(9999999999.0);
            AtomicReference<Double> endExecutionTime = new AtomicReference<>(0.0);
            AtomicReference<Double> energy = new AtomicReference<>(0.0);
            for (Task task : workflow.getTasks()) {
                PowerVm vm = (PowerVm) vmList.get(task.getVmId());
                energy.set(energy.get() + (task.getTaskState().getState(cycle).endExecutionTime - task.getTaskState().getState(cycle).startExecutionTime) * vm.getUtilizationMean());
                
                if (endExecutionTime.get() < task.getTaskState().getState(cycle).endExecutionTime) {
                    endExecutionTime.set(task.getTaskState().getState(cycle).endExecutionTime);
                }
                if (startExecutionTime.get() > task.getTaskState().getState(cycle).startExecutionTime) {
                    startExecutionTime.set(task.getTaskState().getState(cycle).startExecutionTime);
                }
            }
            data[index] = new String[]{
                    workflow.getWorkflowId() + "",
                    String.format("%.2f", startExecutionTime.get()),
                    String.format("%.2f", endExecutionTime.get()),
                    String.format("%.2f", endExecutionTime.get() - startExecutionTime.get()),
                    String.format("%.2f", energy.get())
            };
        });

        System.out.println(FlipTable.of(headers, data));
    }
}
