package mac.yk.report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mac-yk on 2017/1/24.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        boolean flag=sp.getBoolean("flag",false);
        if (flag){

            Intent intent1=new Intent(context, MyService.class);
            context.startService(intent1);
        }
    }
}
