package com.blm.qiubopay.modules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ISavePromotions;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.PromotionDTO;
import com.blm.qiubopay.models.bimbo.SavePromotionRequest;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.Random;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_ofertas_bimbo_2 extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;
    private Button btn_solicitar;
    String opcion = "";
    public static PromotionDTO promotion;
    private CViewEditText text_cantidad;

    public static Fragment_ofertas_bimbo_2 newInstance() {
        return new Fragment_ofertas_bimbo_2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_ofertas_bimbo_2, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ImageView img_producto = getView().findViewById(R.id.img_producto);
        Button btn_solicitar = getView().findViewById(R.id.btn_solicitar);
        TextView text_title = getView().findViewById(R.id.text_title);
        TextView text_price = getView().findViewById(R.id.text_price);
        TextView text_subtitle = getView().findViewById(R.id.text_subtitle);
        TextView text_description = getView().findViewById(R.id.text_description);

        text_title.setText(promotion.getPromotion_name());
        text_price.setText(Utils.paserCurrency(promotion.getPiece_priece() + ""));
        text_subtitle.setText(promotion.getExp_date().split(" ")[0]);
        text_description.setText(promotion.getPromo_detail());

        Utils.setImagePromotion(promotion.getPromotion_code() + "", img_producto);

        text_cantidad = CViewEditText.create(getView().findViewById(R.id.edit_amount))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(2)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_ofertas_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if(text_cantidad != null) {
                            if(text_cantidad.isValid())
                                btn_solicitar.setEnabled(true);
                            else
                                btn_solicitar.setEnabled(false);
                        }
                    }
                });

        btn_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePromotion(promotion.getPromotion_code(), text_cantidad.getTextInteger());
            }
        });

    }

    public void savePromotion(String code, String cantidad){

        getContext().loading(true);

        try {
            SavePromotionRequest savePromotionRequest = new SavePromotionRequest();
            savePromotionRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            savePromotionRequest.setPromotion_code(code);
            savePromotionRequest.setQuantity(Integer.parseInt(cantidad));

            CApplication.setAnalytics(CApplication.ACTION.CB_GANA_MAS_aplican, ImmutableMap.of(CApplication.ACTION.OPCION.name(), opcion));

            ISavePromotions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_BaseResponse response = new Gson().fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            Random rand = new Random();
                            int number = rand.nextInt((99999 - 100) + 1) + 10;

                            getContext().alert("Tú código de redención es " + number + "", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    getContextMenu().initHome();
                                }
                            });

                        } else {

                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());

                            getContext().loading(false);

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.savePromotions(savePromotionRequest);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}