package com.blm.qiubopay.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blm.qiubopay.R;

import mx.devapps.utils.components.HActivity;

public class SnakeActivity extends HActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getBaseContext(), description, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {

            }
        });

        webView.loadUrl("file:///android_asset/snake.html");

        Button btn_arriba = findViewById(R.id.btn_arriba);
        Button btn_abajo = findViewById(R.id.btn_abajo);
        Button btn_derecha = findViewById(R.id.btn_derecha);
        Button btn_izquierda = findViewById(R.id.btn_izquierda);

        ImageView btn_close =  findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().finish();
            }
        });

        //btn_arriba.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
        //btn_abajo.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
        //btn_derecha.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
        //btn_izquierda.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));

        btn_arriba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
            }
        });

        btn_abajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
            }
        });

        btn_derecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
            }
        });

        btn_izquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
            }
        });

        //arrow left	37	37
        //arrow up	38	38
        //arrow right	39	39
        //arrow down	40	40

    }
}