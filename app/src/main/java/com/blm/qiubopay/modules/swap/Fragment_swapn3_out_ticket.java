package com.blm.qiubopay.modules.swap;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.models.swap.QPAY_SwapN3OutResponse;
import com.blm.qiubopay.printers.N3PrinterHelper;
import com.blm.qiubopay.tools.Tools;
import com.google.gson.Gson;

import mx.devapps.utils.interfaces.IAlertButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_swapn3_out_ticket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_swapn3_out_ticket extends HFragment {

    private static final String TAG = "swapn3_1";

    private Button btn_continuar;

    //Variable para distinguir si va a ser login de swap out o swap in
    private boolean isSwapOut;
    private QPAY_SwapN3OutResponse response;

    public Fragment_swapn3_out_ticket() {
        // Required empty public constructor
    }

    public static Fragment_swapn3_out_ticket newInstance(Object... data) {
        Fragment_swapn3_out_ticket fragment = new Fragment_swapn3_out_ticket();

        Bundle args = new Bundle();
        args.putBoolean("Fragment_swapn3_out_1", (Boolean) data[0]);
        args.putString("Fragment_swapn3_out_2", new Gson().toJson(data[1]));
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

        if (getArguments() != null) {
            isSwapOut = getArguments().getBoolean("Fragment_swapn3_out_1");
            response = new Gson().fromJson(getArguments().getString("Fragment_swapn3_out_2"),QPAY_SwapN3OutResponse.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_swapn3_out_ticket, container, false), R.drawable.background_splash_header_1);
    }


    public void initFragment(){

        btn_continuar = getView().findViewById(R.id.btn_continuar);
        final TextView tv_description = getView().findViewById(R.id.tv_description);

        if(isSwapOut){
            tv_description.setText(R.string.text_swapn3_out_finish);
            finishSwapOut();
        } else {
            tv_description.setText(R.string.text_swapn3_in_finish);
            finishSwapIn();
        }

    }


    private void finishSwapOut(){

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.setCloseSessionFlag("1",getContext().getResources().getString(R.string.message_logout));
                AppPreferences.Logout(getContext());
                getContext().alert(getContext().getResources().getString(R.string.message_logout_swap), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        getContext().startActivity(LoginActivity.class, true);
                    }
                });
            }
        });

        if(Tools.isN3Terminal()) {
            Toast.makeText(getContext(), "Imprimiendo Ticket.", Toast.LENGTH_SHORT).show();
            N3PrinterHelper printer = new N3PrinterHelper(getContext());
            printer.printSwapOutN3Ticket(response.getQpay_object()[0].getQpay_folio(),
                    response.getQpay_object()[0].getQpay_balance());
        }

    }


    private void finishSwapIn(){

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(LoginActivity.class, true);
            }
        });

        if(Tools.isN3Terminal()) {
            Toast.makeText(getContext(), "Imprimiendo Ticket.", Toast.LENGTH_SHORT).show();
            N3PrinterHelper printer = new N3PrinterHelper(getContext());
            printer.printSwapInN3Ticket(response.getQpay_object()[0].getQpay_balance());
        }

    }

}