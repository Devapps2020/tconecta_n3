package mx.devapps.utils.components;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.intuit.sdp.BuildConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mx.devapps.utils.R;
import mx.devapps.utils.exceptions.HExceptionHandler;
import mx.devapps.utils.interfaces.IAlertButton;

public class HThrowException extends HActivity {

    private WebView web_support;
    private HCircleButton btn_close;
    private String strCurrentErrorLog;

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);

        getContext().loading(true);

        web_support = findViewById(R.id.web_support);
        btn_close = findViewById(R.id.btn_close);

        web_support.setWebViewClient(new WBClient());
        web_support.getSettings().setJavaScriptEnabled(true);
        web_support.getSettings().setDomStorageEnabled(true);
        web_support.getSettings().setLoadWithOverviewMode(true);
        web_support.getSettings().setUseWideViewPort(true);
        web_support.getSettings().setBuiltInZoomControls(true);
        web_support.getSettings().setAllowContentAccess(true);
        web_support.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_support.setWebViewClient(new WBClient());

        web_support.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getContext().loading(false);
                    }
                }, 5000 );

            }
        });

        web_support.loadUrl(HConstant.SUPPORT_LINK.replace("{1}", getApplicationName(this)).replace("{2}", getAllErrorDetailsFromIntent(HThrowException.this, getIntent()).replace("\n", "<br>")));

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("text", strCurrentErrorLog);
                clip.setPrimaryClip(data);

                toast("Copy error");

                //getContext().startActivity(HExceptionHandler.Builder.launch, true);
                finish();
                System.exit(0);
            }
        });

    }

    private class WBClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            getContext().loading(false);
        }
        @Override
        public void onReceivedSslError(WebView v, final SslErrorHandler handler, SslError er){

            String message = "SSL Certificate error.";
            switch (er.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }

            message += " Do you want to continue anyway?";

            getContext().alert("SSL Certificate", message, new IAlertButton() {
                @Override
                public String onText() {
                    return "cancel";
                }

                @Override
                public void onClick() {
                    handler.cancel();
                }
            }, new IAlertButton() {
                @Override
                public String onText() {
                    return "Proceed";
                }

                @Override
                public void onClick() {
                    handler.proceed();
                }
            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (web_support.canGoBack()) {
                        web_support.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private Integer getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }

    private String getActivityLogFromIntent(Intent intent) {
        return intent.getStringExtra(HExceptionHandler.EXTRA_ACTIVITY_LOG);
    }

    private String getStackTraceFromIntent(Intent intent) {
        return intent.getStringExtra(HExceptionHandler.EXTRA_STACK_TRACE);
    }

    private String getAllErrorDetailsFromIntent(Context context, Intent intent) {
        if (TextUtils.isEmpty(strCurrentErrorLog)) {
            String LINE_SEPARATOR = "\n";
            StringBuilder errorReport = new StringBuilder();
            errorReport.append("***** Exception Handler Library ");
            errorReport.append("\n***** by devapps.mx \n");
            errorReport.append("\n***** DEVICE INFO \n");
            errorReport.append("Brand: ");
            errorReport.append(Build.BRAND);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Device: ");
            errorReport.append(Build.DEVICE);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Model: ");
            errorReport.append(Build.MODEL);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Manufacturer: ");
            errorReport.append(Build.MANUFACTURER);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Product: ");
            errorReport.append(Build.PRODUCT);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("SDK: ");
            errorReport.append(Build.VERSION.SDK);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Release: ");
            errorReport.append(Build.VERSION.RELEASE);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("\n***** APP INFO \n");
            String versionName = getVersionName(context);
            Integer versionCode = getVersionCode(context);
            errorReport.append("Version: ");
            errorReport.append(versionName + " " + versionCode);
            errorReport.append(LINE_SEPARATOR);
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String firstInstallTime = getFirstInstallTimeAsString(context, dateFormat);
            if (!TextUtils.isEmpty(firstInstallTime)) {
                errorReport.append("Installed On: ");
                errorReport.append(firstInstallTime);
                errorReport.append(LINE_SEPARATOR);
            }
            String lastUpdateTime = getLastUpdateTimeAsString(context, dateFormat);
            if (!TextUtils.isEmpty(lastUpdateTime)) {
                errorReport.append("Updated On: ");
                errorReport.append(lastUpdateTime);
                errorReport.append(LINE_SEPARATOR);
            }
            errorReport.append("Current Date: ");
            errorReport.append(dateFormat.format(currentDate));
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("\n***** ERROR LOG \n");
            errorReport.append(getStackTraceFromIntent(intent));
            errorReport.append(LINE_SEPARATOR);
            String activityLog = getActivityLogFromIntent(intent);
            errorReport.append(LINE_SEPARATOR);
            if (activityLog != null) {
                errorReport.append("\n***** USER ACTIVITIES \n");
                errorReport.append("User Activities: ");
                errorReport.append(activityLog);
                errorReport.append(LINE_SEPARATOR);
            }
            errorReport.append("\n***** END OF LOG *****\n");
            strCurrentErrorLog = errorReport.toString();
            return strCurrentErrorLog;
        } else {
            return strCurrentErrorLog;
        }
    }

    private String getFirstInstallTimeAsString(Context context, DateFormat dateFormat) {
        long firstInstallTime;
        try {
            firstInstallTime = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .firstInstallTime;
            return dateFormat.format(new Date(firstInstallTime));
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    private String getLastUpdateTimeAsString(Context context, DateFormat dateFormat) {
        long lastUpdateTime;
        try {
            lastUpdateTime = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .lastUpdateTime;
            return dateFormat.format(new Date(lastUpdateTime));
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

}