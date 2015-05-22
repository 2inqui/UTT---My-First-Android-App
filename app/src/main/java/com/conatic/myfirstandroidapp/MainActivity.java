package com.conatic.myfirstandroidapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    EditText editTextUsuario;
    EditText editTextMensaje;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUsuario = (EditText) findViewById(R.id.editTextUsuario);
        editTextMensaje = (EditText) findViewById(R.id.editTextMensaje);

        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            MensajesTask performBackgroundTask = new MensajesTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
    }

    public void enviarMensaje(View view){
        Mensaje mensaje = new Mensaje(
                editTextUsuario.getText().toString(),
                editTextMensaje.getText().toString()
        );
        new EnviarMensajesTask().execute(mensaje);
    }

    private class EnviarMensajesTask extends AsyncTask<Mensaje,Void,Void>{

        @Override
        protected Void doInBackground(Mensaje... params) {
            if (params != null && params.length > 0)
                APIManager.getInstance().enviarMensaje(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            editTextMensaje.setText("");
        }
    }

    private class MensajesTask extends AsyncTask<Void,Void,List<Mensaje>>{

        @Override
        protected List<Mensaje> doInBackground(Void... params) {
            return APIManager.getInstance().mensajes();
        }

        @Override
        protected void onPostExecute(List<Mensaje> mensajes) {
            super.onPostExecute(mensajes);
            if (mensajes != null && mensajes.size() > 0) {
                ArrayAdapter adapter = new ArrayAdapter<Mensaje>(
                        MainActivity.this, android.R.layout.simple_list_item_1, mensajes);
                listView.setAdapter(adapter);
                listView.setSelection(adapter.getCount() - 1);
            }
        }
    }
}
