package com.blm.qiubopay.modules;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.OcrActivity;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.ocr.OcrMatch;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;

/**
 * A simple {@link androidx.fragment.app.Fragment} subclass.
 */
public class Fragment_registro_financiero_ocr_1 extends HFragment implements IMenuContext {

    private View view;
    private MenuActivity context;

    private ImageView img_photo_frente;
    private ImageView img_photo_detras;
    private Button btn_continuar;

    public static String nombre_negocio;
    private QPAY_FinancialInformation data = SessionApp.getInstance().getFinancialInformation();

    private Uri uri;
    private int imagen = 0;

    //OCR id's de retorno
    private final String CURP_KEY = "CURP";
    private final String NAME_KEY = "NOMBRE";
    private final String ID_KEY = "ID";

    //CURP REGEX
    private final String CURP_REGEX = "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" +
            "(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" +
            "[HM]{1}" +
            "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
            "[B-DF-HJ-NP-TV-Z]{3}" +
            "[0-9A-Z]{1}[0-9]{1}";
    //NOMBRE REGEX
    private final String NAME_1_REGEX = "NOMBRE[\\r\\n]{1,}[A-Z ]{3,}[\\r\\n]{1,}[A-Z ]{3,}[\\r\\n]{1,}[A-Z ]{3,}";
    //ID REGEX
    private final String ID_REGEX = "[0-9]{13}";

    public Fragment_registro_financiero_ocr_1() {
        // Required empty public constructor
    }

    public static Fragment_registro_financiero_ocr_1 newInstance(Object... data) {
        Fragment_registro_financiero_ocr_1 fragment = new Fragment_registro_financiero_ocr_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_registro_financiero_ocr_1", data[0].toString());

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_registro_financiero_ocr_1, container, false);

        setView(view);

        initFragment();

