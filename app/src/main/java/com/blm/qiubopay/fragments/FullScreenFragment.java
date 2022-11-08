package com.blm.qiubopay.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.fragments.viewmodels.FullScreenVM;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_publicities;
import com.blm.qiubopay.models.carousel.FullScreenData;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.LocalRedirectUtils;
import com.bumptech.glide.Glide;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class FullScreenFragment extends HFragment implements IMenuContext {
    private static FullScreenData mFullScreenData;
    private ImageView ivPromo;
    private AppCompatButton btnLink;
    private LinearLayout llInclude;
    private ScrollView svPromo;
    private WebView wvPromo;
    private ImageView ivSharePrint;
    private ImageView ivBack;
    private static Context myContext;
    private FullScreenVM fullScreenVM;

    public static FullScreenFragment newInstance(FullScreenData fullScreenData, Context context) {
        mFullScreenData = fullScreenData;
        myContext = context;
        return new FullScreenFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fullScreenVM = new FullScreenVM(getContext());
        init();
        getData();
        setListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_full_screen, container, false), R.drawable.background_splash_header_1 );
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().initHome();
                    }
                }).showLogo();
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    void init() {
        ivPromo = getContext().findViewById(R.id.ivPromo);
        btnLink = getContext().findViewById(R.id.btnLink);
        llInclude = getContext().findViewById(R.id.llInclude);
        svPromo = getContext().findViewById(R.id.svPromo);
        wvPromo = getContext().findViewById(R.id.wvPromo);
        ivSharePrint = getContext().findViewById(R.id.ivSharePrint);
        ivBack = getContext().findViewById(R.id.ivBack);
    }

    void getData() {
        if (mFullScreenData != null) {
            // This service does not need catch response
            fullScreenVM.createCampaignViewed(mFullScreenData.getCampaignId());
            setContent();
        }
    }

    void setContent() {
        if (mFullScreenData.getLink() == null || mFullScreenData.getLink().isEmpty()) {
            btnLink.setVisibility(View.INVISIBLE);
        }
        llInclude.setVisibility(View.GONE);
        svPromo.setVisibility(View.INVISIBLE);
        wvPromo.setVisibility(View.INVISIBLE);
        btnLink.setText(mFullScreenData.getTextButton());
        Glide.with(this)
                .load(mFullScreenData.getOnDemandImage())
                .centerCrop()
                .into(ivPromo);
    }

    void setListeners() {
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFullScreenData.getLink().contains(Globals.APPLINK_URL)) {
                    selectDestination();
                } else {
                    openWeb();
                }
            }
        });
        ivSharePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QPAY_TipsAdvertising_publicities tip = new QPAY_TipsAdvertising_publicities(
                        0,
                        mFullScreenData.getTitle(),
                        mFullScreenData.getOnDemandImage(),
                        "",
                        mFullScreenData.getTextButton(),
                        mFullScreenData.getDescription(),
                        mFullScreenData.getLink(),
                        "",
                        0,
                        0);
                printTicket(tip, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                    }
                });
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().onBackPressed();
            }
        });
    }

    private void printTicket(QPAY_TipsAdvertising_publicities tip,final IFunction function){
        //N3_FLAG_COMMENT
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "PROMOCIÓN"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getTitle()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getDescription()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, ""));
        if (tip.getLink() != null && !tip.getLink().isEmpty()) {
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getLink()));
        } else {
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getImage()));
        }
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        String blmid = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();
        blmid = (blmid != null && !blmid.isEmpty() ? blmid : "na");

        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        bimboId = (bimboId != null && !bimboId.isEmpty() ? bimboId : "");

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, ""));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "BLM ID " + blmid));

        if(!bimboId.isEmpty())
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "BIMBO ID " + bimboId));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, getContext());

    }

    private void selectDestination() {
        btnLink.setVisibility(View.INVISIBLE);
        try {
            Uri appLinkData = Uri.parse(mFullScreenData.getLink());
            String lastSegment = appLinkData.getLastPathSegment();
            Logger.d("Promocion: " + lastSegment);
            LocalRedirectUtils redirectUtils = new LocalRedirectUtils(myContext);
            redirectUtils.redirectInAppFromCarousel(lastSegment);
        } catch (Exception e) {
            getContext().alert(R.string.general_error_url);
        }
    }

    private void openWeb() {
        btnLink.setVisibility(View.GONE);
        ivPromo.setVisibility(View.GONE);
        ivSharePrint.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.INVISIBLE);
        llInclude.setVisibility(View.VISIBLE);
        svPromo.setVisibility(View.VISIBLE);
        wvPromo.setVisibility(View.VISIBLE);
        wvPromo.getSettings().setDomStorageEnabled(true);
        wvPromo.getSettings().setJavaScriptEnabled(true);
        wvPromo.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {

            }
        });
        wvPromo.loadUrl(mFullScreenData.getLink());
    }

}
