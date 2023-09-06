package widesim.examples.ensemble_workflows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.jgrapht.alg.util.Pair;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerVm;
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

import java.io.File;

public class EnsembleWorkflows {

    public static List<String> getRandomElements(List<Integer> originalList, int n, String name) {
        List<String> randomList = new ArrayList<>();
        Random rand = new Random();

        int originalSize = originalList.size();

        // Generate random indices to select elements
        for (int i = 0; i < n; i++) {
            int randomIndex = rand.nextInt(originalSize);
            int randomElement = originalList.get(randomIndex);
            randomList.add(name + "_" + randomElement);
        }

        return randomList;
    }
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

        String wfName = "Sipht";
        int simSize = 5;

        List<Integer> sizes = new ArrayList<>();
        sizes.add(30);
        sizes.add(60);
        sizes.add(100);
        sizes.add(1000);

        List<String> strList = getRandomElements(sizes, simSize, wfName);
        System.out.println("List of Workflows:");
        System.out.println(strList);

        int startId = 0;
        List<Workflow> workflowList = new ArrayList<>();

        for (String name : strList) {
            var daxParser = new DaxParser(String.format("src/main/resources/dax/%s.xml", name));
            Pair<Workflow, Integer> wf = daxParser.buildMultipleWorkflow(startId, 0);
            var workflow = List.of(wf.getFirst());
            workflowList.addAll(workflow);
            startId+=wf.getSecond();
        }

        var workflowEngine = new WorkflowEngine(fogBroker.getId());
        fogBroker.setWorkflowEngineId(workflowEngine.getId());

        var taskManager = new TaskManager(workflowEngine.getId(), workflowList);

        CloudSim.startSimulation();

        CloudSim.stopSimulation();

        int utilSize = 0;
        List<Task> tasks = fogBroker.getReceivedTasks();
        for (Vm vm : fogBroker.getVmList()) {
            PowerVm newvm = (PowerVm) vm;
            utilSize += newvm.getUtilizationMean();
            System.out.println(newvm.getUtilizationMean());
        }
        
        IntStream.range(0, fogBroker.getMaximumCycle() + 1).forEach(cycle -> {
            System.out.println("Cycle: " + cycle);
            Logger.printResult(cycle, tasks, fogBroker.getVmToFogDevice(), fogDevices);
        });
        System.out.println("Sum of utilizations: " + utilSize);
    }
    
}
