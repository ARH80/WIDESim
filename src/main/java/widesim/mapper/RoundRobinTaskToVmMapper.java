package widesim.mapper;

import org.cloudbus.cloudsim.power.PowerVm;
import org.jgrapht.alg.util.Pair;
import widesim.computation.Task;
import widesim.entity.FogVm;

import java.util.*;

public class RoundRobinTaskToVmMapper implements TaskToVmMapper {

    @Override
    public Map<Integer, Integer> map(List<PowerVm> createdVms, List<PowerVm> failedVms, List<Task> queuedTasks, Set<Task> completedTasks, Set<Task> dispatchedTasks, Map<Integer, Integer> taskToVm, Map<Pair<Integer, Integer>, Integer> routingTable, Map<Integer, Integer> vmToFogDevice) {

        HashMap<Integer, Integer> newTaskToVm = new HashMap<>();
        int numVms = createdVms.size();
        int vmIndex = 0; // Index of the current VM to assign task

        if (!createdVms.isEmpty()) {
            for (Task queuedTask : queuedTasks) {
                if (queuedTask.getAssignedVmId() != null) {
                    newTaskToVm.put(queuedTask.getTaskId(), queuedTask.getAssignedVmId());
                } else {
                    PowerVm selectedVm = createdVms.get(vmIndex);
                    FogVm fogVm = (FogVm) selectedVm;

                    int limit = 0;

                    // Find the next available VM in round-robin fashion
                    while (fogVm.isBusy()) {
                        vmIndex = (vmIndex + 1) % numVms; // Move to the next VM
                        selectedVm = createdVms.get(vmIndex);
                        fogVm = (FogVm) selectedVm;

                        if (limit++ > numVms)
                            break;
                    }

                    vmIndex = (vmIndex + 1) % numVms; // Move to the next VM
                    selectedVm = createdVms.get(vmIndex);
                    fogVm = (FogVm) selectedVm;

                    // Assign the task to the selected VM
                    fogVm.setBusy(true);
                    queuedTask.setVmId(selectedVm.getId());
                    newTaskToVm.put(queuedTask.getTaskId(), selectedVm.getId());

                    // Move to the next VM for the next task
                    vmIndex = (vmIndex + 1) % numVms;
                }
            }
        }
        return newTaskToVm;
    }
}
