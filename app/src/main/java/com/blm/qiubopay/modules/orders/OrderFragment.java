package com.blm.qiubopay.modules.orders;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.adapters.odrer.OrderAdapter;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.order.OrderModel;
import com.blm.qiubopay.models.order.ProductsAcceptModel;
import com.blm.qiubopay.modules.orders.communicator.IOrder;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;

public class OrderFragment extends HFragment implements IMenuContext {

    private static String mOrderId;
    private static ArrayList<OrderModel> mOrderModelList;
    private static Context myContext;
    private OrderAdapter adapter;
    private TextView tvDistance;
    private RecyclerView rvOrder;
    private EditText etDeliverTime;
    private Spinner spnTimeUnit;
    private TextView tvTotalAmount;
    private TextView tvFormError;
    private TextView tvDeliverTimeError;
    private Button btnDeliverOrder;
    private Button btnRejectOrder;
    private IOrder communicator;
    private LastLocation storeLocation;
    private static String mLatitude;
    private static String mLongitude;

    public static OrderFragment newInstance(String strOrderId, ArrayList<OrderModel> orderModelList, String latitude, String longitude, Context context) {
        mOrderId = strOrderId;
        mOrderModelList = orderModelList;
        mLatitude = latitude;
        mLongitude = longitude;
        myContext = context;
        return new OrderFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicator = (IOrder) myContext;
        init();
        getData();
        setContent();
        setListeners();
        setupSpinner();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_order, container, false), R.drawable.background_splash_header_1 );
    }

    private void init() {
        tvDistance = getContext().findViewById(R.id.tvDistance);
        rvOrder = getContext().findViewById(R.id.rvOrder);
        etDeliverTime = getContext().findViewById(R.id.etDeliverTime);
        spnTimeUnit = getContext().findViewById(R.id.spnTimeUnit);
        tvTotalAmount = getContext().findViewById(R.id.tvTotalAmount);
        tvFormError = getContext().findViewById(R.id.tvFormError);
        tvDeliverTimeError = getContext().findViewById(R.id.tvDeliverTimeError);
        btnDeliverOrder = getContext().findViewById(R.id.btnDeliverOrder);
        btnRejectOrder = getContext().findViewById(R.id.btnRejectOrder);
    }

    void getData() {
        tvFormError.setVisibility(View.GONE);
        tvDeliverTimeError.setVisibility(View.GONE);
        etDeliverTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvDeliverTimeError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvOrder.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvOrder.setLayoutManager(linearLayoutManager);
        adapter = new OrderAdapter(mOrderModelList, tvFormError, tvTotalAmount);
        rvOrder.setItemViewCacheSize(50);
        rvOrder.setAdapter(adapter);
    }

    void setContent() {
        storeLocation = AppPreferences.getStoreLocation();
        if (storeLocation != null) {
            LatLng llStoreLocation = new LatLng(storeLocation.getLatitude(), storeLocation.getLongitude());
            LatLng clientLocation = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
            tvDistance.setText(getDistance(clientLocation, llStoreLocation) + " ");
        } else {
            communicator.onGetOrderAcceptanceListener();
        }
    }

    private void setListeners() {
        btnDeliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateOrderForm()) {
                    String strTime = etDeliverTime.getText().toString();
                    Integer time = Integer.parseInt(strTime);
                    String strTotalAmount = tvTotalAmount.getText().toString().replace(",", "");
                    Double totalAmount = Double.parseDouble(strTotalAmount);
                    if (spnTimeUnit.getSelectedItemPosition() == 1) time *= 60;
                    communicator.onDeliverOrderClickListener(mOrderId, productsList(), totalAmount, time);
                }
            }
        });
        btnRejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.onRejectOrderClickListener(mOrderId);
            }
        });
    }

    private Boolean validateOrderForm() {
        Boolean timeResponse = true;
        Boolean orderListResponse = true;
        int time = 0;
        if (!etDeliverTime.getText().toString().isEmpty())
            time = Integer.parseInt(etDeliverTime.getText().toString());
        if (etDeliverTime.getText().toString().isEmpty() || time < 5) {
            tvDeliverTimeError.setVisibility(View.VISIBLE);
            timeResponse =  false;
        }
        if (!validateOrderList()) {
            tvFormError.setVisibility(View.VISIBLE);
            orderListResponse =  false;
        }
        return timeResponse & orderListResponse;
    }

    private Boolean validateOrderList() {
        ArrayList<OrderModel> orderList = adapter.getDataList();
        for (OrderModel item : orderList) {
            if (!item.getBoxChecked()) {
                if (!boxUncheckedValidations(item))
                    return false;
            } else {
                if (!boxCheckedValidations(item))
                    return false;
            }
        }
        return true;
    }

    private Boolean boxUncheckedValidations(OrderModel item) {
        if (item.getProductPrice() == null || item.getProductPrice().isEmpty()) return false;
        else {
            int price = Integer.parseInt(item.getProductPrice());
            if (price <= 0) return false;
        }
        if (item.getProductQuantity() <= 0) return false;
        return true;
    }

    private Boolean boxCheckedValidations(OrderModel item) {
        if (item.getSimilarProductName() != null && !item.getSimilarProductName().isEmpty()) {
            if (item.getProductPrice() == null || item.getProductPrice().isEmpty()) return false;
            if (item.getProductQuantity() <= 0) return false;
            if (item.getProductPrice() != null && !item.getProductPrice().isEmpty() && item.getProductPrice().equals("00"))
                return false;
        } else {
            if (item.getProductPrice() != null && !item.getProductPrice().isEmpty() && !item.getProductPrice().equals("00"))
                return false;
        }
        return true;
    }

    private ArrayList<ProductsAcceptModel> productsList() {
        ArrayList<ProductsAcceptModel> res = new ArrayList<>();
        ArrayList<OrderModel> orderList = adapter.getDataList();
        for (OrderModel item : orderList) {
            ProductsAcceptModel productsAcceptModel;
            if (!item.getBoxChecked()) {
                Double amount = Double.valueOf(item.getProductPrice());
                productsAcceptModel = new ProductsAcceptModel(item.getProductName(), item.getProductQuantity(), amount);
                res.add(productsAcceptModel);
            } else {
                if (item.getProductPrice() != null && !item.getProductPrice().isEmpty()) {
                    Double amount = Double.valueOf(item.getProductPrice());
                    productsAcceptModel = new ProductsAcceptModel(item.getSimilarProductName(), item.getProductQuantity(), amount);
                    res.add(productsAcceptModel);
                }
            }
        }
        return res;
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.time_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTimeUnit.setAdapter(adapter);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    @Override
    public void initFragment() {

    }

    @Override
    public boolean onBackPressed() {
        communicator.onOrderBackClickListener();
        return true;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private String getDistance(LatLng point1, LatLng point2) {
        Double R = 6371.0710; // use it for obtain kilometers
        Double rlat1 = point1.latitude * (Math.PI/180); // Convert degrees to radians
        Double rlat2 = point2.latitude * (Math.PI/180); // Convert degrees to radians
        Double difflat = rlat2-rlat1; // Radian difference (latitudes)
        Double difflon = (point2.longitude-point1.longitude) * (Math.PI/180); // Radian difference (longitudes)
        Double d = 2 * R * Math.asin(Math.sqrt(Math.sin(difflat/2)*Math.sin(difflat/2)+Math.cos(rlat1)*Math.cos(rlat2)*Math.sin(difflon/2)*Math.sin(difflon/2)));
        return df.format(d * 1000); // Convert to meters and format with 2 decimals
    }

}
