package com.blm.qiubopay.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.blm.qiubopay.R;

public class Dots extends Fragment {
    private static View view;

    private int mCount;
    private Context mContext;

    private RelativeLayout rlDot0;
    private RelativeLayout rlDot1;
    private RelativeLayout rlDot2;
    private RelativeLayout rlDot3;
    private RelativeLayout rlDot4;

    private ImageView ivPositiveDot0;
    private ImageView ivPositiveDot1;
    private ImageView ivPositiveDot2;
    private ImageView ivPositiveDot3;
    private ImageView ivPositiveDot4;

    private ImageView ivNegativeDot0;
    private ImageView ivNegativeDot1;
    private ImageView ivNegativeDot2;
    private ImageView ivNegativeDot3;
    private ImageView ivNegativeDot4;

    private static final String TAG = "Dots";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dots, container, false);

        rlDot0 = view.findViewById(R.id.rl_dot0);
        rlDot1 = view.findViewById(R.id.rl_dot1);
        rlDot2 = view.findViewById(R.id.rl_dot2);
        rlDot3 = view.findViewById(R.id.rl_dot3);
        rlDot4 = view.findViewById(R.id.rl_dot4);

        ivPositiveDot0 = view.findViewById(R.id.iv_positive_dot0);
        ivPositiveDot1 = view.findViewById(R.id.iv_positive_dot1);
        ivPositiveDot2 = view.findViewById(R.id.iv_positive_dot2);
        ivPositiveDot3 = view.findViewById(R.id.iv_positive_dot3);
        ivPositiveDot4 = view.findViewById(R.id.iv_positive_dot4);

        ivNegativeDot0 = view.findViewById(R.id.iv_negative_dot0);
        ivNegativeDot1 = view.findViewById(R.id.iv_negative_dot1);
        ivNegativeDot2 = view.findViewById(R.id.iv_negative_dot2);
        ivNegativeDot3 = view.findViewById(R.id.iv_negative_dot3);
        ivNegativeDot4 = view.findViewById(R.id.iv_negative_dot4);

        return view;
    }

    public void setDotColor() {
        ivPositiveDot0.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_positive_blue_dot));
        ivPositiveDot1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_positive_blue_dot));
        ivPositiveDot2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_positive_blue_dot));
        ivPositiveDot3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_positive_blue_dot));
        ivPositiveDot4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_positive_blue_dot));
        ivNegativeDot0.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_negative_blue_dot));
        ivNegativeDot1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_negative_blue_dot));
        ivNegativeDot2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_negative_blue_dot));
        ivNegativeDot3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_negative_blue_dot));
        ivNegativeDot4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_negative_blue_dot));
    }

    /**
     * Set number of images to show
     *
     * @param count
     */
    public void numberOfItems(int count, Context context) {
        this.mCount = count;
        this.mContext = context;
        switch (count) {
            case 0:
                setLayoutVisibility(false, false, false, false, false);
                break;
            case 1:
                setLayoutVisibility(true, false, false, false, false);
                break;
            case 2:
                setLayoutVisibility(true, true, false, false, false);
                break;
            case 3:
                setLayoutVisibility(true, true, true, false, false);
                break;
            case 4:
                setLayoutVisibility(true, true, true, true, false);
                break;
            default:
                setLayoutVisibility(true, true, true, true, true);
                break;
        }
    }

    /**
     * Refresh page position (dots indicator)
     *
     * @param position
     */
    public void refreshDots(int position) {
        final int[] mPosition = {position};
        Runnable runOnUI = new Runnable() {
            @Override
            public void run() {
                switch (mPosition[0]) {
                    case 0:
                        setPositiveDotVisibility(true, false, false, false, false);
                        setNegativeDotVisibility(false, true, true, true, true);
                        break;
                    case 1:
                        setPositiveDotVisibility(false, true, false, false, false);
                        setNegativeDotVisibility(true, false, true, true, true);
                        break;
                    case 2:
                        setPositiveDotVisibility(false, false, true, false, false);
                        setNegativeDotVisibility(true, true, false, true, true);
                        break;
                    case 3:
                        setPositiveDotVisibility(false, false, false, true, false);
                        setNegativeDotVisibility(true, true, true, false, true);
                        break;
                    case 4:
                        setPositiveDotVisibility(false, false, false, false, true);
                        setNegativeDotVisibility(true, true, true, true, false);
                        break;
                    default:
                        break;
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mPosition[0] >= mCount) mPosition[0] -= mCount;
                if (getActivity() != null)  getActivity().runOnUiThread(runOnUI);
            }
        }).start();
    }


    /**
     * Show or hide dots
     *
     * @param layout0
     * @param layout1
     * @param layout2
     * @param layout3
     * @param layout4
     */
    private void setLayoutVisibility(boolean layout0, boolean layout1, boolean layout2, boolean layout3, boolean layout4) {
        if (layout0)
            rlDot0.setVisibility(View.VISIBLE);
        else
            rlDot0.setVisibility(View.GONE);
        if (layout1)
            rlDot1.setVisibility(View.VISIBLE);
        else
            rlDot1.setVisibility(View.GONE);
        if (layout2)
            rlDot2.setVisibility(View.VISIBLE);
        else
            rlDot2.setVisibility(View.GONE);
        if (layout3)
            rlDot3.setVisibility(View.VISIBLE);
        else
            rlDot3.setVisibility(View.GONE);
        if (layout4)
            rlDot4.setVisibility(View.VISIBLE);
        else
            rlDot4.setVisibility(View.GONE);
    }

    /**
     * Show or hide positive dots
     *
     * @param dot0
     * @param dot1
     * @param dot2
     * @param dot3
     * @param dot4
     */
    private void setPositiveDotVisibility(boolean dot0, boolean dot1, boolean dot2, boolean dot3, boolean dot4) {
        if (dot0)
            ivPositiveDot0.setVisibility(View.VISIBLE);
        else
            ivPositiveDot0.setVisibility(View.GONE);
        if (dot1)
            ivPositiveDot1.setVisibility(View.VISIBLE);
        else
            ivPositiveDot1.setVisibility(View.GONE);
        if (dot2)
            ivPositiveDot2.setVisibility(View.VISIBLE);
        else
            ivPositiveDot2.setVisibility(View.GONE);
        if (dot3)
            ivPositiveDot3.setVisibility(View.VISIBLE);
        else
            ivPositiveDot3.setVisibility(View.GONE);
        if (dot4)
            ivPositiveDot4.setVisibility(View.VISIBLE);
        else
            ivPositiveDot4.setVisibility(View.GONE);
    }

    /**
     * Show or hide negative dots
     *
     * @param dot0
     * @param dot1
     * @param dot2
     * @param dot3
     * @param dot4
     */
    private void setNegativeDotVisibility(boolean dot0, boolean dot1, boolean dot2, boolean dot3, boolean dot4) {
        if (dot0)
            ivNegativeDot0.setVisibility(View.VISIBLE);
        else
            ivNegativeDot0.setVisibility(View.GONE);
        if (dot1)
            ivNegativeDot1.setVisibility(View.VISIBLE);
        else
            ivNegativeDot1.setVisibility(View.GONE);
        if (dot2)
            ivNegativeDot2.setVisibility(View.VISIBLE);
        else
            ivNegativeDot2.setVisibility(View.GONE);
        if (dot3)
            ivNegativeDot3.setVisibility(View.VISIBLE);
        else
            ivNegativeDot3.setVisibility(View.GONE);
        if (dot4)
            ivNegativeDot4.setVisibility(View.VISIBLE);
        else
            ivNegativeDot4.setVisibility(View.GONE);
    }

}
