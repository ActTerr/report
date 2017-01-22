package mac.yk.report;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-yk on 2017/1/22.
 */

public class MyApplication extends Application {
    private static Context context;
    private static List<Activity> list;
    @Override
    public void onCreate() {
        super.onCreate();
        list=new ArrayList<>();
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
    public static void addActivity(Activity activity){
        list.add(activity);
    }
    public static void rmActivity(Activity activity){
        list.remove(activity);
    }
    public static void finishAll(){
        for(Activity a:list){
            a.finish();
        }
    }
}
