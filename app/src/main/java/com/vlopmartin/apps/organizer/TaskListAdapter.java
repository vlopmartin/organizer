package com.vlopmartin.apps.organizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vlopmartin.apps.organizer.activities.MainActivity;
import com.vlopmartin.apps.organizer.activities.TaskDetailsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Task> taskList;
    private Resources resources;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private static final DateFormat dateFormat = new SimpleDateFormat("dd MMM");

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

        public void setTaskDueDate(Date taskDueDate, String futureColor) {
            TextView textView = this.itemView.findViewById(R.id.task_due_date);
            if (taskDueDate != null) {
                textView.setText(dateFormat.format(taskDueDate));

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(taskDueDate);
                Calendar now = Calendar.getInstance();
                Calendar tomorrow = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                if (calendar.compareTo(tomorrow) > 0) {
                    itemView.setBackgroundColor(Color.parseColor(futureColor));
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
        taskViewHolder.setTaskDueDate(task.getDueDate(), resources.getString(R.string.colorFutureTaskBackground));
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
