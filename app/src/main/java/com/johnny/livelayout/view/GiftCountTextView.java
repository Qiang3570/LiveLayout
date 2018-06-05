package com.johnny.livelayout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SEELE on 2016/10/6.
 */
@SuppressLint("AppCompatCustomView")
public class GiftCountTextView extends TextView {

    public GiftCountTextView(Context context) {
        super(context);
        init();
    }

    public GiftCountTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftCountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GiftCountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/customfontstyle.otf"));
    }
}