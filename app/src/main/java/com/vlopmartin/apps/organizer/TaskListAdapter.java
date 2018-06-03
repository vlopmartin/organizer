package com.vlopmartin.apps.organizer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Task> taskList;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TaskViewHolder(View itemView) {
            super(itemView);
        }

        public void setTaskName(String taskName) {
            TextView textView = this.itemView.findViewById(R.id.task_name);
            textView.setText(taskName);
        }

        public void setTaskDescription(String taskDescription) {
            TextView textView = this.itemView.findViewById(R.id.task_description);
            textView.setText(taskDescription);
        }

    }

    public TaskListAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        TaskViewHolder holder = new TaskViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskViewHolder taskViewHolder = (TaskViewHolder)holder;
        Task task = taskList.get(position);
        taskViewHolder.setTaskName(task.getName());
        taskViewHolder.setTaskDescription(task.getDescription());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
