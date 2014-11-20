package com.run.ui;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

import com.run.R;
import com.run.adapter.WordFragmentAdapter;
import com.run.adapter.WordFragmentStatePagerAdapter;
import com.run.bean.Word;
import com.run.db.DbHelper;
import com.run.service.wordService;
import com.run.until.MyLogger;

public class FragmentMainActivity extends FragmentActivity  {
	private List<Word> words=new ArrayList<Word>();
public 	ViewPager viewpager;
private  WordFragmentStatePagerAdapter adapter;
	private List<Fragment> wordFragments=new ArrayList<Fragment>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLogger.i(getPackageName(),"onCreate");
		setContentView(R.layout.activity_fragment_main);	
		getWordFromDb();
		initData();	
	
		
//		viewpager.setAdapter(adapter);
		
		getSupportFragmentManager().beginTransaction().add(R.id.ll,wordFragments.get(2),"2").commit();
		getSupportFragmentManager().beginTransaction().add(R.id.ll,wordFragments.get(1),"1").commit();
		startService(new Intent(FragmentMainActivity.this, wordService.class));
		MyLogger.i(getPackageName(), "oncreate");	

	}
	private void getWordFromDb(){
		DbHelper mDbHelper=new DbHelper(this);
		mDbHelper.getWord(words,DbHelper.TABLE_LASTEST_WORD,10);
		mDbHelper.getWord(words,10-words.size());
		mDbHelper.close();
	}
	private void initData(){
		for (Word word : words) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("word",word);
			WordFragment wordFragment=new WordFragment();
			wordFragment.setArguments(bundle);
			wordFragments.add(wordFragment);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		MyLogger.i(getPackageName(), "onstart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		MyLogger.i(getPackageName(), "onRestart");
		super.onRestart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		MyLogger.i(getPackageName(), "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		MyLogger.i(getPackageName(), "onResume");
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		MyLogger.i(getPackageName(), "onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyLogger.i(getPackageName(), "ondestroy");
		super.onDestroy();
	}

	@Override
	public void onNewIntent(Intent intent) {
		MyLogger.i(getPackageName(), "onnewIntent");
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.containsKey("word")) {
			//	setContentView(R.layout.fragment_word);
				 
				Word mWord = (Word) extras.get("word");
				Bundle bundle = new Bundle();
				bundle.putSerializable("word",mWord);
				WordFragment wordFragment=new WordFragment();
				wordFragment.setArguments(bundle);
				viewpager.setCurrentItem(words.lastIndexOf(mWord));
				
			}
		}
	}

	public void removeWord(WordFragment wordFragment ){
		if (wordFragments.size()==1) {
			MyLogger.i("finish");
			finish();
		    return;
		}
		boolean value=  wordFragments.remove(wordFragment);
        if (value) {
			MyLogger.i("remove success");
		}
        adapter.notifyDataSetChanged();
        MyLogger.i("remove fragement");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_main, menu);
		return true;
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (transTextView.getVisibility() == View.VISIBLE) {
				return super.onKeyDown(keyCode, event);
			} else {
				transTextView.setVisibility(View.VISIBLE);
				return true;
			}
		default:
			return false;
		}

	}
*/
}
