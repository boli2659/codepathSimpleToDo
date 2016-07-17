package com.codepath.simpletodo;

/**
 * Created by zachboline on 7/13/16.
 */
public class Task {
    public int taskid;
    public String name;
    public long dueDate;
    public float priority;

    public Task (String name) {
        this.name = name;
        taskid = -1;
        dueDate = -1;
        priority = -1;
    }
    public Task (String name, Long dueDate, float priority) {
        this.name = name;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}
