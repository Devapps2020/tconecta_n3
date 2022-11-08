package com.blm.qiubopay.modules.chambitas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.OcrActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HOcrActivity;
import com.blm.qiubopay.helpers.interfaces.IOcrListener;
import com.blm.qiubopay.listeners.IChallengeDoneOrder;
import com.blm.qiubopay.listeners.IGetCampaignActive;
import com.blm.qiubopay.listeners.IMakeChallengePhoto;
import com.blm.qiubopay.listeners.IMakeChallengeQR;
import com.blm.qiubopay.listeners.IMakeChallengeQuestion;
import com.blm.qiubopay.listeners.IMakeChallengeVideo;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.HOcrMatch;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Challenges;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Questions;
import com.blm.qiubopay.models.chambitas.retos.OpenQuestion.Question;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeAnswer;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeOrder;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengePhoto;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengePhotoResponse;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQR;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQRResponse;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQuestion;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQuestionResponse;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeVideo;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeVideoResponse;
import com.blm.qiubopay.models.chambitas.retos.TipoReto;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_chambitas_retos extends HFragment implements IMenuContext {


    public static String campaign_id = "";
    public static List<QPAY_ActiveCampaign_Challenges> list = new ArrayList<>();
    public  QPAY_ActiveCampaign_Challenges selected = null;
    ChallengesAdapter adapter;

    public static Fragment_chambitas_retos newInstance() {
        return new Fragment_chambitas_retos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_2, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ListView list_challenges = getView().findViewById(R.id.list_challenges);

        //ARREGLAR EL SERVICIO DE LA LISTA DE DONDE SE OBTIENE
        adapter = new ChallengesAdapter(getContext(),list);
        list_challenges.setAdapter(adapter);

        list_challenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                selected = list.get(position);

                if(selected.getDone())
                    return;

                switch (selected.getChallengeType()) {
                    case "Foto":
                        setOCRAction(selected.getId());
                        break;
                    case "QR":
                    case "CodigoQR":
                        takeQR(selected.getId());
                        break;
                    case "CodigoBarras":
                        takeBarra(selected.getId());
                        break;
                    case "Quiz":
                    case "Encuesta":

                        Fragment_chambitas_encuesta.tipoReto = selected.getChallengeType().equals("Encuesta") ? TipoReto.ENCUESTA : TipoReto.QUIZ;
                        Fragment_chambitas_encuesta.encuestaImg = selected.getImage();
                        Fragment_chambitas_encuesta.list = selected.getQuestions();
                        Fragment_chambitas_encuesta.execute = new IFunction<Object>() {
                            @Override
                            public void execute(Object[] obj) {
                                viewQuestion(selected.getId() + "", (List<QPAY_ChallengeAnswer>)obj[0]);
                            }
                        };

                        getContext().setFragment(Fragment_chambitas_encuesta.newInstance());

                        break;
                    case "Video":

                        Fragment_chambitas_video.execute = new IFunction() {
                            @Override
                            public void execute(Object[] obj) {
                                Log.e("Video onFinished: ","VideoFinish");
                                 viewVideo(selected.getId() + "");
                            }
                        };

                        getContext().setFragment(Fragment_chambitas_video.newInstance(selected.getLink()));

                        break;

                    case "OrdenImagen":
                    case "OrdenEncuesta":
                        Fragment_chambitas_ordenamiento.indication = selected.getIndication();
                        Fragment_chambitas_ordenamiento.items = (ArrayList<QPAY_ActiveCampaign_Questions>) selected.getQuestions();
                        Fragment_chambitas_ordenamiento.tipoReto = selected.getChallengeType().equals("OrdenImagen")  ? TipoReto.ORDENAMIENTO_IMAGEN : TipoReto.ORDENAMIENTO_PREGUNTA;

                        Fragment_chambitas_ordenamiento.execute = new IFunction<Object>() {
                            @Override
                            public void execute(Object[] obj) {
                                doneOrderChallenge(selected.getId()+ "", (ArrayList<Question>)obj[0]);
                            }
                        };
                        getContext().setFragment(Fragment_chambitas_ordenamiento.newInstance());
                        break;

                    case "PreguntaAbierta":
                        Fragment_chambitas_pregunta_abierta.items = (ArrayList<QPAY_ActiveCampaign_Questions>) selected.getQuestions();
                        Fragment_chambitas_pregunta_abierta.tipoReto =  TipoReto.PREGUNTA_ABIERTA;

                        Fragment_chambitas_pregunta_abierta.execute = new IFunction<Object>() {
                            @Override
                            public void execute(Object[] obj) {
                                doneOrderChallenge(selected.getId()+ "", (ArrayList<Question>)obj[0]);
                            }
                        };
                        getContext().setFragment(Fragment_chambitas_pregunta_abierta.newInstance());
                        break;
                    default:
                        getContext().alert("Reto no disponible");
                }

            }
        });

    }

    public class ChallengesAdapter extends ArrayAdapter<QPAY_ActiveCampaign_Challenges> {

        public ChallengesAdapter(Context context, List<QPAY_ActiveCampaign_Challenges> datos) {
            super(context, 0, datos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_ActiveCampaign_Challenges item = getItem(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_option_new_challenge, parent, false);

            ImageView img_status = convertView.findViewById(R.id.img_status);
            ImageView img_challenge = convertView.findViewById(R.id.img_challenge);
            TextView text_title = (TextView) convertView.findViewById(R.id.text_title);
            TextView text_description = (TextView) convertView.findViewById(R.id.text_description);

            text_title.setText(item.getTitle());
            text_description.setText(item.getDescription());

            img_challenge.setImageDrawable(getContext().getResources().getDrawable(R.drawable.illustrations_objetivos_100_x_100));

            switch (item.getChallengeType()) {
                case "Foto":
                case "QR":
                case "CodigoQR":
                    Utils.setImageReto(item.getLink(), img_challenge);
                    break;
            }

            if(!item.getDone())
                img_status.setVisibility(View.GONE);

            return convertView;
        }
    }

    /**
     *
     */
    private void setOCRAction(String challenge_id){
        setResultOCR(challenge_id);
        Intent intent = new Intent(getContext(), OcrActivity.class);
        getContext().startActivityForResult(intent, OcrActivity.RC_OCR_CAPTURE);
    }

    private void setResultOCR(String challenge_id) {

        getContext().setOnActivityResult(new IActivityResult() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent dataResult) {

                if(requestCode == OcrActivity.RC_OCR_CAPTURE) {
                    if (resultCode == CommonStatusCodes.SUCCESS_CACHE) {
                        if (dataResult != null) {

                            byte[] originalPhoto = dataResult.getByteArrayExtra(OcrActivity.PHOTO_TAKEN);

                            if(originalPhoto!=null){
                                Bitmap image = Utils.decodeSampledBitmapFromByteArray(originalPhoto);
                                setPhoto(image,challenge_id);
                            }

                        } else {
                            Log.d("OCR", "No Text captured, intent data is null");
                        }
                    } else {
                        Log.d("OCR", "ERROR");
                    }
                }
            }

        });

    }

    private void setPhoto(Bitmap image, String challenge_id) {
        String strImg = Utils.convert(image);
        uploadImage(challenge_id, strImg );
    }

