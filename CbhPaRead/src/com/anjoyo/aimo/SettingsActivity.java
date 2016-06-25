﻿package com.anjoyo.aimo;




import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;





public class SettingsActivity extends TabActivity implements OnCheckedChangeListener{
	private TabHost mHost;
	private RadioGroup radioderGroup;
	RadioButton rjb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings_layout);
		mHost=this.getTabHost();
        mHost.addTab(mHost.newTabSpec("One").setIndicator("One")
        		.setContent(new Intent(this,BasicsActivity.class)));
        mHost.addTab(mHost.newTabSpec("Two").setIndicator("Two")
        		.setContent(new Intent(this,StyleActivity.class)));
        mHost.addTab(mHost.newTabSpec("Three").setIndicator("Three")
        		.setContent(new Intent(this,AoreActivity.class)));
        radioderGroup = (RadioGroup) findViewById(R.id.main_radiosz);
		radioderGroup.setOnCheckedChangeListener(this);
		//默认第一个按钮被选中
		rjb = (RadioButton)findViewById(R.id.jiben);
		rjb.setChecked(true);		
		
	}
	
	
	public void onShezhiBack(View v){
		finish();
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){
		case R.id.jiben:
			mHost.setCurrentTabByTag("One");
			break;
		case R.id.peise:
			mHost.setCurrentTabByTag("Two");
			break;
		case R.id.qite:
			mHost.setCurrentTabByTag("Three");
			break;
			}
		
	}
		
		
	}


