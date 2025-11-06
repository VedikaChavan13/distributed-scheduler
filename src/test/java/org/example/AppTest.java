package org.example;

import org.example.model.TaskNode;
import org.example.model.WorkflowDAG;
import org.example.model.TopoSort;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AppTest {

    @Test
    public void testTopologicalSortOrder() {
        // Create a sample workflow DAG
        TaskNode t1 = new TaskNode("task1", Arrays.asList());
        TaskNode t2 = new TaskNode("task2", Arrays.asList("task1"));
        TaskNode t3 = new TaskNode("task3", Arrays.asList("task1"));
        TaskNode t4 = new TaskNode("task4", Arrays.asList("task2", "task3"));

        Map<String, TaskNode> taskMap = new HashMap<>();
        taskMap.put(t1.getId(), t1);
        taskMap.put(t2.getId(), t2);
        taskMap.put(t3.getId(), t3);
        taskMap.put(t4.getId(), t4);

        WorkflowDAG workflow = new WorkflowDAG("demoWorkflow", taskMap);
        List<TaskNode> sorted = TopoSort.sortTasks(workflow.getTasks());

        // Topo order: t1 before t2 and t3; t2 and t3 before t4
        assertTrue(sorted.indexOf(t1) < sorted.indexOf(t2));
        assertTrue(sorted.indexOf(t1) < sorted.indexOf(t3));
        assertTrue(sorted.indexOf(t2) < sorted.indexOf(t4));
        assertTrue(sorted.indexOf(t3) < sorted.indexOf(t4));
    }
}
