/* **************************************************
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, kxu356@bham.ac.uk

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.uob.websense.data_storage;

import java.io.IOException;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.uob.websense.support.Util;

/**
 * Adapter to provide functions to access database more smoothly.
 * @author karthikeyaudupa
 *
 */
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
			Util.loge("Error Opening db: "+ mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() 
	{
		Util.logi("Database closed: ["+mDbHelper.getDbname()+"]");
		mDbHelper.close();
	}

	public void insert(ContentValues content,String tableName) {
		try{
			Util.loge("TRY Inserting db: "+ content);
			mDb.insert(tableName, null, content);
		}catch(Exception e){
			Util.loge("Error Inserting db: "+ e.toString());
		}
	}

	public Boolean updateRecords(String ids, String tableName){

		String query = "UPDATE "+tableName+" set synced = 1 WHERE record_id IN ("+ids+")";
		createDatabase();
		open();

		if(execute(query)==true){
			Util.logi("Update Records: ["+tableName+"]");
			return true;
		}else{
			Util.loge("Error Updating Records: ["+tableName+"]");
			return false;
		}

	}

	public JSONArray getUnSyncedAppRecords(int count, String tableName){

		createDatabase();
		open();
		String query = "SELECT * FROM "+ tableName +" WHERE synced=0 LIMIT "+ count;
		Cursor c = fetch(query, null);
		JSONArray jArray = new JSONArray();
		jArray = Util.cursorToJSONArray(c);
		return jArray;
	}

	public int getUnsyncedRecordCount(String tableName){

		createDatabase();
		open();
		int count = 0;
		Cursor c = fetch("SELECT count(*) as record_count FROM "+ tableName +" where synced = 0", null);


		if(c==null){
			return 0;
		}
		if (c.moveToFirst()){
			do{
				count = c.getInt(c.getColumnIndex("record_count"));

			}while(c.moveToPrevious());
		}
		c.close();
		return count;
	}

	public boolean execute(String sql) {
		try{
			mDb.execSQL(sql);
			return true;
		}catch(Exception e){
			Util.loge("Error Getting Records: "+ e.toString());
		}
		return false;

	}

	public Cursor fetch(String query,String [] arguments)  {
		try{
			Cursor c = mDb.rawQuery(query, null);
			return c;
		}catch(Exception e){
			Util.loge("Error fetch db: "+ e.toString());
		}
		return null;

	}



}