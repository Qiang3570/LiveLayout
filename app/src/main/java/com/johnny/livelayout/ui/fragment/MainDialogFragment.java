package com.johnny.livelayout.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.johnny.livelayout.R;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * 可以看得出来这是个全屏dailogFragment，他的内部有一个pager
 * 分别控制着EmptyFragment与LayerFragment
 * EmptyFragment：什么都没有
 * LayerFragment：交互界面
 * 这样就达到了滑动隐藏交互的需求，这样做也是为了避免我们自定义动画时，显示卡顿的问题
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
@SuppressLint("ValidFragment")
public class MainDialogFragment extends DialogFragment implements ViewPager.OnPageChangeListener {


    @BindView(R.id.viewpager) VerticalViewPager viewPager;
    private BaseFragmentPagerAdapter fragmentPagerAdapter;

    private FrameLayout viewById;
    private int mCurrentItem = 1;
    private LinkedList<Fragment> pageFragmentCache = new LinkedList<>();

    public MainDialogFragment(FrameLayout viewById) { this.viewById=viewById; }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);/*同时将界面改为resize已达到软键盘弹出时LiveFragment不会跟随移动*/
        initViewPagerData();
        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), pageFragmentCache);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(mCurrentItem);
    }

    /**
     * 初始化viewpager的数据
     */
    private void initViewPagerData() {
        pageFragmentCache.clear();
        for (int x = 0; x < 3; x++) {
            Fragment fragment = null;
            if (x == 1) {
                fragment = new LayerFragment();
            } else {
                fragment = new LoadingFragment();
            }
            pageFragmentCache.add(fragment);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog) {/*设置MainDialogFragment的样式，这里的代码最好还是用我的，大家不要改动*/
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                getActivity().finish();
            }
        };
        return dialog;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            viewById.setY(-(positionOffsetPixels-viewById.getHeight()));
        }else if (position == 1) {
            viewById.setY(-positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        if(state==0&&mCurrentItem!=1){
            viewById.setY(viewById.getHeight());
            initViewPagerData();
            fragmentPagerAdapter.setPageFragmentCache(pageFragmentCache);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(1, false);
                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 200);
        }
    }

    /**
     * 该类私有数据适配器
     */
    private class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager mFragmentManager;
        private LinkedList<Fragment> pageFragmentCache;

        public BaseFragmentPagerAdapter(FragmentManager fm, LinkedList<Fragment> pageFragmentCache) {
            super(fm);
            this.mFragmentManager = fm;
            this.pageFragmentCache = pageFragmentCache;
        }

        @Override
        public Fragment getItem(int position) {
            return pageFragmentCache.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return pageFragmentCache.size();
        }

        public void setPageFragmentCache(LinkedList<Fragment> pageFragmentCache) {
            if (this.pageFragmentCache != null) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                for (Fragment f : this.pageFragmentCache) {
                    ft.remove(f);
                }
                ft.commit();
                mFragmentManager.executePendingTransactions();
            }
            this.pageFragmentCache = pageFragmentCache;
            notifyDataSetChanged();
        }
    }
}