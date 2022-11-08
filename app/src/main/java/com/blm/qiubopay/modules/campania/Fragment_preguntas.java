package com.blm.qiubopay.modules.campania;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestions_object;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestions_questions;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_preguntas extends HFragment {

    private static QPAY_DynamicQuestions_object data;
    private static ViewPager viewPager;
    private static IFunction continuar;

    public static Fragment_preguntas newInstance() {
        return new Fragment_preguntas();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_preguntas, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        getContext().setDefaultBack(new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().startActivity(LoginActivity.class, true);
            }
        });

        configPreguntas();

    }

    public void configPreguntas() {

        List<HFragment> fragments = new ArrayList();

        int position = 0;
        for (QPAY_DynamicQuestions_questions quet : data.getQuestion()) {
            quet.setPosition(position++);
            quet.setCampaignId(data.getId());
            fragments.add(Fragment_item_pregunta.newInstance(quet));
        }

        CFragmentAdapter adapter = new CFragmentAdapter(getContext().getSupportFragmentManager(), fragments);

        viewPager = getView().findViewById(R.id.view_pager);
        TabLayout indicator = getView().findViewById(R.id.tab_layout);

        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager);

        viewPager.beginFakeDrag();

        int PAGE = 2;
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewPager.getCurrentItem() == PAGE) {
                    viewPager.setCurrentItem(PAGE-1, false);
                    viewPager.setCurrentItem(PAGE, false);
                    return  true;
                }
                return false;
            }
        });

    }

    public static void setData(QPAY_DynamicQuestions_object data) {
        Fragment_preguntas.data = data;
    }

    public static ViewPager getViewPager() {
        return viewPager;
    }

    public class CFragmentAdapter extends FragmentPagerAdapter {

        private List<HFragment> fragments;

        public CFragmentAdapter(FragmentManager fm, List<HFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public HFragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    public static void setContinue(IFunction continuar) {
        Fragment_preguntas.continuar = continuar;
    }

    public static void setContinuar(IFunction continuar) {
        Fragment_preguntas.continuar = continuar;
    }

    public static void setPositionPager(Integer position) {

        position++;

        if(position < data.getQuestion().length) {
            viewPager.setCurrentItem(position);
        } else {
            if(continuar != null)
                continuar.execute();
        }

    }
}