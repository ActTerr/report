package mac.yk.report.selectCity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mac.yk.report.model.bean.City;
import mac.yk.report.model.bean.County;
import mac.yk.report.model.bean.Province;
import mac.yk.report.model.db.weatherDao;
import mac.yk.report.util.HttpCallbackListener;
import mac.yk.report.util.HttpUtil;
import mac.yk.report.util.Utility;

import static com.google.common.base.Preconditions.checkNotNull;
import static mac.yk.report.selectCity.MainActivity.LEVEL_CITY;
import static mac.yk.report.selectCity.MainActivity.LEVEL_COUNTY;
import static mac.yk.report.selectCity.MainActivity.LEVEL_PROVINCE;

/**
 * Created by mac-yk on 2017/2/3.
 */

public class selectPresenter implements selectContract.Presenter {
    private final weatherDao wdao;

    private final selectContract.View mview;

    public selectPresenter(@NonNull weatherDao dao, @NonNull selectContract.View view) {
        wdao = checkNotNull(dao, "dao not null");
        mview = checkNotNull(view, "view not null");
        mview.setPresenter(this); //构造时调用set方法，将presenter传给view
    }

    private List<String> dataList = new ArrayList<String>();

    private int currentLevel;

    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;

    @Override
    public void start() {
        queryProvinces();
    }


    @Override
    public void query(int position) {
        if (currentLevel == LEVEL_PROVINCE) {
            selectedProvince = provinceList.get(position);
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            selectedCity = cityList.get(position);
            queryCounties();
        } else if (currentLevel == LEVEL_COUNTY) {
            String countyCode = countyList.get(position).getCountyCode();
            mview.gotoWeatherDetail(countyCode);

        }
    }

    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public void queryProvinces() {
        provinceList = wdao.loadProvince();
        if (provinceList != null && provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            mview.refresh(dataList);
            mview.setSelection(0);
            mview.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
            Log.e("main", "zhixing");
        }
    }

    @Override
    public void queryCities() {
        cityList = wdao.loadCitys(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            mview.refresh(dataList);
            mview.setSelection(0);
            mview.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    @Override
    public void queryCounties() {
        countyList = wdao.loadCounty(selectedCity.getId());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            mview.refresh(dataList);
            mview.setSelection(0);
            mview.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }


    public void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        mview.showProgressDialog();
        HttpUtil.sendRequest(address, new HttpCallbackListener() {

            @Override
            public void finish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(wdao, response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(wdao,
                            response, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(wdao, response, selectedCity.getId());
                }
                if (result) {
                   mview.closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }

                }


            @Override
            public void error(Exception e) {
                mview.showError(e);
            }
        });
    }

}
