package org.example;

import org.example.model.TaskNode;
import org.example.model.TopoSort;
import org.example.model.WorkflowDAG;

import java.util.*;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) {
        // Create TaskNodes
        TaskNode t1 = new TaskNode("task1", Arrays.asList());
        TaskNode t2 = new TaskNode("task2", Arrays.asList("task1"));
        TaskNode t3 = new TaskNode("task3", Arrays.asList("task1"));
        TaskNode t4 = new TaskNode("task4", Arrays.asList("task2", "task3"));

        // Add TaskNodes to Map
        Map<String, TaskNode> taskMap = new HashMap<>();
        taskMap.put(t1.getId(), t1);
        taskMap.put(t2.getId(), t2);
        taskMap.put(t3.getId(), t3);
        taskMap.put(t4.getId(), t4);

        // Create WorkflowDAG
        WorkflowDAG workflow = new WorkflowDAG("demoWorkflow", taskMap);

        // Print workflow structure
        System.out.println("Workflow: " + workflow.getWorkflowId());
        for (TaskNode node : workflow.getTasks().values()) {
            System.out.println("Task: " + node.getId() + " | Depends on: " + node.getDependencies() + " | State: " + node.getState());
        }

        // Print Topological Order
        System.out.println("\nTopological execution order:");
        List<TaskNode> sortedTasks = TopoSort.sortTasks(workflow.getTasks());
        for (TaskNode node : sortedTasks) {
            System.out.println("Task: " + node.getId());
        }

        // Simulate executing tasks in topological order (single-threaded)
        System.out.println("\nSimulating Task Execution:");
        for (TaskNode node : sortedTasks) {
            node.setState(org.example.model.TaskState.RUNNING);
            System.out.println("Task: " + node.getId() + " is RUNNING");
            node.setState(org.example.model.TaskState.SUCCESS);
            System.out.println("Task: " + node.getId() + " completed SUCCESS\n");
        }

        // Print final workflow states
        System.out.println("Final Workflow State:");
        for (TaskNode node : workflow.getTasks().values()) {
            System.out.println("Task: " + node.getId() + " | State: " + node.getState());
        }

        // --- Dependency-Aware Parallel Execution ---
        for (TaskNode node : sortedTasks) {
            node.setState(org.example.model.TaskState.PENDING);
        }

        System.out.println("\nDependency-Aware Parallel Execution Simulation:");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Set<String> completed = Collections.synchronizedSet(new HashSet<>());
        List<Future<?>> futures = new ArrayList<>();

        while (completed.size() < sortedTasks.size()) {
            boolean madeProgress = false;

            for (TaskNode node : sortedTasks) {
                if (node.getState() == org.example.model.TaskState.PENDING) {
                    boolean ready = true;
                    for (String dep : node.getDependencies()) {
                        if (!completed.contains(dep)) {
                            ready = false;
                            break;
                        }
                    }
                    if (ready) {
                        node.setState(org.example.model.TaskState.RUNNING);
                        madeProgress = true;
                        // Submit task for execution and add future for tracking completion
                        Future<?> future = executor.submit(() -> {
                            System.out.println("Task: " + node.getId() + " is RUNNING (thread " + Thread.currentThread().getName() + ")");
                            try { Thread.sleep(500); } catch (InterruptedException e) {}
                            node.setState(org.example.model.TaskState.SUCCESS);
                            completed.add(node.getId());
                            System.out.println("Task: " + node.getId() + " completed SUCCESS\n");
                        });
                        futures.add(future);
                    }
                }
            }
            // Wait for this batch of tasks to finish before proceeding
            for (Future<?> f : futures) {
                try { f.get(); } catch (Exception e) {}
            }
            futures.clear(); // Clear finished tasks
            // If no progress, break to avoid infinite loop
            if (!madeProgress) break;
        }

        executor.shutdown();
        try { executor.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) {}

        System.out.println("Final Workflow State after Dependency-Aware Parallel Execution:");
        for (TaskNode node : workflow.getTasks().values()) {
            System.out.println("Task: " + node.getId() + " | State: " + node.getState());
        }

    }

}
