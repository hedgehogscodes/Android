package com.hfad.lab3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    ContentValues values;
    String timeText;
    Integer iterator=1;
    final int DB_VERSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeText = timeFormat.format(currentDate);
        iterator = 1;

        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS classmates (id INTEGER, FIO TEXT, DATA TEXT)");
        db.execSQL("DELETE FROM classmates");
        String[] people_name = { "Tom", "John", "Max", "Mary", "Kirill"};
        values = new ContentValues();
        for (int i = 0; i < people_name.length; i++) {
            values.clear();
            values.put("ID", iterator);
            values.put("FIO", people_name[i]);
            values.put("DATA", timeText);
            db.insert("classmates", null, values);
            iterator+=1;
        }
    }
    public void Show(View v) {
        Intent intent = new Intent(this, ShowActivity.class);
        startActivity(intent);
    }

    public void Add(View v) {
        values.put("ID", iterator);
        values.put("FIO", "Anna");
        values.put("DATA", timeText);
        db.insert("classmates", null, values);
        iterator+=1;
    }

    public void Alter(View v) {
        values.put("ID", iterator-1);
        values.put("FIO", "Ivanov Ivan");
        values.put("DATA", timeText);
        db.update("classmates", values, "ID = ?", new String[]{String.valueOf(iterator-1)});
    }

}