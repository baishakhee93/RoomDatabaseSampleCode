package com.taskdisplay.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taskdisplay.R;
import com.taskdisplay.data.Task;
import com.taskdisplay.interfaces.AdapterClick;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<Task> itemsList;
    private AdapterClick callBack;

    public void setData(List<Task> task_list) {
        if (itemsList != null && itemsList.size() > 0) {
            itemsList.clear();
        }
        this.itemsList = task_list;
        notifyDataSetChanged();
    }


    public TaskAdapter(Context context, AdapterClick callBack, List<Task> task_list) {
        this.context = context;
        this.itemsList = task_list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Task task = itemsList.get(position);
        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            holder.tvTitle.setText(task.getTitle());
        }
        if (task.getAssignTo() != null && !task.getAssignTo().isEmpty()) {
            holder.tvAssignTo.setText(task.getAssignTo());
        }
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            holder.tvDescription.setText(task.getDescription());
        }
        if (task.getDuration() != null && !task.getDuration().isEmpty()) {
            holder.tvDuration.setText(task.getDuration() + " hours");
        }
        if (task.getTaskassigndate() != null && !task.getTaskassigndate().isEmpty()) {
            holder.tvTaskAssignDate.setText(task.getTaskassigndate());
        }

        holder.check.setImageResource(task.isCompleted() ? R.drawable.ic_checkbox_select : R.drawable.ic_checkbox_unselect);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemsList != null && itemsList.size() > 0) {
                    if (callBack != null) {
                        callBack.onDeleteTask(itemsList.get(position));
                    }
                }
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemsList != null && itemsList.size() > 0) {
                    if (callBack != null) {
                        callBack.onEditTask(itemsList.get(position));
                    }
                }
            }
        });


        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setCompleted(!task.isCompleted());
                boolean b = task.isCompleted();
                if (callBack != null) {
                    String status;
                    if (b) {
                        status = "completed";
                    } else {
                        status = "in progress";
                    }
                    callBack.onCompletedTask(task, status);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void addData(Task response) {
        this.itemsList.add(response);
        notifyItemInserted(this.itemsList.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDuration, tvDescription, tvAssignTo, tvTaskAssignDate;
        ImageButton delete, edit;
        ImageView check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvDescription = itemView.findViewById(R.id.tv_discription);
            tvAssignTo = itemView.findViewById(R.id.tv_assign);
            tvTaskAssignDate = itemView.findViewById(R.id.tv_assign_date);
            delete = itemView.findViewById(R.id.btn_delete);
            edit = itemView.findViewById(R.id.btn_edit);
            check = itemView.findViewById(R.id.check);
        }
    }
}

