package com.blm.qiubopay.modules.remesas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.OcrActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ocr.OcrMatch;
import com.blm.qiubopay.models.remesas.TC_PayRemittancePetition;
import com.blm.qiubopay.models.remesas.TC_RemittanceResponse1;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_remesas_3 extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos;
    private ArrayList<RadioButton> options;
    private Button btn_continuar;
    private Button btn_take_1;
    private Button btn_take_2;

    private LinearLayout layout_back;

    private String date_control_string = "dd/mm/aaaa";

    private TC_RemittanceResponse1 remittanceResponse;
    private TC_PayRemittancePetition payRemittancePetition;

    private String front_image  = "";
    private String back_image = "";

    private Object data;

    private enum PhotoType {
        FRONT,
        BACK
    }

    private enum IdType {
        CURP,
        ELECTOR,
        PASAPORTE
    }

    private int identificationType;
    private int newIdentificationType;

    private PhotoType photoType;

    private int gender = 0;

    //CURP REGEX
    private final String PASAPORTE_ID_REGEX = "[A-Z]{1}" + "[0-9]{8}";
    private final String PASAPORTE_CLAVE_REGEX = "[A-Z]{1}" +
            "[0-9]{9}" +
            "[A-Z]{3}" +
            "[0-9]{7}" +
            "[HM]{1}";
    private final String CURP_REGEX = "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" +
            "(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" +
            "[HM]{1}" +
            "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
            "[B-DF-HJ-NP-TV-Z]{3}" +
            "[0-9A-Z]{1}[0-9]{1}";
    private final String ELECTOR_REGEX = "[A-Z]{6}[0-9]{8}" +
            "[HM]{1}[0-9]{3}";
    private final String INE_REGEX = "[I]{1}[D]{1}[M]{1}[E]{1}[X]{1}" +
            "[0-9]{10}";
    private final String IFE_REGEX = "[0-9]{13}";

    //OCR id's de retorno
    private final String CURP_KEY = "CURP";
    private final String IFE_KEY = "IFE";
    private final String ELECTOR_KEY = "ELECTOR";
    private final String INE_KEY = "INE";
    private final String PASSPORT_KEY = "PASSPORT";
    private final String PASSPORT_KEY_2 = "PASSPORT2";

    public static Fragment_remesas_3 newInstance(Object... data) {
        Fragment_remesas_3 fragment = new Fragment_remesas_3();
        Bundle args = new Bundle();

        if(data.length > 0) {
            args.putString("Fragment_remesas_3"     , new Gson().toJson(data[0]));
            args.putString("Fragment_remesas_3_tipo", new Gson().toJson(data[1]));
        }

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_remesas_3 newInstance() {
        return new Fragment_remesas_3();
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_remesas_3, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            data                = new Gson().fromJson(getArguments().getString("Fragment_remesas_3"), Object.class);
            identificationType  = Integer.parseInt(getArguments().getString("Fragment_remesas_3_tipo"));
        }
    }

    @Override
    public void initFragment() {

        campos = new ArrayList();

        Gson gson = new Gson();
        String queryData = gson.toJson(data);
        remittanceResponse = gson.fromJson(queryData, TC_RemittanceResponse1.class);

        payRemittancePetition = new TC_PayRemittancePetition();

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
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

        //Botones
        btn_continuar   = getView().findViewById(R.id.btn_continuar);
        btn_take_1      = getView().findViewById(R.id.btn_take_1);
        btn_take_2      = getView().findViewById(R.id.btn_take_2);

        //Layout foto por detrás
        layout_back     = getView().findViewById(R.id.layout_back);

        //Identificación
        TextView text_tipo_1 = getView().findViewById(R.id.text_tipo_1);
        TextView text_tipo_2 = getView().findViewById(R.id.text_tipo_2);

        ImageView img_front = getView().findViewById(R.id.img_front);
        ImageView img_revert = getView().findViewById(R.id.img_revert);

        switch(identificationType){
            case 1://INE
                text_tipo_1.setText("INE\nfrente");
                text_tipo_2.setText("INE\nreverso");

                img_front.setImageResource(R.drawable.illustrations_ife_100_x_100);
                img_revert.setImageResource(R.drawable.illustrations_reverso_ife_100_x_100);

                newIdentificationType = 1;
                break;
            case 2://IFE
                text_tipo_1.setText("IFE\nfrente");
                text_tipo_2.setText("IFE\nreverso");

                img_front.setImageResource(R.drawable.illustrations_ine_100_x_100_2);
                img_revert.setImageResource(R.drawable.illustrations_nie_reverso_100_x_100);

                newIdentificationType = 1;
                break;
            case 3://PASAPORTE
                text_tipo_1.setText("Pasaporte\nfrontal");
                text_tipo_2.setText("Pasaporte\nreverso");

                img_front.setImageResource(R.drawable.illustrations_passport_100_x_100);
                img_revert.setImageResource(R.drawable.illustrations_reverso_passport_100_x_100);

                newIdentificationType = 2;

                layout_back.setVisibility(View.GONE);

                break;
        }

        //Campos de entrada
        //0 - ID de la identificación
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id))
                .setRequired(true)
                .setMinimum(9)
                .setMaximum(identificationType == 3 ? 11 : 13)
                .setType(identificationType == 3 ? CViewEditText.TYPE.TEXT : CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_remesas_11)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //1 - Primer nombre
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_primer_nombre))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_2)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //2 - Segundo nombre
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_segundo_nombre))
                .setRequired(remittanceResponse.getBeneficiario().getSegundoNombre().trim().equals("") ? false : true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_9)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //3 - Primer apellido
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_primer_apellido))
                .setRequired(true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_3)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //4 - Segundo apellido
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_segundo_apellido))
                .setRequired(remittanceResponse.getBeneficiario().getSegundoApellido().trim().equals("") ? false : true)
                .setMinimum(3)
                .setMaximum(30)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_remesas_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //5 - Fecha de nacimiento
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_fecha_nacimiento))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NONE)
                .setDatePicker(true)
                .setHint(R.string.text_remesas_12)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));
        campos.get(5).setEnabled(false);
        campos.get(5).setText(date_control_string);

        //6 - Número celular
        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_remesas_13)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        btn_take_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoType = PhotoType.FRONT;
                setOCRAction();
            }
        });
        btn_take_1.setBackgroundResource(R.drawable.background_button_principal_disabled);

        btn_take_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoType = PhotoType.BACK;
                setOCRAction();
            }
        });
        btn_take_2.setBackgroundResource(R.drawable.background_button_principal_disabled);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().authPIN(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().backFragment();
                        updatePetitionObject();
                        getContext().setFragment(Fragment_remesas_4.newInstance(payRemittancePetition));
                    }
                }, true);
            }
        });

        options = new ArrayList();

        options.add((RadioButton) getView().findViewById(R.id.rad_genero_h));
        options.add((RadioButton) getView().findViewById(R.id.rad_genero_m));

        // Listeners para elementos en pantalla
        options.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 2;
                options.get(1).setChecked(false);
                validate();
            }
        });

        options.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 1;
                options.get(0).setChecked(false);
                validate();
            }
        });

        updateInfo();

        if(identificationType == 3)
            getContext().alert(R.string.alert_message_remesas_5);
        else
            getContext().alert(R.string.alert_message_remesas_4);
    }

    private void validate(){

        btn_continuar.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(i == 0){
                if(identificationType == 3){//Pasaporte
                    if (!campos.get(i).isValid() || (campos.get(i).getText().length() != 11 && !Utils.matchRegex(PASAPORTE_ID_REGEX, campos.get(i).getText()))){
                        return;
                    }
                }else{
                    if(campos.get(i).getText().length() != 9 && campos.get(i).getText().length() != 10 && campos.get(i).getText().length() != 13){
                        return;
                    }
                }
            }else {
                if (!campos.get(i).isValid()) {
                    return;
                }
            }

        if(identificationType == 3){
            if(front_image.equals("") || gender == 0)
                return;
        }else{
            if(front_image.equals("") || back_image.equals("") || gender == 0)
                return;
        }

        btn_continuar.setEnabled(true);

    }

    private void updateInfo(){
        campos.get(1).setText(!remittanceResponse.getBeneficiario().getPrimerNombre().trim().equals("") ? remittanceResponse.getBeneficiario().getPrimerNombre().trim() : "");
        campos.get(2).setText(!remittanceResponse.getBeneficiario().getSegundoNombre().trim().equals("") ? remittanceResponse.getBeneficiario().getSegundoNombre().trim() : "");
        campos.get(3).setText(!remittanceResponse.getBeneficiario().getPrimerApellido().trim().equals("") ? remittanceResponse.getBeneficiario().getPrimerApellido().trim() : "");
        campos.get(4).setText(!remittanceResponse.getBeneficiario().getSegundoApellido().trim().equals("") ? remittanceResponse.getBeneficiario().getSegundoApellido().trim() : "");
        campos.get(6).setText(Tools.getOnlyNumbers(remittanceResponse.getBeneficiario().getTelefono().trim()));
    }

    private void updatePetitionObject(){
        payRemittancePetition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        payRemittancePetition.setAccountNumber(remittanceResponse.getRemesa().getAccountNumber());
        payRemittancePetition.setIdRemesa(""+remittanceResponse.getRemesa().getIdRemesa());
        payRemittancePetition.setAmount(""+remittanceResponse.getRemesa().getMontoMonedaPago());

        payRemittancePetition.getBeneficiario().setPrimerNombre(campos.get(1).getText());
        payRemittancePetition.getBeneficiario().setSegundoNombre(campos.get(2).getText());
        payRemittancePetition.getBeneficiario().setPrimerApellido(campos.get(3).getText());
        payRemittancePetition.getBeneficiario().setSegundoApellido(campos.get(4).getText());
        payRemittancePetition.getBeneficiario().setTelefono(campos.get(6).getText());
        payRemittancePetition.getBeneficiario().setIdTipoIdentificacion(newIdentificationType);
        payRemittancePetition.getBeneficiario().setNumeroIdentificacion(campos.get(0).getText());
        payRemittancePetition.getBeneficiario().setImagenFrenteIdentificacion(front_image);
        payRemittancePetition.getBeneficiario().setImagenReversoIdentificacion(back_image);
        payRemittancePetition.getBeneficiario().setImagenFirmaIdentificacion("");
        payRemittancePetition.getBeneficiario().setIdGenero(gender);
        payRemittancePetition.getBeneficiario().setFechaNacimiento(campos.get(5).getText());

        payRemittancePetition.setRemitente(remittanceResponse.getRemitente());
    }

    /**
     * Arma la accion para ejecutar OCR
     */
    private void setOCRAction(){
        setResultOCR();
        Intent intent = new Intent(getContext(), OcrActivity.class);
        intent.putParcelableArrayListExtra(OcrActivity.OBJECT_TO_READ, buildOCRObjectToMatch());
        getContext().startActivityForResult(intent, OcrActivity.RC_OCR_CAPTURE);
    }

    private ArrayList<OcrMatch> buildOCRObjectToMatch(){

        ArrayList<OcrMatch> ocrMatchList = new ArrayList<OcrMatch>();
        OcrMatch ocrMatch;

        if(identificationType == 1) {//INE

            if(photoType == PhotoType.FRONT) {
                ocrMatch = new OcrMatch(CURP_KEY, CURP_REGEX, true, null);
                ocrMatchList.add(ocrMatch);
            }else if(photoType == PhotoType.BACK) {
                ocrMatch = new OcrMatch(INE_KEY, INE_REGEX, true, null);
                ocrMatchList.add(ocrMatch);
            }

        } else if(identificationType == 2) {//IFE

            if(photoType == PhotoType.FRONT) {
                ocrMatch = new OcrMatch(ELECTOR_KEY, ELECTOR_REGEX, true, null);
                ocrMatchList.add(ocrMatch);
            }else if(photoType == PhotoType.BACK) {
                ocrMatch = new OcrMatch(IFE_KEY, IFE_REGEX, true, null);
                ocrMatchList.add(ocrMatch);
            }

        } else if(identificationType == 3) {//PASAPORTE

            //if(photoType == PhotoType.FRONT) {
                //ocrMatch = new OcrMatch(CURP_KEY, CURP_REGEX, true, null);
                //ocrMatchList.add(ocrMatch);
                ocrMatch = new OcrMatch(PASSPORT_KEY_2, PASAPORTE_CLAVE_REGEX, true, null);
                ocrMatchList.add(ocrMatch);

                //ocrMatch = new OcrMatch(PASSPORT_KEY, PASAPORTE_ID_REGEX, true, null);
                //ocrMatchList.add(ocrMatch);
            /*}else if(photoType == PhotoType.BACK) {
                ocrMatch = new OcrMatch(PASSPORT_KEY, PASAPORTE_ID_REGEX, true, null);
                ocrMatchList.add(ocrMatch);
            }*/

        }

        return ocrMatchList;
    }


    /**
     * Metodo para colocar el resultado de lo interpretado por el lector OCR
     */
    private void setResultOCR() {

        getContextMenu().setOnActivityResult(new IActivityResult() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent dataResult) {
                try {
                    if (requestCode == OcrActivity.RC_OCR_CAPTURE) {
                        if (resultCode == CommonStatusCodes.SUCCESS_CACHE) {
                            if (dataResult != null) {

                                HashMap<String, String> ocrReaded = (HashMap<String, String>) dataResult.getSerializableExtra(OcrActivity.OBJECT_READED);
                                String curp = "";

                                if (identificationType == 1) {//INE
                                    if (photoType == PhotoType.FRONT) {
                                        curp = ocrReaded.get(CURP_KEY);
                                        Log.d("OcrActivity", "Text read: " + curp);

                                        if (null != curp && !curp.isEmpty()) {
                                            setValuesWithCurp(curp, IdType.CURP);
                                        }

                                    } else if (photoType == PhotoType.BACK) {
                                        String clave_ine = ocrReaded.get(INE_KEY);
                                        Log.d("OcrActivity", "Text read: " + clave_ine);

                                        if (null != clave_ine && !clave_ine.isEmpty())
                                            campos.get(0).setText(Tools.getOnlyNumbers(clave_ine));
                                    }
                                } else if (identificationType == 2) {//IFE
                                    if (photoType == PhotoType.FRONT) {
                                        String clave_elector = ocrReaded.get(ELECTOR_KEY);
                                        Log.d("OcrActivity", "Text read: " + clave_elector);

                                        if (null != clave_elector && !clave_elector.isEmpty()) {
                                            setValuesWithCurp(clave_elector, IdType.ELECTOR);
                                        }
                                    } else if (photoType == PhotoType.BACK) {
                                        String clave_ife = ocrReaded.get(IFE_KEY);
                                        Log.d("OcrActivity", "Text read: " + clave_ife);

                                        if (null != clave_ife && !clave_ife.isEmpty())
                                            campos.get(0).setText(clave_ife);
                                    }
                                } else if (identificationType == 3) {//PASAPORTE
                                    //if(photoType == PhotoType.FRONT) {
                                    curp = ocrReaded.get(PASSPORT_KEY_2);
                                    Log.d("OcrActivity", "Text read: " + curp);

                                    if (null != curp && !curp.isEmpty()) {
                                        setValuesWithCurp(curp, IdType.PASAPORTE);
                                    }

                                    /*String pasaporte = ocrReaded.get(PASSPORT_KEY);
                                    Log.d("OcrActivity", "Text read: " + pasaporte);

                                    if (null != pasaporte && !pasaporte.isEmpty())
                                        campos.get(0).setText(pasaporte);*/
                                /*} else if(photoType == PhotoType.BACK) {
                                    String pasaporte = ocrReaded.get(PASSPORT_KEY);
                                    Log.d("OcrActivity", "Text read: " + pasaporte);

                                    if(null!=pasaporte && !pasaporte.isEmpty())
                                        campos.get(0).setText(pasaporte);
                                }*/
                                }

                                byte[] originalPhoto = dataResult.getByteArrayExtra(OcrActivity.PHOTO_TAKEN);
                                //byte[] compressPhoto;
                                if (originalPhoto != null) {
                                    Bitmap image = Utils.decodeSampledBitmapFromByteArray(originalPhoto);
                                    //compressPhoto = Utils.compressByteArrayFromBitmap(image, 50);
                                    //String photoEncoded = Base64.encodeToString(compressPhoto, Base64.DEFAULT);
                                    String photoEncoded = Base64.encodeToString(originalPhoto, Base64.DEFAULT);
                                    if (photoType == PhotoType.FRONT) {
                                        front_image = photoEncoded;
                                        btn_take_1.setBackgroundResource(R.drawable.background_button_principal_enabled);
                                    } else if (photoType == PhotoType.BACK) {
                                        back_image = photoEncoded;
                                        btn_take_2.setBackgroundResource(R.drawable.background_button_principal_enabled);
                                    }
                                }

                            } else {
                                Log.d("OCR", "No Text captured, intent data is null");
                            }
                        } else {
                            Log.d("OCR", "ERROR");
                        }
                    }
                }catch (Exception e){
                    Log.e("T-Conecta", "Error al procesar datos leídos por el OCR.");
                }
                validate();
            }

        });

    }

    /**
     * Construye la fecha de nacimiento
     */
    public String buildBirthDate(String date){
        Integer.valueOf(date);
        Integer iYear = Integer.valueOf(date.substring(0,2));
        String actualYear;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            actualYear = Year.now().getValue() + "";
        } else {
            actualYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        }
        actualYear = actualYear.substring(2,4);
        Integer iActualYear = Integer.valueOf(actualYear);
        String year = (iYear>iActualYear ? "19"+iYear : "20"+iYear);
        String month = date.substring(2,4);
        String day = date.substring(4,6);

        return day + "/" + month + "/" + year;//year + "-" + month + "-" + day;
    }

    private void setValuesWithCurp(String curp, IdType idType){

        String birthDate = "";
        String genderString = "";
        String passportId = "";

        try {
            if (idType == IdType.CURP) {
                birthDate = buildBirthDate(curp.substring(4, 10));
                campos.get(5).setText(birthDate);
                genderString = curp.substring(10, 11);
            } else if (idType == IdType.ELECTOR) {
                birthDate = buildBirthDate(curp.substring(6, 12));
                campos.get(5).setText(birthDate);
                genderString = curp.substring(14, 15);
            } else if (idType == IdType.PASAPORTE) {
                birthDate = buildBirthDate(curp.substring(13, 19));
                campos.get(5).setText(birthDate);
                genderString = curp.substring(20, 21);
                passportId = curp.substring(0, 9);
                campos.get(0).setText(passportId);
            }


            Log.d("BIRTH_DATE", birthDate);
            Log.d("GENDER", genderString);

            if (idType == IdType.PASAPORTE) {
                if (genderString.equals("F")) {
                    gender = 1;
                    options.get(0).setChecked(false);
                    options.get(1).setChecked(true);
                } else if (genderString.equals("M")) {
                    gender = 2;
                    options.get(1).setChecked(false);
                    options.get(0).setChecked(true);
                }
            }else {
                if (genderString.equals("M")) {
                    gender = 1;
                    options.get(0).setChecked(false);
                    options.get(1).setChecked(true);
                } else if (genderString.equals("H")) {
                    gender = 2;
                    options.get(1).setChecked(false);
                    options.get(0).setChecked(true);
                }
            }
        }catch (Exception e){
            Log.e("ERROR_PARSER","Error al obtener los datos de la identificación");
        }

        validate();
    }

}
