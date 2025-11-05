package org.example.model;

import java.util.List;

public class TaskNode {
    private String id;
    private List<String> dependencies; // List of IDs of tasks this depends on
    private TaskState state;

    public TaskNode(String id, List<String> dependencies) {
        this.id = id;
        this.dependencies = dependencies;
        this.state = TaskState.PENDING;
    }

    // Getters and setters...
    public String getId() { return id; }
    public List<String> getDependencies() { return dependencies; }
    public TaskState getState() { return state; }
    public void setState(TaskState state) { this.state = state; }
}
