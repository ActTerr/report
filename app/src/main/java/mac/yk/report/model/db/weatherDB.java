package mac.yk.report.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mac.yk.report.model.bean.City;
import mac.yk.report.model.bean.County;
import mac.yk.report.model.bean.Province;

/**
 * Created by mac-yk on 2017/1/22.
 */

public class weatherDB {

    private static weatherDB weatherDB = new weatherDB();
    private SQLiteDatabase db;
    private weatherOpenHelper openHelper;

    void onInt(Context context) {
        openHelper = weatherOpenHelper.onInit(context);

    }

    public static weatherDB getInstance() {
        return weatherDB;
    }

    public void CloseDB() {

        if (openHelper != null) {
            openHelper.closeDB();
        }
    }

    public void saveProvince(Province province) {
        db = openHelper.getWritableDatabase();
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    public void saveCity(City city) {
        db = openHelper.getWritableDatabase();
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }
    public List<Province> loadProvince(){
        db=openHelper.getReadableDatabase();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        List<Province> list=new ArrayList<>();
        while(cursor.moveToNext()){
            Province province=new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            list.add(province);
        }
        return list;
    }
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor
                        .getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor
                        .getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        } }

    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[] { String.valueOf(cityId) }, null, null, null); if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor
                        .getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor
                        .getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
