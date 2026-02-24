package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
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

        // НОВЫЙ КОД LISTVIEW
        ListView lvScreens = findViewById(R.id.lvScreens);

        // Массив пунктов меню (экраны/действия)
        String[] screens = {
                "Открыть профиль",
                "Открыть экран с расчётом",
                "Открыть экран настроек"
        };

        // Адаптер для отображения строк в ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                screens
        );

        // Привязываем адаптер к ListView
        lvScreens.setAdapter(adapter);

        // Обработчик клика по элементу списка
        lvScreens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position — индекс элемента (0, 1, 2, ...)
                if (position == 0) {
                    // Открыть уже существующую ProfileActivity
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    // Открыть новую Activity для расчёта (создадим позже)
                    Intent intent = new Intent(MainActivity.this, CalcActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    // Открыть новую Activity настроек (создадим позже)
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}