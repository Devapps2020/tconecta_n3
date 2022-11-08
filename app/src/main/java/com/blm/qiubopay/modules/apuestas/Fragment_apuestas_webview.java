package com.blm.qiubopay.modules.apuestas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.N3Constants;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.apuestas.QPAY_GetUrlResponse;
import com.blm.qiubopay.models.apuestas.TicketInfo;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.utils.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.interfaces.IFunction;

public class Fragment_apuestas_webview extends HFragment implements IMenuContext {

    private View view;
    private Object data;

    private WebView webView;

    private QPAY_GetUrlResponse response;

    public static Fragment_apuestas_webview newInstance(Object... data) {
        Fragment_apuestas_webview fragment = new Fragment_apuestas_webview();
        Bundle args = new Bundle();

        if (data.length > 0)
            args.putString("Fragment_apuestas_webview", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_GetUrlResponse.QPAY_GetUrlResponseExcluder()).create();
            String json = getArguments().getString("Fragment_apuestas_webview");
            this.response = gson.fromJson(json, QPAY_GetUrlResponse.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_apuestas_webview, container, false), R.drawable.background_splash_header_1);

    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .showLogo()
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        webView = getView().findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getContextMenu().loading(false);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                Log.d("InternetActivity", request.getUrl() + "");

                if (("" + request.getUrl()).contains("appPrintTicket")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.stopLoading();
                            printTicket("" + request.getUrl());
                        }
                    });
                } else if (("" + request.getUrl()).contains("appGoToHome")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.stopLoading();
                            getContext().backFragment();
                            //goToHome();
                        }
                    });
                }

                return super.shouldInterceptRequest(view, request);
            }

        });

        /*if(isLocalTest == true)
            webView.loadUrl(url_browser);
        else*/

        webView.loadUrl(response.getQpay_object()[0].getUrl());

    }

    @Override
    public void onPause() {
        Log.d("DEBUG", "OnPause");
        super.onPause();

        getContextMenu().cargarSaldo(true,false,false,new IFunction() {
            @Override
            public void execute(Object[] data) {

            }
        });
    }

    private String getDataFromUrl(String url){
        String back = "";
        String[] array = url.split("data=");

        if(array.length>1)
            back = array[1];

        return back;
    }

    private List<TicketInfo> getRows(String data){

        List<TicketInfo> rows = new ArrayList<>();
        String[] lines = data.split("°");
        TicketInfo ticketInfo;

        try {
            for (int i = 0; i < lines.length; i++) {
                String[] info = lines[i].split("\\|");
                ticketInfo = new TicketInfo();

                ticketInfo.setAlign(info[0]);
                ticketInfo.setFontType(info[1]);
                ticketInfo.setLeftText(info[2]);
                if (info.length > 3)
                    ticketInfo.setRightText(info[3]);
                else
                    ticketInfo.setRightText("");

                rows.add(ticketInfo);
            }
        }catch (Exception e){
            rows = null;
        }

        return rows;
    }

    private AlignEnum getAlign(String align)
    {
        AlignEnum back = AlignEnum.LEFT;

        if(align.equals("L"))
            back = AlignEnum.LEFT;
        else if(align.equals("R"))
            back = AlignEnum.RIGHT;
        else if(align.equals("C"))
            back = AlignEnum.CENTER;

        return back;
    }

    void printTicket(String data){
        Log.d("APUESTAS-DEPORTIVAS","Solicitud de impresión de ticket");

        /*data = data.replace("&vert;","|");
        data = data.replace("&deg;","°");
        data = data.replace("%20"," ");
        data = data.replace("%22","\"");
        data = data.replace("%3C","<");
        data = data.replace("%3E",">");

        data = data.replace("&frac12;",""+(char)189);
        data = data.replace("&frac14;",""+(char)188);
        data = data.replace("&frac34;",""+(char)190);*/

        /*try {
            data = URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            data = "";
        }*/

        Log.d("InternetActivity", data);

        List<TicketInfo> rows = getRows(getDataFromUrl(data));
        List<FormattedLine> lines = new ArrayList<FormattedLine>();

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        if(null != rows) {
            //Impresión del ticket
            if (rows.size() > 0) {
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).getAlign().equals("D"))//Es una línea dividida en dos columnas
                    {
                        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG,
                                rows.get(i).getFontType().equals("B") ? N3Constants.fontBold : N3Constants.fontNormal,
                                AlignEnum.LEFT,
                                PrinterLineType.TEXT,
                                "",
                                rows.get(i).getLeftText(),
                                rows.get(i).getRightText()));
                    } else {
                        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG,
                                rows.get(i).getFontType().equals("B") ? N3Constants.fontBold : N3Constants.fontNormal,
                                getAlign(rows.get(i).getAlign()),
                                PrinterLineType.TEXT,
                                rows.get(i).getLeftText()));
                    }
                }

                PrinterManager printerManager = new PrinterManager(getActivity());
                printerManager.printFormattedTicket(lines, getContext());
            }
        }else{
            //Hubo un error de formato en el ticket
            Toast.makeText(getContext(), "Hubo un error en la impresión del ticket.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    /*private void goToHome() {
        //getContextM
        context.initHome();
    }*/

}