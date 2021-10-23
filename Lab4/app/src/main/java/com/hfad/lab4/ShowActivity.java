package com.hfad.lab4;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class ShowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("Megabait.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM songs;", null);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
        while(query.moveToNext()){
            int id = query.getInt(0);
            String SINGER = query.getString(1);
            String SONG = query.getString(2);
            String DATA = query.getString(3);
            textView.append("ID: " + id + " SINGER: " + SINGER + " SONG: " + SONG + " DATA: " + DATA + "\n");
        }
        query.close();
        db.close();
    }
}