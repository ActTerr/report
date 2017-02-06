package mac.yk.report.model;

/**
 * Created by mac-yk on 2017/2/6.
 */

public interface IModel {
    void queryWeatherCode(String countyCode);
    void queryFromServer(String address, String s);
    void queryWeatherInfo(String weatherCode);
}
