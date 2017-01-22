package mac.yk.report.model.db;

import android.content.Context;

import java.util.List;

import mac.yk.report.model.bean.City;
import mac.yk.report.model.bean.County;
import mac.yk.report.model.bean.Province;

/**
 * Created by mac-yk on 2017/1/22.
 */

public class weatherDao {
    public weatherDao(Context context){
        weatherDB.getInstance().onInt(context);
    }
    public void closeDB(){
        weatherDB.getInstance().CloseDB();
    }
    public void saveCity(City city){
        weatherDB.getInstance().saveCity(city);
    }
    public void saveProvince(Province province){
        weatherDB.getInstance().saveProvince(province);
    }
    public void saveCounty(County county){
        weatherDB.getInstance().saveCounty(county);
    }
    public List<City> loadCitys(int ProvinceID){
        return   weatherDB.getInstance().loadCities(ProvinceID);

    }
    public List<Province> loadProvince(){
        return weatherDB.getInstance().loadProvince();
    }
    public List<County> loadCounty(int cityId){
        return weatherDB.getInstance().loadCounties(cityId);
    }
}
