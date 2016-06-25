﻿package com.anjoyo.aimo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class YingyongActivity extends Activity{
	WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yingyong);
		webview=(WebView)findViewById(R.id.webview);
		webview.loadUrl("http://d.91.com/");
	}
	public void onBack(View v){
		finish();
	}
	public boolean isNetworkConnected(Context context){
		if (context!=null) {
			ConnectivityManager mConnectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo!=null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	private boolean isConnect() {
			  ConnectivityManager conManager = (ConnectivityManager)

			getSystemService(Context.CONNECTIVITY_SERVICE);

			  NetworkInfo networkInfo = conManager
			    .getActiveNetworkInfo();

			  if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错

			   return networkInfo
			     .isAvailable();

			  }

			  return false;

			 }
}
