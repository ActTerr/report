package mac.yk.report.WeatherDetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clawpo on 2016/9/22.
 */
public class MainTabAdpter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();
    private FragmentManager fm;

    public MainTabAdpter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.saveState();
    }



    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void clear() {
        mFragments.clear();
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }



}
