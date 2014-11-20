package com.run.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class WordFragmentAdapter extends PagerAdapter{
	List<Fragment> wordFragments;
	FragmentManager fragmentManager;
	
    public WordFragmentAdapter(FragmentManager fragmentManager,List<Fragment> wordFragments){
    	this.fragmentManager=fragmentManager;
    	this.wordFragments=wordFragments;
    };
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wordFragments.size();
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = wordFragments.get(position);
		if (!fragment.isAdded()) { // 如果fragment还没有added
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			fragmentManager.executePendingTransactions();
		}
         if (fragment.getView()!=null) {
        	 if (fragment.getView().getParent() == null) {
     			container.addView(fragment.getView()); // 为viewpager增加布局
     		}
		}
         

		return fragment.getView();
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView(wordFragments.get(position).getView()); // 移出viewpager两边之外的page布局
	}
	
	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}


}
