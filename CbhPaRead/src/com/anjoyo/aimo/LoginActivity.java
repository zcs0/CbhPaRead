package com.anjoyo.aimo;

import com.anjoyo.net.ShuChengActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//账号为：123  密码为：zcx
public class LoginActivity extends Activity {

	EditText zhanghao, mima;
	Button lijilogin;
	CheckBox checkbox;

	// int check=2;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		zhanghao = (EditText) findViewById(R.id.loginedit);
		mima = (EditText) findViewById(R.id.passwordedit);
		Get();
	}

	public void ReturnClick(View v) {

		finish();
	}

	// 立即登录按钮
	public int LiGilogin(View v) {

		String name = zhanghao.getText().toString();
		String pwd = mima.getText().toString();
		if (name.equals("") || pwd.equals("")) {
			Toast.makeText(LoginActivity.this, "登录名或密码不能为空", 2000).show();
			return 0;
		}
		if (name.equals("123")&& pwd.equals("zcx")) {
			Intent intent = new Intent(LoginActivity.this,
					ShuChengActivity.class);
			startActivity(intent);
			finish();

		} else {
			Toast.makeText(this, "登录失败,请检查输入是否正确、网络是否开通", 2000).show();

		}
//		 /*保存数据
//		 * 获得share对象*/
//		 SharedPreferences share = getPreferences(Activity.MODE_PRIVATE);
//		 //获取edit对象
//		 SharedPreferences.Editor edit = share.edit();
//		 //对账号做永久性绑定
//		 edit.putString("zhanghao", this.zhanghao.getText().toString());
//		 if (checkbox.isChecked()==true) {
//		 edit.putBoolean("remember", true);
//		 edit.putString("upwd", this.mima.getText().toString());
//		
//		
//		 } else {
//		 edit.putBoolean("remember", false);
//		 edit.putString("upwd", "");
//		
//		 }
//		 edit.commit();
		return 0;

	}

	public void Get() {
		SharedPreferences sharedPre = getPreferences(Activity.MODE_PRIVATE);
		// 得到编辑框里的值
		zhanghao.setText(sharedPre.getString("zhanghao", ""));
		if (sharedPre.getBoolean("remember", true)) {
			mima.setText(sharedPre.getString("upwd", ""));
		} else {
			checkbox.setChecked(false);
		}

	}

	

}