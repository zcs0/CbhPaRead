package entity;

import java.io.File;
import java.io.Serializable;

/**
 * @ClassName:     FileEntity.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2016年5月14日 上午11:45:43 
 * @Description:   文件信息
 */
public class BookFile  implements Serializable{
	//"f_name","f_path","f_size","f_readPos","f_type","f_arr","f_time"
	public String _id;
	public String f_name;
	public String f_path;
	public long f_size;
	public long f_read_pager;
	public long f_pager_count;
	/** 1:打开，2:添加到列表 3:收藏 4:已删除*/
	public int f_type;
	public String f_arr;
	public long f_time;
	private File file;
	public BookFile(){};
	public BookFile(File file){
		this.file = file;
		f_name = file.getName();
		f_path = file.getPath();
		f_size = file.length();
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	
	

}
