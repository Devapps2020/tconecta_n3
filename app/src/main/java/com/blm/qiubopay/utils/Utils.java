package com.blm.qiubopay.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.RenderScript;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.core.content.FileProvider;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.AppImages;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.CircleTransform;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.helpers.views.RSBlurProcessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;

//import com.example.android.multidex.myCApplication.R;

public class Utils {

    public static final Integer CODE = 200;

    public static final String HOST = "https://958a19fb.ngrok.io";
    public static final String TOKEN = "3d524a53c110e4c22463b10ed32cef9d";

    public static final String SOPORTE = "/qiubo-pay-server/api/v1/fc/sendReport";

    public static String file_absolutePath;

    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,15}$");

    public static boolean validatePIN_consecutivos(String pin){

        for(int n=0; n<pin.length()-1; n++){

            int num = Integer.parseInt(pin.charAt(n) + "");
            int nums = Integer.parseInt(pin.charAt(n + 1) + "");

            if(nums == (num+1) || nums == num)
                return true;

        }

        return false;
    }

    public static boolean validatePIN_repeat(String pin) {

        for(char c : pin.toCharArray() ){
            int cont = 0;
            for(int i=0; i<pin.length(); i++) {
                if(pin.charAt(i) == c) {
                    cont++;

                    if(cont > 1)
                        return true;
                }
            }
        }

        return false;
    }

    public static String paserCurrency(String dato) {


        try {
            Double value = Double.parseDouble(dato.replace(",", "").replace("$", "").replace("E-", ""));

            return java.text.NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value);

        } catch (Exception ex) {
            return "$0.00";
        }

    }

    public static String paserCurrency2(String dato) {


        try {
            Double value = Double.parseDouble(dato.replace(",", "").replace("$", "").replace("E-", ""));

            String result =  java.text.NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value);

            return  result.substring(0, result.length() - 1);

        } catch (Exception ex) {
            return "$0.0";
        }

    }

    public static String paserCurrencyInt(String dato) {


        try {
            Double value = Double.parseDouble(dato.replace(",", "").replace("$", "").replace("E-", ""));

            String result =  java.text.NumberFormat
                    .getCurrencyInstance(Locale.US)
                    .format(value).replace("$", "").replace(".0", "").replace(".00", "");

            return  result.substring(0, result.length() - 1);

        } catch (Exception ex) {
            return "0";
        }

    }

    public static boolean isPasswordValid(String password) {

        Boolean back = true, mayusc=false, minus= false, number=false;

        if(password.length()>=8 && password.length()<=15)
        {

            for(int i=0; i<password.length();i++)
            {
                //Log.i("Password: ", ""+password.charAt(i));
                //Se checan mayúsculas
                if(password.charAt(i) >= 65 && password.charAt(i) <= 90)
                    mayusc = true;
                //Se checan minúsculas
                if(password.charAt(i) >= 97 && password.charAt(i) <= 122)
                    minus = true;
                //Se checan números
                if(password.charAt(i) >= 48 && password.charAt(i) <= 57)
                    number = true;
            }

            if(!mayusc || !minus || !number)
                back = false;

        }
        else
            back = false;

        return back;
    }

    public static File createImageFile(HActivity context) {

        File image = null;

        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".JPEG",         /* suffix */
                    storageDir      /* directory */
            );

        } catch (Exception ex){

        }

        return image;
    }

    public static String convert(Bitmap bitmap) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 3;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }

    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        // src
        float width = src.getWidth();
        float height = src.getHeight();
        // matrix
        Matrix matrix = new Matrix();
        //
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //
        matrix.postScale(scaleWidth, scaleHeight);
        //
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height,
                matrix, true);
    }
    
    public static String convertPNG(Bitmap bit) {

        Bitmap bitmap = scale(bit, 100.0, 100.0);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 3;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Metodo que comprime un Bitmap en un ByteArray
     * @param bmp
     * @param quality
     * @return
     */
    public static byte[] compressByteArrayFromBitmap (Bitmap bmp, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromView(View view, Context context) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        view.draw(canvas);

        RenderScript renderScript = RenderScript.create(context);

        return new RSBlurProcessor(renderScript).blur(returnedBitmap, 15, 1);
    }

    public static String groupId(){

        String id = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_group();

        if(id == null)
            return "";
        else
            return "/" + id;
    }

    public static void shareVoucherPagosQiubo(HActivity context, String c){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String bosy = Globals.HOST + "/api/v1/c/ticketConcentra" +  groupId() + "?c=" + c;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta- ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket -Conecta"));

    }

    public static void shareVoucherPagosQiubo(mx.devapps.utils.components.HActivity context, String c, String transactionNumber){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String bosy = Globals.HOST + "/api/v1/c/ticketConcentra?c=" + c + "&tr=" + transactionNumber;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta- ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket -Conecta"));

    }

    public static void shareVoucherTae(HActivity context, String ti, String ri){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String bosy = Globals.HOST + "/api/v1/" + Globals.VAS_PROVIDER + "/ticketTae" +  groupId() + "?ti=" + ti + "&ri=" + ri;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta - ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket Conecta- "));

    }

    public static void shareVoucherServicios(mx.devapps.utils.components.HActivity context, String ti, String ri){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String bosy = Globals.HOST + "/api/v1/" + Globals.VAS_PROVIDER + "/ticketService" +  groupId() + "?ti=" + ti + "&ri=" + ri;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta- ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket -Conecta"));

    }

    public static void shareVoucherFinanciero(mx.devapps.utils.components.HActivity context, String ri){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String bosy = Globals.HOST + "/api/v1/v/ticket" +  groupId() + "?ri=" + ri;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta- ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket -Conecta"));

    }

    //20210115 RSB. Improvements 0121. Ticket restaurante
    public static void shareVoucherRestaurante(HActivity context, String ri){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String bosy = Globals.HOST + "/api/v1/v/getRestaurantTicket?ticket_id=" + ri;

        /*if(Globals.isSegalmex){
            bosy = Globals.HOST + "/api/v1/v/getRestaurantTicket2?ticket_id=" + ri;
        }*/

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket - ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket - "));

    }

    public static void shareVoucherRemesas(mx.devapps.utils.components.HActivity context, String ti, String ri){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //https://qtcdev2.qiubo.mx/service/api/v1/g/remittanceTicket/1?ti=200897&ri=1631133084
        String bosy = Globals.HOST + "/api/v1/g/remittanceTicket/1?ti=" + ti + "&ri=" + ri;

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta - ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bosy);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket - "));

    }

    public static void share(HActivity context, String texto){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ticket Conecta - ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, texto);
        context.startActivity(Intent.createChooser(sharingIntent, "Compartir Ticket -Conecta"));

    }

    public static void share(HActivity context, String asunto, String texto, String titulo){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, texto);
        context.startActivity(Intent.createChooser(sharingIntent, titulo));

    }

    public static String formatDate(String date){

        try {

            if("-06:00".equals(date.substring(date.length() - 6)))
                return date.substring(0, date.length() - 6).replaceAll("T"," ");

            if("-05:00".equals(date.substring(date.length() - 6)))
                return date.substring(0, date.length() - 6).replaceAll("T"," ");

        }catch (Exception ex){

        }

        return date;
    }

    public static ArrayList<HEditText> functionPIN(View view, final IFunction function){

        final ArrayList<HEditText> campos = new ArrayList();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                function.execute();
            }

            @Override
            public void onMaxLength() {

            }
        };

        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_pin_1),
                true, 1, 1, HEditText.Tipo.PASS, iTextChanged));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_pin_2),
                true, 1, 1, HEditText.Tipo.PASS, iTextChanged));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_pin_3),
                true, 1, 1, HEditText.Tipo.PASS, iTextChanged));
        


        campos.get(0).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() != 0){
                    campos.get(1).getEditText().setText("");
                    campos.get(1).getEditText().requestFocus();
                    return;
                }
            }
        });

        campos.get(0).getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(campos.get(0).getText().length() > 0)
                    switch ((char)event.getUnicodeChar()){
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            campos.get(1).getEditText().setText("");
                            campos.get(1).getEditText().setText((char)event.getUnicodeChar() + "");
                            break;
                    }

                return false;
            }
        });

        campos.get(1).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    campos.get(0).getEditText().requestFocus();
                    campos.get(0).getEditText().setSelection(campos.get(0).getText().length());
                } else {
                    campos.get(2).getEditText().setText("");
                    campos.get(2).getEditText().requestFocus();
                    return;
                }
            }
        });

        campos.get(1).getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if(campos.get(1).getText().length() == 0){
                        campos.get(0).getEditText().setText("");
                        campos.get(0).getEditText().requestFocus();
                    }
                }else{
                    if(campos.get(1).getText().length() > 0)
                        switch ((char)event.getUnicodeChar()){
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                campos.get(2).getEditText().setText("");
                                campos.get(2).getEditText().setText((char)event.getUnicodeChar() + "");
                                break;
                        }
                }

                return false;
            }
        });

        campos.get(2).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    campos.get(1).getEditText().requestFocus();
                    campos.get(1).getEditText().setSelection(campos.get(1).getText().length());
                } else {
                    campos.get(3).getEditText().setText("");
                    campos.get(3).getEditText().requestFocus();
                    return;
                }
            }
        });

        campos.get(2).getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if(campos.get(2).getText().length() == 0){
                        campos.get(1).getEditText().setText("");
                        campos.get(1).getEditText().requestFocus();
                    }
                }else{
                    if(campos.get(2).getText().length() > 0)
                        switch ((char)event.getUnicodeChar()){
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                campos.get(3).getEditText().setText("");
                                campos.get(3).getEditText().setText((char)event.getUnicodeChar() + "");
                                break;
                        }
                }
                return false;
            }
        });

        campos.get(3).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    campos.get(2).getEditText().requestFocus();
                    campos.get(2).getEditText().setSelection(campos.get(2).getText().length());
                }
            }
        });

        campos.get(3).getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if(campos.get(3).getText().length() == 0){
                        campos.get(2).getEditText().setText("");
                        campos.get(2).getEditText().requestFocus();
                    }
                }
                return false;
            }
        });

        return campos;

    }

    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = context.getResources().getDimension(pixels);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return transform(output);
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 60;
        int targetHeight = 60;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return transform(targetBitmap);
    }

    public static Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    public static Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static Uri getFilePhoto(final HActivity context){

        File photoFile = createImageFile(context);

        file_absolutePath = photoFile.getAbsolutePath();

        return FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), photoFile);

    }

    public static void deleteFilePhoto(){

        File fdelete = new File(file_absolutePath);

        if (fdelete.exists())
            fdelete.delete();

    }

    public static void deleteFilePhoto(String Path){

        try {

            File fdelete = new File(Path);

            if (fdelete.exists()) {
                fdelete.delete();
                Logger.d("DELETE FILE " + Path);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Metodo que permite generar un Bitmap comprimido
     * @param photoByte
     * @return
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] photoByte) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 500, 500);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length, options);
    }

    public static Bitmap decodeSampledBitmapFromFile() {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(file_absolutePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 400, 400);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return Bitmap.createScaledBitmap(bt, reqWidth, reqHeight, false);
        return BitmapFactory.decodeFile(file_absolutePath, options);//Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file_absolutePath, options), 400, 250, true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getTimestamp(){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return simpleDateFormat.format(calander.getTime());
    }

    public static String timeStamp(){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            return sdf.format(new Date());
        }catch (Exception e) {
        }
        return "";
    }

    public static String getTime(){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        return simpleDateFormat.format(calander.getTime());
    }

    public static String getDay(){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMM");

        return simpleDateFormat.format(calander.getTime());
    }

    public static String getDayOfWeek(){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

        return simpleDateFormat.format(calander.getTime());
    }

    public static int getIntDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String day = simpleDateFormat.format(calander.getTime()).toUpperCase();
        day.replace("Á","A").replace("É","E");*/
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int iDay = 0;
        switch (day) {
            case Calendar.MONDAY:       iDay=0; break;
            case Calendar.TUESDAY:      iDay=1; break;
            case Calendar.WEDNESDAY:    iDay=2; break;
            case Calendar.THURSDAY:     iDay=3; break;
            case Calendar.FRIDAY:       iDay=4; break;
            case Calendar.SATURDAY:     iDay=5; break;
            case Calendar.SUNDAY:       iDay=6; break;
        }

        return iDay;
    }

    public static Bitmap getImage(String img) {
        byte[] decodedString = com.blm.qiubopay.helpers.Base64.decode(img);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Drawable getDrawable(String name, Context context) {

        try {
            int resourceId = context.getResources().getIdentifier(name.trim(), "drawable", context.getPackageName());
            return context.getResources().getDrawable(resourceId);
        } catch (Exception ex) {
            return getDrawable("no_disponible", context);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();

        }

    }

    public static void setImageSeller(String id, ImageView image) {

        if(AppImages.getExists("SELL-" + id)) {
            image.setImageBitmap(new CircleTransform().transform(AppImages.getImage("SELL-" + id)));
        } else {
            try {
                Picasso.get()
                        .load(Globals.URL_IMAGES_SELLER +id + ".jpg")
                        .placeholder(R.drawable.no_disponible)
                        .error(R.drawable.no_disponible)
                        .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image.setImageBitmap(new CircleTransform().transform(bitmap.copy(bitmap.getConfig(), true)));
                        AppImages.setImage("SELL-" + id, bitmap.copy(bitmap.getConfig(), true));
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        image.setImageDrawable(errorDrawable);
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        image.setImageDrawable(placeHolderDrawable);
                    }
                });
            } catch (Exception e) { }
        }

    }

    public static void setImagePromotion(String id, ImageView image) {

        if(AppImages.getExists("PROM-" + id)) {
            image.setImageBitmap(AppImages.getImage("PROM-" + id));
        } else {
            try {
                Picasso.get()
                        .load(Globals.URL_IMAGES_PROMOTION + id + ".jpg")
                        .placeholder(R.drawable.no_disponible)
                        .error(R.drawable.no_disponible)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                image.setImageBitmap(bitmap.copy(bitmap.getConfig(), true));
                                AppImages.setImage("PROM-" + id, bitmap.copy(bitmap.getConfig(), true));
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                image.setImageDrawable(errorDrawable);
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                image.setImageDrawable(placeHolderDrawable);
                            }
                        });
            } catch (Exception e) { }
        }

    }

    public static void setImagePremios(String id, ImageView image) {

        if(AppImages.getExists("PREM-" + id)) {
            image.setImageBitmap(AppImages.getImage("PREM-" + id));
        } else {
            try {
                Picasso.get()
                        .load(Globals.URL_IMAGES_PREMIOS + id + ".jpg")
                        .placeholder(R.drawable.no_disponible)
                        .error(R.drawable.no_disponible)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                image.setImageBitmap(bitmap.copy(bitmap.getConfig(), true));
                                AppImages.setImage("PREM-" + id, bitmap.copy(bitmap.getConfig(), true));
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                image.setImageDrawable(errorDrawable);
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                image.setImageDrawable(placeHolderDrawable);
                            }
                        });
            } catch (Exception e) { }
        }

    }

    public static void setImageProduct(String id, ImageView image) {

        if(AppImages.getExists("PROD-" + id)) {
            image.setImageBitmap(AppImages.getImage("PROD-" + id));
        } else {
            try {
                Picasso.get()
                        .load(Globals.URL_IMAGES_PRODUCT + id + ".jpg")
                        .placeholder(R.drawable.no_disponible)
                        .error(R.drawable.no_disponible)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                image.setImageBitmap(bitmap.copy(bitmap.getConfig(), true));
                                AppImages.setImage("PROD-" + id, bitmap.copy(bitmap.getConfig(), true));
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                image.setImageDrawable(errorDrawable);
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                image.setImageDrawable(placeHolderDrawable);
                            }
                        });
            } catch (Exception e) { }
        }

    }

    public static void setImageReto(String url, ImageView image) {

        if(AppImages.getExists("RETO-" + url)) {
            image.setImageBitmap(AppImages.getImage("RETO-" + url));
        } else {
            try {
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.no_disponible)
                        .error(R.drawable.no_disponible)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                image.setImageBitmap(bitmap.copy(bitmap.getConfig(), true));
                                AppImages.setImage("RETO-" + url, bitmap.copy(bitmap.getConfig(), true));
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                image.setImageDrawable(errorDrawable);
                            }
                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                image.setImageDrawable(placeHolderDrawable);
                            }
                        });
            } catch (Exception e) { }
        }

    }


    public static boolean validateRFC(String rfc) {
        Pattern p = Pattern.compile("^([A-ZÑ\\x26]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1]))([A-Z\\d]{3})?$");
        Matcher m = p.matcher(rfc.toUpperCase());
        return m.find();
    }

    //MEAX71062750
    //MEAX71062750

    public static boolean validateCURP(String curp) {

        String CURP_REGEX = "[A-Z]{1}[AEIOUX]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}";

        Pattern p = Pattern.compile(CURP_REGEX);
        Matcher m = p.matcher(curp.toUpperCase());
        return m.find();
    }

    public static void main(String[] args) {


        System.out.println("CURP : " + validateCURP("MXAS710627HMNNLR00"));

        System.out.println("RFC : " + validateRFC("Meax7106275s0"));

        System.out.println("CURP : " + validateCURP("MXAS710627HMNNLR00"));

        System.out.println("RFC : " + validateRFC("FOML930815E34"));

    }

    public static String getTimestampToBlockRemesas(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.MINUTE, 30);

        return simpleDateFormat.format(calendar.getTime());
    }

    private static Date getRemesasBlockedDate(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();

        try{
            date = format.parse(dateString);
        }catch (Exception e){

        }

        return date;
    }

    public static boolean isFinishedRemesasBlockedTime(){
        boolean back = false;

        Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(calendar.getTime().after(getRemesasBlockedDate(AppPreferences.getDateRemesasModuleBlocket())))
            back = true;

        return back;
    }

    public static boolean matchRegex(String regex, String cad) {

        boolean back = true;

        if (!cad.matches(regex)){
            back = false;
        }

        return back;

    }

}

