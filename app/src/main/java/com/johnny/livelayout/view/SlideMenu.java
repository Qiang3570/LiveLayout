package com.johnny.livelayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 继承自FrameLayout:
 * 1.因为FrameLayout帮我们实现了onMeasure方法，不需要我们自己实现
 * 2.因为FrameLayout代码最少，在四大布局中属于轻量级
 * 3.替代了原先使用的ViewPager
 */
public class SlideMenu extends FrameLayout {

    /*左边菜单的view*/
    private View leftMenuView;
    /*左边菜单的宽度*/
    private int leftMenuWidth;
    /*左边菜单的高度*/
    private int leftMenuHeight;
    /*主界面的view*/
    private View mainView;
    /*主界面的宽度*/
    private int mainWidth;
    private Scroller scroller;
    private float downX;
    private float downY;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    /**
     * 当完成从布局文件加载VIew的时候,该方法执行完后就知道自己又几个子view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        leftMenuView = getChildAt(0);
        mainView = getChildAt(1);
    }

    /**
     * 这个方法是onMeasure执行之后执行，所以在这个方法中可以获取所有子view的宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftMenuWidth = leftMenuView.getMeasuredWidth();
        leftMenuHeight = leftMenuView.getMeasuredHeight();
        mainWidth = mainView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftMenuView.layout(-leftMenuWidth, 0, 0, leftMenuHeight);
        mainView.layout(0, 0, mainWidth, leftMenuHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float deltaX = moveX - downX;/*x方向滑动的距离*/
                float deltaY = moveY - downY;/*y方向滑动的距离*/
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    /*如果move的方向偏于水平方向，此时才拦截
                    *如果move的方向偏于垂直方向，此时不应该拦截
                    */
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float deltaX = moveX - downX;
                int newScrollX = (int) (getScrollX() - deltaX);
                if (newScrollX < -leftMenuWidth) {
                    newScrollX = -leftMenuWidth;
                }
                if (newScrollX > 0) {
                    newScrollX = 0;
                }
                scrollTo(newScrollX, 0);
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() >= -leftMenuWidth / 2) {
                    closeLeftMenu();
                } else {
                    openLeftMenu();
                }
                break;
        }
        return true;
    }

    /**
     * 切换菜单
     */
    public void switchMenu() {
        if (getScrollX() == -leftMenuWidth) {
            closeLeftMenu();/*此时是开着的，应该关*/
        } else {
            openLeftMenu();/*应该打开*/
        }
    }

    /**
     * 关闭左边菜单
     */
    private void closeLeftMenu() {
        scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 100);
        invalidate();
    }

    /**
     * 打开左边菜单
     */
    private void openLeftMenu() {
        scroller.startScroll(getScrollX(), 0, -leftMenuWidth - getScrollX(), 0, 100);
        invalidate();
    }

    /**
     * 由于computeScroll方法不会自动调用，所有invalidate来调该方法
     * invalidate->draw->computeScroll
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {/*如果返回true，表示动画没有结束，反之就结束*/
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}