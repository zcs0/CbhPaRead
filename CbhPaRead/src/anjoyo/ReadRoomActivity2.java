package anjoyo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anjoyo.aimo.R;
import com.aphidmobile.flip.FlipViewController;

public class ReadRoomActivity2 extends Activity {

	private static final String TAG = null;
	private FlipViewController flipView;
	/**
	 * 一页显示的总字数
	 */
	private int count2;
	private String readFile;
	//页数
	private int pagerNum;
	private int readIndex;//读取字的位置
	/**
	 * 每页总行数
	 */
	private int lineNum;
	/**
	 * 一行的字数
	 */
	int rowCount;
	private TextView tv;
	List<String> list = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		   requestWindowFeature(Window.FEATURE_NO_TITLE);
		   setContentView(R.layout.read_tv);
		   flipView = (FlipViewController) findViewById(R.id.flipView);
//		   flipView.set
//		   flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		   tv = new TextView(this);
		   TextView tv2 = (TextView) findViewById(R.id.tv_test);
		   tv2.measure(Integer.MAX_VALUE, Integer.MAX_VALUE);
		   int maxLines = tv2.getMaxLines();
		   	tv = new TextView(this);
			tv.setBackgroundColor(Color.BLUE);
			tv.setTextColor(Color.BLACK);
			float lineSpacingMultiplier = tv.getLineSpacingMultiplier();
			int lineHeight = (int) (tv.getLineHeight()+lineSpacingMultiplier+lineSpacingMultiplier);//行高
			Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
			int height = defaultDisplay.getHeight();//最高底
//			lineNum = height/lineHeight;//每页总行数
//			int divider = lineHeight*lineNum;//间距
			lineNum = height/lineHeight+2;//每页总行数
			
			float textSize = tv.getTextSize();//一个字的大小
			int width = defaultDisplay.getWidth();//最大宽度
			rowCount = (int) (width/textSize);//一行的字数
			count2 = rowCount*lineNum;//一页显示的字数
			
			readFile = getReadFile(new File(Environment.getExternalStorageDirectory()+"/test2.txt"));
			readFile = readFile.replaceAll("(\n)+", "\n");
			pagerNum = readFile.length()/count2;//页数
			for (int i = 0; i < pagerNum; i++) {
				readString(i);
			}
			
			Log.d(TAG, readFile);
			flipView.setBackgroundColor(Color.WHITE);
			flipView.setAdapter(new FlipAdapter(tv));
//			setContentView(flipView);
	        
	}
	class FlipAdapter extends BaseAdapter{
		private TextView tv ;
		public FlipAdapter(TextView tv){
			this.tv = tv;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			TextView tv = new TextView(ReadRoomActivity2.this);
			String readString = list.get(arg0);
//			String readString = readString(arg0);
//			String readString = returnStr(arg0, readFile);
			tv.setText(readString);
			if(arg0%2==0){
				tv.setBackgroundColor(Color.BLUE);
			}else{
				tv.setBackgroundColor(Color.CYAN);
			}
			return tv;
		}
		
	}
	private int getStrN(String fileString){
		if(null==fileString)return 0;
		fileString = fileString.replaceAll("(\n)+", "\n");
		int n = fileString.split("\n").length-1;
		int t = fileString.split("\t").length-1;
		return n;
		
	}
	private String readString(int postion){
		//readIndex = //开始位置
		if(postion==0){
			readIndex=0;
		}else{
			readIndex = readIndex - rowCount;
		}
		String subSequence = readFile.substring(readIndex, readIndex+count2);//(上一次开始的位置)
		System.out.println(subSequence.length()+"------------------------");
		int strN = getStrN(subSequence);//\n的个数
		int c =lineNum*rowCount;
		if(strN>0){
			c = (lineNum-strN/2)*rowCount;
		}
		
//		int c = (lineNum - (strN/2+1))*rowCount;//总行数-\n)*一行的字数---------(36-11)*25
		c=(lineNum-(strN/2+1))*rowCount+readIndex;
//		c=(lineNum-i)*rowCount+readIndex;
//		c = rowCount*(lineNum-6);//一页显示的字数
//		c = count2-7*rowCount;
		String substring = readFile.substring(readIndex, c);
		System.err.println("开始位置: "+readIndex+"             结束位置："+c+"  换行-"+strN);
		System.err.println("截取长度:"+substring.length()+": 页截取后 ---\n\t"+substring+"\n--------------------"+postion+"----------------------------");
		readIndex = c;
		list.add(substring);
		return substring;
	}
	private String readString2(int postion){
		//readIndex = //开始位置
		if(postion==0){
			readIndex=0;
		}else{
			readIndex = readIndex - rowCount;
		}
		String subSequence = readFile.substring(readIndex, count2*(postion+1));//一页显示的字(启始位置，一页字数)
		Log.w(TAG, subSequence.length() +" 页截取前 ---"+subSequence);
		int strN = getStrN(subSequence);//\n的个数
//		int s = subSequence.length()-strN*lineNum;
		int c = (lineNum - (strN/2+1))*rowCount;//总行数-\n)*一行的字数---------(36-11)*25
		c=(lineNum-(strN/2+1))*rowCount;
//		c = rowCount*(lineNum-6);//一页显示的字数
//		c = count2-7*rowCount;
		String substring = readFile.substring(readIndex, c);
		Log.w(TAG, substring.length()+": 页截取后 ---"+substring);
		readIndex = c;
		return substring;
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
					if(!readLine.equals("　　"))
					buffer.append(readLine+"\n");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}
	
	
	/*
	 * @param m 
	 * @param readstr
	 * <p>传递页数 m 获取到m页的字符串</p>
	 * <p>传递读取的所有字符串 readstr</p>
	 * */
	public String returnStr(int m,String readstr){
		m+=1;
		int linefont = rowCount;//每行字数 设置12
		int pageline = lineNum;//每页行数 设置15
		String returnstr = "";
		int strline = 0;
		int k = -1;//用于判断到第几个readline
		String arr[] = readstr.split("\n");
		for(int i = 0;i < arr.length;i++){
			String str = arr[i];
			strline += str.length()/linefont; 
			if(str.length()%linefont > 0){
				strline += 1;
			}
			if(strline%pageline != 0){//避免第一行是换行
				strline += 1;
			}else{
				if(strline/pageline >= m){//这里 是为了判断 加上/n后 正好这页结束
					k = i;
					break;
				}
			}
			if(strline/pageline >= m){
				k = i;
				break;
			}
		}
		int kline = 0;//截图页 当前行数
		for(int i = k;i < arr.length; i++){
			String str = arr[i];
			if(i == k){
				if(strline/pageline == m){
					
				}else{
					int num = str.length()/linefont; 
					if(str.length()%linefont != 0){
						num += 1;
					}
					returnstr = str.substring((num+m*pageline-strline)*linefont)+"\n";//加上换行
					kline = (strline - m*pageline) +1;
				}
			}else{
				int num = str.length()/linefont; 
				if(str.length()%linefont != 0){
					num += 1;
				}
				kline += num;
				if(kline >= pageline){
					if(kline == pageline){
						returnstr += str;
					}else{
						returnstr += str.substring(0, (num+pageline-kline)*linefont);
					}
					break;
				}else{
					returnstr += str+"\n";
				}
			}
		}
		return returnstr;
	}

}
