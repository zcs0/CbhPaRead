package anjoyo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aphidmobile.flip.FlipViewController;

public class ReadRoomActivity3 extends Activity {

	private static final String TAG = null;
	private FlipViewController flipView;
	/**
	 * 一页显示的总字数
	 */
	private String readFile;
	//页数
	private int pagerNum;
	/**
	 * 读取字的位置
	 */
	private int readIndex;//读取字的位置
	/**
	 * 每页总行数
	 */
	private int lineNum;
	/**
	 * 一行的字数
	 */
	int rowCount;
	int screenHeight;
	/**
	 * 屏幕的宽度
	 */
	int screenWidth;
	int rowAllWidth = 35702;
	private TextView tv;
	List<String> list = new ArrayList<String>();
	private float textSize;
	private int count2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		   requestWindowFeature(Window.FEATURE_NO_TITLE);
		   flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		   tv = new TextView(this);
		   	tv = new TextView(this);
			tv.setBackgroundColor(Color.BLUE);
			tv.setTextColor(Color.BLACK);
			float lineSpacingMultiplier = tv.getLineSpacingMultiplier();
			int lineHeight = (int) (tv.getLineHeight()+lineSpacingMultiplier+lineSpacingMultiplier);//行高
			Display defaultDisplay = getWindow().getWindowManager().getDefaultDisplay();
			screenHeight = defaultDisplay.getHeight();//最高底
			screenWidth = defaultDisplay.getWidth();//最大宽度
			lineNum = screenHeight/lineHeight;//每页总行数
			textSize = tv.getTextSize();//一个字的大小
			rowCount = (int) (screenWidth/textSize);//一行的字数
			count2 = rowCount*lineNum;
			rowAllWidth = (int) (textSize*rowCount*lineNum);
			rowAllWidth = screenWidth*(lineNum-1);
			
			
			readFile = getReadFile(new File(Environment.getExternalStorageDirectory()+"/test2.txt"));
//			readFile = readFile.replaceAll("\n", "\n");
			pagerNum = readFile.length()/count2;//页数
			readString(1);
//			for (int i = 0; i < pagerNum; i++) {
//				readString(i);
//			}
			
			Log.d(TAG, readFile);
			flipView.setBackgroundColor(Color.WHITE);
			flipView.setAdapter(new FlipAdapter(tv));
			setContentView(flipView);
	        
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
			TextView tv = new TextView(ReadRoomActivity3.this);
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
		Paint pFont = new Paint();
		pFont.setTextSize(textSize);
		Rect rect = new Rect();
		String substring = "";
		while (rect.width()<=(rowAllWidth)) {//-5行
			String subSequence = readFile.substring(readIndex, rowCount+readIndex);
			substring += subSequence;
			pFont.getTextBounds(substring, 0, substring.length(), rect);
			readIndex += subSequence.length();
		}
		int leng = substring.split("\n").length-1;
		int i = rowCount*leng;
		int ff=0;
//		if(substring.indexOf("\n")!=-1){
//			String[] split = substring.split("\n");
//			for (int j = 0; j < split.length-1; j++) {
//				String string = split[j];
//				int width = (int) (textSize*rowCount);
//				pFont.getTextBounds(string, 0, string.length(), rect);
//				int f = rect.width();
//				ff += f%width;  
//			}
//		}
//		int result = (int) (ff/textSize);
//		result = 0;
		//substring = substring.substring(0, substring.length()-i+20);
		
