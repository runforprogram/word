package com.run.service;
import com.run.ui.MainActivity;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class wordService extends Service {

	private static String TAG = "ZdLockService";
	private Intent zdLockIntent = null ;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate(){
		super.onCreate();
		
		
		zdLockIntent = new Intent(wordService.this , MainActivity.class);
		zdLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		IntentFilter mScreenOnFilter = new IntentFilter("android.intent.action.SCREEN_ON");
		wordService.this.registerReceiver(mScreenOnReceiver, mScreenOnFilter);
		
	
		IntentFilter mScreenOffFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
		wordService.this.registerReceiver(mScreenOffReceiver, mScreenOffFilter);
	}

	public int onStartCommand(Intent intent , int flags , int startId){
		
		return Service.START_STICKY;
		
	}
	
	public void onDestroy(){
		super.onDestroy();
		wordService.this.unregisterReceiver(mScreenOnReceiver);
		wordService.this.unregisterReceiver(mScreenOffReceiver);
		startService(new Intent(wordService.this, wordService.class));
	}
	
	private KeyguardManager mKeyguardManager = null ;
	private KeyguardManager.KeyguardLock mKeyguardLock = null ;
	private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context , Intent intent) {
			
            Log.i(TAG, intent.getAction());

			if(intent.getAction().equals("android.intent.action.SCREEN_ON")){
				Log.i(TAG, "----------------- android.intent.action.SCREEN_ON------");
			}
		}
		
	};
	
	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context , Intent intent) {
			String action = intent.getAction() ;
			
		    Log.i(TAG, intent.toString());
		    
			if(action.equals("android.intent.action.SCREEN_OFF")
					|| action.equals("android.intent.action.SCREEN_ON") ){
				mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("zdLock 1"); 
				mKeyguardLock.disableKeyguard();
				startActivity(zdLockIntent);
			}
		}
		
	};
	
}
