package mac.yk.report.WeatherDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mac.yk.report.MyService;
import mac.yk.report.R;
import mac.yk.report.WeatherList.weatherListActivity;
import mac.yk.report.selectCity.MainActivity;
import mac.yk.report.util.LogUtil;
import mac.yk.report.util.SpUtil;

/**
 * Created by mac-yk on 2017/1/23.
 */

public class WeatherFragment extends Fragment implements View.OnClickListener,WeatherDetailContract.view{
    private Button btn1,btn2;
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    RelativeLayout layout,back;
    boolean flag;
    /**
     * 用于显示发布时间
     */
    private TextView publishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView temp1Text;
    /**
     * 用于显示气温2
     */
    private TextView temp2Text;
    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCity;
    /**
     * 更新天气按钮
     */
    private Button refreshWeather,goList;

    private EditText time;
    private Activity mContext;

    private int index;
    private WeatherDetailContract.Presenter presenter;
    public WeatherFragment(Context context,int index) {
        mContext= (Activity) context;
        this.index=index;
        presenter=new WeatherDetailPresenter(this,index,context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 初始化各控件
        Log.e("main","onCreateView"+index);
        View view = inflater.inflate(R.layout.activity_county, container,false);
        weatherInfoLayout = (LinearLayout) view.findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) view.findViewById(R.id.city_name);
        publishText = (TextView) view.findViewById(R.id.publish_text);
        weatherDespText = (TextView) view.findViewById(R.id.weather_desp);
        temp1Text = (TextView) view.findViewById(R.id.temp1);
        temp2Text = (TextView) view.findViewById(R.id.temp2);
        currentDateText = (TextView) view.findViewById(R.id.current_date);
        switchCity = (Button) view.findViewById(R.id.switch_city);
        refreshWeather = (Button) view.findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        btn1= (Button) view.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2= (Button) view.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        goList= (Button) view.findViewById(R.id.btn_goList);
        goList.setOnClickListener(this);
        layout= (RelativeLayout) view.findViewById(R.id.background);
        time= (EditText) view.findViewById(R.id.et_time);
        back= (RelativeLayout) view.findViewById(R.id.back);
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(mContext);
        flag=sp.getBoolean("flag",false);
        time.setText(sp.getInt("time",0)+"");
        if (flag){
            showstart();
        }else {
            showclose();
        }
        String countyCode = mContext.getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
// 有县级代号时就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            if (presenter!=null){
                presenter.queryWeatherCode(countyCode);
            }

        } else {
// 没有县级代号时就直接显示本地天气
            showWeather();
        }
        LogUtil.e("main","fg success");

        return view;
    }


    @Override
    public void publishSetText(final String s) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() { publishText.setText(s);
            } });
    }
    @Override
    public void showWeather() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = SpUtil.getSp2(mContext, index);
                cityNameText.setText( prefs.getString("city_name", ""));
                temp1Text.setText(prefs.getString("temp1", ""));
                temp2Text.setText(prefs.getString("temp2", ""));
                String weather=prefs.getString("weather_desp", "");
                if (weather.equals("晴")){
                    back.setBackground(getResources().getDrawable(R.drawable.a));
                }
                weatherDespText.setText(prefs.getString("weather_desp", ""));
                publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
                currentDateText.setText(prefs.getString("current_date", ""));
                weatherInfoLayout.setVisibility(View.VISIBLE);
                cityNameText.setVisibility(View.VISIBLE);
            }
        });

//        if (flag){
//            Intent intent=new Intent(mContext, MyService.class);
//            mContext.startService(intent);
//        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.switch_city:
                Log.e("main","dianjici");
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                mContext.finish();
                break;
            case R.id.refresh_weather:
                Log.e("main","dianjiwe");
                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.
                        getDefaultSharedPreferences(mContext);
                String weatherCode = prefs.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    presenter.queryWeatherInfo(weatherCode);
                }
                break;
            case R.id.btn1:
                showstart();
                flag=true;
                saveFlag(flag);
                int t= Integer.parseInt(time.getText().toString());
                SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(mContext);
                sp.edit().putInt("time",t).apply();
                break;
            case R.id.btn2:
                showclose();
                flag=false;
                saveFlag(flag);
                Intent in2=new Intent(mContext,MyService.class);
                mContext.stopService(in2);
                break;
            case R.id.btn_goList:
                Intent inte=new Intent(mContext,weatherListActivity.class);
                startActivity(inte);
                getActivity().finish();
            default:
                break;
        }
    }

    private void showclose() {
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.GONE);
        layout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void showstart() {
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    private void saveFlag(boolean flag) {
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putBoolean("flag",flag).commit();
    }




    @Override
    public void setPresenter(WeatherDetailContract.Presenter presenter) {

    }
}

