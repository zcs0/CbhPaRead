package anjoyo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.anjoyo.util.CharsetDetector;
/**
 * 修改阅读界面Canvas的工具类
 * @author Administrator
 * setTypeface(Typeface.DEFAULT_BOLD)</br>
 *  Typeface.DEFAULT //常规字体类型</br>
  * Typeface.DEFAULT_BOLD //黑体字体类型</br>
  * Typeface.MONOSPACE //等宽字体类型</br>
  * Typeface.SANS_SERIF //sans serif字体类型</br>
  * Typeface.SERIF //serif字体类型</br></br>
  * 
  * 粗体：</br>
	Paint mp = new Paint();</br>
	Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);</br>
	p.setTypeface( font );</br>
  * Typeface.BOLD //粗体</br>
  * Typeface.BOLD_ITALIC //粗斜体</br>
  * Typeface.ITALIC //斜体</br>
  * Typeface.NORMAL //常规</br>
  * mp.setFakeBoldText(true); //true为粗体，false为非粗体</br>
	mp.setTextSkewX(-0.5f); //float类型参数，负数表示右斜，整数左斜</br>
	mp.setUnderlineText(true); //true为下划线，false为非下划线</br>
	mp.setStrikeThruText(true); //true为删除线，false为非删除线</br>
	p.setStrokeWidth(w); //设置线宽，float型，如2.5f，默认绘文本无需设置（默认值好像为0）</br>
 */
public class BookPageFactory {
	private int mHeight, mWidth;
	private int mVisibleHeight, mVisibleWidth;
	/**
	 * 一页的行数
	 */
	private int mPageLineCount;
	/**
	 * 行间距
	 */
	private int mLineSpace = 2;
	/**
	 * 文件的长度
	 */
	private int m_mpBufferLen;
	/**
	 * 开始位置
	 */
	private int m_mbBufBeginPos =0;
	private MappedByteBuffer m_mpBuff;
	private int m_mbBufEndPos = 0;
	private Paint mPaint;
	private int margingHeight = 30;
	private int margingWeight = 30;
	private int mFontSize = 30;
	String charsetName = "utf-8";
	private Bitmap m_book_bg;
	private Vector<String> m_lines = new Vector<String>();
	/**
	 * 每一页
	 */
	private List<Vector> listPager = new ArrayList<Vector>();
	/**
	 * 设置整页的参数
	 * @param w 要显示的宽
	 * @param h 要显示的高
	 * @param fontsize 字体大小
	 * @param mLineSpace 行间距
	 * @param color 颜色
	 */
	public BookPageFactory(int w, int h, int fontsize,int mLineSpace,int color){	
		mWidth = w;
		mHeight = h;
		this.mLineSpace = mLineSpace;
		mFontSize = fontsize;
		mVisibleHeight = mHeight - margingHeight*2 - mFontSize;
		mVisibleWidth = mWidth -margingWeight*2;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(mFontSize);
		mPaint.setColor(color);
//		mPaint.setColor(Color.BLACK);
		mPageLineCount = (int)mVisibleHeight/(mFontSize+2);
	}
	public void openBook(String path, int[] position) throws FileNotFoundException, IOException{
		listPager.clear();
		charsetName = CharsetDetector.getEncode(new FileInputStream(path));//获得编码
		File file = new File(path);
		long length = file.length();
		m_mpBufferLen = (int)length;
		m_mpBuff = new RandomAccessFile(file, "r"). getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
		int conut = 0;
		while (m_mbBufEndPos<m_mpBufferLen) {
			Vector<String> pageDown = pageDown();
			listPager.add(pageDown);
		}
		m_mbBufEndPos = position[1];
		m_mbBufBeginPos = position[0];
	}
	/**
	 * 
	 * @param canvas
	 * @param pagerIndex
	 */
	public void onDrowToPager(Canvas canvas,int pagerIndex){
		pagerIndex = listPager==null||pagerIndex>=listPager.size()?0:pagerIndex;
		m_lines = listPager.get(pagerIndex);
		if(m_lines.size()==0){
			m_lines = pageDown();
		}
		if(m_lines.size()>0){
			int y = margingHeight;
			if(m_book_bg != null){
				Rect rectF = new Rect(0, 0, mWidth, mHeight);
				canvas.drawBitmap(m_book_bg, null, rectF, null);//背景图片
			}else{
				canvas.drawColor(Color.WHITE);
			}
			for(String line : m_lines){
				y+=mFontSize+mLineSpace;
				canvas.drawText(line, margingWeight, y, mPaint);
			}
			float persent =(1+pagerIndex)*100f/listPager.size();
			DecimalFormat strPersent  = new DecimalFormat("#0.00");
			int strLen = (int)mPaint.measureText(strPersent.format(persent));
			canvas.drawText(strPersent.format(persent) + "%", (mWidth-strLen)/2, mHeight-margingHeight, mPaint);
		}
		
	
	}
	
