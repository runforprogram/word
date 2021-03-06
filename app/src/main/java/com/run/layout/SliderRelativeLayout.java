package com.run.layout;
import com.run.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SliderRelativeLayout extends RelativeLayout {
	private static String TAG = "SliderRelativeLayout";
	private TextView tv_slider_icon = null; 
	private Bitmap dragBitmap = null; 
	private Context mContext = null; 
	private Handler mainHandler = null; 	
	public SliderRelativeLayout(Context context) {
		super(context);
		mContext = context;
		initDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		mContext = context;
		initDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initDragBitmap();
	}
	

	private void initDragBitmap() {
		if (dragBitmap == null)
			dragBitmap = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.getup_slider_ico_pressed);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tv_slider_icon = (TextView) findViewById(R.id.slider_icon);
	}
	private int mLastMoveX = 1000;  
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Log.i(TAG, "onTouchEvent" + " X is " + x + " Y is " + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMoveX = (int) event.getX();
			return handleActionDownEvenet(event);
		case MotionEvent.ACTION_MOVE:
			mLastMoveX = x; 
            invalidate();		    
			return true;
		case MotionEvent.ACTION_UP:
			handleActionUpEvent(event);
			return true;
		}
		return super.onTouchEvent(event);
	}


	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
	
		invalidateDragImg(canvas);
	}

	private void invalidateDragImg(Canvas canvas) {
	
		int drawXCor = mLastMoveX - dragBitmap.getWidth();
		int drawYCor = tv_slider_icon.getTop();
		Log.i(TAG, "invalidateDragImg" + " drawXCor "+ drawXCor + " and drawYCor" + drawYCor);
	    canvas.drawBitmap(dragBitmap,  drawXCor < 0 ? 5 : drawXCor , drawYCor , null);
	}

	
	private boolean handleActionDownEvenet(MotionEvent event) {
		Rect rect = new Rect();
		tv_slider_icon.getHitRect(rect);
		boolean isHit = rect.contains((int) event.getX(), (int) event.getY());
		
		if(isHit) 
			tv_slider_icon.setVisibility(View.INVISIBLE);
		

		return isHit;
	}


	private static int BACK_DURATION = 20 ;   // 20ms
  
	private static float VE_HORIZONTAL = 0.7f ;  //0.1dip/ms

	private void handleActionUpEvent(MotionEvent event){		
		int x = (int) event.getX() ;	
		Log.e(TAG, "handleActionUpEvent : x -->" + x + "   getRight() " + getRight() );

		boolean isSucess= Math.abs(x - getRight()) <= 15 ;
		
		if(isSucess){
		   Toast.makeText(mContext, "解锁成功", 1000).show();
		   resetViewState();	
		   virbate();

		   mainHandler.obtainMessage(1).sendToTarget();  //todo 
		}
		else {
			mLastMoveX = x ;  
			int distance = x - tv_slider_icon.getRight() ;
			Log.e(TAG, "handleActionUpEvent : mLastMoveX -->" + mLastMoveX + " distance -->" + distance );
			if(distance >= 0)
			    mHandler.postDelayed(BackDragImgTask, BACK_DURATION);
			else{
				resetViewState();
			}
		}
	}
	private void resetViewState(){
		mLastMoveX = 1000 ;
		tv_slider_icon.setVisibility(View.VISIBLE);
		invalidate();      
	}
	

	private Runnable BackDragImgTask = new Runnable(){
		
		public void run(){
			mLastMoveX = mLastMoveX - (int)(BACK_DURATION * VE_HORIZONTAL);			
			invalidate();	
			boolean shouldEnd = Math.abs(mLastMoveX - tv_slider_icon.getRight()) <= 8 ;			
			if(!shouldEnd)
			    mHandler.postDelayed(BackDragImgTask, BACK_DURATION);
			else { 
				resetViewState();	
			}				
		}
	};
	
	private Handler mHandler =new Handler (){
		
		public void handleMessage(Message msg){
			
			Log.i(TAG, "handleMessage :  #### " );
			
		}
	};
	private void virbate(){
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}
	public void setMainHandler(Handler handler){
		mainHandler = handler;
	}
}
