package com.anjoyo.aimo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjoyo.manager.DataManagerFactory;

public class BasicsActivity extends BaseActivity {
	View btnFontSize, btnFontSpacing, btnLineSpacing;
	TextView tvFontSize, tvFontSpacing, lineSpacingView;
	int size = 26;
	String[] fontSizeArr = new String[30];
	String[] zijv = { "2", "4", "6", "8", "10", "12", "14", "16", "18", "20",
			"22" };
	String[] lineSparcing = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11" };
	// 颜色选择器
	Context context;
	private Button btnColorPicker;
	private TextView tvText, tvText2;

	private ColorPickerDialog dialog;
	private SharedPreferences fontInfo;

	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.default_setting_layout);
		fontInfo = DataManagerFactory.getSP(BasicsActivity.this);
		initViews();
		btnFontSize = findViewById(R.id.btn_font_size);
		btnFontSpacing = findViewById(R.id.btn_font_spacing);
		btnLineSpacing = findViewById(R.id.btn_line_spacing);
		tvText = (TextView) findViewById(R.id.tv_text);
		tvFontSize = (TextView) findViewById(R.id.tv_size);
		tvFontSpacing = (TextView) findViewById(R.id.zjh);
		lineSpacingView = (TextView) findViewById(R.id.line_spacing);
		int fontSize = fontInfo.getInt(DataManagerFactory.books_font_size, 35);
		tvFontSize.setText(fontSize+"");
		int fontspacing = fontInfo.getInt(DataManagerFactory.books_font_spacing, 2);
		tvFontSpacing.setText(fontspacing+"");
		int lineSpacing = fontInfo.getInt(DataManagerFactory.books_font_line_spacing, 2);
		lineSpacingView.setText(lineSpacing+"");
		fontSizeArr[0]="18";
		for (int i = 0; i < fontSizeArr.length; i++) {
			int size = Integer.valueOf(fontSizeArr[i==0?i:i-1])+2;
			fontSizeArr[i] = String.valueOf(size);
		}
		
		// 字号的监听事件
		btnFontSize.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder dialog = new AlertDialog.Builder(BasicsActivity.this);
				dialog.setTitle("字号");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setItems(fontSizeArr, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						String xiabiao = fontSizeArr[arg1];
						tvFontSize.setText(xiabiao);
						saveSettings(DataManagerFactory.books_font_size,
								Integer.valueOf(xiabiao));
					}
				});
				dialog.setNegativeButton("取消", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				dialog.show();

			}
		});
		// 字距的监听事件
		btnFontSpacing.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder dialog = new AlertDialog.Builder(BasicsActivity.this);
				dialog.setTitle("字距");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setItems(zijv, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						String xiabiao = zijv[arg1];
						tvFontSpacing.setText(xiabiao);
						saveSettings(DataManagerFactory.books_font_spacing,
								Integer.valueOf(xiabiao));

					}
				});
				dialog.setNegativeButton("取消", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				dialog.show();
			}
		});
		// 行距的监听
		btnLineSpacing.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder dialog = new AlertDialog.Builder(BasicsActivity.this);
				dialog.setTitle("行距");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setItems(lineSparcing, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						String xiabiao = lineSparcing[arg1];
						lineSpacingView.setText(xiabiao);
						saveSettings(DataManagerFactory.books_font_line_spacing,
								Integer.valueOf(xiabiao));
					}
				});
				dialog.setNegativeButton("取消", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				dialog.show();
			}
		});
	}

	// 设置按钮与字体的同步变化-颜色
	private void initViews() {
		btnColorPicker = (Button) findViewById(R.id.btn_color_picker);
		
		final ImageView ivColor = (ImageView) findViewById(R.id.iv_color);
		int color = fontInfo.getInt(DataManagerFactory.books_font_color, Color.BLACK);
		if(tvText!=null){
			
			tvText.setTextColor(color);
		}
		ivColor.setBackgroundColor(color);
		btnColorPicker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = new ColorPickerDialog(context, tvText.getTextColors()
						.getDefaultColor(), getResources().getString(
						R.string.btn_color_picker),
						new ColorPickerDialog.OnColorChangedListener() {

							@Override
							public void colorChanged(int color) {
								saveSettings(DataManagerFactory.books_font_color, color);
								tvText.setTextColor(color);
								ivColor.setBackgroundColor(color);
							}
						});
				dialog.show();
			}
		});
		

	}

	// 字型按钮跳转
	public void ZiXing(View v) {
		Intent in = new Intent(BasicsActivity.this, ZiXingActivity.class);
		startActivity(in);
	}
	private void saveSettings(String booksFontSpacing,
			Integer valueOf) {
		Editor edit = fontInfo.edit();
		edit.putInt(booksFontSpacing, valueOf).commit();
		
	}

}
