package mac.yk.report;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import mac.yk.report.model.util.HttpCallbackListener;
import mac.yk.report.model.util.HttpUtil;
import mac.yk.report.model.util.Utility;

/**
 * Created by mac-yk on 2017/1/24.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
       int t= sharedPreferences.getInt("time",0);
        int anHour=0;
        if (t==0){
             anHour= 8 * 60 * 60 * 1000;
        }else {
            anHour=t*60*60*1000;
        }

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i=new Intent(this,MyReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        Log.e("main","service start"+anHour);
        return super.onStartCommand(intent, flags, startId);

    }

    private void updateWeather() {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String code=sp.getString("weather_code","");
        String address = "http://www.weather.com.cn/data/cityinfo/" +
                code + ".html";
        HttpUtil.sendRequest(address, new HttpCallbackListener() {
            @Override
            public void finish(String response) {
                Utility.handleWeatherResponse(getApplicationContext(),response);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("main","service stop");
    }
}
