package anjoyo.zcx.com;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.anjoyo.aimo.HomeCbhPaReadActivity;
import com.anjoyo.aimo.R;

public class DaoHangActivity extends Activity {
	public static DaoHangActivity dao;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	// 包裹滑动图片LinearLayout
	private ViewGroup main;
	// 包裹小圆点的LinearLayout
	private ViewGroup group;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题窗口
		dao = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();

		pageViews.add(inflater.inflate(R.layout.viewpagerone, null));
		pageViews.add(inflater.inflate(R.layout.viewpagertwo, null));
		pageViews.add(inflater.inflate(R.layout.viewpagerthree, null));
		pageViews.add(inflater.inflate(R.layout.welcome, null));

		main = (ViewGroup) inflater.inflate(R.layout.daohangmain, null);

		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);

		setContentView(main);

		// viewPager.setAdapter(new GuidePageAdapter());//第一次进入时
		// viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		// viewPager.setCurrentItem(1);

		// 进入主界面
		Intent intent = new Intent();
		intent.setClass(DaoHangActivity.this, HomeCbhPaReadActivity.class);
		startActivity(intent);

	}

	// 指引页面数据适配器
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}

	// 指引页面更改事件监听器
	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 3) {
				final Intent intent = new Intent();
				intent.setClass(DaoHangActivity.this, HomeCbhPaReadActivity.class);
				// 创建一个记时器，run方法中是记时器要实现的任务
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						startActivity(intent);
					}
				};
				timer.schedule(task, 1000);
			}

		}
	}

}