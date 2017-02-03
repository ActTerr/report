package mac.yk.report.WeatherDetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import mac.yk.report.util.HttpCallbackListener;
import mac.yk.report.util.HttpUtil;
import mac.yk.report.util.Utility;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mac-yk on 2017/2/4.
 */

public class WeatherDetailPresenter implements WeatherDetailContract.Presenter {
    WeatherDetailContract.view view;
    int index;
    Context mContext;
    public WeatherDetailPresenter(@NonNull WeatherDetailContract.view view, @NonNull int index, Context context) {
        this.view = checkNotNull(view);
        this.index=checkNotNull(index);
        mContext=context;
    }

    @Override
    public void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" +
                countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    @Override
    public void queryFromServer(String address, final String countyCode) {
        HttpUtil.sendRequest(address, new HttpCallbackListener() {
            @Override
            public void finish(String response) {
                Log.e("main",response);
                if ("countyCode".equals(countyCode)){
                    if (!TextUtils.isEmpty(response)){
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(countyCode)){
                    Utility.handleWeatherResponse(mContext,response,index);
                    Log.e("main",response.toString());
                    view.showWeather();
                }
            }

            @Override
            public void error(Exception e) {
               view.publishSetText("同步失败");

            }
        });
    }

    @Override
    public void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    @Override
    public void start() {

    }
}
