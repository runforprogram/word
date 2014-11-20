package com.run.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.run.R;
import com.run.app.Datas;
import com.run.xml.parseWordXml;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * @author run
 * 
 * 
 */
public class DBManager {
	private final int BUFFER_SIZE = 400000;
	private static final String PACKAGE_NAME = "com.run";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases";
	private Context mContext;
	private SQLiteDatabase database;

	public DBManager(Context context) {
		this.mContext = context;
	}

	public void parseXml2Db() {
		if (new File(DB_PATH + "/" + Datas.DATABASE_NAME).exists()) {
			return;
		}
		parseWordXml mParseWordXml = new parseWordXml();
		try {
			mParseWordXml.parse(this.mContext.getResources().openRawResource(
					R.raw.wordxml));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openDateBase() {
		this.database = this.openDateBase(DB_PATH + "/" + Datas.DATABASE_NAME);

	}

	/**
	 * @param dbFile
	 * @return copy the raw db to app data fold
	 */
	private SQLiteDatabase openDateBase(String dbFile) {
		File file = new File(dbFile);
		if (!file.exists()) {
		
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
			InputStream stream = this.mContext.getResources().openRawResource(
					R.raw.testdata);
			try {
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
				stream.close();
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile,
						null);
				return db;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return database;
	}

	public void closeDatabase() {
		if (database != null && database.isOpen()) {
			this.database.close();
		}
	}
}
