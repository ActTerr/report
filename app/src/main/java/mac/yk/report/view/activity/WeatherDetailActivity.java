package mac.yk.report.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import mac.yk.report.R;
import mac.yk.report.model.util.LogUtil;
import mac.yk.report.model.util.SpUtil;
import mac.yk.report.view.adapter.MainTabAdpter;
import mac.yk.report.view.widget.FlowIndicator;
import mac.yk.report.view.widget.MyViewPager;

/**
 * Created by mac-yk on 2017/1/28.
 */

public class WeatherDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    FlowIndicator fl;
    MyViewPager mv;
    MainTabAdpter adapter;
    int currentIndex;
    int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        initView();
    }

    private void initView() {
        currentIndex = getIntent().getIntExtra("position", 0);
        fl = (FlowIndicator) findViewById(R.id.fl);
        mv = (MyViewPager) findViewById(R.id.mv);
        count = SpUtil.getDefault(this).getInt("count", 0);
        LogUtil.e("main",count+"");
        mv.setOffscreenPageLimit(count);
        adapter = new MainTabAdpter(getSupportFragmentManager());
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                adapter.addFragment(new WeatherFragment(this, count));
            }
        }
        mv.setAdapter(adapter);
        mv.setCurrentItem(currentIndex);
        mv.setOnPageChangeListener(this);
        fl.setCount(count);

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
