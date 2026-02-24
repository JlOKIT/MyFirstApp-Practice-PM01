package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dbHelper = new DatabaseHelper(this);
        int taskId = getIntent().getIntExtra("TASK_ID", -1);

        // ★★★ ИСПРАВЛЕННЫЙ блок загрузки ★★★
        try {
            currentTask = findTaskById(taskId);
            if (currentTask != null) {
                // Приведение View → TextView
                TextView tvTaskTitle = findViewById(R.id.tvTaskTitle);
                TextView tvTaskDesc = findViewById(R.id.tvTaskDesc);
                TextView tvTaskId = findViewById(R.id.tvTaskId);

                tvTaskTitle.setText(currentTask.getTitle());
                tvTaskDesc.setText(currentTask.getDescription());
                tvTaskId.setText("ID: #" + currentTask.getId());
            } else {
                Toast.makeText(this, "❌ Задача не найдена!", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "💥 Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }

        // Кнопка Удалить
        Button btnDeleteTask = findViewById(R.id.btnDeleteTask);
        btnDeleteTask.setOnClickListener(v -> {
            try {
                if (dbHelper.deleteTask(currentTask.getId())) {
                    Toast.makeText(this, "🗑️ Задача удалена!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "❌ Ошибка удаления!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "💥 Ошибка БД: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка Редактировать (заглушка)
        Button btnEditTask = findViewById(R.id.btnEditTask);
        btnEditTask.setOnClickListener(v -> {
            Toast.makeText(this, "✏️ Редактирование - самостоятельное задание!", Toast.LENGTH_SHORT).show();
        });
    }

    private Task findTaskById(int id) {
        List<Task> allTasks = dbHelper.getAllTasks();
        for (Task task : allTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }
}