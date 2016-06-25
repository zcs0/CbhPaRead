package anjoyo;

import android.app.Application;
import android.content.SharedPreferences;

import com.anjoyo.manager.DataManagerFactory;

import error.log.CrashHandler;

/**
 * @ClassName:     App.java
 * @author         zcs
 * @version        V1.0  
 * @Date           2016年5月16日 下午1:58:03 
 * @Description:   
 */
public class App extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences sp = DataManagerFactory.getSP(this);
		boolean b = sp.getBoolean(DataManagerFactory.isOperSaveLog, false);
		if(b){//打开记录日志
			CrashHandler crashHandler = CrashHandler.getInstance();
			crashHandler.init(getApplicationContext());
		}
		
	}

}
