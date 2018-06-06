package com.johnny.livelayout.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.johnny.livelayout.R;
import com.johnny.livelayout.adapter.AudienceAdapter;
import com.johnny.livelayout.adapter.MessageAdapter;
import com.johnny.livelayout.bean.GiftBean;
import com.johnny.livelayout.tools.DisplayUtil;
import com.johnny.livelayout.tools.SoftKeyBoardListener;
import com.johnny.livelayout.view.GiftRootLayout;
import com.johnny.livelayout.view.HorizontalListView;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 该Fragment是用于dialogFragment中的pager，为了实现滑动隐藏交互Fragment的
 * 交互的操作都在这个界面实现的，如果大家要改交互主要修改这个界面就可以了
 * <p>
 * Success is the sum of small efforts, repeated day in and day out.
 * 成功就是日复一日那一点点小小努力的积累。
 * AndroidGroup：158423375
 * Author：Johnny
 * AuthorQQ：956595454
 * AuthorWX：Qiang_it
 * AuthorPhone：nothing
 * Created by 2016/9/22.
 */
public class LayerFragment extends Fragment {

    /**
     * 标示判断
     */
    private boolean isOpen;

    /**
     * 界面相关
     */
    @BindView(R.id.llpicimage) LinearLayout llpicimage;
    @BindView(R.id.rlsentimenttime) RelativeLayout rlsentimenttime;
    @BindView(R.id.hlvaudience) HorizontalListView hlvaudience;
    @BindView(R.id.lvmessage) ListView lvmessage;
    @BindView(R.id.iv_privatechat) ImageView tvSendone;
    @BindView(R.id.iv_gift) ImageView tvSendtwo;
    @BindView(R.id.iv_share) ImageView tvSendthree;
    @BindView(R.id.iv_close) ImageView tvSendfor;
    @BindView(R.id.etInput) EditText etInput;
    @BindView(R.id.iv_publicchat) ImageView tvChat;
    @BindView(R.id.sendInput) TextView sendInput;
    @BindView(R.id.llinputparent) LinearLayout llInputParent;
    @BindView(R.id.giftRoot) GiftRootLayout giftRoot;

    /**
     * 动画相关
     */
    private AnimatorSet animatorSetHide = new AnimatorSet();
    private AnimatorSet animatorSetShow = new AnimatorSet();

    private List<String> messageData = new LinkedList<>();
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llInputParent.getVisibility() == View.VISIBLE) {
                    tvChat.setVisibility(View.VISIBLE);
                    llInputParent.setVisibility(View.GONE);
                    hideKeyboard();
                }
            }
        });
        softKeyboardListnenr();
        for (int x = 0; x < 20; x++) {
            messageData.add("Johnny: 默认聊天内容" + x);
        }
        messageAdapter = new MessageAdapter(getActivity(), messageData);
        lvmessage.setAdapter(messageAdapter);
        lvmessage.setSelection(messageData.size());
        hlvaudience.setAdapter(new AudienceAdapter(getActivity()));
    }

    @OnClick(R.id.iv_publicchat) void clickChat(){
        showChat();
    }@OnClick(R.id.sendInput) void clickSendChat(){
        sendText();
    }@OnClick(R.id.iv_privatechat) void clickPrivateChat(){
        GiftBean bean = new GiftBean();
        bean.setGroup(1);
        bean.setSortNum(11);
        bean.setGiftImage(R.mipmap.ic_launcher);
        bean.setGiftName("送出了一个礼物");
        bean.setUserName("A");
        bean.setUserAvatar(R.mipmap.ic_launcher);
        giftRoot.loadGift(bean);
    }@OnClick(R.id.iv_gift) void clickGift(){
        GiftBean bean = new GiftBean();
        bean.setGroup(1);
        bean.setSortNum(22);
        bean.setGiftImage(R.mipmap.ic_launcher);
        bean.setGiftName("送出了一个礼物");
        bean.setUserName("B");
        bean.setUserAvatar(R.mipmap.ic_launcher);
        giftRoot.loadGift(bean);
    }@OnClick(R.id.iv_share) void clickShare(){
        GiftBean bean = new GiftBean();
        bean.setGroup(1);
        bean.setSortNum(33);
        bean.setGiftImage(R.mipmap.ic_launcher);
        bean.setGiftName("送出了一个礼物");
        bean.setUserName("C");
        bean.setUserAvatar(R.mipmap.ic_launcher);
        giftRoot.loadGift(bean);
    }@OnClick(R.id.iv_close) void clickClose(){
        getActivity().finish();
    }

    /**
     * 显示聊天布局
     */
    private void showChat() {
        tvChat.setVisibility(View.GONE);
        llInputParent.setVisibility(View.VISIBLE);
        llInputParent.requestFocus();
        showKeyboard();
    }

    /**
     * 发送消息
     */
    private void sendText() {
        if (!etInput.getText().toString().trim().isEmpty()) {
            messageData.add("Johnny: " + etInput.getText().toString().trim());
            etInput.setText("");
            messageAdapter.NotifyAdapter(messageData);
            lvmessage.setSelection(messageData.size());
            hideKeyboard();
        } else
            hideKeyboard();
    }

    /**
     * 显示软键盘并因此头布局
     */
    private void showKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘并显示头布局
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                animateToHide();
                dynamicChangeListviewH(100);
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                tvChat.setVisibility(View.VISIBLE);
                llInputParent.setVisibility(View.GONE);
                animateToShow();
                dynamicChangeListviewH(150);
            }
        });
    }

    /**
     * 动态的修改listview的高度
     *
     * @param heightPX
     */
    private void dynamicChangeListviewH(int heightPX) {
        ViewGroup.LayoutParams layoutParams = lvmessage.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getActivity(), heightPX);
        lvmessage.setLayoutParams(layoutParams);
    }

    /**
     * 头部布局执行显示的动画
     */
    private void animateToShow() {
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(rlsentimenttime, "translationX", -rlsentimenttime.getWidth(), 0);
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(llpicimage, "translationY", -llpicimage.getHeight(), 0);
        animatorSetShow.playTogether(leftAnim, topAnim);
        animatorSetShow.setDuration(300);
        animatorSetShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetShow.start();
        }
    }

    /**
     * 头部布局执行退出的动画
     */
    private void animateToHide() {
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(rlsentimenttime, "translationX", 0, -rlsentimenttime.getWidth());
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(llpicimage, "translationY", 0, -llpicimage.getHeight());
        animatorSetHide.playTogether(leftAnim, topAnim);
        animatorSetHide.setDuration(300);
        animatorSetHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetHide.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}