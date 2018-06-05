package com.johnny.livelayout.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johnny.livelayout.R;

public class GiftItemLayout extends LinearLayout implements Animation.AnimationListener {

    public final String TAG = GiftItemLayout.class.getSimpleName();

    private CustomRoundView crvheadimage;
    private TextView tvUserName;
    private TextView tvMessage;
    private ImageView ivgift;
    private GiftCountTextView giftNum;

    /**
     * 透明度动画(200ms), 连击动画(200ms)
     */
    private Animation translateAnim, numAnim;

    public GiftItemLayout(Context context) {
        super(context);
        init(context, null);
    }

    public GiftItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GiftItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public GiftItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attts
     */
    private void init(Context context, AttributeSet attts) {
        View view = View.inflate(context, R.layout.item_gift, null);
        crvheadimage = view.findViewById(R.id.crvheadimage);
        tvUserName = view.findViewById(R.id.tv_UserName);
        tvMessage = view.findViewById(R.id.tv_Message);
        ivgift = view.findViewById(R.id.ivgift);
        giftNum = view.findViewById(R.id.giftNum);
        initTranslateAnim();
        initNumAnim();
    }

    /**
     * 初始化位移动画
     */
    private void initTranslateAnim() {
        translateAnim = new TranslateAnimation(-300, 0, 0, 0);
        translateAnim.setDuration(200);
        translateAnim.setFillAfter(true);
        translateAnim.setAnimationListener(this);
    }

    /**
     * 初始化礼物数字动画
     */
    private void initNumAnim() {
        numAnim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        numAnim.setDuration(200);
        numAnim.setAnimationListener(this);
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