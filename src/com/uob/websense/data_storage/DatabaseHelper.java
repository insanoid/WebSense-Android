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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uob.websense.support.Util;

/**
 * Database handling helper.
 * @author karthikeyaudupa
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private SQLiteDatabase mDataBase; 
	private final Context mContext;
	private static String DB_PATH = "";
	private String dbname = null;
	private static final int version =26;

	public DatabaseHelper(Context context, String databaseName) {
		super(context, databaseName, null, version);

		setDbname(databaseName);

		if(android.os.Build.VERSION.SDK_INT >= 17){
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = context.getFilesDir().getPath() +"/"+ context.getPackageName() + "/databases/";
		}

		this.mContext = context;
	}   

	public void createDataBase() throws IOException
	{
		//If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		if(!mDataBaseExist)
		{
			this.getReadableDatabase();
			this.close();
			try 
			{
				copyDataBase();
				Util.logi("Database Created.["+dbname+"]");
			} 
			catch (IOException mIOException) 
			{
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + dbname);
		return dbFile.exists();
	}

	//Copy the database from assets
	private void copyDataBase() throws IOException {

		InputStream mInput = mContext.getAssets().open(dbname);

		String outFileName = DB_PATH + dbname;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer))>0)
		{
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	//Open the database, so we can query it
	public boolean openDataBase() throws SQLException
	{
		String mPath = DB_PATH + dbname;
		mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		return mDataBase != null;
	}

	@Override
	public synchronized void close() 
	{
		if(mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

}