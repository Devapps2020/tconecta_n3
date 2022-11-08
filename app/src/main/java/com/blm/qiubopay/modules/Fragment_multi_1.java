package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LinkCode;
import com.blm.qiubopay.models.QPAY_LinkCodeResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.utils.WSHelper;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_multi_1 extends HFragment {

    private View view;
    private MenuActivity context;


    public Fragment_multi_1() {
        // Required empty public constructor
    }

    public static Fragment_multi_1 newInstance(Object... data) {
        Fragment_multi_1 fragment = new Fragment_multi_1();
        /*Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_multi_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (getArguments() != null)
            //data = new Gson().fromJson(getArguments().getString("Fragment_multi_1"), QPAY_NewUser.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_multi_tc_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CardView card_cajero = view.findViewById(R.id.card_cajero);
        card_cajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLinkCode(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.setFragment(Fragment_multi_2.newInstance(data[0]));
                    }
                });
            }
        });

        CardView card_patron = view.findViewById(R.id.card_patron);
        card_patron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setFragment(Fragment_multi_3.newInstance());
            }
        });

    }

    /**
     * Método para llamar servicio para generar código
     * @param function
     */
    public void getLinkCode(final IFunction function) {

        context.loading(true);

        QPAY_Seed data = new QPAY_Seed();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        IMultiUserListener linkCodeListener = null;
        try {
            linkCodeListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_LinkCodeResponse linkCodeResponse = gson.fromJson(json, QPAY_LinkCodeResponse.class);

                        if (linkCodeResponse.getQpay_response().equals("true")) {

                            LinkCode linkCode = linkCodeResponse.getQpay_object()[0];

                            if(function != null)
                                function.execute(linkCode);

                        } else {
                          context.validaSesion(linkCodeResponse.getQpay_code(), linkCodeResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        linkCodeListener.getLinkCode(data);

    }

}
