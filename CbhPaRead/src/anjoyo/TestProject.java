package anjoyo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.util.Log;

import com.anjoyo.manager.BooksFileDB;
import com.anjoyo.manager.DataManagerFactory;
import com.anjoyo.util.JSONUtils;

import entity.BookFile;

/**
 * @ClassName:     TestProject.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2016年5月14日 上午9:52:33 
 * @Description:   TODO(用一句话描述该文件做什么) 
 */
public class TestProject extends AndroidTestCase {
	private static final String TAG = "TestProject 测试";
	
	
	public void TestFontStyle(){
		
//		SharedPreferences sp = getContext().getSharedPreferences("sp_default_config", Context.MODE_PRIVATE);
		SharedPreferences sp = DataManagerFactory.getSP(getContext());
		Editor edit2 = sp.edit();
		edit2.putInt(DataManagerFactory.books_font_size, 55).commit();
		int fontSize33 = sp.getInt(DataManagerFactory.books_font_size, 35);
		
		SharedPreferences fontInfo = DataManagerFactory.getSP(getContext());
		int fontSize = fontInfo.getInt(DataManagerFactory.books_font_size, 35);
		Editor edit = fontInfo.edit();
		edit.putInt(DataManagerFactory.books_font_spacing, 15).commit();
		int fontSize2 = fontInfo.getInt(DataManagerFactory.books_font_size, 35);
		System.out.println(fontSize2);
	}
	
	
	public void TestUpdateType(){
		BooksFileDB dbManager = DataManagerFactory.getDBManager(getContext());
		List<String> queryAllBook = dbManager.queryAllBook();
		List<String> queryAllPast = dbManager.queryAllPast();
		String bookById = dbManager.getBookById("1");
		try {
			BookFile jsonToObj = dbManager.jsonToObj(bookById, BookFile.class);
			jsonToObj.f_type = 1;
			int update = dbManager.update(jsonToObj);
			String str = dbManager.getBookById("1");
			System.out.println(str);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int selectedItemPosition = flipView.getSelectedItemPosition();
//		bookFile.f_read_pager = selectedItemPosition;
//		bookFile.f_pager_count = listPager.size();
//		bookFile.f_type = 1;
//		bookfileDb.update(bookFile);
		
		
//		dbManager.delete("1");
//		List<String> queryAllBook = dbManager.queryAllBook();
//		List<String> queryAllPast = dbManager.queryAllPast();
//		System.out.println(queryAllBook);
//		System.out.println(queryAllPast);
		
		
	}
	
	public void TestSearch(){
		BooksFileDB dbManager = DataManagerFactory.getDBManager(getContext());
		List<String> search = dbManager.search("sdcard");
		try {
			List<BookFile> jsonListToObj = dbManager.jsonListToObj(search, BookFile.class);
			System.out.println(jsonListToObj);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void TestUpdate(){
		BooksFileDB dbManager = DataManagerFactory.getDBManager(getContext());
		String fileById = dbManager.getBookById("1");
		try {
			BookFile jsonToObj = dbManager.jsonToObj(fileById, BookFile.class);
			jsonToObj.f_name = "测试name";
			jsonToObj.f_read_pager = 3;
			int i = dbManager.update(jsonToObj);
			fileById = dbManager.getBookById("1");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void TestReadFile(){
		ArrayList<BookFile> initFileListInfo = initFileListInfo("/cust");
//		Collections.sort(initFileListInfo);
		Collections.sort(initFileListInfo,new Comparator<BookFile>() {

			@Override
			public int compare(BookFile lhs, BookFile rhs) {
				
				if(TextUtils.isEmpty(lhs.f_name)){
					System.out.println("    --");
					return 0;
				}
				if(TextUtils.isEmpty(rhs.f_name)){
					System.out.println("    ++");
					return 0;
				}
				int i = lhs.f_name.charAt(0)-rhs.f_name.charAt(0);
				System.out.println("   "+lhs.f_name+"   "+rhs.f_name+"   "+i);
				return i>0?1:(i==0?0:-1);
			}
		});
		for (BookFile bookFile : initFileListInfo) {
			Log.d(TAG, "----------------"+bookFile.f_name);
		}
		
	}
	
	
	
	
	// 遍历文件 即扫描
	private ArrayList<BookFile> initFileListInfo(String filePath) {
		ArrayList<BookFile> mFilePathList = new ArrayList<BookFile>();
		mFilePathList.add(new BookFile(new File("")));
		File mFile = new File(filePath);
		File[] mFiles = mFile.listFiles();// 遍历出该文件夹路径下的所有文件/文件夹
		if (!filePath.equals("/")) {
			mFilePathList.add(new BookFile(new File("")));
		}else{
		}
		/* 将所有文件信息添加到集合中 */
		for (File mCurrentFile : mFiles) {
			if (mCurrentFile.canRead()) {// 判断是否可读 判断是否可读 判断是否可读
//					mFileName.add(mCurrentFile.getName());
				mFilePathList.add(new BookFile(mCurrentFile));
			}
		}
		/* 适配数据 */
		
		return mFilePathList;

	}
	public void TestRun() {
		BooksFileDB dbManager = DataManagerFactory.getDBManager(getContext());
		dbManager.query("f_time", "1463375043377");
		String query = dbManager.query("_id", "3");
		String fileById = dbManager.getBookById("3");
		String string = JSONUtils.getString(fileById, "f_path", "");
		try {
			BookFile jsonToObj = JSONUtils.jsonToObj(fileById, BookFile.class);
			String json = JSONUtils.object2Json(jsonToObj);
			System.out.println(json);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void Test01(){
		Context context = getContext();
		BooksFileDB db = new BooksFileDB(context);
//		db.deleteDb();
		long currentTimeMillis = System.currentTimeMillis();
		BookFile file = new BookFile();
		file.f_name="A1";
		file.f_path = "path2";
		file.f_size = 100;
		file.f_read_pager = 200;
		file.f_type = 1;
		file.f_arr = "arr";
		file.f_time = 2345676543l;
		
//		db.intsert(new String[]{"f_name","f_path","f_size","f_readPos","f_type","f_arr","f_time"}, 
//				new String[]{file.f_name,file.f_path,file.f_size+"",file.f_readPos+"",file.f_type+"",file.f_arr,file.f_time+""});
		db.insterOrUpdate(file);
		
		BooksFileDB dbManager = DataManagerFactory.getDBManager(context);
		List<String> queryAll = db.queryAllBook();
		try {
			List<BookFile> jsonListToObj = dbManager.jsonListToObj(queryAll, BookFile.class);
			System.out.println(jsonListToObj);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String fileById = db.getBookById("1");
		String s = db.getFileByName("A1");
		System.out.println(queryAll.toString());
		
	}
	public void TestDel(){
		Context context = getContext();
		BooksFileDB db = new BooksFileDB(context);
		db.deleteDb();
	}

}
