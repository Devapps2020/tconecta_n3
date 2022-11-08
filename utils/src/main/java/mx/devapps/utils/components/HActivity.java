package mx.devapps.utils.components;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;

import mx.devapps.utils.R;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.ILongOperation;
import mx.devapps.utils.interfaces.IOperation;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class HActivity  extends AppCompatActivity {


    private boolean transition = false;

    private boolean lockBack = false;

    private static IFunction<String> onFragmentChange = null;
    private static Integer item_alert_loading = R.layout.item_alert_loading;
    private static Integer item_alert = R.layout.item_alert;
    private static Integer ic_alert = R.drawable.ic_warning;
    private static Boolean cancelable_alert = false;
    private static Integer nav_host_fragment = null;

    private HActivity context = this;
    private AlertDialog alert = null;
    private static AlertDialog loading = null;
    private IActivityResult onActivityResult = null;
    private IRequestPermissions iRequestPermissions = null;
    private HWebService service = null;
    private Integer indexFragment = 0;
    private IFunction finishBack = null;
    private IFunction defaultBack = null;

    private Integer countLoading = 0;

    private boolean actionBtn = false;
    private int contActionBtn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1:

                boolean denied = false;
                boolean never = false;

                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]))
                        denied = true;
                    else if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        never = true;
                }

                if (denied && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(permissions, requestCode);
                else if (never)
                    alert(Html.fromHtml(getResources().getString(R.string.text_never_ask_again)).toString(), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Configurar";
                        }
                        @Override
                        public void onClick() {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            finish();
                        }
                    });
                else
                    iRequestPermissions.onPostExecute();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(onActivityResult != null)
            onActivityResult.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        this.backFragment();
    }

    public void startActivity(Class clazz, boolean... clearStack) {

        try {

            Intent intent = new Intent(context, clazz);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
            super.startActivity(intent, options.toBundle());

            if (clearStack != null && clearStack.length > 0 && clearStack[0]) {
                finish();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public HActivity getContext() {
        return context;
    }

    public void requestPermissions(IRequestPermissions iRequestPermissions, String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.iRequestPermissions = iRequestPermissions;
            requestPermissions(permissions, 1);
        } else
            iRequestPermissions.onPostExecute();

    }

    public boolean isRoot() {

        try {
            Runtime.getRuntime().exec("su");
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public boolean hasConnection() {

        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (!isConnected)
                return false;

            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI, isMovile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

            return isConnected ? (isWiFi ? true : (isMovile ? true : false)) : false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void loading(boolean show) {
        loading(show, (String) null);
    }

    public void loading(boolean show, String text, IAlertButton... listener) {

        try {

            hideKeyboard();

            if(!show) {
                if(loading != null) {

                    try {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(countLoading <= 1) {
                                    loading.dismiss();
                                    countLoading = 0;
                                } else {
                                    --countLoading;
                                }

                            }
                        }, 600 );

                    } catch (Exception ex) {
                        loading.dismiss();
                        countLoading = 0;
                    }

                }

                return;
            }

            ++countLoading;

            if(loading != null && loading.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert_loading, null);

            TextView text_message = view.findViewById(R.id.text_message);
            text_message.setTypeface(text_message.getTypeface(), Typeface.BOLD);
            text_message.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen._15sdp));
            text_message.setGravity(Gravity.CENTER);

            if(text_message != null)
                text_message.setText((text != null) ? text + "\n\n" : "");

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            loading = builder.create();
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void loading(boolean show, Integer text, IAlertButton... listener) {

        try {

            hideKeyboard();

            if(!show) {
                if(loading != null) {

                    try {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(countLoading <= 1) {
                                    closeLoading();
                                } else {
                                    --countLoading;
                                }

                            }
                        }, 600 );

                    } catch (Exception ex) {
                        loading.dismiss();
                        countLoading = 0;
                    }

                }

                return;
            }

            ++countLoading;

            if(loading != null && loading.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert_loading, null);

            TextView text_message = view.findViewById(R.id.text_message);

            if(text_message != null)
                text_message.setText((text != null) ? text + "\n\n" : "");

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            loading = builder.create();
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void closeLoading() {

        try {

            if(loading != null) {
                loading.dismiss();
                countLoading = 0;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(String message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null) {}
            img_alert.setImageDrawable(getResources().getDrawable(ic_alert));

            //if(text_title != null)
            //    text_title.setVisibility(View.GONE);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Integer message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing()) {
                alert.dismiss();
                alert = null;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null)
                img_alert.setImageDrawable(getResources().getDrawable(ic_alert));

            //if(text_title != null)
            //    text_title.setVisibility(View.GONE);

            if(text_message != null)
                text_message.setText(message);

            if(btn_accept != null)
                btn_accept.setVisibility(View.GONE);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(String title, String message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(text_title != null)
                text_title.setText(title);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Integer title, Integer message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(text_title != null)
                text_title.setText(title);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Integer icon, Integer color, String message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null) {
                img_alert.setImageDrawable(getResources().getDrawable(icon));
                img_alert.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            //if(text_title != null)
            //    text_title.setVisibility(View.GONE);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Integer icon, Integer color, Integer message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null) {
                img_alert.setImageDrawable(getResources().getDrawable(icon));
                img_alert.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            //if(text_title != null)
            //    text_title.setVisibility(View.GONE);

            if(text_message != null)
                text_message.setText(message);

            if(btn_accept != null)
                btn_accept.setVisibility(View.GONE);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Integer icon, Integer color, String title, String message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null) {
                img_alert.setImageDrawable(getResources().getDrawable(icon));
                img_alert.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if(text_title != null)
                text_title.setText(title);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.GONE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Integer icon, Integer color, Integer title, Integer message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null) {
                img_alert.setImageDrawable(getResources().getDrawable(icon));
                img_alert.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if(text_title != null)
                text_title.setText(title);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alert(Drawable icon, String title, String message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null)
                img_alert.setImageDrawable(icon);

            if(text_title != null)
                text_title.setText(title);

            if(text_message != null)
                text_message.setText(message);

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            if(btn_other != null) {

                btn_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 2)
                            listener[2].onClick();

                    }
                });

                if(listener.length > 2) {
                    btn_other.setText(listener[2].onText());
                    btn_other.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void alertHTML(String message, IAlertButton... listener) {

        try {

            closeLoading();

            if (alert != null && alert.isShowing())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(item_alert, null);

            ImageView img_alert = view.findViewById(R.id.img_alert);
            TextView text_title = view.findViewById(R.id.text_title);
            TextView text_message = view.findViewById(R.id.text_message);
            Button btn_accept = view.findViewById(R.id.btn_accept);
            Button btn_close = view.findViewById(R.id.btn_close);
            Button btn_other = view.findViewById(R.id.btn_other);

            if(img_alert != null)
                img_alert.setImageDrawable(getResources().getDrawable(ic_alert));

            //if(text_title != null)
            //    text_title.setVisibility(View.GONE);

            if(text_message != null)
                text_message.setText(Html.fromHtml(message));

            if(btn_close != null) {

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 0)
                            listener[0].onClick();

                    }
                });

                if(listener.length > 0)
                    btn_close.setText(listener[0].onText());

            }

            if(btn_accept != null) {

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert.dismiss();

                        if(listener.length > 1)
                            listener[1].onClick();

                    }
                });

                if(listener.length > 1) {
                    btn_accept.setText(listener[1].onText());
                    btn_accept.setVisibility(View.VISIBLE);
                }

            }

            builder.setView(view);
            builder.setCancelable(cancelable_alert);

            alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void setItemAlertLoading(int item_alert_loading) {
        HActivity.item_alert_loading = item_alert_loading;
    }

    public static void setItemAlert(int item_alert) {
        HActivity.item_alert = item_alert;
    }

    public static void setIcAlert(int ic_alert) {
        HActivity.ic_alert = ic_alert;
    }

    public void setCancelableAlert(Boolean cancelable_alert) {
        HActivity.cancelable_alert = cancelable_alert;
    }

    public void setHostFragmentId(Integer nav_host_fragment) {
        this.nav_host_fragment = nav_host_fragment;
    }

    public void setFinishBack(IFunction finishBack) {
        this.finishBack = finishBack;
    }

    public void setFragment(HFragment fragment, boolean... clearStack) {

        if(transition)
            return;

        transition = true;

        try {

            closeLoading();

            if(this.nav_host_fragment == null)
                return;

            if(clearStack.length > 0 && clearStack[0])
                indexFragment = 99;

            context.getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .add(this.nav_host_fragment , fragment, fragment.getName())
                    .addToBackStack(fragment.getName())
                    .commit();

            if(onFragmentChange != null)
                onFragmentChange.execute(fragment.getName());

            indexFragment++;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() { public void run(){
            transition = false;
        }}, 800);

    }

    public void backFragment(IFunction... iBackStack) {

        try {

            hideKeyboard();

            FragmentManager manager = context.getSupportFragmentManager();

            //RSB 20200128. Implementacion ENKO
            List<Fragment> fragmentList = manager.getFragments();

            boolean handled = false;

            for(Fragment f : fragmentList) {
                if(f instanceof HFragment) {
                    handled = ((HFragment)f).onBackPressed();
                    if(handled) {
                        break;
                    }
                }
            }

            if (handled) { return; }

            if(indexFragment == 100) {
                if(defaultBack != null)
                    defaultBack.execute();
                indexFragment = 0;
                return;
            }

            if (indexFragment > 0) {
                manager.popBackStackImmediate();
                indexFragment--;
                return;
            }

            if(finishBack != null) {
                finishBack.execute();
            } else {
                super.onBackPressed();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void backFragmentWithFunction(IFunction... iBackStack) {

        try {

            hideKeyboard();

            FragmentManager manager = context.getSupportFragmentManager();

            //RSB 20200128. Implementacion ENKO
            List<Fragment> fragmentList = manager.getFragments();

            boolean handled = false;

            for(Fragment f : fragmentList) {
                if(f instanceof HFragment) {
                    handled = ((HFragment)f).onBackPressed();
                    if(handled) {
                        break;
                    }
                }
            }

            if (handled) { return; }

            if(indexFragment == 100) {
                if(defaultBack != null)
                    defaultBack.execute();
                indexFragment = 0;
                return;
            }

            if (indexFragment > 0) {
                if(finishBack != null)
                    finishBack.execute();

                manager.popBackStackImmediate();
                indexFragment--;
                return;
            }

            if(finishBack != null) {
                finishBack.execute();
            } else {
                super.onBackPressed();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setDefaultBack(IFunction defaultBack) {
        this.defaultBack = defaultBack;
    }

    //20210427 RSB. Homologacion. Valida salir n3
    public boolean validateCloseTaps(){
        if(actionBtn) {
            if(contActionBtn == 3){
                return true;
            } else {
                contActionBtn++;
            }
        } else {
            actionBtn = true;
            contActionBtn++;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    actionBtn = false;
                    contActionBtn = 0;
                };
            }, 5000);
        }
        return false;
    }

    public void logger(Object object, Object... objects) {

        String log = strObject(object);

        if(objects != null && objects.length > 0)
            for (Object obj: objects)
                log += strObject(obj);

        Logger.d(log);

    }

    private String strObject(Object object) {
        try {
            return new Gson().toJson(object) + "\n";
        } catch (Exception ex) {
            return (String) object + "\n";
        }
    }

    public void request(final IOperation operation) {

        if(service == null)
            service = new HWebService(getContext());

        service.setURL(operation.getUrl());

        service.execute(new ILongOperation() {

            @Override
            public String doInBackground() {
                return service.getClient()._POST(strObject(operation.getData()));
            }

            @Override
            public void onPreExecute() {
                loading(true);
            }

            @Override
            public void onPostExecute(int code, String result) {

                if(code == 200) {
                    operation.onPostExecute(new Gson().fromJson(result, operation.getType()));
                } else {
                    alert(getContext().getResources().getString(R.string.ws_error_general) + " : "+ code);
                    operation.onExecuteError();
                }

                loading(false);

            }
        });

    }

    public IActivityResult getOnActivityResult() {
        return onActivityResult;
    }

    public void setOnActivityResult(IActivityResult onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

    public void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    public void setLockBack(boolean lockBack) {
        this.lockBack = lockBack;
    }

    public static void setOnFragmentChange(IFunction<String> onFragmentChange) {
        HActivity.onFragmentChange = onFragmentChange;
    }

    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getContext().getCurrentFocus();
        if (view == null)
            view = new View(getContext());

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    protected void onDestroy() {
        closeLoading();
        super.onDestroy();
    }

}
