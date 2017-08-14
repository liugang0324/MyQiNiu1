package test.liugang.com.myqiniu;

import android.app.Application;

import com.qiniu.pili.droid.streaming.StreamingEnv;

/**
 * @ Description:
 * @ Date:2017/8/11
 * @ Author:刘刚
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StreamingEnv.init(getApplicationContext());
    }
}
