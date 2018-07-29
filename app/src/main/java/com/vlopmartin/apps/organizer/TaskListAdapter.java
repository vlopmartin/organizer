package com.vlopmartin.apps.organizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vlopmartin.apps.organizer.activities.MainActivity;
import com.vlopmartin.apps.organizer.activities.TaskDetailsActivity;

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
        final Task task = taskList.get(position);
        taskViewHolder.setTaskName(task.getName());
        taskViewHolder.setTaskDescription(task.getDescription());

        // On clicking the list item, open an activity with the task details
        taskViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra(TaskDetailsActivity.TASK_ID, task.getId());
                context.startActivity(intent);
            }
        });

        // On clicking the check mark, delete the task
        taskViewHolder.itemView.findViewById(R.id.check_mark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = taskList.indexOf(task);
                taskList.remove(index);
                task.delete(v.getContext());
                TaskListAdapter.this.notifyItemRemoved(index);
                Snackbar.make(v, R.string.task_completed, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskList.add(index, task);
                        task.save(v.getContext());
                        TaskListAdapter.this.notifyItemInserted(index);
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
