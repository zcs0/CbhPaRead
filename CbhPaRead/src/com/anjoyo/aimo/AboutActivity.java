﻿package com.anjoyo.aimo;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;



public class AboutActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
	public void onBack(View v){
		finish();
	}
		
		
	}


