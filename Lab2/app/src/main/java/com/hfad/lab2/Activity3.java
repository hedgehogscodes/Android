package com.hfad.lab2;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
public class Activity3 extends AppCompatActivity {
    int f = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);

        Button btn = (Button) findViewById(R.id.new_button);


//смена цвета при каждом нажатии на кнопку
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {

                    if (f==1)
                    {
                        btn.setForeground(getDrawable(R.drawable.background_button));
                        f = 0;
                    }
                    else
                    {
                        btn.setForeground(getDrawable(R.drawable.rounded_button));
                        f = 1;
                    }
                }
                catch (Exception e) {}
            }
        });
    }
    @Override
    public void onBackPressed(){
        try {

            Intent intent = new Intent(Activity3.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e) {}
    }

}