/**
 *
 */

    public void takeQR(String challenge_id) {

        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                ScanActivity.action = new ZBarScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {

                        if (result == null)
                            return;

                        String code = result.getContents();

                        if (code.trim().isEmpty())
                            return;

                        getContext().loading(true);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               new Handler().postDelayed(new Runnable() { public void run(){
                                    viewQR(challenge_id, code);
                                }}, 10);
                            }
                        });


                    }
                };

                getContext().startActivity(ScanActivity.class, false);

            }
        }, new String[] {Manifest.permission.CAMERA});

    }

    public void takeBarra(String challenge_id) {

        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                ScanActivity.action = new ZBarScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result result) {

                        if (result == null)
                            return;

                        String code = result.getContents();

                        if (code.trim().isEmpty())
                            return;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() { public void run(){
                                    viewQR(challenge_id, code);
                                }}, 10);
                            }
                        });

                    }
                };

                getContext().startActivity(ScanActivity.class, false);

            }
        }, new String[] {Manifest.permission.CAMERA});

    }

    public void uploadImage(String challenge_id, String base64) {

        getContext().loading(true);

        try {

            QPAY_ChallengePhoto data = new QPAY_ChallengePhoto();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setCampaign_id(campaign_id);
            data.setChallenge_id(challenge_id);
            data.setImage(base64);
            data.setLink(new Date().getTime() + "-" + challenge_id + ".jpg");

            IMakeChallengePhoto petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        String json = new Gson().toJson(result);
                        QPAY_ChallengePhotoResponse response = new Gson().fromJson(json, QPAY_ChallengePhotoResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            //JLH: Se activa consulta de chambitas al regresar a menu: getAllCampaignsActiveNewCount
                            getContextMenu().setCountChambitas(true);
                            getContext().alert(response.getQpay_description(), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    selected.setDone(true);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.makeChallengePhoto(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void viewVideo(String challenge_id) {

        getContext().loading(true);

        try {

            QPAY_ChallengeVideo data = new QPAY_ChallengeVideo();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setCampaign_id(campaign_id);
            data.setChallenge_id(challenge_id);

            IMakeChallengeVideo petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        String json = new Gson().toJson(result);
                        QPAY_ChallengeVideoResponse response = new Gson().fromJson(json, QPAY_ChallengeVideoResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            //JLH: Se activa consulta de chambitas al regresar a menu: getAllCampaignsActiveNewCount
                            getContextMenu().setCountChambitas(true);
                            getContext().alert(response.getQpay_description(), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    selected.setDone(true);
                                    getContext().onBackPressed();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.makeChallengeVideo(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void viewQR(String challenge_id, String code) {

        getContext().loading(true);

        try {

            QPAY_ChallengeQR data = new QPAY_ChallengeQR();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setCampaign_id(campaign_id);
            data.setChallenge_id(challenge_id);
            data.setCode(code);

            IMakeChallengeQR petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        String json = new Gson().toJson(result);
                        QPAY_ChallengeQRResponse response = new Gson().fromJson(json, QPAY_ChallengeQRResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            //JLH: Se activa consulta de chambitas al regresar a menu: getAllCampaignsActiveNewCount
                            getContextMenu().setCountChambitas(true);
                            getContext().alert(response.getQpay_description(), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    selected.setDone(true);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.makeChallengeQR(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void viewQuestion(String challenge_id, List<QPAY_ChallengeAnswer> answers) {

        getContext().loading(true);

        try {

            QPAY_ChallengeQuestion data = new QPAY_ChallengeQuestion();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setCampaign_id(Fragment_chambitas_retos.campaign_id);
            data.setChallenge_id(challenge_id);
            data.setQpay_object(answers);

            IMakeChallengeQuestion petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        String json = new Gson().toJson(result);
                        QPAY_ChallengeQuestionResponse response = new Gson().fromJson(json, QPAY_ChallengeQuestionResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            //JLH: Se activa consulta de chambitas al regresar a menu: getAllCampaignsActiveNewCount
                            getContextMenu().setCountChambitas(true);
                            getContext().alert(response.getQpay_description(), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    selected.setDone(true);
                                    getContext().onBackPressed();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.makeChallengeQuestion(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void doneOrderChallenge(String challenge_id, ArrayList<Question> answers) {

        getContext().loading(true);

        try {

            QPAY_ChallengeOrder data = new QPAY_ChallengeOrder();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setCampaign_id(Fragment_chambitas_retos.campaign_id);
            data.setChallenge_id(challenge_id);
            data.setQpay_object(answers);

            IChallengeDoneOrder petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        String json = new Gson().toJson(result);
                        QPAY_ChallengeQuestionResponse response = new Gson().fromJson(json, QPAY_ChallengeQuestionResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            //JLH: Se activa consulta de chambitas al regresar a menu: getAllCampaignsActiveNewCount
                            getContextMenu().setCountChambitas(true);
                            getContext().alert(response.getQpay_description(), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    selected.setDone(true);
                                    getContext().onBackPressed();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.challengeDoneOrder(data);

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