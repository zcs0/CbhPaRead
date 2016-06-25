package com.anjoyo.aimo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import anjoyo.zhou.read.BookPageFactory;
import anjoyo.zhou.read.MarkDialog;
import anjoyo.zhou.read.MarkHelper;

import com.aphidmobile.flip.FlipViewController;

/**
 * 阅读页
 * 
 * @author Administrator
 *
 */
public class ReadBookActivity extends Activity {
	private static final String TAG = "Read2";
	private static int begin = 0;// 记录的书籍开始位置
	public static Canvas mCurPageCanvas, mNextPageCanvas;
	private static String word = "";// 记录当前页面的文字
	private int a = 0, b = 0;// 记录toolpop的位置
	private TextView bookBtn1, bookBtn2, bookBtn3, bookBtn4;
	private String bookPath;// 记录读入书的路径
	private String ccc = null;// 记录是否为快捷方式调用
	protected long count = 1;
	private SharedPreferences.Editor editor;
	private ImageButton imageBtn2, imageBtn3_1, imageBtn3_2;
	private ImageButton imageBtn4_1, imageBtn4_2;
	private Boolean isNight; // 亮度模式,白天和晚上
	protected int jumpPage;// 记录跳转进度条
	private int light; // 亮度值
	private WindowManager.LayoutParams lp;
	private TextView markEdit4;
	private MarkHelper markhelper;
	private Bitmap mCurPageBitmap, mNextPageBitmap;
	private MarkDialog mDialog = null;
	private Context mContext = null;
	private PageWidget mPageWidget;
	private PopupWindow mPopupWindow, mToolpop, mToolpop1, mToolpop2,
			mToolpop3, mToolpop4;
	protected int PAGE = 1;
	private BookPageFactory pagefactory;
	private View popupwindwow, toolpop, toolpop1, toolpop2, toolpop3, toolpop4;
	int screenHeight;
	int readHeight; // 电子书显示高度
	int screenWidth;
	private SeekBar seekBar1, seekBar2, seekBar4;
	private Boolean show = false;// popwindow是否显示
	private int size = 30; // 字体大小
	private SharedPreferences sp;
	int defaultSize = 0;

	String txtName, txtName1;
	private FlipViewController flipView;
	private int pagerNum;
	private String readFile;
	private int count2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = getBaseContext();
		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		TextView tv = new TextView(this);
		tv.setBackgroundColor(Color.BLUE);
		tv.setTextColor(Color.BLACK);
		int lineHeight = tv.getLineHeight();//行高
		Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
		int height = defaultDisplay.getHeight();//最高底
		int lineNum = height/lineHeight;//一页的行数;
		float textSize = tv.getTextSize();//一个字的大小
		int width = defaultDisplay.getWidth();//最大宽度
		int rowCount = (int) (width/textSize);//一行的字数
		count2 = rowCount*lineNum;//一页显示的字数
		readFile = getReadFile(new File(Environment.getExternalStorageDirectory()+"/test.txt"));
		pagerNum = readFile.length()/count2;
		Log.d(TAG, readFile);
		flipView.setBackgroundColor(Color.WHITE);
		flipView.setAdapter(new FlipAdapter(tv));
		setContentView(flipView);
	}




	class FlipAdapter extends BaseAdapter {
		private TextView tv ;
		public FlipAdapter(TextView tv){
			this.tv = tv;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			return pagerNum;
			return 10;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			//(0*count2)//
//			CharSequence subSequence = readFile.subSequence(arg0*count2, (arg0+1)*count2);//
//			tv.setText(subSequence);
			tv.setText(arg0+"  页");
			if(arg0%2==0){
				tv.setBackgroundColor(Color.BLUE);
			}else{
				tv.setBackgroundColor(Color.CYAN);
			}
			return tv;
		}

	}
	
	/**
	 * 读取文件
	 * @param file
	 * @return
	 */
	private String getReadFile(File file){
		StringBuffer buffer = new StringBuffer();
		if(file.isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String readLine ="";
				while((readLine = br.readLine())!=null){
					buffer.append(readLine+"\n");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}

}
