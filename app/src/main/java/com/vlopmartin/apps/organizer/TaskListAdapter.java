package com.vlopmartin.apps.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vlopmartin.apps.organizer.activities.MainActivity;
import com.vlopmartin.apps.organizer.activities.TaskDetailsActivity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Task> taskList;
    private Resources resources;

    private DateTimeFormatter dateFormat;

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

        public void setTaskDueDate(LocalDate taskDueDate, DateTimeFormatter dateFormat, int defaultColor, int futureColor) {
            TextView textView = this.itemView.findViewById(R.id.task_due_date);
            itemView.setBackgroundColor(defaultColor);
            textView.setTypeface(null, Typeface.NORMAL);
            if (taskDueDate != null) {
                textView.setText(taskDueDate.format(dateFormat));
                if (taskDueDate.compareTo(LocalDate.now()) > 0) {
                    itemView.setBackgroundColor(futureColor);
                } else if (taskDueDate.compareTo(LocalDate.now()) < 0) {
                    textView.setTypeface(null, Typeface.BOLD);
                }
            } else {
                textView.setText("");
            }
        }

        public void setTaskPriority(long priority, int[] priorityValues, String[] priorityStrings, String[] priorityColors) {
            TextView textView = this.itemView.findViewById(R.id.task_priority);
            if (priority != 0) {
                int priorityIndex = 0;
                for (int i = 0; i < priorityValues.length; i++) {
                    if (priorityValues[i] == priority) {
                        priorityIndex = i;
                    }
                }
                textView.setText(priorityStrings[priorityIndex]);
                textView.setTextColor(Color.parseColor(priorityColors[priorityIndex]));
            } else {
                textView.setText("");
            }
        }
    }

    public TaskListAdapter(List<Task> taskList, Resources resources) {
        this.taskList = taskList;
        this.resources = resources;
        this.dateFormat = DateTimeFormatter.ofPattern(resources.getString(R.string.list_date_format));
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
        taskViewHolder.setTaskDueDate(task.getDueDate(), dateFormat,
                resources.getColor(R.color.colorDefaultTaskBackground),
                resources.getColor(R.color.colorFutureTaskBackground));
        taskViewHolder.setTaskPriority(task.getPriority(),
                resources.getIntArray(R.array.priority_values),
                resources.getStringArray(R.array.priorities),
                resources.getStringArray(R.array.priority_colors));

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
                // Complete the task
                final int index = taskList.indexOf(task);
                taskList.remove(index);
                final Task newTask = task.complete(v.getContext());
                TaskListAdapter.this.notifyItemRemoved(index);
                // If the task got repeated, insert the new task
                final int newIndex = taskList.size();
                if (newTask != null) {
                    taskList.add(newIndex, newTask);
                    TaskListAdapter.this.notifyItemInserted(newIndex);
                }
                Snackbar.make(v, R.string.task_completed, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Remove the new task, if it was created
                        if (newTask != null) {
                            newTask.delete(v.getContext());
                            taskList.remove(newIndex);
                            TaskListAdapter.this.notifyItemRemoved(newIndex);
                        }
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
