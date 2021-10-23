package com.hfad.lab4;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String SONG_EQ = "";
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    SQLiteDatabase db;
    ContentValues values;
    String timeText;
    Integer iterator = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = getBaseContext().openOrCreateDatabase("Megabait.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS songs (id INTEGER, SINGER TEXT, SONG TEXT, DATA TEXT)");
        // db.execSQL("DELETE FROM songs");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.ConnectingToInternet();
        //Проверяем Интернет статус:
        if (isInternetPresent) {
            //Интернет соединение есть
            //делаем HTTP запросы:
            final Toast toast = Toast.makeText(getApplicationContext(),
                    "У вас есть Интернет", Toast.LENGTH_SHORT);
            toast.show();
            new CallAPI().execute();
            refresh(true);
        } else {
            //Интернет соединения нет
            //просим пользователя подключить Интернет:
            final Toast toast = Toast.makeText(getApplicationContext(),
                    "У вас нет Интернета", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void refresh(boolean b) {
        if (b) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new CallAPI().execute();
                    final Toast toast = Toast.makeText(getApplicationContext(),
                            "ПОЛУЧЕНО", Toast.LENGTH_SHORT);
                    toast.show();
                    refresh(true);
                }
            }, 10 * 1000);
        }
    }

    public void Show(View v) {
        Intent intent = new Intent(this, ShowActivity.class);
        startActivity(intent);
    }


    class CallAPI extends AsyncTask<String, String, String> {
        String resultString = null;
        byte[] data = null;
        InputStream is = null;

        public CallAPI() {
            //set context variables if required
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = "https://media.itmo.ru/api_get_current_song.php"; // URL to call
            String login = "4707login";
            String password = "4707pass";

            String parammetrs = "login=" + login + "&password=" + password;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //Сохранение соединения
                conn.setRequestProperty("Connection", "Keep-Alive");
                //Стандартное кодирование URL(Способ кодировки запроса)
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //Длина парамтера отправляемого на сервер
                conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // конвертируем передаваемую строку в UTF-8
                data = parammetrs.getBytes("UTF-8");

                OutputStream os = conn.getOutputStream();

                // передаем данные на сервер
                os.write(data);
                // Записываем все данные из буфера в поток
                os.flush();
                os.close();
                data = null;
                conn.connect();
                //Получаем код ответа от сервера
                int responseCode = conn.getResponseCode();

                // передаем ответ сервер
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                if (responseCode == 200) {    // Если все ОК (ответ 200)
                    is = conn.getInputStream();

                    byte[] buffer = new byte[8192]; // размер буфера

                    // Далее так читаем ответ
                    int bytesRead;

                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }


                    data = baos.toByteArray();

                    // сохраняем в переменную ответ сервера
                    resultString = new String(data, "UTF-8");
                    JSONObject responseJSON = new JSONObject(resultString);
                    String result = responseJSON.getString("result");
                    String info = responseJSON.getString("info");
                    String singer = info.split("-")[0];
                    String song = info.split("-")[1];
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(getApplicationContext(),
                                    song, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    String selectQuery = "SELECT  * FROM " + "songs";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToLast();
                        iterator = cursor.getInt(0);
                        SONG_EQ = cursor.getString(2);
                        iterator+=1;
                    }
                    if(!SONG_EQ.equals(song)) {
                        // Текущее время
                        Date currentDate = new Date();
                        // Форматирование времени как "часы:минуты:секунды"
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        timeText = timeFormat.format(currentDate);
                        values = new ContentValues();
                        values.clear();
                        values.put("ID", iterator);
                        values.put("SINGER", singer);
                        values.put("SONG", song);
                        values.put("DATA", timeText);
                        db.insert("songs", null, values);
                    }
                } else {
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultString;
        }
    }

    class ConnectionDetector {

        private Context _context;

        public ConnectionDetector(Context context) {
            this._context = context;
        }

        public boolean ConnectingToInternet() {
            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
            return false;
        }
    }
}
