package org.example.model;

import java.util.List;

public class TaskNode {
    private String id;
    private List<String> dependencies; // List of IDs of tasks this depends on
    private TaskState state;
    private int retryCount = 0;
    private int maxRetries = 2; // You can change this if you want more retries

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

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int count) { this.retryCount = count; }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
}
