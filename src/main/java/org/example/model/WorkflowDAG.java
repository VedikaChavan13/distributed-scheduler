package org.example.model;

import java.util.Map;

public class WorkflowDAG {
    private String workflowId;
    private Map<String, TaskNode> tasks; // id to TaskNode mapping

    public WorkflowDAG(String workflowId, Map<String, TaskNode> tasks) {
        this.workflowId = workflowId;
        this.tasks = tasks;
    }

    // Getters and setters...
    public String getWorkflowId() { return workflowId; }
    public Map<String, TaskNode> getTasks() { return tasks; }
}