        return view;
    }


    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        if(data == null)
            data = new QPAY_FinancialInformation();

        data.setQpay_image_1(null);
        data.setQpay_image_2(null);

        // Recuperar elementos de pantalla

        img_photo_frente = getView().findViewById(R.id.img_photo_frente);
        ImageView img_add_photo_frente = getView().findViewById(R.id.img_add_photo_frente);
        img_photo_detras = getView().findViewById(R.id.img_photo_detras);
        ImageView img_add_photo_detras = getView().findViewById(R.id.img_add_photo_detras);
        btn_continuar = getView().findViewById(R.id.btn_continuar);

        // Listeners

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.setQpay_identification_type("INE");
                data.setQpay_image_name_1("identification_front.png");
                data.setQpay_image_name_2("identification_behind.png");

               getContext().setFragment(Fragment_registro_financiero_1.newInstance(nombre_negocio));
            }
        });

        View.OnClickListener photo_frente_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = 1;
                //setCameraAction();
                setOCRAction();
            }
        };

        View.OnClickListener photo_detras_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = 2;
                //setCameraAction();
                setOCRAction();
            }
        };

        img_add_photo_frente.setOnClickListener(photo_frente_listener);
        img_photo_frente.setOnClickListener(photo_frente_listener);

        img_add_photo_detras.setOnClickListener(photo_detras_listener);
        img_photo_detras.setOnClickListener(photo_detras_listener);

    }

    /**
     * Construye la acción para ejecutar la camara y tomar la fotografía
     */
    private void setCameraAction() {
        /*
        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {

                        setResultCamera();
                        uri = Utils.getFilePhoto(getContext());
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        getContext().startActivityForResult(i, 1);

                    }
                }, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE});

            }
        }, new String[]{ Manifest.permission.CAMERA });
        */
    }

    /**
     * Construye la respuesta tras tomar la fotografia
     */
    private void setResultCamera(){
        /*
        getContext().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                try {
                    Bitmap image = Utils.decodeSampledBitmapFromFile();
                    String photoEnconded = Utils.convert(image);
                    setPhoto(image,photoEnconded,resultCode);

                    Utils.deleteFilePhoto();
                    uri = null;
                } catch (Exception e) {

                }

            }
        });
        */
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

    /**
     * Método para armar el objeto a hacer match con el OCR
     * @return
     */
    private ArrayList<OcrMatch> buildOCRObjectToMatch(){

        ArrayList<OcrMatch> ocrMatchList = new ArrayList<OcrMatch>();
        OcrMatch ocrMatch;

        if(imagen == 1) {
            ocrMatch = new OcrMatch(CURP_KEY, CURP_REGEX, true, null);
            ocrMatchList.add(ocrMatch);

            ocrMatch = new OcrMatch(NAME_KEY, NAME_1_REGEX, true, null);
            ocrMatchList.add(ocrMatch);

        } else if(imagen == 2){
            ocrMatch = new OcrMatch(ID_KEY, ID_REGEX, true, null);
            ocrMatchList.add(ocrMatch);
        }

        return ocrMatchList;
    }


    /**
     * Metodo para colocar el resultado de lo interpretado por el lector OCR
     */
    private void setResultOCR() {

        getContext().setOnActivityResult(new IActivityResult() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent dataResult) {

                if(requestCode == OcrActivity.RC_OCR_CAPTURE) {
                    if (resultCode == CommonStatusCodes.SUCCESS_CACHE) {
                        if (dataResult != null) {

                            HashMap<String,String> ocrReaded = (HashMap<String, String>) dataResult.getSerializableExtra(OcrActivity.OBJECT_READED);
                            byte[] originalPhoto = dataResult.getByteArrayExtra(OcrActivity.PHOTO_TAKEN);

                            if(ocrReaded!=null) {

                                if(imagen == 1){

                                    String bloqueCurp = ocrReaded.get(CURP_KEY);
                                    Log.d("OcrActivity", "Text read: " + bloqueCurp);
                                    if (bloqueCurp != null && !bloqueCurp.isEmpty()) {

                                        data.setQpay_curp(bloqueCurp);
                                        data.setQpay_rfc(bloqueCurp.substring(0, 10));

                                        String bithdate = buildBirthDate(bloqueCurp.substring(4, 10));
                                        data.setQpay_birth_date(bithdate);

                                        String gender = bloqueCurp.substring(10, 11);
                                        gender = gender.compareTo("H")==0 ? "Masculino" : "Femenino";
                                        data.setQpay_gender(gender);
                                    }

                                    String bloqueNombre = ocrReaded.get(NAME_KEY);
                                    Log.d("OcrActivity", "Text read: " + bloqueNombre);
                                    if (bloqueNombre != null && !bloqueNombre.isEmpty()) {
                                        String nombreArray[] = bloqueNombre.split("\\r?\\n");
                                        data.setQpay_father_surname(nombreArray[1]);
                                        data.setQpay_mother_surname(nombreArray[2]);
                                        data.setQpay_name(nombreArray[3]);
                                    }

                                } else if(imagen == 2){

                                    String bloqueId = ocrReaded.get(ID_KEY);
                                    Log.d("OcrActivity", "Text read: " + bloqueId);
                                    if (bloqueId != null && !bloqueId.isEmpty()) {
                                        data.setQpay_identification(bloqueId);
                                    }

                                }

                            }

                            if(originalPhoto!=null){
                                Bitmap image = Utils.decodeSampledBitmapFromByteArray(originalPhoto);
                                String photoEncoded = Base64.encodeToString(originalPhoto, Base64.DEFAULT);
                                setPhoto(image,photoEncoded,resultCode);
                            }

                        } else {

                            getContext().alert("No se encontró ningún texto");
                            Log.d("OCR", "No Text captured, intent data is null");

                        }
                    } else {

                        Log.d("OCR", "ERROR");

                    }
                }
            }

        });


    }

    /**
     * Metodo para presentar la foto en pantalla
     * @param image
     * @param photoEncoded
     * @param resultCode
     */
    private void setPhoto(Bitmap image, String photoEncoded, int resultCode) {

        Log.d("OcrActivity","Codigo Resultado: " + resultCode);

        if(imagen == 1){

            img_photo_frente.setImageBitmap(image);

            if(resultCode == 0)
                data.setQpay_image_1(null);
            else
                data.setQpay_image_1(photoEncoded);

            //Se solicita que ingrese a la otra pantalla automáticamente para la segunda foto
            if(data.getQpay_image_2()==null){
                getContext().alert(R.string.financiero_ocr_1_foto_auto_detras, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        imagen = 2;
                        setOCRAction();
                    }
                });
            }

            validate();

        } else if(imagen == 2) {

            img_photo_detras.setImageBitmap(image);

            if(resultCode == 0)
                data.setQpay_image_2(null);
            else
                data.setQpay_image_2(photoEncoded);

            validate();

        }
    }

    /**
     * Validacion de campos requeridos
     */
    public void validate() {

        if((data.getQpay_image_1() == null || data.getQpay_image_2() == null)){
            btn_continuar.setEnabled(false);
            return;
        }

        btn_continuar.setEnabled(true);

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


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) context;
    }
}
