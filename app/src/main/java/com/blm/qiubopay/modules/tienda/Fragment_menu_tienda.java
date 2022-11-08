package com.blm.qiubopay.modules.tienda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.coupons.CouponsCountResponse;
import com.blm.qiubopay.modules.chambitas.Fragment_chambitas_menu;
import com.blm.qiubopay.modules.chambitas.cupones.FragmentCoupons;
import com.blm.qiubopay.modules.chambitas.cupones.viewmodel.CouponsVM;
import com.blm.qiubopay.modules.fiado.Fragment_fiado_1;
import com.blm.qiubopay.modules.perfil.Fragment_bimbo_id;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.CalcularActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_tienda extends HFragment implements IMenuContext {

    private CouponsVM viewModel;
    private LinearLayout llCouponsCountBadge;
    private TextView tvCouponsCounter;
    public static Fragment_menu_tienda newInstance() {
        return new Fragment_menu_tienda();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_tienda, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new CouponsVM(getContext());
        init();
        setListeners();
        setObservers();
    }

    private void init() {
        llCouponsCountBadge = getView().findViewById(R.id.llCouponsCountBadge);
        tvCouponsCounter = getView().findViewById(R.id.tvCouponsCounter);
        viewModel.getCountCouponsbyUser();
        llCouponsCountBadge.setVisibility(View.GONE);
    }

    private void setListeners() {
        getContext().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (((MenuActivity) getContext()).wasCouponDetailsOpenBadgeTest) {
                    ((MenuActivity) getContext()).wasCouponDetailsOpenBadgeTest = false;
                    viewModel.getCountCouponsbyUser();
                }
            }
        });
    }

    private void setObservers() {
        viewModel._couponsCountResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<CouponsCountResponse>>() {
            @Override
            public void onChanged(BaseResponse<CouponsCountResponse> baseResponse) {
                if (baseResponse != null) {
                    if (baseResponse.getQpayObject() != null && !baseResponse.getQpayObject().isEmpty()) {
                        if (!baseResponse.getQpayObject().get(0).getCouponsAvailable().equals("0")) {
                            llCouponsCountBadge.setVisibility(View.VISIBLE);
                            tvCouponsCounter.setText(baseResponse.getQpayObject().get(0).getCouponsAvailable());
                        } else {
                            llCouponsCountBadge.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView()).showLogo();
        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });
        CardView card_mis_clientes = getView().findViewById(R.id.card_mis_clientes);
        CardView card_mis_proveedores = getView().findViewById(R.id.card_mis_proveedores);
        CardView card_aprende = getView().findViewById(R.id.card_aprende);
        CardView card_chambitas = getView().findViewById(R.id.card_chambitas);
        CardView card_fiado = getView().findViewById(R.id.card_fiado);
        CardView card_cupones = getView().findViewById(R.id.card_cupones);
        LinearLayout llCouponsCountBadge = getView().findViewById(R.id.llCouponsCountBadge);


        //220610 RSB. Improvements cell. Ocultar Enko
        card_aprende.setVisibility(View.GONE);
        card_aprende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppPreferences.getIsRegisteredEnko()){
                    getContextMenu().loginEnko();
                }else{
                    getContextMenu().registerEnko();
                }
            }
        });

        card_mis_clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_menu_tienda_clientes.newInstance());
            }
        });

        card_mis_proveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().validaBimboId(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_menu_tienda_proveedor.newInstance());
                    }
                });
            }
        });

        card_chambitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().setFragment(Fragment_chambitas_menu.newInstance());

            }
        });

        card_fiado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().validacionFinanciera = new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContextMenu().servicioFinanciero(1);
                    }
                };

                getContextMenu().setFragment(Fragment_fiado_1.newInstance());

            }
        });

        card_cupones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().setFragment(FragmentCoupons.newInstance());
            }
        });

        Button btn_calculadora = getView().findViewById(R.id.btn_calculadora);
        btn_calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(CalcularActivity.class);
            }
        });

        //20210531 RSB. Recomendaciones. Activar ingresar codigo bimbo
        TextView btnCodigoBimbo = getView().findViewById(R.id.btn_codigo_bimbo);
        String bimboID = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        if(bimboID == null || bimboID.isEmpty()) {
            btnCodigoBimbo.setVisibility(View.VISIBLE);
            btnCodigoBimbo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().setFragment(Fragment_bimbo_id.newInstance());
                }
            });
        } else {
            btnCodigoBimbo.setVisibility(View.GONE);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}