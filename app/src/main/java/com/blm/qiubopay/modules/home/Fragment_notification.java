package com.blm.qiubopay.modules.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;

import java.io.Serializable;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_notification extends HFragment {

    public static IFunction notificationFunction = null;
    NotificationData data = null;

    public static Fragment_notification newInstance(NotificationData data) {
        Fragment_notification fragment = new Fragment_notification();
        Bundle args = new Bundle();

        if(data!=null)
            args.putSerializable("Fragment_notification", data);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = (NotificationData) getArguments().getSerializable("Fragment_notification");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(data.getLayoutStyle(), container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop top = CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        final ImageView icon = getView().findViewById(R.id.img_alert);
        final TextView title = getView().findViewById(R.id.text_title);
        final TextView message = getView().findViewById(R.id.text_message);
        final Button btn_aceptar = getView().findViewById(R.id.btn_aceptar);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notificationFunction!=null){
                    notificationFunction.execute();
                } else {
                    getContext().backFragment();
                }

            }
        });

        if(data != null) {

            if(data.getIcon() != null)
                icon.setImageDrawable(getContext().getDrawable(data.getIcon()));

            if(data.getTitle() != null)
                title.setText(data.getTitle());

            if(data.getMessage() != null)
                message.setText(data.getMessage());

            if(data.getIntTitle() != null)
                title.setText(data.getIntTitle());

            if(data.getIntMessage() != null)
                message.setText(data.getIntMessage());

            if(data.getLayoutStyle() == R.layout.fragment_notification_2){
                top.setColorBack(R.color.clear_blue);
            }

        }

    }

    public static class NotificationData implements Serializable {

        String title;
        String message;
        Integer icon;
        Integer layoutStyle;
        Integer intTitle;
        Integer intMessage;

        public NotificationData(String title, String message, Integer icon, Integer layoutStyle) {
            this.title = title;
            this.message = message;
            this.icon = icon;
            this.layoutStyle = layoutStyle;
        }

        public NotificationData(Integer intTitle, Integer intMessage, Integer icon, Integer layoutStyle) {
            this.intTitle = intTitle;
            this.intMessage = intMessage;
            this.icon = icon;
            this.layoutStyle = layoutStyle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getIcon() {
            return icon;
        }

        public void setIcon(Integer icon) {
            this.icon = icon;
        }

        public Integer getLayoutStyle() {
            return layoutStyle;
        }

        public void setLayoutStyle(Integer layoutStyle) {
            this.layoutStyle = layoutStyle;
        }

        public Integer getIntTitle() {
            return intTitle;
        }

        public void setIntTitle(Integer intTitle) {
            this.intTitle = intTitle;
        }

        public Integer getIntMessage() {
            return intMessage;
        }

        public void setIntMessage(Integer intMessage) {
            this.intMessage = intMessage;
        }
    }

}