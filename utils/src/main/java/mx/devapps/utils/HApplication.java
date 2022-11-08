package mx.devapps.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import mx.devapps.utils.components.HConstant;
import mx.devapps.utils.exceptions.HExceptionHandler;

public abstract class HApplication extends Application {

    public void onConfig(Class launcher) {
        super.onCreate();

//        new HExceptionHandler.Builder(getApplicationContext())
//                .setTrackActivitiesEnabled(true)
//                .addLauncher(launcher)
//                .build();
//
//        Logger.addLogAdapter(new AndroidLogAdapter(
//                PrettyFormatStrategy.newBuilder()
//                        .showThreadInfo(true)
//                        .methodCount(1)
//                        .tag(HConstant.SUPPORT_TAG)
//                        .build()) {
//            @Override public boolean isLoggable(int priority, String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
//
//        Logger.addLogAdapter(new AndroidLogAdapter(
//                CsvFormatStrategy.newBuilder()
//                        .logStrategy(new LogStrategy() {
//                            @Override
//                            public void log(int priority, @Nullable String tag, @NonNull String message) {
//                                //Registrar en log, etc
//                            }
//                        })
//                        .tag(HConstant.SUPPORT_TAG)
//                        .build()));

    }

}
