package mac.yk.report.WeatherDetail;

import android.content.Context;
import android.support.annotation.NonNull;

import mac.yk.report.model.Model;
import mac.yk.report.util.Utility;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mac-yk on 2017/2/4.
 */

public class WeatherDetailPresenter implements WeatherDetailContract.Presenter {
    WeatherDetailContract.view view;
    int index;
    Context mContext;
    Model model;
    public WeatherDetailPresenter(@NonNull WeatherDetailContract.view view, @NonNull int index, Context context) {
        this.view = checkNotNull(view);
        this.index=checkNotNull(index);
        mContext=context;
        model=new Model(this);

    }

    @Override
    public void queryWeatherCode(String countyCode) {
        model.queryWeatherCode(countyCode);
    }

    @Override
    public void queryFromServer(String address, final String countyCode) {
        model.queryFromServer(address,countyCode);
    }

    @Override
    public void queryWeatherInfo(String weatherCode) {
      model.queryWeatherInfo(weatherCode);
    }

    @Override
    public void handleResponse(String response) {
        Utility.handleWeatherResponse(mContext,response,index);
    }

    @Override
    public void pusherror() {
        view.publishSetText("同步失败");
    }

    @Override
    public void showWeather() {
        view.showWeather();
    }

    @Override
    public void start() {

    }

}
