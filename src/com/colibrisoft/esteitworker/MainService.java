package com.colibrisoft.esteitworker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;



public class MainService extends Service {
	
	DBHelper dbHelper;
	
	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
       
    }
}

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
      // конструктор суперкласса
      super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {      
      // создаем таблицу с полями
      db.execSQL("create table mytable ("
          + "id integer primary key autoincrement," 
          + "name text,"
          + "email text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
  }

