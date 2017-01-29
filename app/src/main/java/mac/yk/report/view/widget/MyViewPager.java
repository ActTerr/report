package mac.yk.report.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mac-yk on 2017/1/28.
 */

public class MyViewPager extends ViewPager {
    private boolean Scrollable=true;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollable() {
        return Scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!Scrollable){
            return true;
            //如果不能滚动，不再让其他回调方法处理
        }
        return super.onTouchEvent(ev);
    }

    public void setScrollable(boolean scrollable) {
        Scrollable = scrollable;
    }
}
