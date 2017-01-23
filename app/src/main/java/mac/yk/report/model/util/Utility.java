package mac.yk.report.model.util;

import android.text.TextUtils;

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
}
