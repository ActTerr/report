package mac.yk.report.selectCity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mac.yk.report.R;
import mac.yk.report.WeatherDetail.WeatherDetailActivity;
import mac.yk.report.base.BaseActivity;
import mac.yk.report.model.db.weatherDao;
import mac.yk.report.util.LogUtil;
import mac.yk.report.util.SpUtil;

public class MainActivity extends BaseActivity implements selectContract.View{
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    boolean isFromWeatherActivity;


    private selectContract.Presenter presenter;
    private List<String> dataList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        if (prefs.getBoolean("city_selected", false)
                && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherDetailActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        titleText= (TextView) findViewById(R.id.title_text);
        listView= (ListView) findViewById(R.id.list_view);
        dataList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        presenter=new selectPresenter(new weatherDao(getApplicationContext()),this);
        //不能传Activity的context,如果finish之后presenter里没处理完，就会导致内存泄漏
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.query(position);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void closeProgressDialog() {
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    @Override
    public void showProgressDialog() {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.setMessage("正在加载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        int currentLevel=presenter.getCurrentLevel();
        if (currentLevel == LEVEL_COUNTY) {
            presenter.queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            presenter.queryProvinces();
        } else {
            if (isFromWeatherActivity) {
                    Intent intent = new Intent(this, WeatherDetailActivity.class);
                    startActivity(intent);
                }
                finish();

        } }

    @Override
    public void showError(Exception e) {
        closeProgressDialog();
        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSelection(final int i) {
        sel(i);


    }
    private void sel(int i){
        listView.setSelection(i);
    }
    private void change(){
        adapter.notifyDataSetChanged();
    }
    @Override
    public void refresh(List<String> list) {
        dataList=list;
        LogUtil.e("main",dataList.toString());
        change();

    }


    @Override
    public void setText(String s) {
        titleText.setText(s);
    }

    @Override
    public void gotoWeatherDetail(String countyCode) {
        Intent intent = new Intent(MainActivity.this,
                WeatherDetailActivity.class);
        intent.putExtra("county_code", countyCode);
        SharedPreferences sp = SpUtil.getDefault(getApplicationContext());
        int count = sp.getInt("count", 0);
        sp.edit().putInt("count", count + 1).apply();
        startActivity(intent);
        LogUtil.e("main", "已经启动");


        finish();
    }

    @Override
    public void setPresenter(selectContract.Presenter presenter) {
        this.presenter=presenter;
    }
}
