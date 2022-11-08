package com.blm.qiubopay.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blm.qiubopay.R;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;
    private static final String DRAWABLE_RESOURE = "resource";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_image_detail);

        imageView = (ImageView)findViewById(R.id.img);
        button = (Button)findViewById(R.id.btnClose);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String base64Image = bundle.getString(DRAWABLE_RESOURE);
            Bitmap decodedByte = null;
            if (base64Image != null) {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            imageView.setImageBitmap(decodedByte);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
