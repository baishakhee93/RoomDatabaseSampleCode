package com.taskdisplay.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taskdisplay.R;
import com.taskdisplay.data.Task;
import com.taskdisplay.interfaces.AdapterClick;
import com.taskdisplay.viewmodel.TaskViewModel;
import com.taskdisplay.views.adapters.TaskAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterClick {

    private RecyclerView rvTasks;
    private Button btnAdd;
    private TaskAdapter adapter;
    private AdapterClick callBack;
    private TextView tv_no_tasks;
    private List<Task> task_list;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvTasks = findViewById(R.id.rv_tasks);
        btnAdd = findViewById(R.id.btn_add);
        tv_no_tasks = findViewById(R.id.tv_no_tasks);
        callBack = this;
        task_list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new TaskAdapter((Context) MainActivity.this, callBack, task_list);
        rvTasks.setLayoutManager(linearLayoutManager);
        rvTasks.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            this.task_list = tasks;
            if (task_list != null && task_list.size() == 0) {
                rvTasks.setVisibility(View.GONE);
                tv_no_tasks.setVisibility(View.VISIBLE);
            } else {
                rvTasks.setVisibility(View.VISIBLE);
                adapter.setData(task_list);
                tv_no_tasks.setVisibility(View.GONE);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_add_task);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER | Gravity.CENTER);

                EditText edtTask = dialog.findViewById(R.id.ettask);
                EditText edtTaskDescription = dialog.findViewById(R.id.ettaskDescription);
                EditText edtTaskDuration = dialog.findViewById(R.id.ettaskduration);
                EditText edtTaskAssignTo = dialog.findViewById(R.id.ettaskassinTo);
                EditText edtaskassigndate = dialog.findViewById(R.id.ettaskdate);
                Button btnSubmit = dialog.findViewById(R.id.btn_add);
                edtaskassigndate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();

                        // on below line we are getting
                        // our day, month and year.
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        // on below line we are creating a variable for date picker dialog.
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                // on below line we are passing context.
                                MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // on below line we are setting date to our text view.
                                        edtaskassigndate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                },
                                // on below line we are passing year,
                                // month and day for selected date in our date picker.
                                year, month, day);
                        // at last we are calling show to
                        // display our date picker dialog.
                        datePickerDialog.show();
                    }


                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtTask.getText().toString().trim().isEmpty()) {
                            edtTask.setError("Please enter task");
                            edtTask.requestFocus();
                        } else if (edtTaskDescription.getText().toString().trim().isEmpty()) {
                            edtTaskDescription.setError("Please enter Description");
                            edtTaskDescription.requestFocus();
                        } else if (edtTaskDuration.getText().toString().trim().isEmpty()) {
                            edtTaskDuration.setError("Please enter Duration");
                            edtTaskDuration.requestFocus();
                        }  else if (edtTaskAssignTo.getText().toString().trim().isEmpty()) {
                            edtTaskAssignTo.setError("Please enter AssignTo");
                            edtTaskAssignTo.requestFocus();
                        }  else if (edtaskassigndate.getText().toString().trim().isEmpty()) {
                            edtaskassigndate.setError("Please enter Task Assign Date");
                            edtaskassigndate.requestFocus();
                        } else {
                            dialog.dismiss();
                            tv_no_tasks.setVisibility(View.GONE);
                            rvTasks.setVisibility(View.VISIBLE);
                            insertData(edtTask.getText().toString(),edtTaskDescription.getText().toString(),edtTaskDuration.getText().toString(),edtTaskAssignTo.getText().toString(),edtaskassigndate.getText().toString());
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void insertData(String task, String description, String duration, String assignTo, String taskassigndate) {
        Task newTask = new Task();
        newTask.setTitle(task);
        newTask.setDescription(description);
        newTask.setDuration(duration);
        newTask.setAssignTo(assignTo);
        newTask.setTaskassigndate(taskassigndate);
        newTask.setStatus("in progress");
        if (taskViewModel != null) {
            taskViewModel.insert(newTask);
        }
        adapter.addData(newTask);
        adapter.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateData(Task updatedTask, String task, String description, String duration, String assignTo, String taskassigndate) {
       // Task updatedTask = new Task();
        updatedTask.setTitle(task);
        updatedTask.setDescription(description);
        updatedTask.setDuration(duration);
        updatedTask.setAssignTo(assignTo);
        updatedTask.setTaskassigndate(taskassigndate);
        updatedTask.setStatus("in progress");

        if (taskViewModel != null) {
            try {
                taskViewModel.update(updatedTask);
                // Log a message to check if the update operation is called
                Log.d("UpdateData", "Task updated successfully");
            } catch (Exception e) {
                e.printStackTrace();
                // Log an error message if an exception occurs during the update
                Log.e("UpdateData", "Error updating task: " + e.getMessage());
            }
        }
        // Find the index of the updated task in the task_list
        int indexOfUpdatedTask = task_list.indexOf(updatedTask);
        if (indexOfUpdatedTask != -1) {
            // Update the task in the task_list
            task_list.set(indexOfUpdatedTask, updatedTask);
            // Notify the adapter that the item at this index has changed
            adapter.notifyItemChanged(indexOfUpdatedTask);
        }
       // adapter.notifyDataSetChanged();
    }



    @Override
    public void onDeleteTask(Task task) {
        if (taskViewModel != null) {
            taskViewModel.delete(task);
        }
    }

    @Override
    public void onEditTask(Task task) {
        taskDialogBox("edit",task);
    }

    private void taskDialogBox(String status, Task task) {
        if (status.equalsIgnoreCase("edit")){
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_add_task);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER | Gravity.CENTER);

            EditText edtTask = dialog.findViewById(R.id.ettask);
            EditText edtTaskDescription = dialog.findViewById(R.id.ettaskDescription);
            EditText edtTaskDuration = dialog.findViewById(R.id.ettaskduration);
            EditText edtTaskAssignTo = dialog.findViewById(R.id.ettaskassinTo);
            EditText edtaskassigndate = dialog.findViewById(R.id.ettaskdate);
            Button btnSubmit = dialog.findViewById(R.id.btn_add);

            edtTask.setText(task.getTitle());
            edtTaskDescription.setText(task.getDescription());
            edtTaskDuration.setText(task.getDuration());
            edtTaskAssignTo.setText(task.getAssignTo());
            edtaskassigndate.setText(task.getTaskassigndate());



            edtaskassigndate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            final Calendar c = Calendar.getInstance();

                            // on below line we are getting
                            // our day, month and year.
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH);
                            int day = c.get(Calendar.DAY_OF_MONTH);

                            // on below line we are creating a variable for date picker dialog.
                            DatePickerDialog datePickerDialog = new DatePickerDialog(
                                    // on below line we are passing context.
                                    MainActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            // on below line we are setting date to our text view.
                                            edtaskassigndate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    },
                                    // on below line we are passing year,
                                    // month and day for selected date in our date picker.
                                    year, month, day);
                            // at last we are calling show to
                            // display our date picker dialog.
                            datePickerDialog.show();
                        }


            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtTask.getText().toString().trim().isEmpty()) {
                        edtTask.setError("Please enter task");
                        edtTask.requestFocus();
                    } else if (edtTaskDescription.getText().toString().trim().isEmpty()) {
                        edtTaskDescription.setError("Please enter Description");
                        edtTaskDescription.requestFocus();
                    } else if (edtTaskDuration.getText().toString().trim().isEmpty()) {
                        edtTaskDuration.setError("Please enter Duration");
                        edtTaskDuration.requestFocus();
                    } else if (edtTaskAssignTo.getText().toString().trim().isEmpty()) {
                        edtTaskAssignTo.setError("Please enter AssignTo");
                        edtTaskAssignTo.requestFocus();
                    }else if (edtaskassigndate.getText().toString().trim().isEmpty()) {
                        edtaskassigndate.setError("Please enter Task Assign Date");
                        edtaskassigndate.requestFocus();
                    } else {
                        dialog.dismiss();
                        tv_no_tasks.setVisibility(View.GONE);
                        rvTasks.setVisibility(View.VISIBLE);

                         updateData(task ,edtTask.getText().toString(), edtTaskDescription.getText().toString(), edtTaskDuration.getText().toString(), edtTaskAssignTo.getText().toString(), edtaskassigndate.getText().toString());
                    }
                }
            });
            dialog.show();
        }

    }

    @Override
    public void onCompletedTask(Task task, String status) {
        if (taskViewModel != null) {
            taskViewModel.updateTaskStatus(task, status);
        }
    }





}