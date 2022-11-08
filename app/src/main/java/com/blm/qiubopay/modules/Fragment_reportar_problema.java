package com.blm.qiubopay.modules;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.ISaveErrorReport;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.soporte.QPAY_CommonError;
import com.blm.qiubopay.models.soporte.QPAY_CommonErrorResponse;
import com.blm.qiubopay.models.soporte.QPAY_ErrorReport;
import com.blm.qiubopay.models.soporte.QPAY_ErrorReportResponse;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.interfaces.IAlertButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_reportar_problema#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_reportar_problema extends HFragment {

    private static final String TAG = "reportar_problema";

    private QPAY_CommonErrorResponse response;
    private QPAY_CommonError commonError;

    private ArrayList<CViewEditText> campos;
    private Button btn_soporte_enviar;

    public Fragment_reportar_problema() {
        // Required empty public constructor
    }

    public static Fragment_reportar_problema newInstance(Object... data) {
        Fragment_reportar_problema fragment = new Fragment_reportar_problema();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_reportar_problema", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_CommonErrorResponse.QPAY_CommonErrorResponseExcluder()).create();
            response = gson.fromJson(getArguments().getString("Fragment_reportar_problema"), QPAY_CommonErrorResponse.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_reportar_problema, container, false),R.drawable.background_splash_header_3);
    }

    public void initFragment(){

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        btn_soporte_enviar = getView().findViewById(R.id.btn_soporte_enviar);

        String cellphone = AppPreferences.getUserProfile() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone() : "";

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_soporte_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setText(cellphone));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_mensaje))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(255)
                .setType(CViewEditText.TYPE.MULTI_TEXT)
                .setHint(R.string.text_soporte_26)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_motivo_de_contacto))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.text_soporte_25)
                .setSpinner(getErrorTypeList())
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if(campos.get(2).getText().toUpperCase().compareTo("OTRO")==0){
                            campos.get(1).setHint(R.string.text_soporte_27).setRequired(true);
                        } else {
                            campos.get(1).setHint(R.string.text_soporte_26).setRequired(false);
                        }
                        validate();
                    }
                }));

        campos.get(2).setTag(response.getQpay_object()[0].getId()+"");
        campos.get(2).setText(response.getQpay_object()[0].getDescription());

        btn_soporte_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_soporte_enviar.setEnabled(false);
                sendErrorReport();
            }
        });

    }


    private void validate() {
        btn_soporte_enviar.setEnabled(false);
        for(int i=0; i<campos.size(); i++) {
            if (!campos.get(i).isValid()) {
                return;
            }
        }
        btn_soporte_enviar.setEnabled(true);
    }


    private List<ModelItem> getErrorTypeList(){

        List<ModelItem> errorTypeList = new ArrayList<>();

        for (QPAY_CommonError errorType: response.getQpay_object()) {
            errorTypeList.add(new ModelItem(errorType.getDescription(),errorType.getId()+""));
        }

        return errorTypeList;
    }


    public void sendErrorReport() {

        QPAY_ErrorReport request = new QPAY_ErrorReport();
        if(AppPreferences.getUserProfile()!=null) {
            request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        }
        request.setQpay_phone(campos.get(0).getText());
        request.setQpay_description(campos.get(1).getText());
        request.setQpay_error_id(campos.get(2).getTag());

        getContext().loading(true);

        try {

            ISaveErrorReport petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_ErrorReportResponse.QPAY_ErrorReportResponseExcluder()).create();
                        String json = gson.toJson(result);
                        Log.d(TAG,"Response sendErrorReport: " + result);
                        QPAY_ErrorReportResponse response = gson.fromJson(json, QPAY_ErrorReportResponse.class);

                        if(response.getQpay_response().equals("true")){

                            getContext().alert("Su reporte ha sido enviado con éxito. Su folio de reporte es "
                                    + response.getQpay_object()[0].getQpay_folio() + ".", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    getContext().backFragment();
                                }
                            });

                        } else {

                            getContext().alert("Hubo un error al guardar su reporte.");
                            btn_soporte_enviar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_soporte_enviar.setEnabled(true);
                }
            }, getContext());

            petition.saveErrorReport(request);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_soporte_enviar.setEnabled(true);
        }

    }

}