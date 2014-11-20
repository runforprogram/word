package com.run.adapter;

import java.util.List;

import com.run.until.MyLogger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class WordFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> fragments; // 每个Fragment对应一个Page

	public WordFragmentStatePagerAdapter(FragmentManager fragmentManager,
			List<Fragment> fragments) {
		super(fragmentManager);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		MyLogger.i("getcount=" + fragments.size());
		return fragments.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Log.i("dddd", "调用getItem");
		return fragments.get(arg0);
	}

	@Override
	public int getItemPosition(Object object) {
		Log.i("ddd", "调用getItemPosition");
		return PagerAdapter.POSITION_NONE;
	}

}
