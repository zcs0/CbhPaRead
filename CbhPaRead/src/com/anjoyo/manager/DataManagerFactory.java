package com.anjoyo.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @ClassName: DataManagerFra.java
 * @author zcs
 * @version V1.0
 * @Date 2016年5月13日 下午6:00:13
 * @Description: 数据查询提供者
 */
public class DataManagerFactory {
	private static BooksFileDB fileDb;
	private static Context mContext;
	private static SharedPreferences sp;
	public final static String books_font_color="books_font_color";
	public final static String books_font_line_spacing = "books_font_line_spacing";
	public final static String isOperSaveLog = "oper_save_log";
	private final static String sp_name="sp_default_config";
	public final static String last_flie_id="lastFile";
	public final static String last_open_sd_path="last_open_sd_path";
	public final static String books_font_size="books_font_size";
	public final static String books_font_spacing="books_font_spacing";

	public static BooksFileDB getDBManager(Context mContext) {
		fileDb = fileDb != null ? fileDb : new BooksFileDB(mContext);
		return fileDb;
	}
	public static SharedPreferences getSP(Context mContext) {
		return getSP(mContext,sp_name);
	}
	public static SharedPreferences getSP(Context mContext,String spName) {
		sp = sp!=null?sp:mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
		return sp;
	}
	

}
