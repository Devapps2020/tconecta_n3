package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IQualifySeller;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.QualifySellerRequest;
import com.blm.qiubopay.models.bimbo.SellerUserDTO;
import com.blm.qiubopay.models.bimbo.SellerUserResponse;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.HashMap;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_proveedor_bimbo_2 extends HFragment {

    private View view;
    private MenuActivity context;
    private Object data;
    private Button btn_calificar;
    private Button btn_reportar;
    private ImageView image_star_1,image_star_2,image_star_3,image_star_4,image_star_5;
    private ImageView image_star_11,image_star_22,image_star_33,image_star_44,image_star_55;

    private int valor = 0;
    private CViewEditText text_message, text_date;
    public static SellerUserDTO seller;

    public static Fragment_proveedor_bimbo_2 newInstance(Object... data) {
        Fragment_proveedor_bimbo_2 fragment = new Fragment_proveedor_bimbo_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_soporte_bimbo_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
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

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_soporte_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_proveedor_bimbo_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment() {

        TextView text_organization = getView().findViewById(R.id.text_organization);
        text_organization.setText("Evalúa a tu vendedor " + seller.getOrganization().getOrganization_name());

        ImageView profile_image = view.findViewById(R.id.profile_image);
        TextView text_nombre = view.findViewById(R.id.text_nombre);
        TextView text_feche = view.findViewById(R.id.text_fecha);

        CardView card_evaluacion = view.findViewById(R.id.card_evaluacion);
        LinearLayout layout_evaluacion = view.findViewById(R.id.layout_evaluacion);

        String[] names = seller.getSeller_name().split(" ");
        text_nombre.setText(Html.fromHtml(names[0] + "<br><b>" + names[1] + "</b>"));
        Utils.setImageSeller(seller.getSeller_id() + "", profile_image);

        btn_calificar = view.findViewById(R.id.btn_calificar);
        btn_reportar = view.findViewById(R.id.btn_reportar);

        text_message = CViewEditText.create(view.findViewById(R.id.edit_message))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.MULTI_TEXT)
                .setHint(R.string.text_proveedores_2)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validate();
                    }
                });

        text_date = CViewEditText.create(view.findViewById(R.id.edit_date))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NONE)
                .setDatePicker()
                .setHint(R.string.text_ofertas_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        validate();
                    }
                });

        btn_reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setFragment(Fragment_soporte_bimbo_1.newInstance());

            }
        });

        btn_calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valor == 0){
                    return;
                }
                sendValoracion();
            }
        });


        image_star_1 = view.findViewById(R.id.image_star_1);
        image_star_2 = view.findViewById(R.id.image_star_2);
        image_star_3 = view.findViewById(R.id.image_star_3);
        image_star_4 = view.findViewById(R.id.image_star_4);
        image_star_5 = view.findViewById(R.id.image_star_5);

        if(seller.getSeller_qualify() != null) {
            activateStar(Integer.parseInt(("" + seller.getSeller_qualify()).split("\\.")[0] ));
            card_evaluacion.setVisibility(View.GONE);
        }

        image_star_11 = view.findViewById(R.id.image_star_1_2);
        image_star_22 = view.findViewById(R.id.image_star_2_2);
        image_star_33 = view.findViewById(R.id.image_star_3_2);
        image_star_44 = view.findViewById(R.id.image_star_4_2);
        image_star_55 = view.findViewById(R.id.image_star_5_2);

        image_star_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStar2(1);
            }
        });

        image_star_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStar2(2);
            }
        });

        image_star_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStar2(3);
            }
        });

        image_star_44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStar2(4);
            }
        });

       image_star_55.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                activateStar2(5);
            }
        });

        activateStar2(0);
    }

    public void validate() {

        if(text_message.isValid() && text_date.isValid() && valor > 0)
            btn_calificar.setEnabled(true);
        else
            btn_calificar.setEnabled(false);

    }

    public void activateStar(int valor){

        image_star_1.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_2.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_3.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_4.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_5.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));

        switch (valor) {
            case 5:
                image_star_5.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 4:
                image_star_4.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 3:
                image_star_3.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 2:
                image_star_2.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 1:
                image_star_1.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
        }

    }

    public void activateStar2(int valor){

        this.valor = valor;

        image_star_11.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_22.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_33.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_44.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));
        image_star_55.setColorFilter(getContext().getResources().getColor(R.color.colorBimboGrayLight2));

        switch (valor) {
            case 5:
                image_star_55.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 4:
                image_star_44.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 3:
                image_star_33.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 2:
                image_star_22.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
            case 1:
                image_star_11.setColorFilter(getContext().getResources().getColor(R.color.colorBimboStar));
        }

        validate();
    }

    public void sendValoracion(){

        context.loading(true);

        try {

            QualifySellerRequest qualifySellerRequest = new QualifySellerRequest();
            qualifySellerRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            qualifySellerRequest.setSeller_id(seller.getSeller_id());
            qualifySellerRequest.setSale_center_code("012821");
            qualifySellerRequest.setSeller_qualify(valor);
            qualifySellerRequest.setVisit_date(text_date.getText().replace("/", "-"));
            qualifySellerRequest.setComments(text_message.getText());
            qualifySellerRequest.setRoute_type(seller.getRoute().getRoute_type());

            qualifySellerRequest.setRoute_id(seller.getRoute().getRoute_id() + "");
            qualifySellerRequest.setSeller_name(seller.getSeller_name());
            qualifySellerRequest.setSupervisor_id("");
            qualifySellerRequest.setSupervisor_name("");

            CApplication.setAnalytics(CApplication.ACTION.CB_EVALUANOS_califican, new HashMap<String, String>() {{
                put(CApplication.ACTION.REPARTIDOR.name(), seller.getSeller_id());
                put(CApplication.ACTION.VALOR.name(), "" + valor);
            }});


            IQualifySeller petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                        context.loading(false);

                    } else {

                        context.loading(false);

                        String json = new Gson().toJson(result);
                        SellerUserResponse response = new Gson().fromJson(json, SellerUserResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(valor > 2) {
                                context.alert("Evaluación enviada","Gracias, tus comentarios \nnos ayudan a mejorar.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Entendido";
                                    }

                                    @Override
                                    public void onClick() {
                                        context.initHome();
                                    }
                                });

                            } else {
                                context.alert(getContext().getResources().getDrawable(R.drawable.icons_puntos), "¿Necesitas ayuda?", "Pronto nos pondremos en contacto contigo \npara solucionar tu problema, gracias.", new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Necesito ayuda";
                                    }

                                    @Override
                                    public void onClick() {

                                        Fragment_soporte_bimbo_1.soporte = false;
                                        context.setFragment(Fragment_soporte_bimbo_1.newInstance());

                                    }
                                }, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "cancelar";
                                    }

                                    @Override
                                    public void onClick() {

                                        context.initHome();

                                    }
                                });
                            }

                        } else {
                           context.validaSesion(response.getQpay_code(), response.getQpay_description());
                            context.loading(false);
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);

            petition.qualifySeller(qualifySellerRequest);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }


}

