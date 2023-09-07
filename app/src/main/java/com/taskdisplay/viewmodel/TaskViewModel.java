package com.taskdisplay.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.taskdisplay.data.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        taskRepository.insert(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void updateTaskStatus(Task task, String newStatus) {
        taskRepository.updateTaskStatus(task, newStatus);
    }
}
