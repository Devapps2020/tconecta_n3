package com.blm.qiubopay.modules.home;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;

import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_browser extends HFragment {

    public static IAlertButton execute;
    private WebView webView;
    private String url = "";

    private boolean isChatbot = false;
    private boolean isEnko = false;

    public static Fragment_browser newInstance(String url) {
        Fragment_browser fragment = new Fragment_browser();
        Bundle args = new Bundle();

        args.putString("Fragment_registro_3", url);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_browser newInstanceChatbot(String url) {
        Fragment_browser fragment = new Fragment_browser();
        Bundle args = new Bundle();

        args.putString("Fragment_registro_3", url);
        args.putBoolean("Fragment_registro_3_isChatbot", true);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_browser newInstanceEnko(String url) {
        Fragment_browser fragment = new Fragment_browser();
        Bundle args = new Bundle();
        args.putString("Fragment_registro_3", url);
        args.putBoolean("Fragment_registro_3_isEnko", true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString("Fragment_registro_3");
        isEnko = getArguments().getBoolean("Fragment_registro_3_isEnko",false);
        isChatbot = getArguments().getBoolean("Fragment_registro_3_isChatbot",false);
        if(isChatbot){
            getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_browser, container, false));
    }

    @Override
    public void initFragment(){

        getContext().loading(true);

        CApplication.setAnalytics(CApplication.ACTION.CB_terminos_condiciones);

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                execute = null;
                getContext().backFragment();
                if(isChatbot){
                    getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }
        }).showLogo();

        webView = getView().findViewById(R.id.web_view);
        //webView.getSettings().setUserAgentString(Globals.USER_AGENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WBClient());
        webView.loadUrl(url);

        getContext().logger(url);

        Button btn_ok = getView().findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if(execute != null) {
                        execute.onClick();
                        execute = null;
                    }

                } catch (Exception ex) {
                    execute = null;
                }

            }
        });

        LinearLayout layout_btn_ok = getView().findViewById(R.id.layout_btn_ok);
        layout_btn_ok.setVisibility(View.GONE);

        if(execute != null) {
            layout_btn_ok.setVisibility(View.VISIBLE);
            btn_ok.setText(execute.onText());
        }

    }

    private class WBClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            getContext().loading(false);
        }
        @Override
        public void onReceivedSslError(WebView v, final SslErrorHandler handler, SslError er){
            if(Tools.isOnlyN3Terminal())
                handler.proceed();
            else {
                getContext().alert(R.string.general_error_browser, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        handler.cancel();
                    }
                });
            }
        }
    }


    @Override
    public boolean onBackPressed() {

        if (isEnko && webView.copyBackForwardList().getCurrentIndex() > 1) {
            webView.goBack();
            return true;
        } else {
            if(isChatbot){
                getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
            return false;
        }

    }

}

