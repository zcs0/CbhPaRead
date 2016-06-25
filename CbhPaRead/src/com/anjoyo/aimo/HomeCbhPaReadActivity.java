package com.anjoyo.aimo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;
import anjoyo.ReadRoomActivity;
import anjoyo.zcx.adapter.BookAdapter;

import com.anjoyo.manager.BooksFileDB;
import com.anjoyo.manager.DataManagerFactory;
import com.anjoyo.net.ShuChengActivity;

import entity.BookFile;

/**
 * 主界面
 * 
 * @author Administrator
 *
 * @param <LocalBook>
 * @param <LocAdapter>
 */
public class HomeCbhPaReadActivity<LocalBook, LocAdapter> extends Activity {
	int request = 111;//Activity请求码
	Button shucheng, shangci;
	private PopupWindow menuPop, titlePopupWindow;
	View menuPopView, titlePop;
	GridView toolbarGrView;
	static HomeCbhPaReadActivity cbhPaReadActivity;

	WifiManager wifiManager;
//	ArrayList<HashMap<String, Object>> data;
	HashMap<String, Object> map;
	private int REQUEST_CODE_SD = request+1;
	private List<BookFile> bookFileList;
	private BookAdapter bookAdapter;
	private EditText et_search;
	private BooksFileDB dbManager;
	private View btn_search;
	private View btn_select_delete;;

	// RadioButton lookButton;

	// RadioButton lookButton;

	/**
	 * 长按菜单响应函数
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:// 删除文件
				// 获取点击的是哪一个文件
			BooksFileDB dbManager = DataManagerFactory.getDBManager(this);
//			dbManager.delete(id);
			break;

		case 1:// 创建快捷方式
				// 获取点击的是哪一个文件

		}
		return super.onContextItemSelected(item);
	}
	@Override
	protected void onResume() {
		super.onResume();
		try {
			List<String> list = null;
			if(isPastList){
				list = dbManager.queryAllPast();//历史
				bookAdapter.setData(bookFileList);
			}else{
				list = dbManager.queryAllBook();
			}
			bookFileList = dbManager.jsonListToObj(list, BookFile.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		bookAdapter.setData(bookFileList);
		
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		dbManager = DataManagerFactory.getDBManager(this);
		toolbarGrView = (GridView) findViewById(R.id.gv1);
		bookAdapter = new BookAdapter(HomeCbhPaReadActivity.this);
		toolbarGrView.setAdapter(bookAdapter);
		et_search = (EditText) findViewById(R.id.et_search);
		btn_search = findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(HomeCbhPaReadActivity.this,
						ExternalStorageDirectoryActivity.class);
				startActivityForResult(in, REQUEST_CODE_SD);
			}
		});
		et_search.addTextChangedListener(new TextListener());
		shucheng = (Button) findViewById(R.id.shucheng);
		shangci = (Button) findViewById(R.id.shangci);
		cbhPaReadActivity = this;
		// lookButton=(RadioButton)findViewById(R.id.lookbook);
		// 点击书城，进入网络书城
		shucheng.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(HomeCbhPaReadActivity.this,
						ShuChengActivity.class);
				startActivity(in);
			}
		});
		/**
		 * 点击gridview，进行跳转
		 * 
		 */
		toolbarGrView
				.setOnItemClickListener(new GridView.OnItemClickListener() {
					// boolean bSelect=true;
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if(!bookAdapter.getSelectItemDelete()){//如果不是删除
							Intent intent = new Intent();
							intent.putExtra("file_id", bookFileList.get(arg2)._id);
							intent.setClass(HomeCbhPaReadActivity.this, ReadRoomActivity.class);
							startActivity(intent);
						}else{
							BookFile bookFile = bookFileList.get(arg2);
							String id = bookFile._id;
							bookFileList.remove(bookFile);
							bookAdapter.deleteBook(id);
						}
					}
				});

		// 长按删除和添加书签
