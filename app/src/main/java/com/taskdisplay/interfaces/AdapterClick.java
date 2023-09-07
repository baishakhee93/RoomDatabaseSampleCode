package com.taskdisplay.interfaces;

import com.taskdisplay.data.Task;

public interface AdapterClick {
    void onDeleteTask(Task task);
    void onEditTask(Task task);

    void onCompletedTask(Task task, String status);
}
