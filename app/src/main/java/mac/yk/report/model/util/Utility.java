package mac.yk.report.model.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mac.yk.report.model.bean.City;
import mac.yk.report.model.bean.County;
import mac.yk.report.model.bean.Province;
import mac.yk.report.model.db.weatherDao;

/**
 * Created by mac-yk on 2017/1/23.
 */

public class Utility {
    public synchronized static boolean handleProvincesResponse(weatherDao weatherDao,String response){
        if (!TextUtils.isEmpty(response)){
            String[] provinces=response.split(",");
            if (provinces.length>0){
                for (String p:provinces){
                    String[] array=p.split("\\|");
                    Province pro=new Province();
                    pro.setProvinceName(array[1]);
                    pro.setProvinceCode(array[0]);
                    weatherDao.saveProvince(pro);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCityResponse(weatherDao weatherDao,String response,int pID){
        if (!TextUtils.isEmpty(response)){
            String[] citys=response.split(",");
            if (citys.length>0){
                for (String s:citys){
                    String[] array=s.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(pID);
                    weatherDao.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountyResponse(weatherDao weatherDao,String response,int cID){
        if (!TextUtils.isEmpty(response)){
            String[] countys=response.split(",");
            if (countys.length >0){
                for (String s:countys){
                    String[] array=s.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cID);
                    weatherDao.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
    public static void handleWeatherResponse(Context context,String response,int index){
        int count=SpUtil.getDefault(context).getInt("count",0);
        if (index!=count-1){
            return;
        }
        try {
            JSONObject object=new JSONObject(response);
            JSONObject weatherInfo = object.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
                    weatherDesp, publishTime,index);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                        String temp1, String temp2, String weatherDesp,
                                        String publishTime,int index) {
        try {

            SimpleDateFormat format=new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
            SharedPreferences.Editor editor = SpUtil.getSp2((Activity) context,index).edit();
            editor.putBoolean("city_selected", true);
            editor.putString("city_name", cityName);
            editor.putString("weather_code", weatherCode);
            editor.putString("temp1", temp1);
            editor.putString("temp2", temp2);
            editor.putString("weather_desp", weatherDesp);
            editor.putString("publish_time", publishTime);
            editor.putString("current_date", format.format(new Date()));
            editor.apply();
            SharedPreferences sp = SpUtil.getSp(context, index);
            Log.e("main","save"+index);
            SpUtil.putValue(sp,cityName, temp1+temp2);
        }catch (Exception e){

            SharedPreferences sharedPreferences = SpUtil.getDefault(context);
            int count = sharedPreferences.getInt("count", 0);
            sharedPreferences.edit().putInt("count",count-1);
            Activity activity= (Activity) context;
            activity.finish();
            LogUtil.e("main",e.toString());
        }

    }
}
