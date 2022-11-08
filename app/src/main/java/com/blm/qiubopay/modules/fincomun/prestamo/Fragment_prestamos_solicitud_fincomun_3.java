package com.blm.qiubopay.modules.fincomun.prestamo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import mx.devapps.utils.components.HActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class Fragment_prestamos_solicitud_fincomun_3 extends HFragment {

    private View view;
    private HActivity context;
    private Object data;
    private Button btn_save;
    private Button btn_next;

    private PDFView pdfView;
    private Retrofit retrofit;

    public static Fragment_prestamos_solicitud_fincomun_3 newInstance(Object... data) {
        Fragment_prestamos_solicitud_fincomun_3 fragment = new Fragment_prestamos_solicitud_fincomun_3();
        Bundle args = new Bundle();

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
            data = new Gson().fromJson(getArguments().getString("Fragment_prestamos_solicitud_fincomun_3"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_prestamos_solicitud_fincomun_3, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){


        btn_next = view.findViewById(R.id.btn_next);
        btn_save = view.findViewById(R.id.btn_save);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.TRES);
                AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                context.setFragment(Fragment_prestamos_solicitud_fincomun_4.newInstance());*/
                context.backFragment();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.DOS);
                AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                context.alert("Datos Guardados");*/
                context.backFragment();
            }
        });

        context.loading(true);

        pdfView = (PDFView) view.findViewById(R.id.pdfView);

        String encode_url = "https://www.fincomun.com.mx/descargas/aviso/fc-aviso-privacidad.pdf";
        getPDF(encode_url);
    }

    private void getPDF(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.fincomun.com.mx/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RetrofitPDFFC services = retrofit.create(RetrofitPDFFC.class);

        Call<ResponseBody> responseCall = services.downloadPDF(url);
        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showPdf(response.body());
                }else {
                    context.loading(false);
                    pdfView.fromAsset("fincomun/fc-aviso-privacidad.pdf").load();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                context.loading(false);
                pdfView.fromAsset("fincomun/fc-aviso-privacidad.pdf").load();

            }
        });

    }

    private boolean showPdf(ResponseBody response) {
        InputStream inputStream = null;

        try {
            inputStream = response.byteStream();
            pdfView.fromStream(inputStream)
                    .enableDoubletap(true)
                    .load();
            context.loading(false);
            return true;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    context.loading(false);
                }
            }
        }
    }



}
interface RetrofitPDFFC {
    @GET
    Call<ResponseBody>  downloadPDF(@Url String fileUrl);
}