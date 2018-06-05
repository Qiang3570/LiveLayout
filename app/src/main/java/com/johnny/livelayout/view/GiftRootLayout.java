package com.johnny.livelayout.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.johnny.livelayout.R;
import com.johnny.livelayout.bean.GiftBean;

import java.util.Comparator;
import java.util.TreeMap;

public class GiftRootLayout extends LinearLayout implements Animation.AnimationListener {

    public final String TAG = GiftRootLayout.class.getSimpleName();

    private GiftItemLayout firstItemLayout, lastItemLayout;
    private Animation firstGiftItemInAnim, firstGiftItemOutAnim;
    private Animation lastGiftItemInAnim, lastGiftItemOutAnim;
    private final TreeMap<Long, GiftBean> giftBeanTreeMap = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o2.compareTo(o1);
        }
    });

    public GiftRootLayout(Context context) {
        super(context);
        init(context);
    }

    public GiftRootLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GiftRootLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GiftRootLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        firstGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_in);
        firstGiftItemInAnim.setFillAfter(true);
        firstGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_out);
        firstGiftItemInAnim.setFillAfter(true);

        lastGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_in);
        lastGiftItemInAnim.setFillAfter(true);
        lastGiftItemInAnim = AnimationUtils.loadAnimation(context, R.anim.gift_out);
        lastGiftItemInAnim.setFillAfter(true);

        firstGiftItemInAnim.setAnimationListener(this);
        lastGiftItemInAnim.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}