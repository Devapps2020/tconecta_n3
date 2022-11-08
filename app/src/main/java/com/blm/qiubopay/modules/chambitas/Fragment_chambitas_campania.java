package com.blm.qiubopay.modules.chambitas;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign_Object;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;
import com.blm.qiubopay.modules.chambitas.cupones.FragmentCouponDetails;
import com.blm.qiubopay.modules.chambitas.cupones.viewmodel.CouponsVM;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_chambitas_campania extends HFragment implements IMenuContext {

    public static Integer type = 0;
    public static List<QPAY_ActiveCampaign_Object> list = new ArrayList<>();
    private CouponsVM viewModel;

    public static Fragment_chambitas_campania newInstance() {
        return new Fragment_chambitas_campania();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_chambitas_retos, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        viewModel = new CouponsVM(getContext());
        setObservers();
        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        ListView list_challenges = getView().findViewById(R.id.list_challenge);


        //ARREGLAR EL SERVICIO DE LA LISTA DE DONDE SE OBTIENE
        ChallengeAdapter adapter = new ChallengeAdapter(getContext(),list);
        list_challenges.setAdapter(adapter);

        list_challenges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                QPAY_ActiveCampaign_Object item = list.get(position);

                if(type == 3)
                    return;

                String description = "Retos:\n\n";

                for(int i=0; i<item.getChallenges().size(); i++)
                    description += (i+1) + ".-  " + item.getChallenges().get(i).getDescription() + "\n";

                getContext().setItemAlert(R.layout.view_alert_modal_left);
                getContext().alert(item.getName(), description, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Continuar";
                    }
                    @Override
                    public void onClick() {
                        Fragment_chambitas_retos.campaign_id = item.getId();
                        Fragment_chambitas_retos.list = item.getChallenges();
                        getContext().setFragment(Fragment_chambitas_retos.newInstance());
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Cancelar";
                    }

                    @Override
                    public void onClick() {

                    }
                });
                getContext().setItemAlert(R.layout.view_alert_modal);


            }
        });

        TextView text_title = getView().findViewById(R.id.text_title);

        switch (type) {
            case 1:
                text_title.setText(Html.fromHtml("Chambita<br><b>Nueva</b>"));
            break;
            case 2:
                text_title.setText(Html.fromHtml("Chambita<br><b>Activa</b>"));
                break;
            case 3:
                text_title.setText(Html.fromHtml("Chambita<br><b>Cumplida</b>"));
                break;
        }

    }

    private void setObservers() {
        viewModel._couponDetailByUserResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<CouponDetailsListResponse>>() {
            @Override
            public void onChanged(BaseResponse<CouponDetailsListResponse> couponDetailsListResponseBaseResponse) {
                if (couponDetailsListResponseBaseResponse != null) {
                    if (!couponDetailsListResponseBaseResponse.getQpayObject().isEmpty()) {
                        ((MenuActivity) getContext()).setFragment(FragmentCouponDetails.newInstance(couponDetailsListResponseBaseResponse.getQpayObject().get(0)));
                    }
                }
            }
        });
    }

    public class ChallengeAdapter extends ArrayAdapter<QPAY_ActiveCampaign_Object> {

        public ChallengeAdapter(Context context, List<QPAY_ActiveCampaign_Object> datos) {
            super(context, 0, datos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_ActiveCampaign_Object item = getItem(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_option_challenge, parent, false);

            TextView text_name = convertView.findViewById(R.id.text_name);
            TextView text_prize = convertView.findViewById(R.id.text_prize);
            Button btnWatchCoupon = convertView.findViewById(R.id.btnWatchCoupon);
            if (item.getStatus().equals("Premio Entregado")) {
                if (item.getCoupon() != null) {
                    if (item.getCoupon().getPrintedAt() == null || item.getCoupon().getPrintedAt().isEmpty()) btnWatchCoupon.setVisibility(View.VISIBLE);
                }
            }

            btnWatchCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
                    if (bimboId == null || bimboId.isEmpty()) {
                        ((MenuActivity) getContext()).setFragment(FragmentCouponDetails.newInstance(null));
                    } else {
                        viewModel.getCouponDetailByUser(item.getCoupon().getId() + "");
                    }
                }
            });

            text_name.setText(item.getName());
            text_prize.setText(Html.fromHtml("<b>Premio: </b>" + item.getPrize_amount()));

            if(type == 3)
                text_prize.setText(Html.fromHtml("<b>Premio: </b>" + item.getPrize_amount() + "<br><b>Estatus: </b>" + item.getStatus()));

            return convertView;
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}