//		toolbarGrView.setOnCreateContextMenuListener(new GridView.OnCreateContextMenuListener() {
//					
//
//					@Override
//					public void onCreateContextMenu(ContextMenu menu, View v,
//							ContextMenuInfo menuInfo) {
//						bookAdapter.setSelectItemDelete(!bookAdapter.getSelectItemDelete());
//					}
//				});
		
		/**
		 * 选择删除
		 */
		btn_select_delete = findViewById(R.id.btn_select_delete);
		btn_select_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setSelected(!v.isSelected());
				bookAdapter.setSelectItemDelete(v.isSelected());
			}
		});
	}

	private void import_book(List<BookFile> list) {
		if(list==null||list.size()<=0)return;
		
		bookAdapter.notifyDataSetChanged();
	}
	private void deleteFileById(String id){
		BooksFileDB dbManager = DataManagerFactory.getDBManager(HomeCbhPaReadActivity.this);
		dbManager.delete(id);
		List<String> queryAll2 = dbManager.queryAllBook();
		try {
			bookFileList = dbManager.jsonListToObj(queryAll2, BookFile.class);
			bookAdapter.setData(bookFileList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// 登陆界面
	public void OnLogin(View v) {
		Intent in = new Intent(HomeCbhPaReadActivity.this, LoginActivity.class);
		startActivity(in);
	}

	// 点击主页面的熊猫 弹出popwindow

	public void showMenuPop(View v) {
		if(menuPop==null){
			menuPopView = getLayoutInflater().inflate(R.layout.mainactivitymenupop,
					null);
	
			menuPop = new PopupWindow(menuPopView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		}
		// popwindow消失
		menuPop.setBackgroundDrawable(new BitmapDrawable());
		menuPop.showAtLocation(v, Gravity.BOTTOM|Gravity.LEFT, 10, v.getHeight()+35);

		menuPop.setFocusable(true);
		menuPop.setOutsideTouchable(true);
		menuPop.update();

	}

	// 菜单头部弹出popwindow
	public void titleMiddle(View v) {
		titlePop = getLayoutInflater().inflate(R.layout.titlepop, null);
		titlePopupWindow = new PopupWindow(titlePop, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		titlePopupWindow.showAsDropDown(v, 0, 0);
		titlePopupWindow.setFocusable(true);
		titlePopupWindow.setOutsideTouchable(true);
		titlePopupWindow.update();
		// lookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.syn_title_top));
	}

	// 菜单中部popwindow中按钮监听
	public void Booklogin(View v) {
		titlePopupWindow.dismiss();
		fengzhuang();
	}

	//
	public void YYong(View v) {
		menuPop.dismiss();
		Intent inweb = new Intent(HomeCbhPaReadActivity.this,
				YingyongActivity.class);
		startActivity(inweb);
	}

	// popwindow的关于按钮跳转
	public void OnAbout(View v) {
		menuPop.dismiss();
		Intent in = new Intent(HomeCbhPaReadActivity.this, AboutActivity.class);
		startActivity(in);
	}

	public void WifiCs(View v) {
		menuPop.dismiss();
		Intent inw = new Intent(HomeCbhPaReadActivity.this, WifiActivity.class);
		startActivity(inw);
	}

	// popwindow的摇摇分享按钮跳转
	public void YaoOnClick(View v) {
		fengzhuang();
	}
	private boolean isPastList = false;
	/**
	 * 历史
	 * @param v
	 */
	public void btnPastBookList(View v){
		v.setSelected(!v.isSelected());
		menuPop.dismiss();
		try {
			if(v.isSelected()){
				isPastList = true;
				List<String> queryAllPast = dbManager.queryAllPast();
				bookFileList = dbManager.jsonListToObj(queryAllPast, BookFile.class);
			}else{
				isPastList = false;
				List<String> queryAllPast = dbManager.queryAllBook();
				bookFileList = dbManager.jsonListToObj(queryAllPast, BookFile.class);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		bookAdapter.setData(bookFileList);
		
		
	}
	// wifi传输按钮 获取wifi服务，并开启wifi
	public void WiFiOn(View v) {
		if (wifiManager == null)

			wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if (!wifiManager.isWifiEnabled()) {

			wifiManager.setWifiEnabled(true);

		}
	}

	// 上次按钮的跳转
	public void lastFileViewClick(View v) {
		
		SharedPreferences sp = DataManagerFactory.getSP(HomeCbhPaReadActivity.this);
		String lastFileId = sp.getString(DataManagerFactory.last_flie_id, "");
		if(!TextUtils.isEmpty(lastFileId)){
			try {
				Intent in = new Intent(HomeCbhPaReadActivity.this, ReadRoomActivity.class);//ReadRoomActivity
				in.putExtra("file_id", lastFileId);
				startActivity(in);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	// 跳转封装
	public void fengzhuang() {
		Intent in = new Intent(HomeCbhPaReadActivity.this, LoginActivity.class);

		startActivity(in);
	}

	// 本地wenjian
	public void btnOpenSD(View v) {
		menuPop.dismiss();
		Intent in = new Intent(HomeCbhPaReadActivity.this,
				ExternalStorageDirectoryActivity.class);
		startActivityForResult(in, REQUEST_CODE_SD);
	}

	// 点击熊猫 popwndow消失
	public void MiongMaoXiaoShi(View v) {
		menuPop.dismiss();
	}

	// popwindow的设置按钮跳转
	public void settings(View v) {
		menuPop.dismiss();
		Intent in = new Intent(HomeCbhPaReadActivity.this, SettingsActivity.class);
		startActivity(in);

	}

	// 连击次数 2 
	long[] mHits = new long[2];

	public void closeActivity() {
		Toast.makeText(this, "再点击一下退出 ", Toast.LENGTH_SHORT).show();
		System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
		mHits[mHits.length - 1] = SystemClock.uptimeMillis();
		Date date = new Date(SystemClock.uptimeMillis());
		// 点击间隔时间
		if (mHits[0] >= (SystemClock.uptimeMillis() - 800)) {
//			Toast.makeText(this, "我被2击了 ", 0).show();
//			DaoHangActivity.dao.finish();
			System.exit(0);
		}
	}
	
	// 拦截back键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(bookAdapter!=null&&bookAdapter.getSelectItemDelete()){
				btn_select_delete.setSelected(false);
				bookAdapter.setSelectItemDelete(false);
				return true;
			}
			closeActivity();
//			Builder build = new AlertDialog.Builder(this);
//			build.setTitle("亲！你真的要退出程序吗？");
//			build.setIcon(R.drawable.bq18);
//			build.setPositiveButton("确定", new OnClickListener() {
//
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					DaoHangActivity.dao.finish();
//					System.exit(0);
//				}
//			});
//			build.setNegativeButton("取消", new OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//
//				}
//			});
//			build.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	// 熊猫popwindow中的退出按钮
	public void exitwindow(View v) {
//		DaoHangActivity.dao.finish();
		System.exit(0);
	}
	
	class TextListener implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			if(!TextUtils.isEmpty(s)){
				List<String> search = dbManager.search(s.toString());
				try {
					List<BookFile> jsonListToObj = dbManager.jsonListToObj(search, BookFile.class);
					bookAdapter.setData(jsonListToObj);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				bookAdapter.setData(bookFileList);
			}
		}
	}

}
