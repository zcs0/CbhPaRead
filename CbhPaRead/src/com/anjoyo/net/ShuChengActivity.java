package com.anjoyo.net;
import java.util.ArrayList;
import java.util.List;

import com.anjoyo.aimo.LoginActivity;
import com.anjoyo.aimo.R;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ShuChengActivity extends TabActivity {
		// 页卡内容
		private ViewPager mPager;
		// Tab页面列表
		private List<View> listViews;
		private final Context context = ShuChengActivity.this;
		private LocalActivityManager manager = null;
	RadioGroup radioGroup;
	RadioButton shouye,xiaoshuo,tushu,zazhi,manhua;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wangluotab);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		InitViewPager();
		//绑定RadioGroup和RadioButton
		radioGroup=(RadioGroup)findViewById(R.id.main_radio);
		shouye=(RadioButton)findViewById(R.id.radio_shou);
		xiaoshuo=(RadioButton)findViewById(R.id.radio_xiao);
		tushu=(RadioButton)findViewById(R.id.radio_tu);
		zazhi=(RadioButton)findViewById(R.id.radio_za);
		manhua=(RadioButton)findViewById(R.id.radio_man);
		//RadioGroup监听、默认首页(shouye)选中
		shouye.setChecked(true);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radio_shou:
							mPager.setCurrentItem(0);
							break;
						case R.id.radio_xiao:
							mPager.setCurrentItem(1);
							break;
						case R.id.radio_tu:
							mPager.setCurrentItem(2);
							break;
						case R.id.radio_za:
							mPager.setCurrentItem(3);
							break;
						case R.id.radio_man:
							mPager.setCurrentItem(4);
							break;

						default:
							break;
						}
					}
				});
	}
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewPages);
		listViews = new ArrayList<View>();
		MyPagerAdapter mpAdapter = new MyPagerAdapter(listViews);
		Intent intent = new Intent(context, ShouyeActivity.class);
		listViews.add(getView("0", intent));
		Intent intent2 = new Intent(context, XiaoshuoActivity.class);
		listViews.add(getView("1", intent2));
		Intent intent3 = new Intent(context, TushuActivity.class);
		listViews.add(getView("2", intent3));
		Intent intent4 = new Intent(context, ZazhiActivity.class);
		listViews.add(getView("3", intent4));
		Intent intent5 = new Intent(context, ManhuaActivity.class);
		listViews.add(getView("4", intent5));
		mPager.setAdapter(mpAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;
		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}
		@Override
		public void finishUpdate(View arg0) {
		}
		@Override
		public int getCount() {
			return mListViews.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(View arg0) {
		}
	}
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				shouye.setChecked(true);
				break;
			case 1:
				xiaoshuo.setChecked(true);
				break;
			case 2:
				tushu.setChecked(true);
				break;
			case 3:
				zazhi.setChecked(true);
				break;
			case 4:
				manhua.setChecked(true);
				break;
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	private View getView(String id, Intent intent)
	{
		return manager.startActivity(id, intent).getDecorView();
	}
	//登陆界面
	public void OnLogin(View v){
		Intent in=new Intent(ShuChengActivity.this,LoginActivity.class);
		startActivity(in);
	}
	//返回书架界面
	public void BackClick(View v){
		finish();
	}

}
