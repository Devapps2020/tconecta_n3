package com.blm.qiubopay.printers;

import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.N3Constants;
import com.blm.qiubopay.models.operative.PayType;
import com.blm.qiubopay.models.operative.restaurant.DbDetail;
import com.blm.qiubopay.models.operative.restaurant.DbOrder;
import com.blm.qiubopay.models.operative.restaurant.QPAY_TipOrder;
//import com.blm.qiubopay.models.ticket.TicketInfo;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HActivity;

public class N3PrinterHelper {

    private HActivity context;

    public N3PrinterHelper(HActivity context){
        this.context = context;
    }

    /*public void printTicketVASFinanciero (TicketInfo info) {

        List<FormattedLine> lines = new ArrayList<FormattedLine>();

        //LOGO
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"Transferencia exitosa"));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Traspaso a saldo " + context.getString(R.string.ticket_name)));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Monto: MXN " + Utils.paserCurrency(info.getAmount())));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Fecha y hora: " + info.getTimestamp()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Transacción: " + info.getTransaction()));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Autorización: " + info.getAuthorization()));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"IMPORTANTE"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Conservar este comprobante para futuras aclaraciones"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"Cualquier problema, favor de ponerse en contacto con Soporte al Cliente. ¡¡Gracias!!"));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Tienda: " + info.getNegocio()));

        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"GRACIAS POR SU VISITA!!"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"ESTE NO ES UN COMPROBANTE \nFISCAL"));

        PrinterManager printerManager = new PrinterManager(context);
        printerManager.printFormattedTicket(lines, context);

    }*/

    public void printRestaurantTicket(ArrayList<DbDetail> list, QPAY_TipOrder order, DbOrder dbOrder){
        //Logo
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));


        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"RESUMEN DE ORDEN"));

        //Espacio en blanco
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        //Información del comercio
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, Tools.getStateName(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state().trim())));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Número de orden: ", order.getSell_number()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Monto: ", Utils.paserCurrency(order.getTotal().toString())));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Número de clientes: ", ""+dbOrder.getCommensal_number()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"", "Fecha: ", dbOrder.getDate()));

        //lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "\nDETALLES DE LA ORDEN"));

        for(int i=0; i<list.size(); i++){
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "_____________________________"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "Persona " + (i+1)));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Forma de pago: ", (list.get(i).getPayment_type() == PayType.CASH ? "EFECTIVO" : "TARJETA")));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Personas cubiertas: ", "("+list.get(i).getCommensals_no()+")"));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Subtotal: ", Utils.paserCurrency(list.get(i).getAmount().toString())));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Propina: ", Utils.paserCurrency(list.get(i).getTip_amount().toString())));
            lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, "", "Total: ", Utils.paserCurrency(list.get(i).getTotal().toString())));
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "\n_____________________________"));

        PrinterManager printerManager = new PrinterManager(context);
        printerManager.printFormattedTicket(null, lines, context);//N3_BACKUP
    }


    public void printSwapOutN3Ticket(String folio, String balance){

        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"Desvinculación exitosa"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Por favor conserve el ticket para el proceso de vinculación"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Folio: " + folio));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Saldo: " + Utils.paserCurrency(balance)));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        PrinterManager printerManager = new PrinterManager(context);
        printerManager.printFormattedTicket(lines, context);

    }

    public void printSwapInN3Ticket(String balance){

        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,"Vinculación exitosa"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,"_________________________________"));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_NORMAL, N3Constants.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT,"Saldo: " + Utils.paserCurrency(balance)));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(N3Constants.FONT_SIZE_BIG   , N3Constants.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        PrinterManager printerManager = new PrinterManager(context);
        printerManager.printFormattedTicket(lines, context);

    }

}
