package mac.yk.report.WeatherDetail;

import mac.yk.report.base.BasePresenter;
import mac.yk.report.base.BaseView;

/**
 * Created by mac-yk on 2017/2/4.
 */

public interface WeatherDetailContract {
    interface view extends BaseView<Presenter>{
        void publishSetText(String s);
        void showWeather();
    }
    interface Presenter extends BasePresenter{
        void queryWeatherCode(String countyCode);
        void queryFromServer(String address, String s);
        void queryWeatherInfo(String weatherCode);
    }
}
