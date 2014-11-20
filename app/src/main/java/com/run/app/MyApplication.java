package com.run.app;
import com.run.db.DBManager;
import com.run.xml.parseWordXml;

import android.R;
import android.app.Application;
/**
 * 初始化一些配置
 * @author run
 *
 */
public class MyApplication extends Application {
	 private static MyApplication instance;  
	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;	
		initDb();
	}
	 public static MyApplication getInstance() {  
	        return instance;  
	 }  
	 private void initDb(){
		
		 DBManager manager=new DBManager(this);
	//	 manager.openDateBase();
	     manager.parseXml2Db();
	 }
	
	 
	 
}
