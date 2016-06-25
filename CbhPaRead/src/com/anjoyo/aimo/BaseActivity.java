package com.anjoyo.aimo;

import android.app.Activity;
import android.view.View;

/**
 * @ClassName:     BaseActivity.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2016年5月17日 下午5:29:25 
 * @Description:  所有Activity的父类
 */
public class BaseActivity extends Activity{
	public <T extends View> T findView(int layoutId) {
		return (T) findViewById(layoutId);
	}
}
