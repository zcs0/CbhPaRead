package com.anjoyo.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.anjoyo.util.DBHelper;
import com.anjoyo.util.JSONUtils;

import entity.BookFile;

/**
 * @ClassName:     FileDB.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2016年5月13日 下午6:02:17 
 * @Description:   数据库读取
 */
public class BooksFileDB {
	private Context mContext;
	private String db_file_list=DBHelper.file_table;
	private String orderBy="order by ?";
	DBHelper db;
	private String TAG ="FileDB";
	private String[] arrCols;
	private SQLiteDatabase sqlDb;
	public BooksFileDB(Context mContext){
		this.mContext = mContext;
		arrCols = new String[] {"f_name","f_path","f_size","f_read_pager","f_pager_count","f_type","f_arr","f_time"};
		db = new DBHelper(mContext, arrCols);
	}
	/**
	 * 插入一条数据
	 * @param colName
	 * @param value
	 */
	public void intsert(String[] colName,String[] value){
		String name = "(";
		String values = "values (";
		for (String string : colName) {//insert into person(name, age) values(?,?)
			name += string + ",";
			values += "?,";
		}
		name = name.substring(0, name.length()-1);
		values = values.substring(0, values.length()-1);
		String sql = "insert into "+db_file_list+" "+name +")"+values+")";
		sqlDb = db.getWritableDatabase();
		sqlDb.execSQL(sql, value);
//		close(sqlDb, null);
	}
	/**
	 * 查询所有
	 * @return
	 */
	public List<String> queryAllBook(){
		SQLiteDatabase sql = db.getWritableDatabase();
		Cursor rawQuery = sql.rawQuery("select * from "+db_file_list+" where f_type !=? " +orderBy+" desc ", new String[]{"4","f_time"});
		return queryCurson(rawQuery);
	}
	/**
	 * 查询所有包括打开过的
	 * @return
	 */
	public List<String> queryAllPast(){
		SQLiteDatabase sql = db.getWritableDatabase();
		Cursor rawQuery = sql.rawQuery("select * from "+db_file_list+" " +orderBy+" desc ", new String[]{"f_time"});
		return queryCurson(rawQuery);
	}
	private List<String> queryCurson(Cursor rawQuery){
		List<String> strArr = new ArrayList<String>();
		if(rawQuery==null||rawQuery.getCount() <= 0) return strArr;
		
		String[] columnNames = rawQuery.getColumnNames();
		while (rawQuery.moveToNext()) {
			JSONObject json = new JSONObject();
			String rowValue = "";
			for (String string : columnNames) {
				try {
					json.put(string, rawQuery.getString(rawQuery.getColumnIndex(string)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				rowValue += string+ ":"+ rawQuery.getString(rawQuery.getColumnIndex(string))+",";
			}
			rowValue = json.toString();
			Log.w(TAG , "query: "+rowValue);
			strArr.add(rowValue);
		}
		close(null, rawQuery);
		return strArr;
	}
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public String getBookById(String id){
		SQLiteDatabase sql = db.getWritableDatabase();
		Cursor rawQuery = sql.rawQuery("select * from "+db_file_list+" where _id = ? " +orderBy+" desc ", new String[]{id,"f_time"});
		List<String> queryCurson = queryCurson(rawQuery);
		close(sql, rawQuery);
		return queryCurson!=null&&queryCurson.size()>0?queryCurson.get(0):null;
	}
	/**
	 * 根据name查询
	 * @param name
	 * @return
	 */
	public String getFileByName(String name){
		return query("f_name", name);
	}
	/**
	 * 根据name 查询
	 * @param name
	 * @param value
	 * @return
	 */
	public String query(String name,String value){
		SQLiteDatabase sql = db.getWritableDatabase();
		Cursor rawQuery = sql.rawQuery("select * from "+db_file_list+" where "+name+" = ?  " +orderBy+" desc ", new String[]{value,"f_time"});
		List<String> queryCurson = queryCurson(rawQuery);
		close(sql, rawQuery);
		return queryCurson!=null&&queryCurson.size()>0?queryCurson.get(0):null;
	}
	/**
	 * 删除数据库
	 */
	public void deleteDb(){
		db.deleteDatabase(mContext);
	}
	/**
	 * 集合转对象
	 * @param queryAll
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	public <T> List<T>  jsonListToObj(List<String> queryAll, Class<T> classOfT) throws Exception{
		List<T> list = new ArrayList<T>();
		for (String string : queryAll) {
				T jsonToObj = JSONUtils.jsonToObj(string, classOfT);
				list.add(jsonToObj);
		}
		return list;
	}
	/**
	 * 一个转为对象
	 * @param json
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	public <T> T jsonToObj(String json,Class<T> classOfT)throws Exception{
		return JSONUtils.jsonToObj(json, classOfT);
	}
	
	private void close(SQLiteDatabase sql,Cursor rawQuery){
		if(sql!=null){
			sql.close();
		}
		if(rawQuery!=null){
			rawQuery.close();
		}
	}
	/**
	 * 插入一条
	 * @param file
	 */
	public String intsert(BookFile file){
		intsert(new String[]{"f_name","f_path","f_size","f_read_pager","f_pager_count","f_type","f_arr","f_time"}, 
				new String[]{file.f_name,file.f_path,file.f_size+"",file.f_read_pager+"",file.f_pager_count+"",file.f_type+"",file.f_arr,file.f_time+""});
		
		String query = query("f_path", file.f_path);
		
		return JSONUtils.getString(query, "_id", null);
		
	}
	/**
	 * 
	 * @param colName
	 * @param value
	 * @param id
	 * @return
	 */
	public int update(String[] colName,String[] value,String id){
		SQLiteDatabase sql = db.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0; i < colName.length; i++) {
			if(!TextUtils.isEmpty(colName[i]))
			values.put(colName[i], value[i]);
		}
		int code = sql.update(db_file_list, values, " _id = ?", new String[]{id});
		close(sql, null);
		return code;
	}
	/**
	 * 根据id更新
	 * @param file 
	 * @return
	 */
	public int update(BookFile file){
		return update(arrCols,new String[]{file.f_name,file.f_path,file.f_size+"",file.f_read_pager+"",file.f_pager_count+"",file.f_type+"",file.f_arr,file.f_time+""},file._id);
	}
	/**
	 * 删除一条
	 * @param id
	 */
	public void delete(String id){
		SQLiteDatabase sql = db.getWritableDatabase();
		String json = getBookById(id);
		String path = JSONUtils.getString(json, "f_path", null);
		if(!TextUtils.isEmpty(path)){
			if(!new File(path).isFile()){//没有这个文件
				sql.delete(db_file_list, " _id = ?", new String[]{id});
			}else{
				try {
					BookFile book = jsonToObj(json, BookFile.class);
					book.f_type = 4;
					book.f_read_pager = 0;
					update(book);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		close(sql, null);
	}
	/**
	 * 插入/更新一条数据(路径为空不插入)
	 * @param file
	 * @return
	 */
	public String insterOrUpdate(BookFile file){
		String id = null;
		if(file==null||TextUtils.isEmpty(file.f_path))return id;
		String query = query("f_path", file.f_path);
		if(!TextUtils.isEmpty(query)){
			update(file);
			id = JSONUtils.getString(query, "_id", null);
		}else{
			id = intsert(file);
		}
		return id;
		
	}
	/**
	 * 搜索，名字，或路径
	 * @param value
	 * @return
	 */
	public List<String> search(String value){
		SQLiteDatabase sql = db.getWritableDatabase();
		Cursor rawQuery = sql.rawQuery("select * from "+db_file_list+" where "+"f_name like '%"+value+"?%' or f_path like '%"+value+"%' and f_type !='4' " +orderBy+" desc ", new String[]{"f_time"});
		List<String> queryCurson = queryCurson(rawQuery);
		close(sql, rawQuery);
		return queryCurson;
	}
	
	public long insterList(List<BookFile> list){
		SQLiteDatabase sql = db.getWritableDatabase();
		long insert = 0;
		for (BookFile file : list) {
			insterOrUpdate(file);
//			ContentValues values = new ContentValues();
//			values.put(arrCols[0], file.f_name);
//			values.put(arrCols[1],file. f_path);
//			values.put(arrCols[2], file.f_size);
//			values.put(arrCols[3], file.f_read_pager);
//			values.put(arrCols[4], file.f_pager_count);
//			values.put(arrCols[5], file.f_type);
//			values.put(arrCols[6], file.f_arr);
//			values.put(arrCols[7], file.f_time);
//			insert += sql.insert(db_file_list, null,  values);
//			String json = JSONUtils.toJson(file);
//			Log.w(TAG , "insertList: "+json);
		}
		close(sql, null);
		return insert;
	}
	
	
	
}
