package com.blm.qiubopay.modules.apuestas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IApuestasDeportivas;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.apuestas.QPAY_GetBetDetailsPetition;
import com.blm.qiubopay.models.apuestas.QPAY_GetBetDetailsResponse;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_apuestas_folio extends HFragment implements IMenuContext {


    LinearLayout folioEt;
    LinearLayout keyEt;
    ArrayList<CViewEditText> campos;
    Button consultBtn;
    Button paymentBtn;
    CViewEditText.ITextChanged validate;

    private boolean updateBalance = false;

    public static Fragment_apuestas_folio newInstance() {
        return new Fragment_apuestas_folio();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreated(inflater.inflate(R.layout.fragment_apuestas_folio, container, false), R.drawable.background_splash_header_1);

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

         validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate(text);
            }
        };

        campos = new ArrayList<>();
        folioEt = getView().findViewById(R.id.folio_et);
        keyEt = getView().findViewById(R.id.key_et);
        keyEt.setVisibility(View.GONE);
        consultBtn = getView().findViewById(R.id.consult_btn);
        paymentBtn = getView().findViewById(R.id.payment_btn);

        setFields();
        consultBtn.setEnabled(false);

        consultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticket = campos.get(0).getText();
                checkFolioOrStartBetPayment(true, ticket);
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticket = campos.get(0).getText();
                String key = campos.get(1).getText();
                confirmBetPayment(ticket, key);
            }
        });
    }

    private void checkFolioOrStartBetPayment(boolean isCheckFolio, String ticket){

        getContext().loading(true);

        try {
            final QPAY_GetBetDetailsPetition petition = new QPAY_GetBetDetailsPetition();

            petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            petition.setQpay_ticket_number(ticket);

            IApuestasDeportivas service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_GetBetDetailsResponse response = gson.fromJson(json, QPAY_GetBetDetailsResponse.class);
                        Log.d("APUESTAS_DEPORTIVAS", json);

                        if (response.getQpay_response().equals("true")) {
                            checkStatus(response, isCheckFolio, ticket);
                        } else {
                            getContext().loading(false);
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                    }

                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);

                }
            }, getContext());

            if(isCheckFolio)
                service.getBetDetails(petition);
            else
                service.requestFolio(petition);

        } catch (Exception e) {

            getContext().loading(false);

            e.printStackTrace();
            getContext().alert(R.string.general_error_catch);
        }

    }

    private void checkStatus(QPAY_GetBetDetailsResponse response, Boolean isCheckFolio, String ticket){
        if (!isCheckFolio){
            getContext().alert("En breve el cliente recibirá una clave privada, la cual deberá ser ingresada para reclamar el premio.", new IAlertButton() {
                @Override
                public String onText() {
                    return "Aceptar";
                }

                @Override
                public void onClick() {
                    campos.get(0).setEnabled(false);
                    keyEt.setVisibility(View.VISIBLE);
                    consultBtn.setVisibility(View.GONE);
                    paymentBtn.setVisibility(View.VISIBLE);
                }
            });

        }else {
            if (response.getQpay_object()[0].getData().isWinWager()
                    && response.getQpay_object()[0].getData().isCompleteWager()) {
                switch (response.getQpay_object()[0].getData().getIdTicketStatus()) {
                    case 1://Apuesta en estado pendiente.
                        getContext().alert("¡Apuesta ganadora! \nEl premio es por " + Utils.paserCurrency("" + response.getQpay_object()[0].getData().getPaidAmount()) + "  \n¿Desea pagar la apuesta?", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Sí";
                            }

                            @Override
                            public void onClick() {
                                checkFolioOrStartBetPayment(false, ticket);
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "No";
                            }

                            @Override
                            public void onClick() {

                            }
                        });
                    case 2://Apuesta pagada
                        getContext().alert("La apuesta ya fue pagada.");
                        break;
                    case 3://Apuesta cancelada
                        getContext().alert("La apuesta fue cancelada.");
                        break;
                    case 4://Apuesta reembolsada
                        getContext().alert("La apuesta fue reembolsada.");
                        break;
                    case 5://Apuesta expirada
                        getContext().alert("La apuesta fue expirada.");
                        break;
                    case 6://Apuesta cerrada
                        getContext().alert("La apuesta ya fue cerrada.");
                        break;
                    default:
                        getContext().alert("No se puede continuar el proceso de cobro, es estatus de la apuesta es: " + response.getQpay_object()[0].getData().getIdTicketStatus());
                        break;
                }


            } else if (!response.getQpay_object()[0].getData().isWinWager()) {
                //Folio no ganador
                getContext().alert("El folio ingresado no es ganador.");
            } else if (!response.getQpay_object()[0].getData().isCompleteWager()) {
                //Apuesta no finalizada
                getContext().alert("La apuesta aún no ha finalizado, favor de intentar más tarde.");
            }
        }
    }

    private void confirmBetPayment(String ticket, String privateKey){

        getContext().loading(true);

        try {
            final QPAY_GetBetDetailsPetition petition = new QPAY_GetBetDetailsPetition();

            petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            petition.setQpay_ticket_number(ticket);
            petition.setQpay_code(privateKey);

            IApuestasDeportivas service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_GetBetDetailsResponse response = gson.fromJson(json, QPAY_GetBetDetailsResponse.class);
                        Log.d("APUESTAS_DEPORTIVAS",json);

                        if (response.getQpay_response().equals("true")) {
                            getContext().alert("¡Apuesta pagada!  \nSe ha completado el proceso de pago \nEn breve se enviará un mail y/o sms \nconfirmando el pago.",
                                    new IAlertButton() {
                                        @Override
                                        public String onText() {
                                            return "Finalizar";
                                        }

                                        @Override
                                        public void onClick() {
                                            updateBalance = true;
                                            getContext().backFragment();
                                        }
                                    });

                        } else {
                            getContext().loading(false);
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);

                }
            }, getContext());

            service.payBet(petition);

        } catch (Exception e) {

            getContext().loading(false);

            e.printStackTrace();
            getContext().alert(R.string.general_error_catch);
        }

    }

    private void setFields(){
        campos.add(CViewEditText.create(getView().findViewById(R.id.folio_et))
                .setRequired(true)
                .setMinimum(9)
                .setMaximum(12)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint("Folio")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(!campos.isEmpty()) {
                            if(campos.get(0).isValid())
                                consultBtn.setEnabled(true);
                            else
                                consultBtn.setEnabled(false);
                        }

                        validate.onChange(text);

                    }
                }).setText(""));

        campos.add(CViewEditText.create(getView().findViewById(R.id.key_et))
                .setRequired(true)
                .setMinimum(6)
                .setMaximum(6)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint("Clave de confirmación")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setText(""));
    }

    public void validate(String text) {

        paymentBtn.setEnabled(false);

        for(CViewEditText edit: campos)
            if(!edit.isValid())
                return;

        paymentBtn.setEnabled(true);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void onPause() {
        Log.d("DEBUG", "OnPause");
        super.onPause();

        if(updateBalance == true)
            getContextMenu().cargarSaldo(true,false,false,new IFunction() {
                @Override
                public void execute(Object[] data) {

                }
            });
    }
}