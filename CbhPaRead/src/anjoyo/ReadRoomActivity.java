package anjoyo;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.anjoyo.aimo.R;
import com.anjoyo.manager.BooksFileDB;
import com.anjoyo.manager.DataManagerFactory;
import com.anjoyo.util.JSONUtils;
import com.aphidmobile.flip.FlipViewController;

import entity.BookFile;
/**
 * 阅读的界面
 * @author Administrator
 *
 */
public class ReadRoomActivity extends Activity {

	private static final String TAG = null;
	private FlipViewController flipView;
	private int[] position = new int[] { 0, 0 };
	private TextView tv;
	private float textSize;
	private int count2;
	private BookPageFactory pagefactory;
	private Canvas mCurrentPageCanvas;
	private Bitmap mCurrentPageBitmap;
	private DisplayMetrics dm;
	private List<Vector> listPager;
	private FlipAdapter flipAdapter;
	private BooksFileDB bookfileDb;
	BookFile bookFile = null;
	private SharedPreferences booksSp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("file_id");
		bookfileDb = DataManagerFactory.getDBManager(this);
		String book = bookfileDb.getBookById(id);
		
		if(TextUtils.isEmpty(book)){
			finish();
			return;
		}
		try {
			bookFile = JSONUtils.jsonToObj(book, BookFile.class);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		booksSp = DataManagerFactory.getSP(ReadRoomActivity.this);
		Editor edit = booksSp.edit();
		edit.putString(DataManagerFactory.last_flie_id, id);
		edit.commit();//保存最后一次
		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		setContentView(flipView);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mCurrentPageBitmap = Bitmap.createBitmap(dm.widthPixels,
				dm.heightPixels, Bitmap.Config.ARGB_8888);
		mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
		// mNextPageCanvas = new Canvas(mNextPageBitmap);
		int fontSize = booksSp.getInt(DataManagerFactory.books_font_size, 35);
		int fontColor= booksSp.getInt(DataManagerFactory.books_font_color, Color.BLACK);
		int lineSpacing= booksSp.getInt(DataManagerFactory.books_font_line_spacing, 2);
		pagefactory = new BookPageFactory(dm.widthPixels, dm.heightPixels,
				fontSize,lineSpacing,fontColor);
		try {
			pagefactory.setBgBitmap(BitmapFactory.decodeResource(
					this.getResources(), R.drawable.book_bg11));
			pagefactory.openBook(bookFile.f_path, position);
			listPager = pagefactory.getListPager();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
//		flipView.setBackgroundColor(Color.WHITE);
		flipAdapter = new FlipAdapter(tv);
		flipView.setAdapter(flipAdapter);
		if(bookFile!=null&&listPager!=null&&bookFile.f_read_pager>0&&bookFile.f_read_pager<listPager.size()){
			flipView.setSelection((int)bookFile.f_read_pager);
		}
	}

	class FlipAdapter extends BaseAdapter {
		private TextView tv;

		public FlipAdapter(TextView tv) {
			this.tv = tv;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listPager==null?0:listPager.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listPager.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)
				arg1 = new MyPageWidget(ReadRoomActivity.this);
			Bitmap mCurrentPageBitmap = Bitmap.createBitmap(dm.widthPixels,
					dm.heightPixels, Bitmap.Config.ARGB_8888);
			Canvas mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
			MyPageWidget mPageWidget = (MyPageWidget) arg1;
			pagefactory.setPersent(arg0);
//			pagefactory.onDrow(mCurrentPageCanvas);
			pagefactory.onDrowToPager(mCurrentPageCanvas,arg0);
			mPageWidget.setDrawBitMap(mCurrentPageBitmap);
			mPageWidget.invalidate();
			float persent =(1+arg0)*100f/listPager.size();
			arg1.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
//					Toast.makeText(ReadRoomActivity.this, "onLongClick", Toast.LENGTH_SHORT).show();
					openOptionsMenu();
					return false;
				}
			});
			return arg1;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,1,1, R.string.font);
		menu.add(0,2,2, R.string.persent);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			showAlterDialog(1);
			break;
		case 2:
			showAlterDialog(2);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	private void showAlterDialog(int pos) {
		AlertDialog.Builder builder = new Builder(this);
		final int i = pos;
		if(i == 1){
			builder.setTitle(R.string.font);
		}else if(i == 2){
			builder.setTitle(R.string.persent);
		}
		View view = View.inflate(ReadRoomActivity.this, R.layout.set_font, null);//mInflater.inflate(R.layout.set_font, null);
		final EditText et_font = (EditText) view.findViewById(R.id.et_font);
		builder.setView(view);
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String text = et_font.getText().toString();
				if(!TextUtils.isEmpty(text)){
					if(i == 1){//保存字体大小
						Editor edit = booksSp.edit();
						edit.putInt(DataManagerFactory.books_font_size, Integer.parseInt(text)).commit();
						try {
							pagefactory.setTextFont(Integer.parseInt(text));
							pagefactory.openBook(bookFile.f_path, position);
							listPager = pagefactory.getListPager();
							flipView.setAdapter(flipAdapter);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(i == 2){
						int index = Integer.parseInt(text)-1;
						index = index<0?0:index;
						if(listPager!=null&&index<listPager.size()){
							flipView.setSelection(index);
						}
						pagefactory.setPersent(Integer.parseInt(text));
					}
					flipAdapter.notifyDataSetInvalidated();
				}
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.show();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(bookFile!=null){
			int selectedItemPosition = flipView.getSelectedItemPosition();
			bookFile.f_read_pager = selectedItemPosition;
			bookFile.f_pager_count = listPager.size();
			bookFile.f_type = 1;
			bookfileDb.update(bookFile);
		}
	}

}
