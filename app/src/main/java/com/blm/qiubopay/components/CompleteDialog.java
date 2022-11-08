package com.blm.qiubopay.components;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blm.qiubopay.R;

public class CompleteDialog extends DialogFragment {

    private Boolean isCancelable = true;
    private CompleteDialogCallback completeDialogCallback;

    public CompleteDialog() {

    }

    @SuppressLint("ValidFragment")
    public CompleteDialog(CompleteDialogCallback completeDialogCallback, Boolean isCancelable) {
        this.isCancelable = isCancelable;
        this.completeDialogCallback = completeDialogCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View dialog = inflater.inflate(R.layout.complete_dialog, container, false);

        ImageView imageContainer = dialog.findViewById(R.id.containerImage);
        setCancelable(isCancelable);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        imageContainer.setOnClickListener(v -> {
            completeDialogCallback.closeButton(getDialog());
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

}