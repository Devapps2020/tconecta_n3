package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.google.gson.Gson;


import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;

/**
 * RSB 20200128. Fragment to present ENKO menu
 */
public class Fragment_aprende_1 extends HFragment {


    private View view;
    private HActivity context;
    private Object data;

    private final String URL_ENKO = "https://www.enko.org/webservice/rest/qiubo.php";

    public Fragment_aprende_1() {
        // Required empty public constructor
    }

    public static Fragment_aprende_1 newInstance(Object... data) {
        Fragment_aprende_1 fragment = new Fragment_aprende_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_aprende_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_aprende_1"), Object.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_aprende_1, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CardView card_aprende_1 = view.findViewById(R.id.card_aprende_1);
        CardView card_aprende_2 = view.findViewById(R.id.card_aprende_2);
        CardView card_aprende_3 = view.findViewById(R.id.card_aprende_3);
        CardView card_aprende_4 = view.findViewById(R.id.card_aprende_4);


        card_aprende_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSection(1);
            }
        });

        card_aprende_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSection(2);
            }
        });

        card_aprende_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSection(3);
            }
        });

        card_aprende_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSection(4);
            }
        });

    }

    public void gotoSection(int section) {

        String username = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();
        String email = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail();
        String tel = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone();

       context.setFragment(Fragment_browser.newInstance(URL_ENKO + "?username=" + username + "&email=" + email + "&tel=" + tel + "&section=" + section));

    }

}
