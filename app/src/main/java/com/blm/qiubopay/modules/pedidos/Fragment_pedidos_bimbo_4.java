package com.blm.qiubopay.modules.pedidos;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory_Object;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization_Object;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import mx.devapps.utils.components.HFragment;

public class Fragment_pedidos_bimbo_4 extends HFragment implements IMenuContext {

    public static String numero;

    public static PedidoRequest order;
    private ListView list_pedidos;
    private String brand = "";
    TextView text_subtotal;
    TextView text_total;
    TextView text_ticket;

    double total;

    public static Fragment_pedidos_bimbo_4 newInstance() {
        return new Fragment_pedidos_bimbo_4();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pedidos_bimbo_4, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        //2021-12-21 RSB. Analytics unilever
        CApplication.setAnalytics(CApplication.ACTION.Market_TicketDePedido);

        CViewMenuTop.create(getView()).showLogo();

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        for (QPAY_GetInventory_Object ped: order.getProduct()) {
            if(ped.getIdOrganization().equals(brand)) {
                ped.setFirst(false);
            } else {
                ped.setFirst(true);
                brand = ped.getIdOrganization();
            }

        }


        list_pedidos = getView().findViewById(R.id.list_pedidos);
        pedidosAdapter adapter = new pedidosAdapter(getContext(), order.getProduct());
        list_pedidos.setAdapter(adapter);

        Utils.setListViewHeightBasedOnChildren(list_pedidos);

        text_subtotal = getView().findViewById(R.id.text_subtotal);
        text_total = getView().findViewById(R.id.text_total);
        text_ticket = getView().findViewById(R.id.text_ticket);

        text_ticket.setText(Html.fromHtml("Ticket<br>de pedido<br>" + "<b>" + numero + "</b>"));


        updateMontos();

        Button btn_finish = getView().findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        Button btn_print = getView().findViewById(R.id.btn_print);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print(numero);
            }
        });

        btn_print.setVisibility(View.VISIBLE);

        if(order.getProduct().get(0).getIdTicket() != null)
            btn_finish.setVisibility(View.GONE);

    }

    public void updateMontos() {

        total = 0;

        for (QPAY_GetInventory_Object pro : order.getProduct())
            total += pro.getTotalAmount();

        text_subtotal.setText(Utils.paserCurrency("" + total));
        text_total.setText(Utils.paserCurrency("" + total));

    }

    public class pedidosAdapter extends ArrayAdapter<QPAY_GetInventory_Object> {

        List<QPAY_GetInventory_Object> compras;

        public pedidosAdapter(Context context, List<QPAY_GetInventory_Object> compras) {
            super(context,0, compras);
            this.compras=compras;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            QPAY_GetInventory_Object item = compras.get(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ticket_pedido , parent, false);

            LinearLayout layout_cantidad = convertView.findViewById(R.id.layout_cantidad);
            TextView text_categoria = convertView.findViewById(R.id.text_categoria);
            TextView txt_sku = convertView.findViewById(R.id.txt_sku);
            TextView txt_name = convertView.findViewById(R.id.txt_name);
            TextView txt_cantidad = convertView.findViewById(R.id.txt_cantidad);
            TextView txt_precio = convertView.findViewById(R.id.txt_precio);
            TextView txt_total = convertView.findViewById(R.id.txt_total);
            TextView text_entrega = convertView.findViewById(R.id.text_entrega);




            if(item.isFirst()) {
                text_categoria.setVisibility(View.VISIBLE);
                layout_cantidad.setVisibility(View.VISIBLE);
                text_entrega.setVisibility(View.VISIBLE);

            } else {
                text_categoria.setVisibility(View.GONE);
                layout_cantidad.setVisibility(View.GONE);
                text_entrega.setVisibility(View.GONE);
            }

            text_categoria.setText(item.getOrganizationName() != null ? item.getOrganizationName() : getNameOrg(item.getIdOrganization()));


            switch (text_categoria.getText().toString().toUpperCase()){
                case "BIMBO":
                case "BARCEL":
                case "RICOLINO":
                    text_entrega.setText("Entrega en 7 días después de la última visita");
                    break;
                case "GUNA":
                    text_entrega.setText("Entrega en 48 hrs");
                    break;
            }

            txt_sku.setText(item.getSku());
            txt_name.setText(item.getShortName());
            txt_cantidad.setText(item.getQuantity() + "");
            txt_precio.setText(Utils.paserCurrency(item.getPriceInit() + ""));
            txt_total.setText(Utils.paserCurrency(item.getTotalAmount() + ""));

            return convertView;
        }
    }

    public void print(String ticket) {

        if(Tools.isN3Terminal()) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    List<FormattedLine> lines = new ArrayList<>();
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"PEDIDO EXITOSO"));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Ticket de pedido: " + ticket));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Productos solicitados: "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));

                    for (QPAY_GetInventory_Object ped: order.getProduct()) {
                        if(ped.isFirst()) {
                            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, ped.getOrganizationName() != null ? ped.getOrganizationName() : getNameOrg(ped.getIdOrganization())));
                        }

                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, ped.getSku() + " " + ped.getShortName() ));
                        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT, ped.getQuantity() + " x " + Utils.paserCurrency(ped.getPriceInit() + "") + " = " + Utils.paserCurrency(ped.getTotalAmount() + "")));

                    }

                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.RIGHT, PrinterLineType.TEXT,"Total:   " + Utils.paserCurrency("" + total)));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Tienda: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_BIG   , Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"GRACIAS POR SU VISITA!!"));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
                    lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"ESTE NO ES UN COMPROBANTE \nFISCAL"));

                    PrinterManager printerManager = new PrinterManager(getActivity());
                    printerManager.printFormattedTicket(lines, getContext());

                }
            }, 1000);

        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public String getNameOrg(String id) {

        for(QPAY_GetOrganization_Object org : Fragment_pedidos_bimbo_1.organizations) {
            if(id.equals("" + new Formatter().format("%04d", org.getId())))
                return org.getShortName();
        }

        return "Sin nombre";
    }

}