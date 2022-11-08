package com.blm.qiubopay.modules.servicio;

import android.os.Bundle;
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
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.utils.Globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;

public class Fragment_pago_servicio_menu extends HFragment implements IMenuContext {

    private static final String TAG = "servicio_menu";

    private SearchView companiaSearchView;
    private RecyclerView companiasRecyclerView;

    private CompaniasAdapter companiasAdapter;

    public static Fragment_pago_servicio_menu newInstance() {
        return new Fragment_pago_servicio_menu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_servicio_menu, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .showTitle(getString(R.string.text_servicio_title))
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().initHome();
                    }
                });

        CViewSaldo.create(getContextMenu(), getView(), true);

        companiaSearchView = getView().findViewById(R.id.compania_searchview);
        companiasRecyclerView = getView().findViewById(R.id.companias_recyclerview);
        setServiceAdapter(getListService());

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

    private void setServiceAdapter(List<CompaniaDTO> listCompanias){

        companiasAdapter = new CompaniasAdapter(getContext(), listCompanias, new CompaniasAdapter.ListItemClickListener() {
            @Override
            public void onItemClick(int clickedItemIndex) {

                getContext().hideKeyboard();

                CompaniaDTO servicio = listCompanias.get(clickedItemIndex);

                QPAY_ServicePayment servicePayment = new QPAY_ServicePayment();
                servicePayment.setQpay_product(servicio.getId());
                servicePayment.setQpay_name_product(getString(servicio.getName()));

                getContext().setFragment(Fragment_pago_servicio_referencia.newInstance((CompaniaDTO)servicio,(QPAY_ServicePayment)servicePayment));

            }
        });
        companiasRecyclerView.setAdapter(companiasAdapter);
        companiasRecyclerView.setHasFixedSize(true);
        companiasRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

    }

    private List<CompaniaDTO> getListService() {

        List<CompaniaDTO> result = new ArrayList();

        result.add(new CompaniaDTO(Globals.TELMEX_ID, R.string.text_telmex, getString(R.string.text_telmex), R.drawable.ps_telmex, null,""));
        result.add(new CompaniaDTO(Globals.SKY_ID, R.string.text_sky, getString(R.string.text_sky), R.drawable.ps_sky, null,""));
        result.add(new CompaniaDTO(Globals.VTV_ID, R.string.text_vtv, getString(R.string.text_vtv), R.drawable.ps_vtv, null,""));
        result.add(new CompaniaDTO(Globals.CFE_ID, R.string.text_cfe, getString(R.string.text_cfe), R.drawable.ps_cfe, null,""));
        result.add(new CompaniaDTO(Globals.DISH_ID, R.string.text_dish, getString(R.string.text_dish), R.drawable.ps_dish, null,""));
        result.add(new CompaniaDTO(Globals.TELEVIA_ID, R.string.text_televia, getString(R.string.text_televia), R.drawable.ps_televia,
                new ArrayList<>(Arrays.asList("$100.00","$200.00","$300.00")),""));
        result.add(new CompaniaDTO(Globals.NATURGY_ID, R.string.text_naturgy, getString(R.string.text_naturgy), R.drawable.ps_naturgy, null,""));
        result.add(new CompaniaDTO(Globals.IZZI_ID, R.string.text_izzi, getString(R.string.text_izzi), R.drawable.ps_izzi, null,""));
        //result.add(new CompaniaDTO(Globals.OMNIBUS_ID, R.string.text_omnibus, getString(R.string.text_omnibus), R.drawable.ps, null,""));
        result.add(new CompaniaDTO(Globals.MEGACABLE_ID, R.string.text_megacable, getString(R.string.text_megacable), R.drawable.ps_megacable, null,""));
        result.add(new CompaniaDTO(Globals.PASE_URBANO_ID, R.string.text_pase_urbano, getString(R.string.text_pase_urbano), R.drawable.ps_pase_urbano,
                new ArrayList<>(Arrays.asList("$200.00","$300.00","$400.00","$500.00","$600.00","$700.00","$800.00","$900.00","$1000.00")),"IMDM"));
        result.add(new CompaniaDTO(Globals.TOTALPLAY_ID, R.string.text_totalplay, getString(R.string.text_totalplay), R.drawable.ps_totalplay, null,""));
        result.add(new CompaniaDTO(Globals.STARTV_ID, R.string.text_star_tv, getString(R.string.text_star_tv), R.drawable.ps_star_tv, null,"819"));
        result.add(new CompaniaDTO(Globals.CEA_QRO_ID, R.string.text_cea_qro, getString(R.string.text_cea_qro), R.drawable.ps_cea_qro, null,"224"));
        result.add(new CompaniaDTO(Globals.NETWAY_ID, R.string.text_netwey, getString(R.string.text_netwey), R.drawable.ps_netwey, null,"232"));
        result.add(new CompaniaDTO(Globals.VEOLIA_ID, R.string.text_veolia, getString(R.string.text_veolia), R.drawable.ps_veolia, null,"223"));
        //result.add(new CompaniaDTO(Globals.AMAZON_CASH_ID, R.string.text_amazon_cash, getString(R.string.text_amazon_cash), R.drawable.tae_mi_movil, null,"8219"));
        result.add(new CompaniaDTO(Globals.BLUE_TEL_ID, R.string.text_blue_tel, getString(R.string.text_blue_tel), R.drawable.ps_blue_tel, null,"501"));
        result.add(new CompaniaDTO(Globals.POST_ATT_ID, R.string.text_post_att, getString(R.string.text_post_att), R.drawable.ps_plan_att, null,""));
        result.add(new CompaniaDTO(Globals.POST_MOVISTAR_ID, R.string.text_post_movistar, getString(R.string.text_post_movistar), R.drawable.ps_plan_movistar, null,""));
        result.add(new CompaniaDTO(Globals.SACMEX_ID, R.string.text_sacmex, getString(R.string.text_sacmex), R.drawable.ps_sacmex, null,""));
        result.add(new CompaniaDTO(Globals.GOB_MX_ID, R.string.text_gob_mx, getString(R.string.text_gob_mx), R.drawable.ps_gob_mx, null,""));

        result.add(new CompaniaDTO(Globals.GOB_EDOMEX_ID, R.string.text_gob_edomex, getString(R.string.text_gob_edomex), R.drawable.ps_gob_edomex, null,""));
        result.add(new CompaniaDTO(Globals.AYDM_MTY_ID, R.string.text_aydm_mty, getString(R.string.text_aydm_mty), R.drawable.ps_adym_mty, null,""));
        result.add(new CompaniaDTO(Globals.SIAPA_GDL_ID, R.string.text_siapa_gdl, getString(R.string.text_siapa_gdl), R.drawable.ps_siapa_gdl, null,""));
        result.add(new CompaniaDTO(Globals.OPDM_ID, R.string.text_opdm, getString(R.string.text_opdm), R.drawable.ps_opdm, null,"222"));
        result.add(new CompaniaDTO(Globals.CESPT_TIJ_ID, R.string.text_cespt_tij, getString(R.string.text_cespt_tij), R.drawable.ps_cespt_tij, null,""));


        return result;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}