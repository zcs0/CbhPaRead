package com.anjoyo.aimo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.anjoyo.manager.DataManagerFactory;

public class AoreActivity extends BaseActivity {
	CheckBox checkboxone, checkboxtwo, checkboxthree;
	Button huanyan;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aore_layout);
		TextView tv = findView(R.id.tv_size);
		SharedPreferences sp = DataManagerFactory.getSP(this);
		int sizeTxt = sp.getInt(DataManagerFactory.books_font_size, 35);
		tv.setText(sizeTxt + "");
		checkboxone = (CheckBox) findViewById(R.id.CheckBoxone);
		checkboxtwo = (CheckBox) findViewById(R.id.CheckBoxtwo);
		checkboxthree = (CheckBox) findViewById(R.id.CheckBoxthree);
		huanyan = (Button) findViewById(R.id.huanyuan);
		// 还原按钮
		huanyan.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkboxone.isChecked()) {
					checkboxone.setChecked(false);
				}
				if (checkboxtwo.isChecked()) {
					checkboxtwo.setChecked(false);
				}
				if (checkboxthree.isChecked()) {
					checkboxthree.setChecked(false);
				}
			}
		});
	}

}
