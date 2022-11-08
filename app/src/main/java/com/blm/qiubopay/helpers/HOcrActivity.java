package com.blm.qiubopay.helpers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.interfaces.IOcrListener;
import com.blm.qiubopay.models.HOcrMatch;
import com.blm.qiubopay.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.devapps.utils.components.HActivity;

public class HOcrActivity extends HActivity {

    public static boolean take_btn = false;

    public static String CURP_REGEX = "[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}";
    public static String RFC_REGEX = "[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[0-9A-Z]{3}";
    public static String NOMBRE_REGEX = "NOMBRE[\\r\\n]{1,}[A-Z ]{3,}[\\r\\n]{1,}[A-Z ]{3,}[\\r\\n]{1,}[A-Z ]{3,}";
    public static String DOMICILIO_REGEX = "DOMICILIO[\\r\\n]{1,}[A-Z0-9,. ]{3,}[\\r\\n]{1,}[A-Z0-9,. ]{3,}[\\r\\n]{1,}[A-Z0-9,. ]{3,}";
    public static String CP_REGEX = " [0-9]{5}";
    public static String SEXO_REGEX = "SEXO [HM]{1}";
    public static String CLAVE_REGEX = "ELECTOR [A-Z0-9]{18}";
    public static String ESTADO_REGEX = "ESTADO [0-9]{2}";
    public static String MUNICIPIO_REGEX = "MUNICIPIO [0-9]{3}";
    public static String LOCALIDAD_REGEX = "LOCALIDAD [0-9]{4}";
    public static String EMISION_REGEX = "EMISIÓN [0-9]{4}";
    public static String REGISTRO_REGEX = "REGISTRO [0-9]{4}";
    public static String OCR_REGEX = "<<[0-9]{13}";
    public static String CIC_REGEX = "[0-9]{10}<<";
    public static String IFE = "FEDERAL";

    int contador = 0;
    public static List<HOcrMatch> matches = new ArrayList();

    public ProgressBar progress_bar;


    private static IOcrListener listener = new IOcrListener() {
        @Override
        public void execute(List<HOcrMatch> data, Bitmap bitmap) {

        }
    };

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    Button btn_cancel, btn_continue;
    View view_line_red;
    TextView text_indicator;

    final int RequestCameraPermission = 1001;
    private boolean take = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hocr);

        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_continue = findViewById(R.id.btn_continue);
        view_line_red = findViewById(R.id.view_line_red);
        text_indicator = findViewById(R.id.text_indicator);
        progress_bar = findViewById(R.id.progress_bar);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().finish();
            }
        });

        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detected dependence are not found ");
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            // 2
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getContext(), new String[]{Manifest.permission.CAMERA}, RequestCameraPermission);
                        }

                        cameraSource.start(cameraView.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    StringBuilder stringBuilder = new StringBuilder();

                                    for (int i = 0 ;i < items.size();i++){
                                        TextBlock item = items.valueAt(i);
                                        stringBuilder.append(item.getValue());
                                        stringBuilder.append("\n");
                                    }

                                    textView.setText(stringBuilder.toString());

                                    checkValue(stringBuilder.toString());

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        });
                    }
                }
            });
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takePhoto();
            }
        }, 10 * 1000);

        if(take_btn) {
            take_btn = false;
            btn_continue.setVisibility(View.VISIBLE);
            view_line_red.setVisibility(View.GONE);
            progress_bar.setVisibility(View.GONE);
            text_indicator.setVisibility(View.GONE);
        }

        setProgress();

    }

    public void checkValue(String value) {


        if(value == null || value.isEmpty())
            return;

        if(matches.isEmpty()){
            btn_continue.setVisibility(View.VISIBLE);
            return;
        }

        contador = 0;


        for (HOcrMatch match : matches) {
            if(!match.isValid()) {

                Pattern pattern = Pattern.compile(match.getMatch());
                Matcher matcher = pattern.matcher(value);

                if (matcher.find())
                    match.setValue(matcher.group(0));

            }

            if(match.isValid()) {
                contador++;
            }

        }

        setProgress();

        for (HOcrMatch match : matches)
            if(!match.isValid())
                return;

        //Execute
        takePhoto();

    }

    public void setProgress() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int percentage = 0;
                if(contador > 0) {
                    percentage = (contador * 100) / matches.size();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress_bar.setProgress(percentage,true);
                } else {
                    progress_bar.setProgress(percentage);
                }

                text_indicator.setText(contador + "/" + matches.size() + " datos encontrados.");
            }
        });

    }

    public static String matcher(String match, String value) {

        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(value);

        if (matcher.find())
            return matcher.group(0);

        return "";
    }

    public static void setListener(IOcrListener listener) {
        HOcrActivity.listener = listener;
    }

    private void takePhoto() {

        try {

            if(!take)
                cameraSource.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {

                        int exifOrientation = getExifOrientation(bytes);
                        Bitmap bmp = Utils.decodeSampledBitmapFromByteArray(bytes);
                        if (exifOrientation > 1) {
                            bmp = getRotatedBitmap(exifOrientation, bmp);
                        }

                        listener.execute(matches, bmp);

                        take = true;

                        finish();

                    }
                });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Integer getExifOrientation(byte[] byteArray){
        int exifOrientation = 0;

        try {

            InputStream inputStream = new ByteArrayInputStream(byteArray);
            Metadata metadata = null;

            metadata = ImageMetadataReader.readMetadata(inputStream);
            final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION))
            {
                exifOrientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        }
        return exifOrientation;
    }



    private Bitmap getRotatedBitmap(int exifOrientation, Bitmap bmp){
        Bitmap newBitmap = null;
        try
        {
            final Matrix bitmapMatrix = new Matrix();
            switch(exifOrientation)
            {
                case 1:																	        break;  // top left
                case 2: bitmapMatrix.postScale(-1, 1);      							    break;  // top right
                case 3: bitmapMatrix.postRotate(180);                                   break;  // bottom right
                case 4: bitmapMatrix.postRotate(180); 	bitmapMatrix.postScale(-1, 1);	break;  // bottom left
                case 5: bitmapMatrix.postRotate(90);  	bitmapMatrix.postScale(-1, 1);	break;  // left top
                case 6: bitmapMatrix.postRotate(90);                                    break;  // right top
                case 7: bitmapMatrix.postRotate(270);	bitmapMatrix.postScale(-1, 1);	break;  // right bottom
                case 8: bitmapMatrix.postRotate(270);                                   break;  // left bottom
                default:                                                                        break;  // Unknown
            }
            newBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), bitmapMatrix, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return newBitmap;
    }

}