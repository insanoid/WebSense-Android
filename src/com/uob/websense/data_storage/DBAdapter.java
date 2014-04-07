package com.uob.websense.data_storage;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uob.websense.support.Constants;

public class DBAdapter {

	private final Context mContext;
	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;

	public DBAdapter(Context context,String dbName) 
	{
		this.mContext = context;
		mDbHelper = new DatabaseHelper(mContext,dbName);
	}

	public DBAdapter createDatabase() throws SQLException 
	{
		try 
		{
			mDbHelper.createDataBase();
		} 
		catch (IOException mIOException) 
		{
			Log.e(Constants.LOG_TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public DBAdapter open() throws SQLException 
	{
		try 
		{
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getWritableDatabase();
		} 
		catch (SQLException mSQLException) 
		{
			Log.e(Constants.LOG_TAG, "open >>"+ mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() 
	{
		mDbHelper.close();
	}

	public void insert(ContentValues content,String tableName) {
		try{
		mDb.insert(tableName, null, content);
		}catch(Exception e){
			
		}
	}
	
	public void update(ContentValues content) {}
	public void delete(ContentValues content) {}
	public Cursor fetch(String query,String [] arguments)  {
		try{
			Cursor c = mDb.rawQuery(query, null);
			return c;
		}catch(Exception e){
			Log.d("Error",e.getMessage()+"0");
		}
		return null;
		
	}
	
}