package com.blm.qiubopay.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.utils.Utils;
import com.squareup.picasso.Picasso;

public class Fragment_flippable extends Fragment {

    private View view;
    private int image;
    private String imageUrl;

    public static Fragment_flippable newInstance(int image) {
        Fragment_flippable fragment = new Fragment_flippable();
        Bundle args = new Bundle();

        args.putInt("Fragment_flippable",image);

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_flippable newInstance(String image) {
        Fragment_flippable fragment = new Fragment_flippable();
        Bundle args = new Bundle();

        args.putString("Fragment_flippable_string",image);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        image = getArguments().getInt("Fragment_flippable",0);
        imageUrl = getArguments().getString("Fragment_flippable_string");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.item_coverflow_welcome, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        ImageView imagen_banner = view.findViewById(R.id.imagen_banner);

        if(image!=0){
            imagen_banner.setImageBitmap(Utils.getRoundedCornerBitmap(getContext(), drawableToBitmap(getContext().getResources().getDrawable(image)), R.dimen._5sdp));
        } else {
            Picasso.get().load(imageUrl).into(imagen_banner);
        }


    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}

