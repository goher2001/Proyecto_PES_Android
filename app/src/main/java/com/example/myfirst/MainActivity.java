package com.example.myfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirst.MESSAGE";
    Context context;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("userlogged", context.MODE_PRIVATE);
    }
    EditText user;
    EditText pwd;
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.EditResult);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void Login(View view) {


        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {

                    user =findViewById(R.id.etUsername);
                    pwd = findViewById(R.id.etPassword);
                    String query = String.format("http://10.0.2.2:9000/Application/Login?fullname="+user.getText().toString()+"&password="+pwd.getText().toString());
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                   // conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                   /* String params = "fullname="+ R.id.etUsername +"&Password=" + R.id.etPassword;
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params.toString());

                    writer.flush();
                    writer.close();
                    os.close();
                      */
//holoholohola
                    stream = conn.getInputStream();

                    BufferedReader reader = null;

                    StringBuilder sb = new StringBuilder();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    // Mostrar resultat en el quadre de text.
                    // Codi incorrecte
                    // EditText n = (EditText) findViewById (R.id.edit_message);
                    //n.setText(result);

                    //Codi correcte
                    Log.i("MainActivity", result);
                    handler.post(new Runnable() {
                        public void run() {
                            if(result.length() >= 50)
                            {
                                Context context = getApplicationContext();
                                Intent intent = new Intent(context, Logedin.class);
                                String message = "hola";
                                intent.putExtra(EXTRA_MESSAGE, message);
                                startActivity(intent);
                            }
                            else {
                                EditText n = (EditText) findViewById(R.id.EditResult);
                                n.setText(result);
                            }

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void sayBorrarUsuari (View view) {
        new HelloMessage(this).execute("http://192.168.1.111:9000/Application/DonarDeBaixaUsuari?fullname=Usuari1" );

    }
    public void sayRegister (View view) {
        user =findViewById(R.id.etUsername);
        pwd = findViewById(R.id.etPassword);
        new HelloMessage(this).execute("http://10.0.2.2:9000/Application/Register?fullname="+user.getText().toString()+"&password="+pwd.getText().toString());

    }


private class HelloMessage extends AsyncTask<String, Void, String> {
    Context context;
    InputStream stream = null;
    String str = "";
    String result = null;

    private HelloMessage(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... urls) {

        try {
            String query = String.format(urls[0]);
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            stream = conn.getInputStream();

            BufferedReader reader = null;

            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();


            Log.i("lolaforms1", result);


            return result;

        } catch(IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {

        EditText n = (EditText) findViewById (R.id.EditResult);
        n.setText(result);

    }
}

}


