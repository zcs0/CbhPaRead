package com.anjoyo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.text.TextUtils;
import entity.BookFile;

/**
 * @ClassName:     ListSortUtil.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2016年5月16日 下午4:05:42 
 * @Description:   BookFileList排序
 */
public class ListSortUtil {
	public final static void sortBookList(List<BookFile> list){
		Collections.sort(list,new Comparator<BookFile>() {
			@Override
			public int compare(BookFile lhs, BookFile rhs) {
				
				if(TextUtils.isEmpty(lhs.f_name)){
					return 0;
				}
				if(TextUtils.isEmpty(rhs.f_name)){
					return 1;
				}
				char c1 = lhs.f_name.charAt(0);
				char c2 = rhs.f_name.charAt(0);
				if(c1<=90&&c1>=65){
					c1 +=32;
				}
				if(c2<=90&&c2>=65){
					c2 +=32;
				}
				int i = c1-c2;
				return i>0?1:(i==0?0:-1);
			}
		});
	}
	/**
	 * 文件和文件夹排序
	 * @param list
	 * @param file true:文件在后显示
	 * @return
	 */
	public final static List<BookFile>  sortBookList(List<BookFile> list,boolean file){
		List<BookFile> listFile = new ArrayList<BookFile>();
		if(file){
			for (BookFile bookFile : list) {
				if(new File(bookFile.f_path).isDirectory()){
					listFile.add(bookFile);
				}
			}
			for (BookFile bookFile : list) {
				if(new File(bookFile.f_path).isFile()){
					listFile.add(bookFile);
				}
			}
		}else{
			for (BookFile bookFile : list) {
				if(new File(bookFile.f_path).isFile()){
					listFile.add(bookFile);
				}
			}
			for (BookFile bookFile : list) {
				if(new File(bookFile.f_path).isDirectory()){
					listFile.add(bookFile);
				}
			}
		}
		
		return listFile;
	}
}
