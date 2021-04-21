package misty.mapper;

import misty.entity.FogVm;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SimpleVmToFogDeviceMapper implements VmToFogDeviceMapper {
    @Override
    public Map<Integer, Integer> map(Map<Integer, DatacenterCharacteristics> datacenterToCharacteristics, List<FogVm> vms) {
        HashMap<Integer, Integer> vmToFogDevice = new HashMap<>();

        var fogDeviceIds = new ArrayList<>(datacenterToCharacteristics.keySet());

        IntStream.range(0, vms.size()).forEach(vmIndex -> {
            FogVm vm = vms.get(vmIndex);
            if (vm.getAssignedFogDeviceId() != null) {
                vmToFogDevice.put(vm.getId(), vm.getAssignedFogDeviceId());
            } else {
                int fogDeviceIndex = vmIndex % fogDeviceIds.size();

                vmToFogDevice.put(vm.getId(), fogDeviceIds.get(fogDeviceIndex));
            }
        });

        return vmToFogDevice;
    }
}
