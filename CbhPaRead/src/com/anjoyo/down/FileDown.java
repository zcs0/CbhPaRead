package com.anjoyo.down;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;


public class FileDown {
    public String DownLoad(String path){
    	try {
			String line;
			StringBuilder sb=new StringBuilder();
			URL url=new URL(path);
			HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
			InputStream inputStream=httpURLConnection.getInputStream();
			BufferedReader bReader=new BufferedReader(new InputStreamReader(inputStream,"gb2312"));
			while ((line=bReader.readLine())!=null) {
				sb.append(line);
				
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
    	
    }
    //异步加载图片
//    public Drawable GetImgToIntent(String path){
//    	HttpURLConnection httpConnection=null;
//    	InputStream inputStream=null;
//    	BufferedReader bReader=null;
//    	try {
//			URL url=new URL(path);
//			httpConnection=(HttpURLConnection)url.openConnection();
//			inputStream=httpConnection.getInputStream();
//			BufferedInputStream buff = new BufferedInputStream(inputStream);
//			return Drawable.createFromStream(inputStream, null);
//			//return BitmapFactory.decodeStream(buff);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}
//		
//		
//    }
    
   //一次性加载图片 
    public Bitmap GetImgToIntent(String path){
    	HttpURLConnection httpConnection=null;
    	InputStream inputStream=null;
    	BufferedReader bReader=null;
    	try {
			URL url=new URL(path);
			httpConnection=(HttpURLConnection)url.openConnection();
			inputStream=httpConnection.getInputStream();
			BufferedInputStream buff = new BufferedInputStream(inputStream);
		//	return Drawable.createFromStream(inputStream, null);
			return BitmapFactory.decodeStream(buff);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}finally{
			httpConnection.disconnect();
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
    }
}
