package com.anjoyo.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.anjoyo.aimo.R;
import com.anjoyo.finalltxt.Finaltxt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class InterRead extends Activity{
	TextView bokshow;
	String IntertxtName;
	InterRead interRead;
	StringBuffer buffer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		interRead=this;
		setContentView(R.layout.interreadbook);
		bokshow=(TextView)findViewById(R.id.bookshow);
		Intent intent=getIntent();
		IntertxtName=intent.getStringExtra("txtName1");
		String a = interRead.opennerbook(Finaltxt.TXTPA+IntertxtName);
		bokshow.setText(a);
System.out.println("6666666666666"+a);
		
		
	}
	/**
	 * 打开网络书籍
	 * 
	 * @param strFilePath
	 * @return
	 */
	public String opennerbook(String strFilePath) {
		InputStream input = null;
		try {
			URL url = new URL(strFilePath);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			input = connection.getInputStream();

			// BufferedReader bReader=new BufferedReader(new
			// InputStreamReader(input,"gb2312"));
			buffer = new StringBuffer();
			InputStreamReader inputStreamReader = new InputStreamReader(input,
					"gb2312");

			BufferedReader inReader = new BufferedReader(inputStreamReader);
			while (inReader.ready()) {
				buffer.append(inReader.readLine() + "\n");
			}
			
			inReader.close();
System.out.println("55555555" + buffer.toString());


			return buffer.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
