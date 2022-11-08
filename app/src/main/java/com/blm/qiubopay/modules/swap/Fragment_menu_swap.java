package com.blm.qiubopay.modules.swap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_menu_swap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_menu_swap extends HFragment {

    private static final String TAG = "menu_swap";

    public Fragment_menu_swap() {
        // Required empty public constructor
    }

    public static Fragment_menu_swap newInstance() {
        Fragment_menu_swap fragment = new Fragment_menu_swap();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_swap, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment() {

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        //Swap T1000 to N3
        CardView cardMigrar = getView().findViewById(R.id.card_migrar_terminal);
        //SwapIn N3 to N3
        CardView cardCambiar = getView().findViewById(R.id.card_cambiar_terminal);
        //Multidevice T1000
        CardView cardLigar = getView().findViewById(R.id.card_ligar_terminal);

        cardMigrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_swapt1000_1.newInstance());
            }
        });

        cardCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_swapn3_login.newInstance(false));
            }
        });

        cardLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_multi_t1000_1.newInstance());
            }
        });

        cardLigar.setVisibility(View.GONE);

    }
}