package com.blm.qiubopay.modules.recarga;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.CompaniasAdapter;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ITaeSale;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.recarga.QPAY_TaeProductRequest;
import com.blm.qiubopay.models.recarga.QPAY_TaeProductResponse;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_pago_recarga_menu extends HFragment implements IMenuContext {

    private static final String TAG = "recarga_menu";

    private SearchView companiaSearchView;
    private RecyclerView companiasRecyclerView;

    private CompaniasAdapter companiasAdapter;

    public static Fragment_pago_recarga_menu newInstance() {
        return new Fragment_pago_recarga_menu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_recarga_menu, container, false),R.drawable.background_splash_header_1);
    }

    @Override
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

        companiaSearchView = getView().findViewById(R.id.compania_searchview);
        companiasRecyclerView = getView().findViewById(R.id.companias_recyclerview);
        setTaeAdapter(getListTae());

        companiaSearchView.setIconifiedByDefault(false);
        companiaSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                companiasAdapter.filtrar(newText);
                return false;
            }
        });

    }

    private void setTaeAdapter(List<CompaniaDTO> listCompanias){

        companiasAdapter = new CompaniasAdapter(getContext(), listCompanias, new CompaniasAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {

                getContext().hideKeyboard();

                CompaniaDTO recarga = listCompanias.get(clickedItemIndex);
                if(recarga.getAmounts()!=null){
                    //Si tiene lista de montos es recarga tradicional
                    getContext().setFragment(Fragment_pago_recarga_paquetes.newInstance((CompaniaDTO)recarga));
                } else {
                    //Sino entonces es MVNO, llamar por lo paquetes
                    getPackagesMVNO(recarga, new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            QPAY_TaeProductResponse response = (QPAY_TaeProductResponse) data[0];
                            if(response.getQpay_object()[0].getProducts()!=null
                                    && response.getQpay_object()[0].getProducts().length > 0) {
                                //Si tiene paquetes disponibles
                                recarga.setPackages(Arrays.asList(response.getQpay_object()[0].getProducts()));
                                getContext().setFragment(Fragment_pago_recarga_paquetes.newInstance((CompaniaDTO)recarga));
                            } else {
                                //Sino tiene paquetes disponibles notifica
                                getContext().alert(getString(R.string.text_recarga_sin_paquetes));
                            }
                        }
                    });
                }

            }
        });
        companiasRecyclerView.setAdapter(companiasAdapter);
        companiasRecyclerView.setHasFixedSize(true);
        companiasRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    }

    private List<CompaniaDTO> getListTae() {

        List<CompaniaDTO> result = new ArrayList();

        //TRADICIONALES

        result.add(new CompaniaDTO(Globals.ID_TELCEL, R.string.text_telcel, getString(R.string.text_telcel_tags), R.drawable.tae_telcel,
                new ArrayList<>(Arrays.asList("$10", "$20", "$30", "$50", "$80", "$100", "$150", "$200", "$300", "$500"))));
        result.add(new CompaniaDTO(Globals.ID_TELCEL_DATOS, R.string.text_telcel_datos, getString(R.string.text_telcel_datos_tags), R.drawable.tae_telcel_datos,
                new ArrayList<>(Arrays.asList("$20","$30","$50","$80","$100","$150","$200","$300","$500"))));
        result.add(new CompaniaDTO(Globals.ID_TELCEL_INTERNET, R.string.text_telcel_internet, getString(R.string.text_telcel_internet_tags), R.drawable.tae_telcel_internet,
                new ArrayList<>(Arrays.asList("$20","$30","$50","$80","$100","$150","$200","$300","$500"))));
        result.add(new CompaniaDTO(Globals.ID_MOVISTAR, R.string.text_movistar, getString(R.string.text_movistar_tags), R.drawable.tae_movistar,
                new ArrayList<>(Arrays.asList("$10","$20","$30","$40","$50","$60","$100","$150","$200","$300","$500"))));
        result.add(new CompaniaDTO(Globals.ID_IUSACELL, R.string.text_att, getString(R.string.text_att_tags), R.drawable.tae_att,
                new ArrayList<>(Arrays.asList("$15","$20","$30","$50","$70","$100","$150","$200","$300","$500"))));
        result.add(new CompaniaDTO(Globals.ID_UNEFON, R.string.text_unefon, getString(R.string.text_unefon_tags), R.drawable.tae_unefon,
                new ArrayList<>(Arrays.asList("$15","$20","$30","$50","$70","$100","$150","$200","$300","$500"))));
        //BAIT
        result.add(new CompaniaDTO(Globals.ID_BAIT, R.string.text_bait, getString(R.string.text_bait_tags), R.drawable.tae_bait, null));

        result.add(new CompaniaDTO(Globals.ID_VIRGIN_MOBILE, R.string.text_virgin, getString(R.string.text_virgin_tags), R.drawable.tae_virgin,
                new ArrayList<>(Arrays.asList("$20","$30","$50","$100","$150","$200","$300","$500"))));
        result.add(new CompaniaDTO(Globals.ID_FREEDOM_POP, R.string.text_freedom, getString(R.string.text_freedom_tags), R.drawable.tae_freedom_pop,
                new ArrayList<>(Arrays.asList("$30","$50","$80","$100","$200"))));

        //MVNO
        result.add(new CompaniaDTO(Globals.ID_MI_MOVIL, R.string.text_mi_movil, getString(R.string.text_mi_movil_tags), R.drawable.tae_mi_movil, null));
        result.add(new CompaniaDTO(Globals.ID_TURBORED_TAE, R.string.text_turbored, getString(R.string.text_turbored_tags), R.drawable.tae_turbored, null));
        result.add(new CompaniaDTO(Globals.ID_TURBORED_DATOS, R.string.text_turbored_datos, getString(R.string.text_turbored_datos_tags), R.drawable.tae_turbored_datos, null));
        result.add(new CompaniaDTO(Globals.ID_SPACE, R.string.text_space, getString(R.string.text_space_tags), R.drawable.tae_space, null));
        result.add(new CompaniaDTO(Globals.ID_DIRI, R.string.text_diri, getString(R.string.text_diri_tags), R.drawable.tae_diri, null));
        result.add(new CompaniaDTO(Globals.ID_PILLOFON, R.string.text_pillofon, getString(R.string.text_pillofon_tags), R.drawable.tae_pillofon, null));
        result.add(new CompaniaDTO(Globals.ID_FOBO, R.string.text_fobo, getString(R.string.text_fobo_tags), R.drawable.tae_fobos, null));

        return result;
    }

    private void getPackagesMVNO(CompaniaDTO  recarga, IFunction function){

        getContext().loading(true);

        QPAY_TaeProductRequest request = new QPAY_TaeProductRequest();
        request.setQpay_carrier(recarga.getId());
        request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        ITaeSale taeSaleListener = null;

        try {
            taeSaleListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_TaeProductResponse response = gson.fromJson(json, QPAY_TaeProductResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            if(function != null)
                                function.execute(response);

                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(),response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            },getContext());
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

        taeSaleListener.getTaeProducts(request);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}