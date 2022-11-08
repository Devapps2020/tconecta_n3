package com.blm.qiubopay.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.layout.CarouselLinearLayout;
import com.blm.qiubopay.models.carousel.CarouselData;
import com.blm.qiubopay.models.carousel.FullScreenData;
import com.blm.qiubopay.models.carousel.PublicityResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;

public class ItemFragment extends Fragment {
    private static final String POSITION = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";
    private static Context mContext;
    private static Object mClassOfT;
    private static Object mSecondClassOfT;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout linearLayout;
    private CarouselLinearLayout root;
    private ImageView imageView;

    private int screenWidth;
    private int screenHeight;

    private static ArrayList<PublicityResponse> mPublicity;
    private static ArrayList<CarouselData> mCarouselDatas;

    private static final String TAG = "ItemFragment";

    public static <T, U> Fragment newInstance(Context context, int pos, float scale, Class<T> classOfT, Class<U> secondClassOfT,
                                              ArrayList<PublicityResponse> publicity, ArrayList<CarouselData> carouselDatas) {
        Bundle b = new Bundle();
        b.putInt(POSITION, pos);
        b.putFloat(SCALE, scale);
        mClassOfT = classOfT;
        mSecondClassOfT = secondClassOfT;
        mPublicity = publicity;
        mCarouselDatas = carouselDatas;
        mContext = context;

        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        final int position = this.getArguments().getInt(POSITION);
        float scale = this.getArguments().getFloat(SCALE);
        layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image_details, container, false);
        try {
            root = linearLayout.findViewById(R.id.root_container);
            imageView = linearLayout.findViewById(R.id.pagerImg);
            imageView.setLayoutParams(layoutParams);
            if (mPublicity != null && mPublicity.size() > 0) {
                String urlImage = mPublicity.get(position).getCarrouselImage();
                Glide.with(getContext())
                        .load(urlImage) // image url
                        .centerInside()
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(6)))
                        .into(imageView);  // imageview object
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendDataToFullScreen(position);
                    }
                });
            }
            root.setScaleBoth(scale);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return linearLayout;
    }

    private void sendDataToFullScreen(int position) {
        String title = mPublicity.get(position).getTitle();
        String description = mPublicity.get(position).getDescription();
        String image = mPublicity.get(position).getImage();
        String linkText = mPublicity.get(position).getLink();
        String textButton = mPublicity.get(position).getButtonText();
        Integer campaignId = mPublicity.get(position).getCampaignId();
        FullScreenData fullScreenData = new FullScreenData(title, description, image, linkText, textButton, campaignId);
//        ((HActivity)mContext).setFragment(FullScreenFragment.newInstance(fullScreenData, mContext));
        ((MenuActivity)mContext).fullScreenFragment = FullScreenFragment.newInstance(fullScreenData, getContext());
        ((HActivity)mContext).setFragment(((MenuActivity)mContext).fullScreenFragment);
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

}
