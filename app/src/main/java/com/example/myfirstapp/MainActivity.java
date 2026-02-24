package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Для навигации
    ListView lvScreens;

    // Для SQLite
    DatabaseHelper dbHelper;
    ListView listViewTasks;
    ArrayAdapter<String> adapter;
    List<Task> tasks = new ArrayList<>();
    int selectedTaskId = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ============== КОД ДЛЯ НАВИГАЦИИ (ListView lvScreens) ==============
        lvScreens = findViewById(R.id.lvScreens);

        String[] screens = {
                "Открыть профиль",
                "Открыть экран с расчётом",
                "Открыть экран настроек"
        };

        ArrayAdapter<String> screensAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                screens
        );

        lvScreens.setAdapter(screensAdapter);

        lvScreens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, CalcActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // ============== КОД ДЛЯ SQLite CRUD (Управление задачами) ==============
        dbHelper = new DatabaseHelper(this);
        listViewTasks = findViewById(R.id.listViewTasks);

        // Кнопка Добавить
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            EditText etTitle = findViewById(R.id.etTitle);
            EditText etDesc = findViewById(R.id.etDesc);
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addTask(title, desc)) {
                Toast.makeText(this, "Задача добавлена!", Toast.LENGTH_SHORT).show();
                refreshList();
                etTitle.setText("");
                etDesc.setText("");
            } else {
                Toast.makeText(this, "Ошибка при добавлении", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка Обновить список
        findViewById(R.id.btnRefresh).setOnClickListener(v -> refreshList());

        // Клик по элементу списка задач
        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            selectedTaskId = tasks.get(position).getId();
            Toast.makeText(this, "Выбрана задача: " + tasks.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Кнопка Удалить выбранную задачу
        findViewById(R.id.btnDeleteSelected).setOnClickListener(v -> {
            if (selectedTaskId == -1) {
                Toast.makeText(this, "Сначала выберите задачу", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.deleteTask(selectedTaskId)) {
                Toast.makeText(this, "Задача удалена!", Toast.LENGTH_SHORT).show();
                refreshList();
                selectedTaskId = -1;
            } else {
                Toast.makeText(this, "Ошибка при удалении", Toast.LENGTH_SHORT).show();
            }
        });

        // Загружаем список задач при старте
        refreshList();
    }

    // Метод для обновления списка задач
    private void refreshList() {
        tasks.clear();
        tasks.addAll(dbHelper.getAllTasks());
        List<String> taskTitles = new ArrayList<>();
        for (Task task : tasks) {
            taskTitles.add(task.getId() + ": " + task.getTitle() + " - " + task.getDescription());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskTitles);
        listViewTasks.setAdapter(adapter);
    }
}