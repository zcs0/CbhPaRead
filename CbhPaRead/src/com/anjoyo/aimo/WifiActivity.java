﻿package com.anjoyo.aimo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class WifiActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifics);
	}
	public void onBack(View v){
		finish();
	}

}
