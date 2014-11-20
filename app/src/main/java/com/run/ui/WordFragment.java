package com.run.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.run.R;
import com.run.app.MyApplication;
import com.run.bean.Word;
import com.run.db.DbHelper;
import com.run.until.MyLogger;
/*向上滑动 没有记住
  向下滑动 记住
  连续单击两下，这个单词不常用，表示已经记住，不在显示出来
  长按屏幕 显示意思

* */
public class WordFragment extends Fragment implements  OnGestureListener {

	public  Word word;
	private View view;
	private TextView wordTextView, transTextView, phoneticTextView;
	private View root;
	protected int lastX;
	protected int lastY;
	protected int left;
	protected int top;
	private float FLING_MIN_DISTANCE_X = 10000;
	private float FLING_MIN_VELOCITY_X = 20;
	private float FLING_MIN_DISTANCE_Y = 50;
	private float FLING_MIN_VELOCITY_Y = 40;
	private int l;
	private int t;
	private int r;
	private int b;
	private int initL,initT,initR,initB;
    private long pressTime;
	
	private GestureDetector mGestureDetector = new GestureDetector(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLogger.i("oncatetview");
		// TODO Auto-generated method stub
		// 这个不懂是什么意思。但是解决了 上一页显示不正确的问题
		if (view != null) {
			MyLogger.i("oncreateview not null");
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			
			return view;
		}

		word = (Word) getArguments().get("word");
		view = inflater.inflate(R.layout.fragment_word, null);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);// get
																				// your
																				// root
																				// layout
		layout.setLongClickable(true); // must set longclickable be
										// true.otherwise gestrure can not get
										// onfling.
		layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetector.onTouchEvent(event);

			}
		});
		setupViews();
		return view;
	}

	private void setupViews() {

		root = (View) view.findViewById(R.id.root);
		initL=root.getLeft();
		initR=root.getRight();
		initT=root.getTop();
		initB=root.getBottom();
		wordTextView = (TextView) view.findViewById(R.id.tv_word);
		wordTextView.setText(word.getWord());
		phoneticTextView = (TextView) view.findViewById(R.id.tv_phonetic);
		phoneticTextView.setText(word.getPhonetic());
		transTextView = (TextView) view.findViewById(R.id.tv_trans);
		transTextView.setVisibility(View.INVISIBLE);
		transTextView.setText(word.getTrans());

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.e("cr", "ondown");
        if (System.currentTimeMillis()-pressTime<500){
            //连续单击两次
            wordNotShowAgain();
        }else {
            pressTime=System.currentTimeMillis();
        }

		// 显示翻译
	//	transTextView.setVisibility(View.VISIBLE);
	//	((MainActivity)getActivity()).removeWord(this);
	/*	TranslateAnimation animation = new TranslateAnimation(
			    Animation.ABSOLUTE, -100,
			    Animation.ABSOLUTE, -100);
			  wordTextView.startAnimation(animation);
			  wordTextView.setAnimation(animation);*/

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.e("cr", "onshowpress");
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.e("cr", "nosingletapup");
		// 隐藏翻译
		transTextView.setVisibility(View.INVISIBLE);
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动
			t = root.getTop() - (int) distanceY;
			b = root.getBottom() - (int) distanceY;
			l = root.getLeft();
			r = root.getRight();
		} else {// 横向移动  不管了 
			l = root.getLeft() - (int) (distanceX);
			r = root.getRight() - (int) distanceX;
			t = root.getTop();
			b = root.getBottom();
			return false;
		}
	//	if (Math.abs(distanceY) >distanceY||Math.abs(distanceX)>distanceX) {
			root.layout(l, t, r, b);
			Log.e("run", "x=" + distanceX + "y=" + distanceY);	
	//	}
		Log.e("cr", "onScroll");

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
        transTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.e("cr", "onFling");
		float distanceX = e2.getX() - e1.getX();
		float distanceY = e2.getY() - e1.getY();
		Log.e("","y的距离是"+distanceY);
		if (distanceX > FLING_MIN_DISTANCE_X) {
			// Fling right? 记住
		} else if (Math.abs(distanceX) > FLING_MIN_DISTANCE_X) {
			// Fling left? 没记住
		} else if (distanceY > FLING_MIN_DISTANCE_Y) {
			// 下滑 记住了
			((MainActivity)getActivity()).removeWord(this);
			
			wordRemebered();
			
		} else if (Math.abs(distanceY) > FLING_MIN_DISTANCE_Y) {
			// 上滑 没记住
		
			MyLogger.i("上滑，，，，");
			wordNoRemebered();
		//	 getActivity().getSupportFragmentManager().popBackStack();
			((MainActivity)getActivity()).move2Next();
			 
	/*	 getActivity().getSupportFragmentManager().popBackStack();
			 getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

*/			// 调用桌面
			/*
			 * Intent mIntent = new Intent();
			 * mIntent.setAction(Intent.ACTION_MAIN);
			 * mIntent.addCategory(Intent.CATEGORY_HOME);
			 * startActivity(mIntent);
			 */

		}
		return false;
	}
    private void wordNotShowAgain(){
        word.setProgress(-1);
        updateDb();
    }
	private void wordRemebered() {
        if (word.getProgress()>=1){
            word.setProgress(word.getProgress() - 1 );
        }
      //  word.setLastTime(System.currentTimeMillis());
		updateDb();
		//deleteFromLastestDb();
	}
    private void wordNoRemebered() {
        transTextView.setVisibility(View.INVISIBLE);
        if (word.getProgress()<9){
            word.setProgress((word.getProgress()) + 1);
        }
      //  word.setLastTime(System.currentTimeMillis());  //
        updateDb();
        showNotification();
        //	insertLastestDb();
    }

	private void deleteFromLastestDb() {
		DbHelper mDbHelper = new DbHelper(MyApplication.getInstance());
		if (mDbHelper.ifInDb(word.getWord(), DbHelper.TABLE_LASTEST_WORD)) {
			mDbHelper.delete(word);
		}
	}

	private void insertLastestDb() {
		DbHelper mDbHelper = new DbHelper(MyApplication.getInstance(), 2);
		mDbHelper.insert(word, DbHelper.TABLE_LASTEST_WORD);
		mDbHelper.close();
	}

	private void updateDb() {
		DbHelper mDbHelper = new DbHelper(MyApplication.getInstance());
		mDbHelper.update(word);
		mDbHelper.close();
	}

	private void showNotification() {
		NotificationManager notificationManager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification.Builder(getActivity())
				.setContentText(word.getWord()).setSmallIcon(R.drawable.bc1)
				.setWhen(System.currentTimeMillis()).build();
		notification.contentView = new RemoteViews(getActivity()
				.getPackageName(), R.layout.notification);
		notification.contentView.setCharSequence(R.id.word, "setText",
				word.getWord());
		Intent mIntent = new Intent(getActivity().getApplicationContext(),
				MainActivity.class);
		mIntent.putExtra("word", word);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent mIntent2 = PendingIntent.getActivity(getActivity()
				.getApplicationContext(), 0, mIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.contentIntent = mIntent2;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000; // 闪光时间，毫秒
		notificationManager.notify(0, notification);

	}

}
