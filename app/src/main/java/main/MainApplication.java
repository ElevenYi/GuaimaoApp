package main;

import android.app.Application;

import com.tendcloud.tenddata.TCAgent;

/**
 * Created by eleven on 16/8/12.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TCAgent.LOG_ON = true;
        TCAgent.init(this,"25E0D925A8B8593AC69FA4B95A8BF126","official");
        TCAgent.setReportUncaughtExceptions(true);
    }
}
