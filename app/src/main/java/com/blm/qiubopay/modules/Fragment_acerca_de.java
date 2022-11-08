package com.blm.qiubopay.modules;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blm.qiubopay.helpers.N3ConfigHelper;
import com.blm.qiubopay.modules.swap.Fragment_swapn3_login;
import com.blm.qiubopay.tools.Tools;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.BuildConfig;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.utils.Globals;

import mx.com.fincomun.origilib.Model.SDKFC;

public class Fragment_acerca_de extends HFragment {

    private View view;
    private MenuActivity context;

    private boolean actionBtn = false;
    private int contActionBtn = 0;

    public Fragment_acerca_de() {
        // Required empty public constructor
    }

    public static Fragment_acerca_de newInstance(Object... data) {
        Fragment_acerca_de fragment = new Fragment_acerca_de();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_acerca_de, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        TextView text_version = view.findViewById(R.id.text_version);
        text_version.setText( Globals.VERSION);

        //20210427 RSB. Homologacion. Configura la n3 mantenimiento
        text_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Globals.debug){
                    getInfoVersion();
                } else {
                    if(actionBtn) {
                        if(contActionBtn == 3){
                            enableMantainance();
                        } else {
                            contActionBtn++;
                        }
                    } else {
                        actionBtn = true;
                        contActionBtn++;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                actionBtn = false;
                                contActionBtn = 0;
                            };
                        }, 5000);
                    }
                }

            }
        });

        Button text_update = view.findViewById(R.id.text_update);
        text_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = context.getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        text_update.setVisibility(View.GONE);

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            Logger.d("" + appUpdateInfo.updateAvailability());

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    text_update.setVisibility(View.VISIBLE);
            }
        });

        //20210209 RSB. SwapN3toN3.
        if(Tools.isN3Terminal()){
            Button btnSwap = view.findViewById(R.id.btn_swap);
            btnSwap.setVisibility(View.VISIBLE);
            btnSwap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().setFragment(Fragment_swapn3_login.newInstance(true));
                }
            });
        }

    }

    private void getInfoVersion(){
        Toast.makeText(context, Globals.HOST + "\n" + Globals.BASE_URL, Toast.LENGTH_LONG).show();
        enableMantainance();
    }

    private void enableMantainance(){
        if(Tools.isN3Terminal()) {
            Toast.makeText(context, "Botones activados", Toast.LENGTH_LONG).show();
            new N3ConfigHelper(context,false);
        }
    }

}
