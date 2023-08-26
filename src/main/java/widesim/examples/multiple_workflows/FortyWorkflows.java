package widesim.examples.multiple_workflows;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerVm;
import org.jgrapht.alg.util.Pair;

import widesim.computation.Task;
import widesim.computation.Workflow;
import widesim.core.Logger;
import widesim.entity.FogBroker;
import widesim.entity.TaskManager;
import widesim.entity.WorkflowEngine;
import widesim.mapper.FCFSTaskToVMMapper;
import widesim.mapper.SimpleVmToFogDeviceMapper;
import widesim.parse.dax.DaxParser;
import widesim.parse.topology.Parser;
import widesim.parse.topology.PostProcessor;
import widesim.provision.SimpleVmProvisioner;

public class FortyWorkflows {
    public static void main(String[] args) throws Exception {
        CloudSim.init(1, Calendar.getInstance(), false);

        // Parse topology
        var topologyParser = new Parser(new File("src/main/resources/topologies/a_edge_server.json"));
        var deviceAndVms = topologyParser.parse();

        var fogDevices = deviceAndVms.getFirst();

        PostProcessor.connectHostToDatacenter(fogDevices);

        var topologyAnalyzer = PostProcessor.buildTopologyAnalyzer(fogDevices);
        var routingTable = topologyAnalyzer.buildRoutingTable();
        PostProcessor.setRoutingTableOfFogDevices(fogDevices, routingTable);
        var convertedRoutingTable = PostProcessor.convertNameToId(fogDevices, routingTable);

        var vms = deviceAndVms.getSecond();
        var fogBroker = new FogBroker("broker", new SimpleVmProvisioner(), new SimpleVmToFogDeviceMapper(), new FCFSTaskToVMMapper(), 10L, 10L, convertedRoutingTable, fogDevices);
        vms.forEach(vm -> {
            vm.setUid(PowerVm.getUid(fogBroker.getId(), vm.getId()));
            vm.setUserId(fogBroker.getId());
        });
        fogBroker.submitVmList(vms);

        String[] strList = {
            "CyberShake_30",
            "Epigenomics_24",
            "Inspiral_30",
            "Montage_25",
            "Sipht_6",

            "CyberShake_50",
            "Epigenomics_46",
            "Inspiral_50",
            "Montage_50",
            "Sipht_30",

            "CyberShake_100",
            "Epigenomics_100",
            "Inspiral_100",
            "Montage_100",
            "Sipht_60",

            "CyberShake_1000",
            "Epigenomics_997",
            "Inspiral_1000",
            "Montage_1000",
            "Sipht_100",

            "CyberShake_1000",
            "Epigenomics_997",
            "Inspiral_1000",
            "Montage_1000",
            "Sipht_100",

            "CyberShake_1000",
            "Epigenomics_997",
            "Inspiral_1000",
            "Montage_1000",
            "Sipht_100",

            "CyberShake_1000",
            "Epigenomics_997",
            "Inspiral_1000",
            "Montage_1000",
            "Sipht_100",

            "CyberShake_1000",
            "Epigenomics_997",
            "Inspiral_1000",
            "Montage_1000",
            "Sipht_100",
        };

        int startId = 0;
        int daxId = 0;
        List<Workflow> workflowList = new ArrayList<>();

        for (String name : strList) {
            var daxParser = new DaxParser(String.format("src/main/resources/dax/%s.xml", name), daxId);
            Pair<Workflow, Integer> wf = daxParser.buildMultipleWorkflow(startId, 0);
            var workflow = List.of(wf.getFirst());
            workflowList.addAll(workflow);
            startId+=wf.getSecond();
            daxId+=1;
        }

        var workflowEngine = new WorkflowEngine(fogBroker.getId());
        fogBroker.setWorkflowEngineId(workflowEngine.getId());

        var taskManager = new TaskManager(workflowEngine.getId(), workflowList);

        CloudSim.startSimulation();

        CloudSim.stopSimulation();

        List<Task> tasks = fogBroker.getReceivedTasks();
        for (Vm vm : fogBroker.getVmList()) {
            PowerVm newvm = (PowerVm) vm;
            System.out.println(newvm.getUtilizationMean());
        }
        IntStream.range(0, fogBroker.getMaximumCycle() + 1).forEach(cycle -> {
            System.out.println("Cycle: " + cycle);
            Logger.printResult(cycle, tasks, fogBroker.getVmToFogDevice(), fogDevices);
        });
    }
}
