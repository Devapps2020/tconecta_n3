package com.blm.qiubopay.modules.reportes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.CompaniasAdapter;
import com.blm.qiubopay.adapters.FinancialReportAdapter;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IReports;
import com.blm.qiubopay.listeners.ITaeSale;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.recarga.CompaniaDTO;
import com.blm.qiubopay.models.recarga.QPAY_TaeProductRequest;
import com.blm.qiubopay.models.recarga.QPAY_TaeProductResponse;
import com.blm.qiubopay.models.reportes.FinancialReport;
import com.blm.qiubopay.models.reportes.FinancialReportRequest;
import com.blm.qiubopay.models.reportes.FinancialReportResponse;
import com.blm.qiubopay.models.reportes.FinancialReportTxn;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_menu;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_paquetes;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_reporte_financiero#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_reporte_financiero extends HFragment implements IMenuContext {

    private static final String TAG = "reporte_financiero";

    CViewEditText cvEditFecha;

    private RecyclerView reportRecyclerView;
    private NestedScrollView scrollViewReport;

    private FinancialReportAdapter reportAdapter;

    private String actualDate;
    private int actualPage = 0;
    private boolean hasNextPage;
    private boolean isLoading;

    private List<FinancialReportTxn> listTxn;

    public static Fragment_reporte_financiero newInstance() {
        return new Fragment_reporte_financiero();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_reporte_financiero, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        scrollViewReport = getView().findViewById(R.id.scrollview_report);
        reportRecyclerView = getView().findViewById(R.id.companias_recyclerview);

        cvEditFecha = CViewEditText.create(getView().findViewById(R.id.edit_fecha))
                .setRequiredInit(true)
                .setEnabledInit(false)
                .setMinimumInit(10)
                .setMaximumInit(10)
                .setTypeInit(CViewEditText.TYPE.NONE)
                .setHintInit(R.string.text_rf_fecha)
                .setAlertInit(R.string.text_input_required)
                .setTextChangedInit(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if(actualPage>0){
                            if(actualDate.compareTo(searchDate())!=0){
                                actualPage=0;
                                initSearch();
                                cleanSummary();
                            }
                        }
                    }
                });

        cvEditFecha.setDatePickerReport();

        scrollViewReport.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(hasNextPage && actualPage>0){
                    if(v.getChildAt(v.getChildCount() - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            if(!isLoading) {
                                Log.d(TAG,"Cargando mas info");
                                isLoading=true;
                                getList(searchDate(),++actualPage);
                            }
                        }
                    }
                }
            }
        });

        initSearch();

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    public void initSearch(){
        if(actualPage==0){
            actualDate = searchDate();
            listTxn = new ArrayList<FinancialReportTxn>();
            getList(searchDate(),actualPage++);
        }
    }


    private String searchDate(){
        String sDate = "";
        String date[] = cvEditFecha.getText().split("/");
        if(date.length>0)
            sDate = date[2] + "-" + date[1] + "-" + date[0];
        return sDate;
    }


    private void setReportAdapter(){
        if(actualPage > 1){
            reportAdapter.setData(listTxn);
            reportAdapter.notifyDataSetChanged();
        } else {
            reportAdapter = new FinancialReportAdapter(new FinancialReportAdapter.ListTxnClickListener() {
                @Override
                public void onListTxnClick(int clickedItemIndex) {
                    FinancialReportTxn item = listTxn.get(clickedItemIndex);
                    if(!item.getMarca().toUpperCase().contains("TRASPASO")){
                        getContext().setFragment(Fragment_reporte_financiero_detalle.newInstance(item));
                    }
                }
            });
            reportAdapter.setData(listTxn);
            reportRecyclerView.setAdapter(reportAdapter);
            reportRecyclerView.setHasFixedSize(true);
            reportRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        isLoading = false;
    }


    private void setSummary(FinancialReportResponse summary){

        final TextView tvTotalCommission = getView().findViewById(R.id.tv_total_commission);
        final TextView tvTotalNetValue = getView().findViewById(R.id.tv_total_net_value);
        final TextView tvTotalSettlement = getView().findViewById(R.id.tv_total_settlement);

        String tasaDescuento = summary.getQpay_object()[0].getTasaDescuento()!=null ?
                summary.getQpay_object()[0].getTasaDescuento().toString() : "0.0";
        String importeNeto = summary.getQpay_object()[0].getImporteNeto()!=null ?
                summary.getQpay_object()[0].getImporteNeto().toString() : "0.0";
        String liquidacionBimbonet = summary.getQpay_object()[0].getLiquidacionBimbonet()!=null ?
                summary.getQpay_object()[0].getLiquidacionBimbonet().toString() : "0.0";

        tvTotalCommission.setText(Utils.paserCurrency(tasaDescuento));
        tvTotalNetValue.setText(Utils.paserCurrency(importeNeto));
        tvTotalSettlement.setText(Utils.paserCurrency(liquidacionBimbonet));

    }


    private void cleanSummary(){

        final TextView tvTotalCommission = getView().findViewById(R.id.tv_total_commission);
        final TextView tvTotalNetValue = getView().findViewById(R.id.tv_total_net_value);
        final TextView tvTotalSettlement = getView().findViewById(R.id.tv_total_settlement);

        tvTotalCommission.setText(Utils.paserCurrency("0.0"));
        tvTotalNetValue.setText(Utils.paserCurrency("0.0"));
        tvTotalSettlement.setText(Utils.paserCurrency("0.0"));

    }


    private void getList(String searchDate, Integer page){

        getContext().loading(true);

        FinancialReportRequest request = new FinancialReportRequest();
        request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        request.setPageAfter(page.toString());
        request.setStartDate(searchDate);
        request.setEndDate(searchDate);

        IReports iReportListener = null;

        try {
            iReportListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        FinancialReportResponse response = gson.fromJson(json, FinancialReportResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(response.getQpay_object()[0].getNextPage()!=null){
                                hasNextPage = true;
                            } else {
                                hasNextPage = false;
                            }

                            if(response.getQpay_object()[0].getFinTrxList()!=null &&
                                    response.getQpay_object()[0].getFinTrxList().size()>0) {
                                for(FinancialReportTxn txn : response.getQpay_object()[0].getFinTrxList()){
                                    listTxn.add(txn);
                                }
                            }

                            if(page == 0){
                                if(listTxn.isEmpty()){
                                    getContext().alert(R.string.text_rf_alert_sin_registros);
                                } else {
                                    setSummary(response);
                                }
                            }

                            setReportAdapter();

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

        iReportListener.getFinancialReport(request);

    }


    /*private void getList(String searchDate, Integer page){

        FinancialReportRequest request = new FinancialReportRequest();
        request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        request.setPageAfter(page.toString());
        request.setStartDate(searchDate);
        request.setEndDate(searchDate);

        FinancialReportResponse response = new FinancialReportResponse();

        List<FinancialReportTxn> txns = new ArrayList<FinancialReportTxn>();
        for(int x=0; x<10; x++){
            FinancialReportTxn txn1 = new FinancialReportTxn();
            txn1.setFechaHora("29/10/2021 23:49");
            txn1.setTasaDescuento(10.0);
            txn1.setMarca("VI");
            txn1.setImporteNeto(100.0);
            txn1.setAutorizacion("123456789");
            txn1.setTarjeta("1234");
            txn1.setImporte(100.0+x);
            txns.add(txn1);
        }

        FinancialReportTxn txn1 = new FinancialReportTxn();
        txn1.setFechaHora("29/10/2021 23:49");
        txn1.setMarca("Traspaso");
        txn1.setImporte(1000.0);
        txns.add(txn1);

        if(actualPage == 1){

            FinancialReport report = new FinancialReport();
            report.setImporteNeto(1000.0);
            report.setTasaDescuento(100.0);
            report.setTransferenciaSaldo(1000.0);
            report.setLiquidacionBimbonet(200.0);
            report.setFinTrxList(txns);
            report.setNextPage("2");

            FinancialReport[] reports = new FinancialReport[1];
            reports[0] = report;

            response.setQpay_object(reports);

        } else if(actualPage==2) {

            FinancialReport report = new FinancialReport();
            report.setFinTrxList(txns);
            report.setNextPage("3");

            FinancialReport[] reports = new FinancialReport[1];
            reports[0] = report;

            response.setQpay_object(reports);
        } else {

            FinancialReport report = new FinancialReport();
            report.setFinTrxList(txns);

            FinancialReport[] reports = new FinancialReport[1];
            reports[0] = report;

            response.setQpay_object(reports);
        }



        if(response.getQpay_object()[0].getNextPage()!=null){
            hasNextPage = true;
        } else {
            hasNextPage = false;
        }

        if(response.getQpay_object()[0].getFinTrxList()!=null &&
                response.getQpay_object()[0].getFinTrxList().size()>0) {
            for(FinancialReportTxn txn : response.getQpay_object()[0].getFinTrxList()){
                listTxn.add(txn);
            }
        }

        if(page == 0){
            if(listTxn.isEmpty()){
                getContext().alert(R.string.text_rf_alert_sin_registros);
            } else {
                setSummary(response);
            }
        }

        setReportAdapter();

    }*/


}