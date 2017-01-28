package mac.yk.report.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mac.yk.report.R;
import mac.yk.report.model.bean.Weather;

/**
 * Created by mac-yk on 2017/1/29.
 */

public class weatherListActivity extends BaseActivity{
    ListView lv;
    ArrayAdapter<Weather> Adpater;
    ArrayList<Weather> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        initView();
        initData();
    }

    private void initData() {
        
    }

    private void initView() {
        list=new ArrayList<>();
        Adpater=new ArrayAdapter<Weather>(this,android.R.layout.simple_list_item_1,list);
        lv= (ListView) findViewById(R.id.lv);
        lv.setAdapter(Adpater);
    }


}
