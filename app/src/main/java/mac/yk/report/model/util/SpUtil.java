package mac.yk.report.model.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mac-yk on 2017/1/29.
 */

public class SpUtil {

    public static SharedPreferences getSp(Context context, int key){
        SharedPreferences sharedPreferences=context.getSharedPreferences("weather"+key,Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    public static int getTem(SharedPreferences sharedPreferences){
       return sharedPreferences.getInt("tem",100);

    }
    public static String getLoc(SharedPreferences sharedPreferences){
        return sharedPreferences.getString("loc","");
    }
    public static void putValue(SharedPreferences sharedPreferences,String loc ,int tem){
        sharedPreferences.edit().putInt("tem",tem).putString("loc",loc).apply();
    }
}
