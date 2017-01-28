package mac.yk.report.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import mac.yk.report.R;
import mac.yk.report.model.bean.Weather;
import mac.yk.report.model.util.SpUtil;

/**
 * Created by mac-yk on 2017/1/29.
 */

public class weatherListActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    ListView lv;
    ArrayAdapter<Weather> Adpater;
    ArrayList<Weather> list;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
        initView();
        initData();
    }

    private void initData() {
        int count = SpUtil.getDefault(this).getInt("count",0);
        if (count>0){
            for (int i=0;i<count;i++){
                SharedPreferences sp = SpUtil.getSp(this, i);
                Weather weather=new Weather(SpUtil.getLoc(sp),SpUtil.getTem(sp));
                list.add(weather);
            }
        }
    }

    private void initView() {
        button= (Button) findViewById(R.id.btn_add);
        button.setOnClickListener(this);
        list=new ArrayList<>();
        Adpater=new ArrayAdapter<Weather>(this,android.R.layout.simple_list_item_1,list);
        lv= (ListView) findViewById(R.id.lv);
        lv.setAdapter(Adpater);

    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("from_weather_activity",true);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,WeatherDetailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
