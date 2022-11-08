package com.blm.qiubopay.modules.recarga;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.ServicePackagesAdapter;
import com.blm.qiubopay.builder.ServiceItemsBuilder;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.services.ServicePackageItemModel;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.utils.Globals;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_recarga_paquetes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_recarga_paquetes extends HFragment implements IMenuContext {

    private static final String TAG = "recarga_paquetes";

    private RecyclerView packagesRecyclerview;

    private CompaniaDTO companiaDTO;
    private ServicePackagesAdapter packagesAdapter;

    private boolean isTurboRed;

    public Fragment_pago_recarga_paquetes() {
        // Required empty public constructor
    }

    public static Fragment_pago_recarga_paquetes newInstance(Object... data) {
        Fragment_pago_recarga_paquetes fragment = new Fragment_pago_recarga_paquetes();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_recarga_paquetes", new Gson().toJson(data[0]));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_recarga_paquetes"),CompaniaDTO.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_recarga_paquetes, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment() {

        CViewMenuTop.create(getView())
                .showTitle(getString(R.string.text_recarga_title))
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        CViewSaldo.create(getContextMenu(), getView(), true);

        ImageView ivCompania = getView().findViewById(R.id.iv_compania);
        ivCompania.setImageDrawable(getResources().getDrawable(companiaDTO.getImage()));

        packagesRecyclerview = getView().findViewById(R.id.packages_recyclerview);

        ServiceItemsBuilder builder = new ServiceItemsBuilder();

        if (companiaDTO.getId().trim().compareTo(Globals.ID_TURBORED_TAE) == 0 ||
                companiaDTO.getId().trim().compareTo(Globals.ID_TURBORED_DATOS) == 0) {
            isTurboRed = true;
        }

        if(companiaDTO.getAmounts()!=null){
            setServiceAdapter(builder.getServiceItemListFromStrings(companiaDTO.getAmounts()),4);
        } else {
            //Si es turbored colocará las descripciones de los paquetes, de otra manera no
            setServiceAdapter(builder.getServiceItemListFromPaquetes(companiaDTO.getPackages(),isTurboRed),3);
        }

    }

    private void setServiceAdapter(List<ServicePackageItemModel> listItems, int columnas){
        //Si es turbored colocará las descripciones de los paquetes y no pintara los montos
        packagesAdapter = new ServicePackagesAdapter(getContext(), listItems,
                new ServicePackagesAdapter.ListPackageClickListener() {
                    @Override
                    public void onListPackageClick(int clickedItemIndex) {
                        ServicePackageItemModel item = listItems.get(clickedItemIndex);

                        QPAY_TaeSale taeSale = new QPAY_TaeSale();
                        taeSale.setQpay_carrier(companiaDTO.getId());
                        taeSale.setIdOffer(item.getId()!=null && !item.getId().isEmpty() ? item.getId() : null);
                        taeSale.setQpay_amount(item.getAmount());

                        getContext().setFragment(Fragment_pago_recarga_numero.newInstance((CompaniaDTO) companiaDTO,(QPAY_TaeSale) taeSale));
                    }
                },isTurboRed);
        packagesRecyclerview.setAdapter(packagesAdapter);
        packagesRecyclerview.setHasFixedSize(true);
        //Si es turbored solo pinta un registro con la descripcion
        if(isTurboRed){
            packagesRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            packagesRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), columnas));
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}