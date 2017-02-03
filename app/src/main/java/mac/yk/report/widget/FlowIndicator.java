package mac.yk.report.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import mac.yk.report.R;


/**
 * Created by yao on 2016/4/16.
 */
public class FlowIndicator extends View {
    /** 指示器的圆钮数量是否发生改变*/
    boolean misCountChange =true;
    /**
     * 实心圆的数量
     */
    private int mCount;
    /** 实心圆的半径*/
    private int mRadius;
    /** 非焦点圆的颜色*/
    private int mNormalColor;
    /** 焦点圆的颜色*/
    private int mFocusColor;
    /** 当前实心圆的索引*/
    private int mFocus;
    /** 实心圆的间距*/
    private int mSpace;

    private Paint mPaint;
    public FlowIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);
        mCount = array.getInt(R.styleable.FlowIndicator_count, 1);
        mRadius = array.getDimensionPixelOffset(R.styleable.FlowIndicator_r, 0);
        mNormalColor = array.getColor(R.styleable.FlowIndicator_normal_color, 0xfff);
        mFocusColor = array.getColor(R.styleable.FlowIndicator_focus_color, 0xff7);
        mSpace = array.getDimensionPixelOffset(R.styleable.FlowIndicator_space, 6);
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setCount(int count) {
        misCountChange =true;//允许回调onMeasure和onLayout
        mCount=count;
        //要求重测量、布局和重绘
        requestLayout();
//        invalidate();//调用本方法，无法改变已有指示器的圆形数量
    }

    /** 设置当前实心圆的索引*/
    public void setFocus(int focus) {
        misCountChange =true;//禁止回调onMeasure和onLayout，减少测量和布局方法的回调
        mFocus=focus;
        invalidate();//同样会回调onMeasure、onLayout和onDraw
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!misCountChange) {//避免焦点改变时，执行不必要的测量
            return;
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        Log.e("main", "onMeasure()");
    }

    /**测量宽度*/
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result=size;
        if (mode != MeasureSpec.EXACTLY) {
            size=getPaddingLeft()+getPaddingRight()
                    +2*mRadius*mCount+(mCount-1)*mRadius;
            result = Math.min(result, size);
        }
        return result;
    }

    /**测量高度*/
    private int measureHeight(int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result=size;
        if (mode != MeasureSpec.EXACTLY) {
            size=getPaddingTop()+getPaddingBottom()+2*mRadius;
            result = Math.min(result, size);
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!misCountChange) {//避免焦点改变时，执行不必要的布局
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
        Log.i("main", "onLayout()");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("main", "onDraw()");
        if (mCount == 0) {
            return;
        }
        //计算左边距
        int leftSpace = (getWidth() - (2 * mCount * mRadius + (mCount - 1) * mSpace) )/ 2;
        //绘制指定数量的圆
        for(int i=0;i<mCount;i++) {
            //计算每个圆的横坐标
            int x=leftSpace+i*(2*mRadius+mSpace)+mRadius;
            //获取当前圆的绘制颜色
            int color = i == mFocus ? mFocusColor : mNormalColor;
            mPaint.setColor(color);
            canvas.drawCircle(x,getHeight()/2,mRadius, mPaint);
        }
    }


}
