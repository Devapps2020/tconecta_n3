package com.blm.qiubopay.modules.fincomun.codi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.QrCodi;
import com.blm.qiubopay.models.bimbo.QrDTO;
import com.blm.qiubopay.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mx.com.fincomun.origilib.Model.Banxico.Objects.ActorOperacion;
import mx.com.fincomun.origilib.Model.Banxico.Objects.BanxicoDesifradoMC;
import mx.com.fincomun.origilib.Model.Banxico.Objects.InfoMensajeCobro;
import mx.com.fincomun.origilib.Objects.Transferencias.DHDatosTransferencia;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_codi_detalle_1 extends HFragment implements IMenuContext {



    public static DHDatosTransferencia transferencia;
    public static QrCodi codigoQR;

    public static Fragment_codi_detalle_1 newInstance(Object... data) {
       Fragment_codi_detalle_1 fragment = new Fragment_codi_detalle_1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_detalle_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        TextView text_folio = getView().findViewById(R.id.text_folio);
        TextView text_rastreo = getView().findViewById(R.id.text_rastreo);
        TextView text_estatus = getView().findViewById(R.id.text_estatus);
        TextView text_status_descripcion = getView().findViewById(R.id.text_status_descripcion);
        TextView text_ordenante = getView().findViewById(R.id.text_ordenante);
        TextView text_beneficiario = getView().findViewById(R.id.text_beneficiario);
        TextView text_monto = getView().findViewById(R.id.text_monto);
        TextView text_fecha = getView().findViewById(R.id.text_fecha);

        text_folio.setText(transferencia.getFolioCodi());
        text_rastreo.setText(transferencia.getClaveDeRastreo());
        text_estatus.setText(transferencia.getEstatus());
        text_status_descripcion.setText(transferencia.getDescripcionDelEstatus());
        text_ordenante.setText(transferencia.getNombreDelOrdenante());
        text_beneficiario.setText(transferencia.getNombreDelBeneficiario());
        text_monto.setText(Utils.paserCurrency(transferencia.getMonto() + ""));
        text_fecha.setText(transferencia.getFechaOperacion().toUpperCase());

        Button bnt_status = getView().findViewById(R.id.bnt_status);
        bnt_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().loading(true);
                getContextMenu().gethCoDi().consultaMensajeCobro(transferencia, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        BanxicoDesifradoMC info = (BanxicoDesifradoMC)data[0];
                        ActorOperacion actor = (ActorOperacion)data[1];

                        getContext().loading(false);

                        Logger.d(new Gson().toJson(info));

                        if(info.getInfoMensajeCobros().isEmpty())
                            getContext().alert("CoDi", "No hay detalle de cobro\npara este registro.");
                        else {

                            InfoMensajeCobro cobro = info.getInfoMensajeCobros().get(0);

                            if(actor == ActorOperacion.BENEFICIARIO) {
                                for (InfoMensajeCobro co: info.getInfoMensajeCobros())
                                    if(co.getId().equals(transferencia.getFolioCodi())) {
                                        cobro = co;
                                        break;
                                    }
                            }

                            String text = "";
                            String message = "";

                            switch (cobro.getE()) {
                                case 0:
                                    text = "Se ha aceptado tu pago a\n";
                                    message = "Se ha aceptado tu pago";
                                    break;
                                case 1:
                                    text = "Se acreditó un pago a\n";
                                    message = "Se acreditó un pago";
                                    break;
                                case 2:
                                    text = "Se ha rechazado tu pago a\n";
                                    message = "Se ha rechazado tu pago";
                                    break;
                                case 3:
                                    text = "Se ha devuelto un pago a\n";
                                    message = "Se ha devuelto un pago";
                                    break;
                                case 4:
                                    text = "Se ha pospuesto tu pago a\n";
                                    message = "Se ha pospuesto tu pago";
                                    break;
                                default:
                                    text = "Operación realizada a\n";
                                    message = "Operación realizada";
                                    break;
                            }

                            Date currentDate = new Date(cobro.getHp());
                            String stringDate = new SimpleDateFormat("MMM dd yyyy HH:mm").format(currentDate).toUpperCase();

                            text += cobro.getV().getNb()  + "\npor " + Utils.paserCurrency("" + transferencia.getMonto()) + "\n\n" + stringDate + "\n" + cobro.getC().getNb() ;


                            if("QR".equals(transferencia.getTipoAviso())) {

                                try {

                                    Map<String, Object> where = new HashMap<>();
                                    where.put("folio", codigoQR.getIc().getIDC());

                                    HDatabase db = new HDatabase(getContext());
                                    QrDTO dto = db.queryFirst(QrDTO.class, where);
                                    codigoQR.setEstatus(message);

                                    codigoQR.setFechaOperacion(stringDate);
                                    codigoQR.setOrdenante(cobro.getC().getNb());

                                    dto.setQrJson(new Gson().toJson(codigoQR));

                                    db.createOrUpdate(dto);

                                    transferencia.setNombreDelOrdenante(cobro.getC().getNb());

                                    initFragment();

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }




                        }

                    }
                });

            }
        });

        if("QR".equals(transferencia.getTipoAviso())) {

            LinearLayout layout_referencia = getView().findViewById(R.id.layout_referencia);
            TextView text_referencia = getView().findViewById(R.id.text_referencia);
            LinearLayout layout_fecha_request = getView().findViewById(R.id.layout_fecha_request);
            TextView text_fecha_request = getView().findViewById(R.id.text_fecha_request);
            TextView text_label_status_descripcion = getView().findViewById(R.id.text_label_status_descripcion);

            layout_referencia.setVisibility(View.VISIBLE);
            layout_fecha_request.setVisibility(View.VISIBLE);

            text_referencia.setText(codigoQR.getReferencia());
            text_fecha_request.setText(transferencia.getFechaOperacion().toUpperCase());
            text_label_status_descripcion.setText("Concepto:");
            text_ordenante.setText(codigoQR.getOrdenante());
            text_fecha.setText(codigoQR.getFechaOperacion().toUpperCase());

        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

