package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.MaterialImagesAdapter;
import com.blm.qiubopay.adapters.MaterialSpecsAdapter;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.listeners.IBuyProduct;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_SeedDeviceRequest;
import com.blm.qiubopay.models.product.EquipmentCost;
import com.blm.qiubopay.models.product.QPAY_EquipmentCostResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_compra_material_1 extends HFragment {

    private static final String TAG = "compra_material_1";
    private static final String PRODUCT_TYPE = "EQUIPMENT";

    private View view;
    private MenuActivity context;

    private EditSpinner edit_paquetes;
    private RecyclerView list_paquete;
    private TextView tv_precio_comun;
    private TextView tv_precio_total;
    private ConstraintLayout layout_precio_total;
    private Button btn_confirmar;
    private LinearLayout layout_imagenes;

    private EquipmentCost equipmentSelected;
    private Double dPrecioEquipo;
    private ArrayList<String> paquetesArray;
    private QPAY_EquipmentCostResponse equipmentCostResponse;

    private RecyclerView image_recyclerView;
    private MaterialImagesAdapter imagesAdapter;


    public Fragment_compra_material_1() {
        // Required empty public constructor
    }

    public static Fragment_compra_material_1 newInstance() {
        Fragment_compra_material_1 fragment = new Fragment_compra_material_1();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_compra_material_1, container, false);

        setView(view);

        initFragment();

        return view;
    }


    @Override
    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().backFragment();
                    }
                });

        //HLocation.start(false,getContext());

        // ELEMENTOS DE PANTALLA

        list_paquete = view.findViewById(R.id.list_paquete);
        tv_precio_comun = view.findViewById(R.id.tv_precio);
        tv_precio_total = view.findViewById(R.id.tv_precio_total);
        layout_precio_total = view.findViewById(R.id.layout_precio_total);
        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        layout_imagenes = view.findViewById(R.id.layout_imagenes);

        layout_imagenes.setVisibility(View.GONE);

        image_recyclerView = view.findViewById(R.id.image_recyclerview);
        image_recyclerView.setVisibility(View.GONE);

        // LISTENERS

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirmar.setEnabled(false);
                if(userCanBuyEquipment(AppPreferences.getKinetoBalance(), dPrecioEquipo)) {
                    getContext().alert(getContext().getResources().getString(R.string.alert_message_buy_equipment).replace("**monto**",
                            Utils.paserCurrency(dPrecioEquipo.toString())), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Si";
                        }

                        @Override
                        public void onClick() {
                            //RSB. Se coloca el tipo de producto ya que el que viene de QTC no es el correcto que solicita QTC XD
                            equipmentSelected.setProductType(PRODUCT_TYPE);
                            context.setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.BUY_EQUIPMENT_ADDRESS,equipmentSelected));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_confirmar.setEnabled(true);
                                }
                            }, 4000);
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "No";
                        }

                        @Override
                        public void onClick() {
                            btn_confirmar.setEnabled(true);
                        }
                    });
                } else {
                    getContext().alert(R.string.compra_dongle_1_saldo_insuficiente);
                    btn_confirmar.setEnabled(true);
                }
            }
        });


        // CARGA DE SERVICIO
        getEquipmentCost(new IFunction() {
            @Override
            public void execute(Object[] result) {

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(result[0]);
                Log.d(TAG,"Response getEquipmentCost: " + json);
                QPAY_EquipmentCostResponse response = gson.fromJson(json, QPAY_EquipmentCostResponse.class);

                if (response.getQpay_response().equals("true")) {
                    equipmentCostResponse = response;
                    setProducts();
                } else {
                    ((MenuActivity) getContext()).validaSesion(response.getQpay_code(), response.getQpay_description());
                }

            }
        });
    }

    /**
     * Coloca los procuctos en el edit
     */
    public void setProducts(){

        edit_paquetes = view.findViewById(R.id.edit_paquetes);
        paquetesArray = new ArrayList();

        for(int i=0; i<equipmentCostResponse.getQpay_object().length; i++){
            paquetesArray.add(equipmentCostResponse.getQpay_object()[i].getName());
        }

        setDataSpinner2(edit_paquetes, paquetesArray);
    }

    public void setDataSpinner2(final EditSpinner spiner, final ArrayList<String> array){

        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
        spiner.setAdapter(adapter);
        spiner.setEditable(false);
        spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spiner.setText(paquetesArray.get(position));
                //Equipo que ha seleccionado
                equipmentSelected = equipmentCostResponse.getQpay_object()[position];
                //Precio total tras el descuento enviado
                dPrecioEquipo = equipmentSelected.getTotalPrice();
                dPrecioEquipo = (dPrecioEquipo !=null ? dPrecioEquipo : 0.0);
                tv_precio_comun.setText(Utils.paserCurrency(dPrecioEquipo.toString()) + " MXN");
                //Precio sin descuento
                Double dPrecioTotal = equipmentSelected.getCommonPrice();
                if (dPrecioTotal!=null && dPrecioTotal>0) {
                    layout_precio_total.setVisibility(View.VISIBLE);
                    tv_precio_total.setText(Utils.paserCurrency(dPrecioTotal.toString()) + " MXN");
                } else {
                    layout_precio_total.setVisibility(view.GONE);
                    tv_precio_total.setText(Utils.paserCurrency("0.0") + " MXN");
                }
                //Listado con equipo en el paquete
                List<String> descripcionPaquete = Arrays.asList(equipmentSelected.getDescription());
                list_paquete.setAdapter(new MaterialSpecsAdapter(descripcionPaquete));
                list_paquete.setHasFixedSize(true);
                list_paquete.setLayoutManager(new LinearLayoutManager(getContext()));

                //20210115 RSB. Improvements 0121. Imagenes en compra de material
                //initFlippableStack(equipmentSelected.getImages());
                //initFlipperView(equipmentSelected.getImages());
                initImageRecycler(equipmentSelected.getImages());

                btn_confirmar.setEnabled(true);
            }
        });
    }

    /**
     * Verifica si el usuario tiene saldo suficiente para comprar equipo
     * @param balance
     * @param equipmentCost
     * @return
     */
    private Boolean userCanBuyEquipment(String balance, Double equipmentCost)
    {
        Boolean isValid = true;
        try
        {
            Double userBalance = new Double(Tools.getOnlyNumbers(balance));
            if(equipmentCost > userBalance)
                isValid = false;
        }catch (Exception e)
        {
            isValid = false;
        }
        return isValid;
    }

    private void getEquipmentCost(IFunction function) {

        getContext().loading(true);

        QPAY_SeedDeviceRequest data = new QPAY_SeedDeviceRequest();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        Log.d(TAG,"Request getEquipmentCost: " + new Gson().toJson(data));

        IBuyProduct equipmentCostListener = null;
        try {
            equipmentCostListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {
                        function.execute(result);
                    }
                }
                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

        equipmentCostListener.getEquipmentCost(data);

    }

    private void initImageRecycler(String[] imageUrls) {

        if(imageUrls==null || imageUrls.length==0){

            image_recyclerView.setVisibility(View.GONE);
            layout_imagenes.setVisibility(View.GONE);

        } else {

            layout_imagenes.setVisibility(View.VISIBLE);
            image_recyclerView.setVisibility(View.VISIBLE);
            imagesAdapter = new MaterialImagesAdapter(Arrays.asList(imageUrls));
            image_recyclerView.setAdapter(imagesAdapter);
            image_recyclerView.setHasFixedSize(true);
            final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            image_recyclerView.setLayoutManager(mLayoutManager);

        }

    }

    /*private void initFlippableStack(String[] imageUrls){

        FlippableStackView flippableStackView = (FlippableStackView) view.findViewById(R.id.flippable_stack_material);
        ViewPagerIndicator viewPagerIndicator = view.findViewById(R.id.pager_indicator_material);

        if(imageUrls==null || imageUrls.length==0){

            flippableStackView.setVisibility(View.GONE);
            viewPagerIndicator.setVisibility(View.GONE);

        } else {

            layout_imagenes.setVisibility(View.VISIBLE);

            flippableStackView.initStack(3, StackPageTransformer.Orientation.HORIZONTAL);

            final CardFragmentAdapter cardFragmentAdapter = new CardFragmentAdapter(getChildFragmentManager(), createCards(imageUrls));

            flippableStackView.setAdapter(cardFragmentAdapter);

            final ViewPager pager = new ViewPager(getContext());
            pager.setAdapter(cardFragmentAdapter);

            viewPagerIndicator.initWithViewPager(pager);

            flippableStackView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) { }

                @Override
                public void onPageSelected(int i) {
                    pager.setCurrentItem(i);
                }

                @Override
                public void onPageScrollStateChanged(int i) { }
            });

            flippableStackView.setCurrentItem(0);
        }

    }

    private  ArrayList<Fragment> createCards(String[] imagesUrl) {

        ArrayList<Fragment> cards = new ArrayList();

        for (int i = 0; i < imagesUrl.length; ++i) {
            cards.add(Fragment_flippable.newInstance(imagesUrl[i]));
        }

        return cards;
    }

    //Cambio de libreria dado que el FlippableStack tiene temas con fragmentos y no refresaca imagenes
    private void initFlipperView(String[] imageUrls) {

        //https://github.com/therealshabi/AutoImageFlipper
        FlipperLayout flipperLayout = (FlipperLayout) view.findViewById(R.id.flipper_layout);

        if(imageUrls==null || imageUrls.length==0){

            flipperLayout.setVisibility(View.GONE);

        } else {

            layout_imagenes.setVisibility(View.VISIBLE);

            flipperLayout.removeAllFlipperViews();
            flipperLayout.removeAutoCycle();

            int num_of_pages = imageUrls.length;
            for (int i = 0; i < num_of_pages; i++) {
                FlipperView view = new FlipperView(context);
                try {
                    final String urlImage = imageUrls[i];
                    view.setImageUrl(urlImage,
                            new Function2<ImageView, Object, Unit>() {
                                @Override
                                public Unit invoke(ImageView imageView, Object image) {
                                    Picasso.get().load(urlImage).into(imageView);
                                    return Unit.INSTANCE;
                                }
                            });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                view.setImageScaleType(ImageView.ScaleType.FIT_XY);
                flipperLayout.addFlipperView(view);
            }
        }

    }*/

}