		readIndex = substring.length();
		list.add(substring);
		return substring;
	}
	private String readString3(int postion){
		//rowAllWidth
		Paint pFont = new Paint();
		pFont.setTextSize(textSize);
		Rect rect = new Rect();
		String substring = "";
		while (rect.width()<=rowAllWidth) {
			String str = readFile.substring(substring.length(), substring.length()+1);
			if(str.equals("\n")){
				pFont.getTextBounds(substring, 0, substring.length(), rect);
				int width2 = rect.width();
				int width3 = (int) (rowCount*textSize);
				int i = width2%screenWidth;
				rowAllWidth -= (screenWidth - i);
			}
			substring += str;
			pFont.getTextBounds(substring, 0, substring.length(), rect);
		}
		readIndex = substring.length();
		list.add(substring);
		return substring;//35704-35700
	}
	
	private String getStr(String substring){//25个1022
		Paint pFont = new Paint();
		pFont.setTextSize(textSize);
		Rect rect = new Rect();
		pFont.getTextBounds(substring, 0, rowCount, rect);
		int width = rect.width();//
		//int oneWidth = width/rowCount;//一个所占用的宽度
		int a =(int) (rowCount*textSize*lineNum);//总宽度
		//lineNum
//		int allCount = 0;//
//		String ss = "";
		int n = 0;
		int i = 0;
		if(substring.indexOf("\n")!=-1){
			String[] split = substring.split("\n");
			i = split.length-1;
			for (String string : split) {
				int count = string.length()%rowCount;//最后一行显示的字数个数
				if(count!=0){
					n +=  (rowCount-count);
				}
				pFont.getTextBounds(string, 0, string.length(), rect);
				int width2 = rect.width();
				
//				pFont.getTextBounds(substring, string.length()-count, string.length(), rect);
//				int c = (int) (textSize*rowCount)-rect.width();
//				int n = rowCount-count;
//				int w = (int) (n*textSize);//空宽度
//				pFont.getTextBounds(substring, 0, string.length(), rect);
//				allCount +=(w+rect.width());
				
				
			}
		}
		
//		substring = substring.substring(0, allCount);
		
		
		System.out.println(width);
		substring = substring.substring(0, substring.length()-n+i);
		pFont.getTextBounds(substring, 0, substring.length(), rect);
		int width2 = rect.width();
		return substring;
	}
	private String getStr2(String substring){
		int Y =0;
		int width=0;//加\n后空的宽度
		int allWidth = 0;
		Paint pFont = new Paint();
		pFont.setTextSize(textSize);
		Rect rect = new Rect();
		int s = screenWidth*(lineNum);
		if(substring.indexOf("\n")!=-1){
			String[] split = substring.split("\n");
			
			for (int i = 0; i < split.length&&allWidth<s; i++) {
				String str = split[i];
				pFont.getTextBounds(str, 0, str.length(), rect);
				int fontWidth = rect.width();
				int n =  fontWidth%screenWidth;
				//计算\n后所占用的长度
				if(fontWidth>screenWidth){//计算一句话所占用空的长度
					width += screenWidth-n;
				}else{
					width += screenWidth-fontWidth;
				}
				allWidth +=fontWidth+width;//宽度 =  字宽度  + 空宽度
			}
		}
		pFont.getTextBounds(substring, 0, substring.length(), rect);
		int width2 = rect.width();
		int t = width2/substring.length();//一个字的宽度
		int count = width/t;//要排队的数字
		substring = substring.substring(0, substring.length()-count);
		return substring;
	}
	
	
	private String readString0(int postion){
		//readIndex = //开始位置
		if(postion==0){
			readIndex=0;
		}else{
			readIndex = readIndex - rowCount;
		}
		String subSequence = readFile.substring(readIndex, readIndex+count2);//(上一次开始的位置)
//		System.out.println(subSequence.length()+"------------------------");
		int strN = getStrN(subSequence);//\n的个数
		int c =(lineNum)*rowCount;
//		if(strN>0){
//			c = (lineNum-strN/2)*rowCount;
//		}
		c+=readIndex;
//		int lines = lineNum-(strN/2+1);
//		c=(lines)*rowCount+readIndex;
		String substring = readFile.substring(readIndex, c);
		System.err.println(postion+"   行数 "+lineNum+"   开始位置: "+readIndex+" 结束位置："+c+"  换行-"+strN+" 字数"+substring.length());
//		System.err.println("截取长度:"+substring.length()+": 页截取后 ---\n\t"+substring+"\n--------------------"+postion+"----------------------------");
		readIndex = c;
		list.add(substring);
		
		Paint pFont = new Paint();
		pFont.setTextSize(textSize);
		Rect rect = new Rect();
		pFont.getTextBounds(substring, 0, substring.length(), rect);
		
		
		Log.e(TAG, "height:"+rect.height()+"width:"+rect.width());
		
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
					if(!readLine.equals("　　")){
						if(TextUtils.isEmpty(readLine)){
							buffer.append(readLine);
						}else{
							buffer.append(readLine+"\n");
						}
					}
						
					
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
