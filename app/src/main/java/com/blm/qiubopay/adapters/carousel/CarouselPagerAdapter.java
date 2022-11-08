package com.blm.qiubopay.adapters.carousel;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blm.qiubopay.R;
import com.blm.qiubopay.fragments.ItemFragment;
import com.blm.qiubopay.layout.CarouselLinearLayout;
import com.blm.qiubopay.models.carousel.CarouselData;
import com.blm.qiubopay.models.carousel.PublicityResponse;
import com.blm.qiubopay.utils.Globals;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mx.devapps.utils.components.HActivity;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.6f;
    public final static float SMALL_SCALE = 1.0f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private Context context;
    private FragmentManager fragmentManager;
    private float scale;
    private int mFirstPage;
    private int mCount;
    private final static int LOOPS = 1000;
    private ViewPager mPager;
    private Object mClassOfT;
    private Object mSecondClassOfT;
    private ArrayList<PublicityResponse> publicity;
    private int mPageCounter;
    private boolean isTimerRunning = false;
    private ArrayList<CarouselData> mCarouselDatas;

    public <T, U> CarouselPagerAdapter(Context context, FragmentManager fm, int firstPage, ViewPager pager, Class<T> classOfT,
                                       Class<U> secondClassOfT, ArrayList<PublicityResponse> publicity, ArrayList<CarouselData> carouselDatas) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.mFirstPage = firstPage;
        this.mCount = firstPage;
        this.mPager = pager;
        this.mClassOfT = classOfT;
        this.mSecondClassOfT = secondClassOfT;
        this.publicity = publicity;
        this.mCarouselDatas = carouselDatas;
        mPageCounter = mFirstPage;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            if (position == mFirstPage) scale = BIG_SCALE;
            else scale = SMALL_SCALE;
            position = position % mCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItemFragment.newInstance(context, position, scale, (Class<Object>) mClassOfT,
                (Class<Object>) mSecondClassOfT, publicity, mCarouselDatas);
    }

    @Override
    public int getCount() {
        return mCount * LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                setTimer(Globals.SCROLLING_TIME);
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);
                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mPageCounter == position)
            mPageCounter++;
        else
            mPageCounter--;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private CarouselLinearLayout getRootView(int position) {
        return fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + mPager.getId() + ":" + position;
    }

    private static Timer myTimer;

    public void setTimer(int time) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ((HActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPager.setCurrentItem(mPageCounter);
                    }
                });
            }
        };
        if (!isTimerRunning) isTimerRunning = true;
        else myTimer.cancel();
        myTimer = new Timer();
        myTimer.schedule(timerTask, time, time);
    }

}

