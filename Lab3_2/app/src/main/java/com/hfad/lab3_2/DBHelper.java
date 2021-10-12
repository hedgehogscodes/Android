package com.hfad.lab3_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
// класс для работы с БД

class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "classmates"; // имя БД
    private static final int DB_VERSION = 1; // версия БД

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS classmates (id INTEGER, FIO TEXT, DATA TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            //Toast.makeText(MainActivity.this, DB_VERSION, Toast.LENGTH_LONG).show();
            db.execSQL("DROP TABLE IF EXISTS "+DB_NAME);
            db.execSQL("CREATE TABLE IF NOT EXISTS classmates (id INTEGER, second TEXT, name TEXT, pat TEXT, DATA TEXT)");
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion < 2) {
            //Toast.makeText(MainActivity.this, DB_VERSION, Toast.LENGTH_LONG).show();
            db.execSQL("DROP TABLE IF EXISTS "+DB_NAME);
            db.execSQL("CREATE TABLE IF NOT EXISTS classmates (id INTEGER, FIO TEXT, DATA TEXT)");
        }
    }

}

