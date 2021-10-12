package com.hfad.lab3_2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String timeText;
    Integer iterator=1;
    final String DB_NAME = "classmates"; // имя БД
    final int DB_VERSION = 1; // версия БД

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeText = timeFormat.format(currentDate);
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db;
        db = dbh.getWritableDatabase();
        db.execSQL("DELETE FROM classmates");
        if(DB_VERSION==1){
            insert_old(db, iterator, "Petrov",timeText);
            Show_old(db);
        }else{
            insert_new(db, iterator, "Ivanov","Kirill","Cepish",timeText);
            Show_new(db);
        }
    }

    public void Show_old(SQLiteDatabase db) {
        Cursor query = db.rawQuery("SELECT * FROM classmates;", null);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
        while(query.moveToNext()){
            int id = query.getInt(0);
            String FIO = query.getString(1);
            String DATA = query.getString(2);
            textView.append("ID: " + id + " FIO: " + FIO + " DATA: " + DATA + "\n");
        }
        query.close();
    }

    public void Show_new(SQLiteDatabase db) {
        Cursor query = db.rawQuery("SELECT * FROM classmates;", null);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
        while(query.moveToNext()){
            int id = query.getInt(0);
            String second = query.getString(1);
            String name = query.getString(2);
            String pat = query.getString(3);
            String DATA = query.getString(4);
            textView.append("ID: " + id + " second: " + second + " name: " + name + " pat: " + pat +  " DATA: " + DATA + "\n");
        }
        query.close();
    }

    public boolean insert_new(SQLiteDatabase db,Integer s1,String s2,String s3,String s4,String s5) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", s1);
        contentValues.put("second", s2);
        contentValues.put("name", s3);
        contentValues.put("pat", s4);
        contentValues.put("data", s5);
        db.insert(DB_NAME, null, contentValues);
        return true;
    }

    public boolean insert_old(SQLiteDatabase db,Integer s1,String s2,String s3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", s1);
        contentValues.put("FIO", s2);
        contentValues.put("DATA", s3);
        db.insert(DB_NAME, null, contentValues);
        return true;
    }


}

