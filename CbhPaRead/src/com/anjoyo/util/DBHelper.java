package com.anjoyo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ClassName: DBHelper.java
 * @Description: 数据库操作类
 */
public class DBHelper extends SQLiteOpenHelper {
    static String DATA_BASE_NAME = "read.db";
    public static String file_table = "db_file_list";//保存从服务器得到的列表
    String[] colName;

    /**
     * 列表名
     * 
     * @param context
     * @param colName
     */
    public DBHelper(Context context, String[] colName) {
        super(context, DATA_BASE_NAME, null, 1);// 数据库名
        this.colName = colName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        // 创建表1
        sb.append("CREATE TABLE ").append(file_table).append("(")
                .append("_id integer primary key autoincrement,");
        for (int i = 0; i < colName.length; i++) {
            sb.append(colName[i]);
            sb.append(i == colName.length - 1 ? ");" : ",");
        }
        String sql = sb.toString();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // LogUtils.w("更新数据库");
    }

    // 删除数据库
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATA_BASE_NAME);
    }

}
