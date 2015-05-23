package com.conatic.myfirstandroidapp;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inqui on 5/21/15.
 */
public class APIManager {

    private static final String url = "https://utt-demo-api.herokuapp.com/";

    private static APIManager mInstance = null;

    private String mString;

    private APIManager(){
    }

    public static APIManager getInstance(){
        if(mInstance == null)
        {
            mInstance = new APIManager();
        }
        return mInstance;
    }

    public String enviarMensaje(Mensaje msj){
        JSONObject mensajeJSON = new JSONObject();
        try {
            mensajeJSON.accumulate("usuario", msj.getUsuario());
            mensajeJSON.accumulate("mensaje", msj.getMensaje());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postJSON(url, mensajeJSON);
    }

    public List<Mensaje> mensajes(){
        String readJSON = getJSON(url);
        List<Mensaje> mensajeList = new ArrayList<Mensaje>();
        try{
            JSONArray mensajesJSON = new JSONArray(readJSON);
            for (int i = 0 ; i < mensajesJSON.length() ; i ++){
                JSONObject mensajeJSON = mensajesJSON.getJSONObject(i);
                Mensaje mensaje = new Mensaje(
                    mensajeJSON.getString("usuario"),
                    mensajeJSON.getString("mensaje")
                );
                mensajeList.add(mensaje);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return mensajeList;
    }

    public String postJSON(String address, JSONObject object){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(address);
        try{
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(object.toString());
            httpPost.setEntity(se);
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failedet JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return "Ok";
    }

    public String getJSON(String address){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e(MainActivity.class.toString(), "Failedet JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }

}
