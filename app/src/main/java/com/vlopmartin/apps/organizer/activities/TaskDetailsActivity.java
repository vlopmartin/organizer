package com.vlopmartin.apps.organizer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.vlopmartin.apps.organizer.R;
import com.vlopmartin.apps.organizer.Task;

public class TaskDetailsActivity extends NewTaskActivity {

    public static final String TASK_ID = "com.vlopmartin.apps.organizer.task_id";

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the task
        long taskId = getIntent().getLongExtra(TASK_ID, 0);
        task = Task.getById(this.getApplicationContext(), taskId);

        // Bind the task data
        taskNameView.setText(task.getName());
        taskDescriptionView.setText(task.getDescription());
        dueDate = task.getDueDate();
        if (dueDate != null) {
            dueDateView.setText(dateFormat.format(dueDate));
        }
        int[] priorityValues = getResources().getIntArray(R.array.priority_values);
        int priorityIndex = 0;
        long taskPriority = task.getPriority();
        for (int i=0; i<priorityValues.length; i++) {
            if (priorityValues[i] == taskPriority) {
                priorityIndex = i;
            }
        }
        priorityView.setSelection(priorityIndex);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setName(taskNameView.getText().toString());
                task.setDescription(taskDescriptionView.getText().toString());
                task.setDueDate(dueDate);
                int taskPriority = getResources().getIntArray(R.array.priority_values)[priorityView.getSelectedItemPosition()];
                task.setPriority(taskPriority);
                task.save(TaskDetailsActivity.this.getApplicationContext());

                TaskDetailsActivity.this.setResult(Activity.RESULT_OK);
                TaskDetailsActivity.this.finish();
            }
        });
    }
}
