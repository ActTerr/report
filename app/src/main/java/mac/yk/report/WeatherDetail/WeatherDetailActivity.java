package mac.yk.report.WeatherDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import mac.yk.report.MyService;
import mac.yk.report.R;
import mac.yk.report.base.BaseActivity;
import mac.yk.report.util.LogUtil;
import mac.yk.report.util.SpUtil;
import mac.yk.report.widget.FlowIndicator;
import mac.yk.report.widget.MyViewPager;

/**
 * Created by mac-yk on 2017/1/28.
 */

public class WeatherDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    FlowIndicator fl;
    MyViewPager mv;
    MainTabAdpter adapter;
    int currentIndex;
    int count;
    boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        initView();
    }

    private void initView() {
        currentIndex = getIntent().getIntExtra("position", 0);
        LogUtil.e("main",currentIndex+":position");
        fl = (FlowIndicator) findViewById(R.id.fl);
        mv = (MyViewPager) findViewById(R.id.mv);
        count = SpUtil.getDefault(this).getInt("count", 0);
        LogUtil.e("main",count+"");
        mv.setOffscreenPageLimit(count);
        adapter = new MainTabAdpter(getSupportFragmentManager());
        adapter.clear();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                adapter.addFragment(new WeatherFragment(this,i));
                LogUtil.e("main","addFragment i");

            }
        }

        mv.setAdapter(adapter);
        mv.setCurrentItem(currentIndex);
        mv.setOnPageChangeListener(this);
        fl.setCount(count);
        fl.setFocus(currentIndex);
        SpUtil.getDefault(this).edit().putBoolean("city_selected",true).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag=SpUtil.getDefault(this).getBoolean("flag",false);
        if (flag){
            Intent in=new Intent(this,MyService.class);

            startService(in);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        mv.setCurrentItem(currentIndex);
        fl.setFocus(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
