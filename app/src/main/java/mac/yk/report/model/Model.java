package mac.yk.report.model;

import android.text.TextUtils;
import android.util.Log;

import mac.yk.report.WeatherDetail.WeatherDetailContract;
import mac.yk.report.util.HttpCallbackListener;
import mac.yk.report.util.HttpUtil;

/**
 * Created by mac-yk on 2017/2/6.
 */

public class Model implements IModel {
    public Model(WeatherDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private WeatherDetailContract.Presenter presenter;
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
                    presenter.handleResponse(response);
                    Log.e("main",response.toString());
                    presenter.showWeather();
                }
            }

            @Override
            public void error(Exception e) {
                presenter.pusherror();


            }
        });
    }

    @Override
    public void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }


}
