package com.colibrisoft.esteitworker;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
//import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

import android.graphics.Color;
import android.widget.Chronometer;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.content.SharedPreferences.Editor;

public class MainActivity extends Activity {
	//основной TextView в котором будем отображать статус
	private TextView mText;
	
	//настройки
	SharedPreferences sPref;
	
	//в настройках EditView в котором записываем username
	EditText etText;
	
	//переменную которую будем хранить в настройках
	final String USERNAME = "username";
	
	//хронометр с отсчетом времени
	private Chronometer mChronometer;

	//вход в программу
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mText = (TextView)findViewById(R.id.statusText);
		etText = (EditText)findViewById(R.id.username);
		mChronometer = (Chronometer)findViewById(R.id.chronometer1);
		
		TabHost tabs=(TabHost)findViewById(R.id.tabhost);       
        tabs.setup();
    
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.current1);
        spec.setIndicator("Статус");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.journal2);
        spec.setIndicator("Журнал");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.settings3);
        spec.setIndicator("Настройки");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        
        final Button refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(button_click);
        
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(save_button);
        
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(USERNAME, "");
        etText.setText(savedText);
        
	}

	public android.view.View.OnClickListener button_click = new View.OnClickListener() {
        public void onClick(View v) {
        	
        	mChronometer.start(); 
        	
        	DownloadPage task = new DownloadPage();
            task.execute(new String[] { "http://portal.esteit.com:8047/location?user=chubatron@gmail.com" });
        }
    };
    
    public android.view.View.OnClickListener save_button = new View.OnClickListener() {
        public void onClick(View v) {
        	
        	sPref = getPreferences(MODE_PRIVATE);
            Editor ed = sPref.edit();
            ed.putString(USERNAME, etText.getText().toString());
            ed.commit();
            Toast.makeText(getApplicationContext(), "Настройки сохранены", Toast.LENGTH_LONG).show();
        }
    };
    
    protected class DownloadPage extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

	        String responseStr = null;
	
	        try {
	            for (String url : urls) {   
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpGet get = new HttpGet(url);
	            HttpResponse httpResponse = httpClient.execute(get);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            responseStr = EntityUtils.toString(httpEntity);
	            } 
	        } catch (UnsupportedEncodingException e) {
	
	        } catch (ClientProtocolException e) {
	
	        } catch (IOException e) {
	
	        }
	        return responseStr;
	    }
	
	    protected void onPostExecute(String result) {
	    	if(result.equals("out")) {
	    		mText.setTextColor(Color.RED);
	    		mText.setText("Не в офисе");
	    	}
	    	if(result.equals("in")) {
	    		mText.setTextColor(Color.GREEN);
	    		mText.setText("В офисе");
	    	}
	    }
    }

}