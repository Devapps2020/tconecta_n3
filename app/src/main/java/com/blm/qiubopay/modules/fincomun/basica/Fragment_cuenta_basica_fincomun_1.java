package com.blm.qiubopay.modules.fincomun.basica;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.SliderItem;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;

public class Fragment_cuenta_basica_fincomun_1 extends HFragment implements IMenuContext {


    Button btn_next;
    SliderView sliderView;

    public static Fragment_cuenta_basica_fincomun_1 newInstance(Object... data) {
       Fragment_cuenta_basica_fincomun_1 fragment = new Fragment_cuenta_basica_fincomun_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_basica_fincomun_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        btn_next = getView().findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_cuenta_basica_fincomun_2.newInstance());
            }
        });

        CheckBox check_acept = getView().findViewById(R.id.check_acept);
        check_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_next.setEnabled(check_acept.isChecked());
            }
        });

        sliderView = getView().findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.NONE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.TRANSPARENT);
        sliderView.setIndicatorUnselectedColor(Color.TRANSPARENT);
        sliderView.setScrollTimeInSec(0);
        sliderView.setAutoCycle(false);
        sliderView.setOffscreenPageLimit(1);
        sliderView.setVerticalScrollBarEnabled(false);
        sliderView.setHorizontalScrollBarEnabled(false);

        List<SliderItem> sliderItemList = new ArrayList<>();
        //sliderItemList.add(new SliderItem("Ingresa tus datos de contacto", R.drawable.img_fc_0, false));
        //sliderItemList.add(new SliderItem("Captura tu identificación", R.drawable.img_fc_1, false));
        //sliderItemList.add(new SliderItem("Tomate una foto", R.drawable.img_fc_2, false));
        //sliderItemList.add(new SliderItem("Ingresa tus beneficiarios", R.drawable.img_fc_3, false));
        //sliderItemList.add(new SliderItem("Crea tu usuario", R.drawable.img_fc_4, false));
        sliderItemList.add(new SliderItem("He leído y estoy de acuerdo con el aviso de privacidad", R.drawable.img_fc_5, false));

        adapter.renewItems(sliderItemList);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterVH> {

        private Context context;
        private List<SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        public void renewItems(List<SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
            notifyDataSetChanged();
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

            SliderItem sliderItem = mSliderItems.get(position);

            viewHolder.imageViewBackground.setImageDrawable(context.getResources().getDrawable(sliderItem.getImagen()));
            viewHolder.textViewDescription.setText(sliderItem.getTexto());

        }


        @Override
        public int getCount() {
            return mSliderItems.size();
        }
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;
        LinearLayout layout_terminos;
        CheckBox check_acept;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.img_imagen);
            textViewDescription = itemView.findViewById(R.id.text_description);
            layout_terminos = itemView.findViewById(R.id.layout_terminos);
            check_acept = itemView.findViewById(R.id.check_acept);
            this.itemView = itemView;
        }
    }

}
