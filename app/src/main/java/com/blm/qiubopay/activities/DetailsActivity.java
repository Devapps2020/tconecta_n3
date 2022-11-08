package com.blm.qiubopay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blm.qiubopay.R;

public class DetailsActivity extends AppCompatActivity /*implements PositiveButton.OnClickListener*/ {

    private TextView tvTitle;
    private TextView tvSubtitle;
    private TextView tvDetail;
    private TextView tvAditionalInfo;

    private String title;
    private String subtitle;
    private String detail;
    private String aditionalInfo;

    private Button btnMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            subtitle = bundle.getString("subtitle");
            detail = bundle.getString("details");
            aditionalInfo = bundle.getString("aditional_info");
        }

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvAditionalInfo = (TextView) findViewById(R.id.tv_aditional_info);
//        getFragmentManager().beginTransaction().add(R.id.fl_map_button, btnMap = new PositiveButton()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (title != null) {
            tvTitle.setText(title);
        }
        if (subtitle != null) {
            tvSubtitle.setText(subtitle);
        }
        if (detail != null) {
            tvDetail.setText(detail);
        }
        if (aditionalInfo != null) {
            tvAditionalInfo.setText(aditionalInfo);
        }
//        btnMap.setIdButton(R.id.fl_map_button);
        btnMap.setText("Cómo llegar al complejo...");
    }

//    @Override
//    public void onPositiveButtonClickListener(int id, String text) {
//        try {
//            Intent intent = new Intent(DetailsActivity.this, Class.forName("com.walmart.activitylibrary.MapsActivity"));
//            Bundle bundle = new Bundle();
//
//            bundle.putBoolean("filter", false);
//            bundle.putBoolean("pin_list", true);
//            bundle.putBoolean("location_button", true);
//            bundle.putString("location_json", location);
//
//            intent.putExtras(bundle);
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

}
