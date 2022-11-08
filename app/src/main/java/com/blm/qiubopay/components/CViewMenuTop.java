package com.blm.qiubopay.components;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blm.qiubopay.R;


public class CViewMenuTop {

    LinearLayout btn_back;
    LinearLayout btn_logo;
    LinearLayout btn_logo_start;
    LinearLayout btn_title;
    LinearLayout btn_user;
    LinearLayout btn_search;
    LinearLayout btn_filter;
    LinearLayout btn_question;
    LinearLayout btn_question_left;

    public static CViewMenuTop create(View view) {
        return new CViewMenuTop(view);
    }

    private CViewMenuTop(View view) {

        btn_back = view.findViewById(R.id.btn_back);
        btn_logo = view.findViewById(R.id.btn_logo);
        btn_logo_start = view.findViewById(R.id.btn_logo_start);
        btn_user = view.findViewById(R.id.btn_user);
        btn_search = view.findViewById(R.id.btn_search);
        btn_filter = view.findViewById(R.id.btn_filter);
        btn_question = view.findViewById(R.id.btn_question);
        btn_title = view.findViewById(R.id.btn_title);
        btn_question_left = view.findViewById(R.id.btn_question_left);

    }

    public CViewMenuTop onClickBack(IClick click) {

        if(btn_back != null) {
            btn_back.setVisibility(View.VISIBLE);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_back.setEnabled(false);
                    click.onClick();
                }
            });
        }

        return this;
    }

    public CViewMenuTop setColorBack(Integer color) {

        if(btn_back != null) {
            ImageView img_back = btn_back.findViewById(R.id.img_back);
            img_back.setColorFilter(ContextCompat.getColor(btn_back.getContext(), color));
        }

        return this;
    }

    public CViewMenuTop showLogo() {

        if(btn_logo != null)
            btn_logo.setVisibility(View.VISIBLE);

        return this;
    }

    public CViewMenuTop showLogoStart() {

        if(btn_logo_start != null)
            btn_logo_start.setVisibility(View.VISIBLE);

        return this;
    }

    public CViewMenuTop showTitle(String title) {

        if(btn_title != null && title != null) {
            btn_title.setVisibility(View.VISIBLE);

            TextView txt_title = btn_title.findViewById(R.id.txt_title);
            txt_title.setText(Html.fromHtml(title));
       }

        return this;
    }

    public CViewMenuTop setColorTitle(Integer colorTitle) {

        if(btn_title != null && colorTitle != null) {
            btn_title.setVisibility(View.VISIBLE);

            TextView txt_title = btn_title.findViewById(R.id.txt_title);
            txt_title.setTextColor(colorTitle);
        }

        return this;
    }

    public CViewMenuTop onClickUser(IClick click) {

        if(btn_user != null) {
            btn_user.setVisibility(View.VISIBLE);
            btn_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick();
                }
            });
        }

        return this;
    }

    public CViewMenuTop onClickSearch(IClick click) {

        if(btn_search != null) {
            btn_search.setVisibility(View.VISIBLE);
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick();
                }
            });
        }

        return this;
    }

    public CViewMenuTop onClickFilter(IClick click) {

        if(btn_filter != null) {
            btn_filter.setVisibility(View.VISIBLE);
            btn_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick();
                }
            });
        }

        return this;
    }

    public CViewMenuTop onClickQuestion(IClick click) {

        if(btn_question != null) {
            btn_question.setVisibility(View.VISIBLE);
            btn_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick();
                }
            });
        }

        return this;
    }

    public CViewMenuTop onClickQuestionLeft(IClick click) {

        if(btn_question_left != null) {
            btn_question_left.setVisibility(View.VISIBLE);
            btn_question_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick();
                }
            });
        }

        return this;
    }

    public interface IClick {
        void onClick();
    }

}
