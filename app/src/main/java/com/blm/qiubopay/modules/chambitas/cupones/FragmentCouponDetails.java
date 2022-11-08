package com.blm.qiubopay.modules.chambitas.cupones;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.coupons.CouponDetailsListResponse;
import com.blm.qiubopay.modules.chambitas.adapter.CouponsAdapter;
import com.blm.qiubopay.modules.chambitas.cupones.viewmodel.CouponsVM;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.DateUtils;
import com.blm.qiubopay.utils.Globals;
import com.bumptech.glide.Glide;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class FragmentCouponDetails extends HFragment implements IMenuContext {

    private final String TAG = "FragmentCouponDetails";
    private View view;
    private HActivity context;
    private CouponsAdapter adapter;
    private CouponDetailsListResponse couponsResponse;
    private TextView tvPrintCouponTitle;
    private TextView tvDescription;
    private TextView tvChambitaName;
    private TextView tvPrintOnce;
    private TextView tvValidity;
    private Button btnPrint;
    private Dialog dialog;
    private CouponsVM viewModel;
    private ImageView ivPrize;
    private boolean isExpired = false;

    public static FragmentCouponDetails newInstance(CouponDetailsListResponse couponsResponse) {
        FragmentCouponDetails fragment = new FragmentCouponDetails();
        Bundle args = new Bundle();
        args.putSerializable("couponsResponse", couponsResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new CouponsVM(context);
        init();
        setContent();
        setListeners();
        setObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        if( view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_coupon_details, container, false);
        setView(view);
        initFragment();
        return view;
    }

    private void init() {
        tvPrintCouponTitle = view.findViewById(R.id.tvPrintCouponTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvChambitaName = view.findViewById(R.id.tvChambitaName);
        tvPrintOnce = view.findViewById(R.id.tvPrintOnce);
        tvValidity = view.findViewById(R.id.tvValidity);
        btnPrint = view.findViewById(R.id.btnPrint);
        ivPrize = view.findViewById(R.id.ivPrize);
    }

    private void getData() {
        context.loading(true);
        viewModel.isValidTerminalforCoupons();
    }

    private void setContent() {
        disablePrintButton();
        couponsResponse = (CouponDetailsListResponse) getArguments().getSerializable("couponsResponse");
        if (couponsResponse == null) {
            tvPrintCouponTitle.setText(context.getResources().getString(R.string.coupons_oops));
            tvDescription.setText(context.getResources().getString(R.string.coupons_no_bimboid));
        } else {
            tvChambitaName.setText(couponsResponse.getChambitaName() +"!");
            tvPrintOnce.setText(Html.fromHtml("Únicamente puedes imprimirlo una vez así que cuídalo mucho. " +
                    "<b>" + "Verifica que tu terminal tenga papel" + "</b>"));
            tvValidity.setText("Este premio expira el " + invertDate(couponsResponse.getValidity()));
            Glide.with(context)
                    .load(couponsResponse.getChambitaURL())
                    .error(R.drawable.illustrations_chambita_248_x_200)
                    .centerCrop()
                    .into(ivPrize);
            if (!couponsResponse.getPrintedAt().equals("")) {
                showDialog(context.getResources().getString(R.string.coupons_printed_coupon_title),
                        context.getResources().getString(R.string.coupons_printed_coupon_description));
            }
            String[] separateDate = couponsResponse.getValidity().split("-");
            reviewValidity(separateDate);
        }
    }

    private String invertDate(String validity) {
        if (validity != null && !validity.isEmpty()) {
            String[] arrValidity = validity.split("-");
            if (arrValidity.length == 3) {
                return arrValidity[2] + "-" + arrValidity[1] + "-" + arrValidity[0];
            } else return "";
        } else return "";
    }

    private void setListeners() {
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isN3Terminal()) {
                    context.loading(true);
                    viewModel.setStatusCouponPrinted(couponsResponse.getIdCoupon());
                } else showDialog(context.getResources().getString(R.string.coupons_change_device),
                        context.getResources().getString(R.string.coupons_only_terminal));
            }
        });
    }

    private void setObservers() {
        viewModel._couponPrintedResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<Boolean>>() {
            @Override
            public void onChanged(BaseResponse<Boolean> stringBaseResponse) {
                ((MenuActivity) context).wasCouponDetailsOpen = true;
                ((MenuActivity) context).wasCouponDetailsOpenBadgeTest = true;
                context.closeLoading();
                if (stringBaseResponse.getQpayObject().get(0)) {
                    printTicket(new IFunction() {
                        @Override
                        public void execute(Object[] data) {}
                    });
                    getContext().backFragment();
                }
            }
        });
        viewModel._couponsIsValidTerminalResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<Boolean>>() {
            @Override
            public void onChanged(BaseResponse<Boolean> validTerminal) {
                context.closeLoading();
                if (validTerminal != null) {
                    if (validTerminal.getQpayObject() != null && !validTerminal.getQpayObject().isEmpty()) {
                        if (couponsResponse != null) {
                            if (validTerminal.getQpayObject().get(0)
                                    && couponsResponse.getPrintedAt().equals("")
                                    && !isExpired) {
                                enablePrintButton();
                            }
                        }
                    }
                }
            }
        });
    }

    private void reviewValidity(String[] date) {
        if (date != null && date.length == 3) {
            try {
                DateUtils dateUtils = new DateUtils(context);
                if (dateUtils.itIsExpired(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]))) {
                    isExpired = true;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void enablePrintButton() {
        btnPrint.setEnabled(true);
        btnPrint.setTextColor(getResources().getColor(R.color.white_two));
    }

    private void disablePrintButton() {
        btnPrint.setEnabled(false);
        btnPrint.setTextColor(getResources().getColor(R.color.colorGrayDark));
    }

    private void printTicket(final IFunction function){
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Vale por: "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,couponsResponse.getDescription()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Válido hasta: "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, couponsResponse.getValidity()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, " "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Folio: "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, couponsResponse.getInvoice()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Comercio: "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT,couponsResponse.getStoreName()));
        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        bimboId = (bimboId != null && !bimboId.isEmpty() ? bimboId : "na");
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, "Bimbo ID: "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT, bimboId));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT,getResources().getString(R.string.app_name) + " v" + Tools.getVersion()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.TEXT," "));
        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, getContext());
    }

    public void initFragment(){
        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    private void showDialog(String title, String description) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_print_warning);
        ImageView ivCouponClose = dialog.findViewById(R.id.ivCouponClose);
        Button btnCouponAccept = dialog.findViewById(R.id.btnCouponAccept);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvDescription = dialog.findViewById(R.id.tvDescription);

        tvTitle.setText(title);
        tvDescription.setText(description);

        btnCouponAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivCouponClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}