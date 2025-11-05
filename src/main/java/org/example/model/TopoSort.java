package org.example.model;

import java.util.*;

public class TopoSort {
    public static List<TaskNode> sortTasks(Map<String, TaskNode> tasks) {
        List<TaskNode> sorted = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for(TaskNode node : tasks.values()) {
            visit(node, tasks, visited, sorted);
        }
        return sorted;
    }

    private static void visit(TaskNode node, Map<String, TaskNode> tasks, Set<String> visited, List<TaskNode> sorted) {
        if (!visited.contains(node.getId())) {
            visited.add(node.getId());
            for (String depId : node.getDependencies()) {
                TaskNode dep = tasks.get(depId);
                if(dep != null) {
                    visit(dep, tasks, visited, sorted);
                }
            }
            sorted.add(node);
        }
    }
}
