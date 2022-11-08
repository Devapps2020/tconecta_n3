package com.blm.qiubopay.modules.remesas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.remesas.TC_PayRemittancePetition;
import com.blm.qiubopay.models.remesas.TC_PayRemittanceResponse1;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_remesas_5 extends HFragment implements IMenuContext {

    private TC_PayRemittanceResponse1 response;
    private TC_PayRemittancePetition payRemittancePetition;

    private Object data;
    private Object data2;

    public static Fragment_remesas_5 newInstance(Object... data) {
        Fragment_remesas_5 fragment = new Fragment_remesas_5();
        Bundle args = new Bundle();

        if(data.length > 0) {
            args.putString("Fragment_remesas_5", new Gson().toJson(data[0]));
            args.putString("Fragment_remesas_5_2", new Gson().toJson(data[1]));
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_remesas_5, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public boolean onBackPressed() {
        getContextMenu().initHome();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            data = new Gson().fromJson(getArguments().getString("Fragment_remesas_5"), Object.class);
            data2 = new Gson().fromJson(getArguments().getString("Fragment_remesas_5_2"), Object.class);
        }
    }

    @Override
    public void initFragment() {

        Gson gson = new Gson();
        String queryData = gson.toJson(data);
        response = gson.fromJson(queryData, TC_PayRemittanceResponse1.class);

        queryData = gson.toJson(data2);
        payRemittancePetition = gson.fromJson(queryData, TC_PayRemittancePetition.class);

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        onBackPressed();
                    }
                });

        Button btn_compartir = getView().findViewById(R.id.btn_compartir);
        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().alert("¿Desea reimprimir el recibo?", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        printTicket(new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                            }
                        });
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });
            }
        });

        Button btn_terminar = getView().findViewById(R.id.btn_terminar);
        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        printTicket(new IFunction() {
            @Override
            public void execute(Object[] data) {

            }
        });
    }

    private String getDateOrTime(String timestamp, int index){
        String back = "";
        String[] array = timestamp.split(" ");

        back = array[0];

        return back;
    }

    private void printTicket(final IFunction function){
        //N3_FLAG_COMMENT
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "PAGO DE REMESA"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Numero de autorización:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getTrxId()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Remesador:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getRemesador()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Remitente:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, String.format("%s %s %s %s", payRemittancePetition.getRemitente().getPrimerNombre(),payRemittancePetition.getRemitente().getSegundoNombre(),payRemittancePetition.getRemitente().getPrimerApellido(),payRemittancePetition.getRemitente().getSegundoApellido())));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Beneficiario:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, String.format("%s %s %s %s", payRemittancePetition.getBeneficiario().getPrimerNombre(),payRemittancePetition.getBeneficiario().getSegundoNombre(),payRemittancePetition.getBeneficiario().getPrimerApellido(),payRemittancePetition.getBeneficiario().getSegundoApellido())));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Monto en dólares:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, Utils.paserCurrency(""+response.getMontoMonedaOrigen()) + " " + response.getMonedaOrigen()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Monto en pesos:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, Utils.paserCurrency(""+response.getMontoMonedaPago()) + " " + response.getMonedaPago()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Fecha y hora:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getCreatedAt().replace(".0","")));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Tipo de cambio:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "$1.00 " + response.getMonedaOrigen() + " = " + Utils.paserCurrency(response.getTipoCambio()) + " " + response.getMonedaPago()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Folio:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getTransactionId()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Clave de cobro:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getVendorReference()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Forma de pago:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "Efectivo"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Total pagado:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getTotalLetra()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Datos fiscales del pagador:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, response.getDatosFiscales()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Liga de aviso de privacidad:"));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, Globals.URL_REMESAS_TERMINOS_Y_CONDICIONES));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        String promo = response.getPromo() != null && !response.getPromo().isEmpty() ? response.getPromo() : " ";
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, promo));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));


        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, getContext());

    }
}
