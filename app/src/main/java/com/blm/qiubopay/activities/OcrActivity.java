package com.blm.qiubopay.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.blm.qiubopay.R;
import com.blm.qiubopay.models.ocr.OcrMatch;
import com.blm.qiubopay.tools.ocr.CameraSource;
import com.blm.qiubopay.tools.ocr.CameraSourcePreview;
import com.blm.qiubopay.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;

public class OcrActivity extends HActivity {
    private final String TAG = "OcrActivity";

    //Elementos de pantalla
    private LinearLayout layoutProgress;
    private ProgressBar progressOcr;
    private TextView textProgress;

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // Constants used to pass extra data in the intent
    //public static final String AutoFocus = "AutoFocus";
    //public static final String UseFlash = "UseFlash";
    public static final int RC_OCR_CAPTURE = 9003;
    public static final String OBJECT_TO_READ = "OBJECT_TO_READ";
    public static final String OBJECT_READED = "OBJECT_READED";
    public static final String PHOTO_TAKEN = "PHOTO_TAKEN";

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;


    // Helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    //RSB. Extraemos los parametros de busqueda
    List<OcrMatch> ocrMatchList;
    //RSB. Este map es para almacenar el retorno de las variables
    HashMap<String,String> mapOcrRead;
    //RSB. Variable para obtener cuantos campos son requeridos del objeto de lectura
    int reqFields;
    //RSB. Contador de lecturas recividas
    int totalLecturas;
    //RSB.
    boolean takingPicture;


    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        try {

            setContentView(R.layout.activity_ocr);

            //Retrieve elements from View

            ImageView imgFlash = (ImageView) findViewById(R.id.img_flash);
            View redLine = (View) findViewById(R.id.view_line_red);
            Button btnPhoto = (Button) findViewById(R.id.btn_photo);
            mPreview = (CameraSourcePreview) findViewById(R.id.preview);
            layoutProgress = (LinearLayout) findViewById(R.id.layout_progress_ocr);
            progressOcr = (ProgressBar) findViewById(R.id.progress_ocr);
            textProgress = (TextView) findViewById(R.id.text_progress_ocr);


            // Get the list to search parameters, it could be null if is only photo
            ocrMatchList = getIntent().getParcelableArrayListExtra("OBJECT_TO_READ");
            // Verify is only photo?
            if(ocrMatchList!=null){
                btnPhoto.setVisibility(View.GONE);
            } else {
                redLine.setVisibility(View.GONE);
            }

            // read parameters from the intent used to launch the activity.
            boolean autoFocus = true; //getIntent().getBooleanExtra(AutoFocus, false);
            boolean useFlash = false; //getIntent().getBooleanExtra(UseFlash, false);

            // Check for the camera permission before accessing the camera.  If the
            // permission is not granted yet, request permission.
            int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (rc == PackageManager.PERMISSION_GRANTED) {
                createCameraSource(autoFocus, useFlash);
            } else {
                requestCameraPermission();
            }


            btnPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhoto();
                }
            });

            imgFlash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

    /**
     * Take a photo using the mCameraSource
     */
    private void takePhoto() {

        try {

            setProgressIndicator(true,"Tomando foto...",100,0);
            takingPicture = true;

            mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] byteArray) {

                    byte[] byteArrayResized = null;



                    try {

                        setProgressIndicator(true,"Tomando foto...",100,100);
                        Log.d(TAG,"Total de Lecturas: " + totalLecturas);

                        int exifOrientation = getExifOrientation(byteArray);
                        Bitmap bmp = Utils.decodeSampledBitmapFromByteArray(byteArray);
                        if (exifOrientation > 1) {
                            bmp = getRotatedBitmap(exifOrientation, bmp);
                        }
                        byteArrayResized = Utils.compressByteArrayFromBitmap(bmp, 50);

                        Intent data = new Intent();
                        if(mapOcrRead!=null) data.putExtra(OBJECT_READED, mapOcrRead);
                        data.putExtra(PHOTO_TAKEN, byteArrayResized);
                        setResult(CommonStatusCodes.SUCCESS_CACHE, data);

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        //Intent data = new Intent();
                        // data.putExtra(PHOTO_TAKEN, byteArrayResized);
                        //setResult(CommonStatusCodes.SUCCESS_CACHE, data);

                    }


                    finish();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        getContext().alert(R.string.permission_camera_rationale, new IAlertButton() {
            @Override
            public String onText() {
                return "ACEPTAR";
            }

            @Override
            public void onClick() {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        });
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {

        try {

            Context context = getApplicationContext();

            if(ocrMatchList != null) {
                // Initializa map to return
                mapOcrRead = new HashMap<String,String>();
                // Get required fields
                reqFields = 0;
                for(OcrMatch obj : ocrMatchList) {
                    if(obj.isRequired())
                        reqFields++;
                }
                totalLecturas = 0;
                takingPicture = false;
                //Inicializamos la barra de progreso
                setProgressIndicator(true,"Obteniendo lectura... ",100,0);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"No se pudieron encontrar lecturas");
                        if(!takingPicture) takePhoto();
                    }
                }, 15000);

            }

            // A text recognizer is created to find text.  An associated processor instance
            // is set to receive the text recognition results and display graphics for each text block
            // on screen.
            TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {

                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    try {

                        //Log.d(TAG,"OCRMATCH " + ocrMatchList);
                        if(ocrMatchList != null) {

                            totalLecturas++;
                            SparseArray<TextBlock> items = detections.getDetectedItems();

                            //Revisamos bloque de texto por bloque de texto
                            for (int i = 0; i < items.size(); ++i) {
                                if (mapOcrRead.size() >= reqFields) {
                                    break;
                                }
                                //Log.d(TAG, "RSB Value at " + i + " " + items.valueAt(i).getValue());
                                regexOCR(items.valueAt(i), ocrMatchList);
                            }

                            if (mapOcrRead.size() >= reqFields && !takingPicture) {
                                takingPicture = true;

                                mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
                                    @Override
                                    public void onPictureTaken(byte[] byteArray) {

                                        try {

                                            Log.d(TAG, "Total de Lecturas: " + totalLecturas);

                                            int exifOrientation = getExifOrientation(byteArray);
                                            Bitmap bmp = Utils.decodeSampledBitmapFromByteArray(byteArray);
                                            if (exifOrientation > 1) {
                                                bmp = getRotatedBitmap(exifOrientation, bmp);
                                            }
                                            byte[] byteArrayResized = Utils.compressByteArrayFromBitmap(bmp, 70);

                                            Intent data = new Intent();
                                            data.putExtra(OBJECT_READED, mapOcrRead);
                                            data.putExtra(PHOTO_TAKEN, byteArrayResized);
                                            setResult(CommonStatusCodes.SUCCESS_CACHE, data);
                                            finish();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                return;
                            }

                        }

                    }catch (Exception ex) {
                        ex.printStackTrace();
                    }



                }
            });

            if (!textRecognizer.isOperational()) {
                // Note: The first time that an app using a Vision API is installed on a
                // device, GMS will download a native libraries to the device in order to do detection.
                // Usually this completes before the app is run for the first time.  But if that
                // download has not yet completed, then the above call will not detect any text,
                // barcodes, or faces.
                //
                // isOperational() can be used to check if the required native libraries are currently
                // available.  The detectors will automatically become operational once the library
                // downloads complete on device.
                Log.e(TAG, "Detector dependencies are not yet available.");

                // Check for low storage.  If there is low storage, the native library will not be
                // downloaded, so detection will not become operational.
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                if (hasLowStorage) {
                    Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                    Log.e(TAG, getString(R.string.low_storage_error));
                }
            }


            // Creates and starts the camera.  Note that this uses a higher resolution in comparison
            // to other detection examples to enable the text recognizer to detect small pieces of text.
            mCameraSource =
                    new CameraSource.Builder(getApplicationContext(), textRecognizer)
                            .setFacing(CameraSource.CAMERA_FACING_BACK)
                            .setRequestedPreviewSize(1280, 1024)
                            .setRequestedFps(1.0f)
                            .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                            .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                            .build();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Valida si dentro del bloque obtenido esta lo que necesitamos
     * @param item
     * @param listOCR
     */
    public void regexOCR(TextBlock item, List<OcrMatch> listOCR){

        try {

            Pattern pattern;
            Matcher matcher;

            //Dentro del bloque buscamos las distintas expresiones que se solicitan
            for(OcrMatch objectToRead:listOCR) {

                try {

                    if(!mapOcrRead.containsKey(objectToRead.getIdValue())){
                        Log.d(TAG,"Searching for... " + objectToRead.getIdValue());
                        //Log.d(TAG,"on... " + item.getValue());
                        setProgressIndicator(true,"Identificando (" + objectToRead.getIdValue()+")",reqFields,mapOcrRead.size());
                        pattern = Pattern.compile(objectToRead.getWordOrRegex());
                        matcher = pattern.matcher(item.getValue());
                        if(matcher.find()) {
                            //Validamos si hay una expresion a buscar dentro del bloque si ya encontramos algo en especifico
                            if(objectToRead.getOcrMatch()!=null){
                                regexOCR(item, listOCR);
                            } else {
                                //Si ya no hay otra expresion a buscar dentro del objeto y ademas se encontro el contenido
                                //añadimos el item al map de retorno
                                Log.d(TAG,objectToRead.getIdValue()+ " --> " + item.getValue());
                                mapOcrRead.put(objectToRead.getIdValue(),matcher.group());
                            }
                        }
                    } else {Log.d(TAG,"Already in map... " + objectToRead.getIdValue());}

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }catch (Exception ex) {
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

    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // We have permission, so create the camerasource
            boolean autoFocus = true; //getIntent().getBooleanExtra(AutoFocus,false);
            boolean useFlash = false; //getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        final Activity thisActivity = this;

        getContext().alert(R.string.no_camera_permission, new IAlertButton() {
            @Override
            public String onText() {
                return "ACEPTAR";
            }

            @Override
            public void onClick() {
                getContext().finish();
            }
        });

    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // Check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    public void setProgressIndicator(boolean isVisible, String text, Integer totalFields, Integer readedFields){

        try {

            if(isVisible){
                layoutProgress.setVisibility(View.VISIBLE);
                textProgress.setText(text);
                int percentage = 0;
                if(readedFields>0) {
                    percentage = (readedFields * 100) / totalFields;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressOcr.setProgress(percentage,true);
                } else {
                    progressOcr.setProgress(percentage);
                }
            } else {
                layoutProgress.setVisibility(View.GONE);
            }

        } catch (Exception ex) {

        }

    }

}
