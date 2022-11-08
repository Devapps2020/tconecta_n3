package com.blm.qiubopay.modules.servicio;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.ServicePackagesAdapter;
import com.blm.qiubopay.builder.ServiceItemsBuilder;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.models.services.ServicePackageItemModel;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_numero;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_paquetes;
import com.blm.qiubopay.utils.Globals;
import com.google.gson.Gson;

import java.util.List;

import mx.devapps.utils.components.HFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_pago_servicio_paquetes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_pago_servicio_paquetes extends HFragment implements IMenuContext {

    private static final String TAG = "pago_servicio_pckgs";

    private RecyclerView packagesRecyclerview;

    private CompaniaDTO companiaDTO;
    private QPAY_ServicePayment servicePayment;
    private ServicePackagesAdapter packagesAdapter;

    public Fragment_pago_servicio_paquetes() {
        // Required empty public constructor
    }

    public static Fragment_pago_servicio_paquetes newInstance(Object... data) {
        Fragment_pago_servicio_paquetes fragment = new Fragment_pago_servicio_paquetes();

        Bundle args = new Bundle();
        args.putString("Fragment_pago_servicio_paquetes_1", new Gson().toJson(data[0]));
        args.putString("Fragment_pago_servicio_paquetes_2", new Gson().toJson(data[1]));
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
            companiaDTO = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_paquetes_1"),CompaniaDTO.class);
            servicePayment = new Gson().fromJson(getArguments().getString("Fragment_pago_servicio_paquetes_2"),QPAY_ServicePayment.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_paquetes, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment() {

        CViewMenuTop.create(getView())
                .showTitle(getString(R.string.text_servicio_title))
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

        if(companiaDTO.getAmounts()!=null){
            setServiceAdapter(builder.getServiceItemListFromStrings(companiaDTO.getAmounts()),3);
        } else {
            if(companiaDTO.getId().equals(Globals.NETWAY_ID)) {
                setServiceAdapter(builder.getServiceItemListFromNetweyResponse(companiaDTO.getServices()), 0);
            }
        }

    }

    private void setServiceAdapter(List<ServicePackageItemModel> listItems, int columnas){

        packagesAdapter = new ServicePackagesAdapter(getContext(), listItems,
                new ServicePackagesAdapter.ListPackageClickListener() {
                    @Override
                    public void onListPackageClick(int clickedItemIndex) {
                        ServicePackageItemModel item = listItems.get(clickedItemIndex);

                        servicePayment.setQpay_amount(item.getAmount().replace("$","").replace(",",""));
                        if(servicePayment.getQpay_product().equals(Globals.NETWAY_ID)){
                            servicePayment.setQpay_name_client(item.getDescription());
                            servicePayment.setQpay_product_id(item.getId());
                        }

                        getContext().setFragment(Fragment_pago_servicio_confirma.newInstance((CompaniaDTO)companiaDTO,(QPAY_ServicePayment)servicePayment));
                    }
                },false);
        packagesRecyclerview.setAdapter(packagesAdapter);
        packagesRecyclerview.setHasFixedSize(true);

        if(columnas==0){
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