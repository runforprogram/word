package com.run.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.run.R;
import com.run.adapter.WordFragmentStatePagerAdapter;
import com.run.bean.Word;
import com.run.db.DbHelper;
import com.run.service.wordService;
import com.run.until.MyLogger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
	private List<Word> words = new ArrayList<Word>();
	public ViewPager viewpager;
	private WordFragmentStatePagerAdapter adapter;
	private List<Fragment> wordFragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLogger.i(getPackageName(), "onCreate");
		setContentView(R.layout.activity_my_main);
		getWordFromDb();
        initWordFragments();
		viewpager = (ViewPager) findViewById(R.id.viewpage);
		adapter = new WordFragmentStatePagerAdapter(
				getSupportFragmentManager(), wordFragments);
		viewpager.setAdapter(adapter);
		startService(new Intent(MainActivity.this, wordService.class));
		MyLogger.i(getPackageName(), "oncreate");
	}

	private void startDesktop() {
		Intent mIntent = new Intent();
		mIntent.setAction(Intent.ACTION_MAIN);
		mIntent.addCategory(Intent.CATEGORY_HOME);
		startActivity(mIntent);
	}

	private void getWordFromDb() {
		DbHelper mDbHelper = new DbHelper(this);
	//	mDbHelper.getWord(words, DbHelper.TABLE_LASTEST_WORD, 10);
	    mDbHelper.getBeforeWord(words);
		mDbHelper.getWord(words, 10 );
		mDbHelper.close();
	}

	private void initWordFragments() {
		for (Word word : words) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("word", word);
			WordFragment wordFragment = new WordFragment();
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
				// setContentView(R.layout.fragment_word);

				Word mWord = (Word) extras.get("word");
		
				viewpager.setCurrentItem(words.indexOf(mWord));
				MyLogger.i("index="+words.lastIndexOf(mWord)+"word="+mWord.getWord());

			}
		}
	}

	public void move2Next() {
        MyLogger.i("in move2Next"+viewpager.getCurrentItem());
		if (viewpager.getCurrentItem() == wordFragments.size() - 1) {
            viewpager.setCurrentItem(0);
		}else {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
        }
	}

	public void removeWord(WordFragment wordFragment) {
		if (wordFragments.size() == 1) {
			MyLogger.i("finish");
			finish();
			return;
		}
		words.remove(wordFragment.word);
		boolean value = wordFragments.remove(wordFragment);
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

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { switch
	 * (keyCode) { case KeyEvent.KEYCODE_BACK: if (transTextView.getVisibility()
	 * == View.VISIBLE) { return super.onKeyDown(keyCode, event); } else {
	 * transTextView.setVisibility(View.VISIBLE); return true; } default: return
	 * false; }
	 * 
	 * }
	 */
}