	public void onDrow(Canvas canvas){
		if(m_lines.size()==0){
			m_mbBufEndPos = m_mbBufBeginPos;
			m_lines = pageDown();
		}
		if(m_lines.size()>0){
			int y = margingHeight;
			if(m_book_bg != null){
				Rect rectF = new Rect(0, 0, mWidth, mHeight);
				canvas.drawBitmap(m_book_bg, null, rectF, null);//背景图片
			}else{
				canvas.drawColor(Color.WHITE);
			}
			for(String line : m_lines){
				y+=mFontSize+mLineSpace;
				canvas.drawText(line, margingWeight, y, mPaint);
			}
			float persent = (float)m_mbBufBeginPos*100/m_mpBufferLen;
			DecimalFormat strPersent  = new DecimalFormat("#0.00");
			int strLen = (int)mPaint.measureText(strPersent.format(persent));
			canvas.drawText(strPersent.format(persent) + "%", (mWidth-strLen)/2, mHeight-margingHeight, mPaint);
		}
		
	}
	/**
	 * 
	 */
	private void pageUp() {
		// TODO Auto-generated method stub
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while((lines.size() < mPageLineCount) && (m_mbBufBeginPos > 0)){
			Vector<String> paraLines = new Vector<String>();

			byte[] parabuffer = readParagraphBack(m_mbBufBeginPos);
			
			m_mbBufBeginPos -= parabuffer.length;
			try {
				strParagraph = new String(parabuffer, charsetName);
//				Log.d("xjd", "strParagraph"+strParagraph);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "  ");
			strParagraph = strParagraph.replaceAll("\n", " ");

			while(strParagraph.length() > 0){
				int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				paraLines.add(strParagraph.substring(0, paintSize));
				strParagraph = strParagraph.substring(paintSize);
			}
			lines.addAll(0, paraLines);
			
			while (lines.size() > mPageLineCount) {
				try {
					m_mbBufBeginPos += lines.get(0).getBytes(charsetName).length;
					lines.remove(0);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			m_mbBufEndPos = m_mbBufBeginPos;
		}
	}	
	/**
	 * 下一页
	 * @return
	 */
	private Vector<String> pageDown() {
		// TODO Auto-generated method stub
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while((lines.size() < mPageLineCount) && (m_mbBufEndPos < m_mpBufferLen)){//已记录的行数<本页的行数 && 结束位置小于最大长度
			byte[] parabuffer = readParagraphForward(m_mbBufEndPos);
			m_mbBufEndPos += parabuffer.length;
			try {
				strParagraph = new String(parabuffer, charsetName);
//				Log.d("xjd", strParagraph);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "  ");
			strParagraph = strParagraph.replaceAll("\n", " ");

			while(strParagraph.length() > 0){
				int paintSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				lines.add(strParagraph.substring(0, paintSize));
				strParagraph = strParagraph.substring(paintSize);
				if(lines.size() >= mPageLineCount){
					break;
				}
			}
			if(strParagraph.length()!=0){
				try {
					m_mbBufEndPos -= (strParagraph).getBytes(charsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return lines;
	}
	/**
	 * 向前读取段
	 * @param m_mbBufPos
	 * @return
	 */
	private byte[] readParagraphForward(int m_mbBufPos) {
		// TODO Auto-generated method stub
		byte b0, b1 ;
		int i = m_mbBufPos;
		while(i < m_mpBufferLen){
			b0 = m_mpBuff.get(i++);
			if(b0 == 0x0a){
				break;
			}
		}		
		int nParaSize = i - m_mbBufPos;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mpBuff.get(m_mbBufPos + i);
		}
		return buf;
	}
	/**
	 * 读取第几段
	 * @param m_mbBufBeginPos
	 * @return
	 */
	private byte[] readParagraphBack(int m_mbBufBeginPos) {
		// TODO Auto-generated method stub
		byte b0, b1 ;
		int i = m_mbBufBeginPos -1 ;
		while(i > 0){
			b0 = m_mpBuff.get(i);
			if(b0 == 0x0a && i != m_mbBufBeginPos -1 ){
				i++;
				break;
			}
			i--;
		}		
		int nParaSize = m_mbBufBeginPos -i ;
		byte[] buf = new byte[nParaSize];
		for (int j = 0; j < nParaSize; j++) {
			buf[j] = m_mpBuff.get(i + j);
		}
		return buf;
	}
	/**
	 * 下一页
	 */
	public void nextPage() {
		// TODO Auto-generated method stub
		if(m_mbBufEndPos >= m_mpBufferLen){//如查结束位置大小开始位置
			return;
		}else{
			m_lines.clear();
			m_mbBufBeginPos = m_mbBufEndPos;
			m_lines = pageDown();
		}
	}
	/**
	 * 上一页
	 */
	public void prePage() {
		// TODO Auto-generated method stub
		if(m_mbBufBeginPos<=0){
			return;
		}
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}
	/**
	 * 获得当前位置
	 * @return
	 */
	public int[] getPosition(){
		int[] a = new int[]{m_mbBufBeginPos, m_mbBufEndPos};
		return a;
	}
	/**
	 * 设置字体大小
	 * @param fontsize
	 */
	public void setTextFont(int fontsize) {
		// TODO Auto-generated method stub
		mFontSize = fontsize;
		mPaint.setTextSize(mFontSize);
		mPageLineCount = mVisibleHeight/(mFontSize+mLineSpace);
		m_mbBufEndPos = m_mbBufBeginPos;
//		nextPage();
	}
	/**
	 * 获得字体大小
	 * @return
	 */
	public int getTextFont() {
		// TODO Auto-generated method stub
		return mFontSize;
	}
	/**
	 * 显示到第几页
	 * @param persent
	 */
	public void setPersent(int persent) {
//		if(listPager!=null){
//			if(listPager.size()>persent){
//				drow
//			}
//		}
		
		
//		System.out.println("第  "+persent+"  页");
//		for (int i = 0; i < persent; i++) {
//			nextPage();
//		}
//		
		// TODO Auto-generated method stub
//		float a =  (float)(m_mpBufferLen*persent)/100;
//		m_mbBufEndPos = (int)a;
//		System.out.println("1 m_mbBufBeginPos "+m_mbBufBeginPos+"       m_mbBufEndPos "+m_mbBufEndPos);
//		if(persent==0){
//			m_mbBufBeginPos=0;
//			m_mbBufEndPos = 0;
//		}
//		nextPage();
//		if(m_mbBufEndPos == 0){
//			nextPage();
//		}else{
//			nextPage();
//			System.out.println("2 m_mbBufBeginPos "+m_mbBufBeginPos+"       m_mbBufEndPos "+m_mbBufEndPos);
//			prePage();
//			System.out.println("3 m_mbBufBeginPos "+m_mbBufBeginPos+"       m_mbBufEndPos "+m_mbBufEndPos);
//			nextPage();
//			System.out.println("4 m_mbBufBeginPos "+m_mbBufBeginPos+"       m_mbBufEndPos "+m_mbBufEndPos);
//		}
	}
	public void setBgBitmap(Bitmap BG) {
		m_book_bg = BG;
	}
	/**
	 * 获得所有页
	 * @return
	 */
	public List<Vector> getListPager() {
		return listPager;
	}
	public void setListPager(List<Vector> listPager) {
		this.listPager = listPager;
	}
	
	
	
}
