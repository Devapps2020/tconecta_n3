package com.blm.qiubopay.modules.fincomun.credito;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCCreditoOXXOResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCConsultaCreditosResponse;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaCreditos;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;


public class Fragment_fincomun_pago_credito extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private TextView tv_next_fee,tv_convenio,tv_referencia,tv_code_oxxo;
    private TextView tv_credit_number,tv_frecuency, tv_clabe, tv_client_number,tv_ref_title,tv_monto_oxxo;
    private ImageView iv_code;
    private LinearLayout ll_convenio, ll_referencia, ll_codigo;
    private Spinner spPayment;
    private Double montoOXXO= 0.0;
    public enum TypePaymet{
        OXXO,
        BBVA,
        TELECOM,
        CHEDRAUI,
        BIMBO
    }
    private PaymentAdapter adapter;

    public static Fragment_fincomun_pago_credito newInstance() {
        Fragment_fincomun_pago_credito fragment = new Fragment_fincomun_pago_credito();

        return fragment;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_pago_credito"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_pago_credito, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        tv_credit_number = getView().findViewById(R.id.tv_credit_number);
        tv_frecuency = getView().findViewById(R.id.tv_frecuency);
        tv_clabe = getView().findViewById(R.id.tv_clabe);
        tv_client_number = getView().findViewById(R.id.tv_client_number);

        tv_next_fee = getView().findViewById(R.id.tv_next_fee);
        tv_convenio = getView().findViewById(R.id.tv_convenio);
        tv_referencia = getView().findViewById(R.id.tv_referencia);
        tv_code_oxxo = getView().findViewById(R.id.tv_code_oxxo);
        iv_code = getView().findViewById(R.id.iv_code);
        tv_monto_oxxo = getView().findViewById(R.id.tv_monto_oxxo);
        tv_monto_oxxo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fincomun_ref_oxxo.minimo = 20.0;
                Fragment_fincomun_ref_oxxo.maximo = montoOXXO;
                Fragment_fincomun_ref_oxxo.numCredito = SessionApp.getInstance().getDhListaCreditos().getNumeroCredito();
                getContextMenu().setFragment(Fragment_fincomun_ref_oxxo.newInstance());
            }
        });

        spPayment = getView().findViewById(R.id.spPayment);
        adapter = new PaymentAdapter(getContext(),getData());
        spPayment.setAdapter(adapter);
        spPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateView(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ll_convenio = getView().findViewById(R.id.ll_convenio);
        ll_referencia = getView().findViewById(R.id.ll_referencia);
        ll_codigo = getView().findViewById(R.id.ll_codigo);

        tv_ref_title = getView().findViewById(R.id.tv_ref_title);
        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        shareIt(getImageUri(getContextMenu(),getBitmapFromView(getView().findViewById(R.id.ll_content))));
                    }
                }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});

            }
        });
        SessionApp.getInstance().getDhListaCreditos().setRefOxxo(null);
        fillData();
    }

    private void updateView(Object obj) {
        ModelPayment item = (ModelPayment)obj;
        DHListaCreditos credit = SessionApp.getInstance().getDhListaCreditos();

        switch (item.getTypePaymet()){
            case BBVA:
                tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoExigible()));
                tv_ref_title.setText("Número de Referencia");
                tv_referencia.setText(credit.getRefBBVA());
                ll_referencia.setVisibility(View.VISIBLE);
                ll_convenio.setVisibility(View.VISIBLE);
                ll_codigo.setVisibility(View.GONE);
                iv_code.setVisibility(View.GONE);
                tv_monto_oxxo.setVisibility(View.GONE);
                break;
            case OXXO:
                tv_ref_title.setText("Número de Referencia");
                ll_referencia.setVisibility(View.GONE);
                ll_convenio.setVisibility(View.GONE);
                ll_codigo.setVisibility(View.VISIBLE);
                tv_code_oxxo.setVisibility(View.VISIBLE);
                iv_code.setVisibility(View.GONE);
                tv_monto_oxxo.setVisibility(View.VISIBLE);
                if(credit.getRefOxxo() != null) {
                    tv_code_oxxo.setText(credit.getRefOxxo());
                    tv_next_fee.setText(Utils.paserCurrency(String.valueOf(montoOXXO)));

                }else {
                    getContextMenu().getTokenFC((String... data) -> {
                        if (data!= null) {
                            getContextMenu().getRefOxxo(credit.getNumeroCredito(),true,0.0, data[0], new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    if (data != null && data[0] != null) {
                                        FCCreditoOXXOResponse response = new Gson().fromJson(String.valueOf(data[0]), FCCreditoOXXOResponse.class);
                                        String ref = new String(Base64.decode(response.getReferenciaOxxo(), Base64.DEFAULT));
                                        SessionApp.getInstance().getDhListaCreditos().setRefOxxo(ref);
                                        montoOXXO = response.getMontoPago();
                                        tv_next_fee.setText(Utils.paserCurrency(String.valueOf(montoOXXO)));

                                        tv_code_oxxo.setText(ref);
                                    }
                                }
                            });
                        }
                    });
                }

                break;
            case BIMBO:
                tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoExigible()));
                tv_ref_title.setText("Cuenta CLABE");
                tv_referencia.setText(credit.getCuentaCable());
                ll_referencia.setVisibility(View.VISIBLE);
                ll_convenio.setVisibility(View.GONE);
                ll_codigo.setVisibility(View.GONE);
                iv_code.setVisibility(View.GONE);
                tv_monto_oxxo.setVisibility(View.GONE);
                break;
            case TELECOM:
                tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoExigible()));
                tv_ref_title.setText("Número de Referencia");
                tv_referencia.setText(credit.getRefTelecom());
                ll_referencia.setVisibility(View.VISIBLE);
                ll_convenio.setVisibility(View.GONE);
                ll_codigo.setVisibility(View.GONE);
                tv_code_oxxo.setVisibility(View.GONE);
                iv_code.setVisibility(View.GONE);
                tv_monto_oxxo.setVisibility(View.GONE);
                break;
            case CHEDRAUI:
                tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoExigible()));
                tv_ref_title.setText("Número de Referencia");
                tv_referencia.setText(credit.getRefChedraui());
                ll_referencia.setVisibility(View.GONE);
                ll_convenio.setVisibility(View.GONE);
                ll_codigo.setVisibility(View.VISIBLE);
                tv_code_oxxo.setVisibility(View.GONE);
                iv_code.setVisibility(View.VISIBLE);
                generateBarcode(credit.getRefChedraui(),iv_code);
                tv_monto_oxxo.setVisibility(View.GONE);

                break;
        }
    }

    private void fillData() {
        DHListaCreditos credit = SessionApp.getInstance().getDhListaCreditos();
        tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoExigible()));
        tv_convenio.setText("0715328");
        tv_referencia.setText(credit.getRefBBVA());
        tv_code_oxxo.setText(credit.getRefOxxo());

        FCConsultaCreditosResponse response = SessionApp.getInstance().getFcConsultaCreditosResponse();
        tv_next_fee.setText(Utils.paserCurrency(credit.getSaldoExigible()));

        tv_credit_number.append(" "+credit.getNumeroCredito());
        tv_frecuency.setVisibility(View.GONE);
        tv_clabe.append(" "+credit.getCuentaCable());
        tv_client_number.append((TextUtils.isEmpty(response.getNumClienteSib()))?" "+ HCoDi.numCliente:" "+response.getNumClienteSib());

    }

    private ArrayList<ModelPayment> getData(){
        ArrayList<ModelPayment> data = new ArrayList<>();

        data.add(new ModelPayment(
                TypePaymet.BIMBO,
                "SPEI",
                "",
                R.drawable.illustrations_200_x_200_mi_tienda));
        data.add(new ModelPayment(
                TypePaymet.OXXO,
                "OXXO",
                "Pago en sucursal",
                R.drawable.illustrations_200_x_200_mi_tienda));
        data.add(new ModelPayment(
                TypePaymet.BBVA,
                "BBVA",
                "Pago en sucursal",
                R.drawable.illustrations_200_x_200_mi_tienda));
        data.add(new ModelPayment(
                TypePaymet.TELECOM,
                "TELECOM",
                "Pago en sucursal",
                R.drawable.illustrations_200_x_200_mi_tienda));
        data.add(new ModelPayment(
                TypePaymet.CHEDRAUI,
                "CHEDRAUI",
                "Pago en sucursal",
                R.drawable.illustrations_200_x_200_mi_tienda));
        return data;
    }

    private void generateBarcode(String folio,ImageView iv_barcode) {
        Bitmap bitmap = null;

        try {

            bitmap = encodeAsBitmap(folio, BarcodeFormat.CODE_128, 600, 300);
            iv_barcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public Bitmap getBitmapFromView(View view) {
        btn_next.setVisibility(View.GONE);
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        btn_next.setVisibility(View.VISIBLE);
        return returnedBitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    private void shareIt(Uri uri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "Pago de tus cuotas Fincomún";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public class ModelPayment{

        private TypePaymet typePaymet;
        private String title;
        private String description;
        private Integer image;

        public ModelPayment(TypePaymet typePaymet, String title, String description, Integer image) {
            this.typePaymet = typePaymet;
            this.title = title;
            this.description = description;
            this.image = image;
        }

        public TypePaymet getTypePaymet() {
            return typePaymet;
        }

        public void setTypePaymet(TypePaymet typePaymet) {
            this.typePaymet = typePaymet;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getImage() {
            return image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }
    }

    public class PaymentAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ModelPayment> items;

        public PaymentAdapter(Context context,ArrayList<ModelPayment> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.item_payment_fincomun,parent,false);
            }

            final  ModelPayment item = (ModelPayment) this.getItem(position);

            TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
            // TextView tv_mode_payment = (TextView) view.findViewById(R.id.tv_mode_payment);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_icon_payment);

            tv_type.setText(item.getTitle());
            // tv_mode_payment.setText(item.getDescription());
            iv.setImageResource(item.getImage());

            return view;
        }
    }
}