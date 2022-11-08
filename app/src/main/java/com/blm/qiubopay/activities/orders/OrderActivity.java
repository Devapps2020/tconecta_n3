package com.blm.qiubopay.activities.orders;

import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_ACCEPT_ORDER;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_DECLINE_ORDER;
import static com.blm.qiubopay.utils.Globals.NotificationType.NOTIFICATION_NEW_ORDER;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.viewmodels.OrderVM;
import com.blm.qiubopay.activities.viewmodels.OrderWithPagesVM;
import com.blm.qiubopay.adapters.odrer.communicator.IStatusOrderForAdapter;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.order.AcceptOrderResponse;
import com.blm.qiubopay.models.order.DeclineOrderResponse;
import com.blm.qiubopay.models.order.GetOrderAcceptanceResponse;
import com.blm.qiubopay.models.order.OrderDetailsResponse;
import com.blm.qiubopay.models.order.OrderIds;
import com.blm.qiubopay.models.order.OrderModel;
import com.blm.qiubopay.models.order.OrderModelResponse;
import com.blm.qiubopay.models.order.OrderNotification;
import com.blm.qiubopay.models.order.OrderResponse;
import com.blm.qiubopay.models.order.ProductsAcceptModel;
import com.blm.qiubopay.models.order.ProductsModel;
import com.blm.qiubopay.modules.orders.ActivateMyOrdersFragment;
import com.blm.qiubopay.modules.orders.OrderDetailFragment;
import com.blm.qiubopay.modules.orders.OrderFragment;
import com.blm.qiubopay.modules.orders.ShippingConfirmationFragment;
import com.blm.qiubopay.modules.orders.StatusOrdersFragment;
import com.blm.qiubopay.modules.orders.communicator.IActivateMyOrders;
import com.blm.qiubopay.modules.orders.communicator.IOrder;
import com.blm.qiubopay.modules.orders.communicator.IOrderDetail;
import com.blm.qiubopay.modules.orders.communicator.IShippngConfirmation;
import com.blm.qiubopay.modules.orders.communicator.IStatusOrder;
import com.blm.qiubopay.utils.SoundUtils;

import java.util.ArrayList;
import java.util.Date;

import mx.devapps.utils.interfaces.IFunction;

public class OrderActivity extends HLocActivity implements IActivateMyOrders, IStatusOrder, IStatusOrderForAdapter, IOrderDetail, IOrder, IShippngConfirmation {

    private String TAG = "OrderActivity";
    private RelativeLayout rlHeader;
    private RelativeLayout rlBack;
    private Boolean isPendingList = true;
    private OrderFragment orderFragment;
    private StatusOrdersFragment statusOrdersFragment;
    private Boolean isFragmentAdded = false;
    private OrderVM viewModel;
    private OrderWithPagesVM viewModelWithPages;
    private Integer orderResponses = 0;
    public static ArrayList<OrderResponse> pendingOrdersList;
    private ArrayList<OrderResponse> deliveredOrdersList;
    private String orderId = "";
    private String orderType = "";
    private Boolean acceptedOrder = false;
    private Boolean rejectedOrder = false;
    private String ordersFlag = "";
    private int orderCounter = 0;
    private String pendingNextPage = "";
    private String deliveredNextPage = "";
    private String pendingLastPage = "";
    private String deliveredLasiPage = "";

    public static String CREATED = "Created";
    public static String ACCEPTED_BY_MERCHANT = "Accepted by merchant";
    public static String DECLINED = "Declined";
    public static String ACCEPTED = "Accepted";
    public static String CANCELLED = "Cancelled";
    public static String EXPIRED = "Expired";
    public static String FAILED = "Failed";
    public static String DELIVERED = "Delivered";
    public static String PENDING = "Pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        viewModel = new OrderVM(this);
        viewModelWithPages = new OrderWithPagesVM(this);
        init();
        getData();
        setListeners();
        setObservers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter newOrderFilter = new IntentFilter("tconecta.sendNewOrder");
        registerReceiver(mNewOrderReceiver, newOrderFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mNewOrderReceiver);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private BroadcastReceiver mNewOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (orderFragment == null || !orderFragment.isAdded()) {
                orderFragment = null;
                String receivedText = intent.getStringExtra("tconecta.newOrder");
                if (receivedText.equals(NOTIFICATION_NEW_ORDER) ||
                        receivedText.equals(NOTIFICATION_ACCEPT_ORDER) ||
                        receivedText.equals(NOTIFICATION_DECLINE_ORDER)) {
                    if (!receivedText.equals(NOTIFICATION_DECLINE_ORDER)) {
                        MenuActivity activity = new MenuActivity();
                        activity.startTimerForOrders();
                    }
                    ArrayList<OrderNotification> orders = AppPreferences.readNewOrders();
                    SoundUtils soundUtils = new SoundUtils();
                    soundUtils.executeSound(OrderActivity.this);
                    AppPreferences.removeOrders();
                    if (orders != null && orders.size() > 0) {
                        orderId = orders.get(0).getNewOrderId();
                        orderType = orders.get(0).getOrderType();
                    }
                    if (orderType != null && !orderType.isEmpty()) {
                        if (orderType.equals(NOTIFICATION_ACCEPT_ORDER)) acceptedOrder = true;
                        else if (orderType.equals(NOTIFICATION_DECLINE_ORDER)) rejectedOrder = true;
                    }
                    callAllOrders();
                }
            }
        }
    };

    private void init() {
        rlHeader = findViewById(R.id.rlHeader);
        rlBack = findViewById(R.id.rlBack);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString("orderId", "");
            orderType = bundle.getString("orderType", "");
        }
        if (!orderType.isEmpty()) {
            if (orderType.equals(NOTIFICATION_ACCEPT_ORDER)) acceptedOrder = true;
            else if (orderType.equals(NOTIFICATION_DECLINE_ORDER)) rejectedOrder = true;
        }
        callAllOrders();
    }

    private void setListeners() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderFragment != null && orderFragment.isAdded()) {
                    orderFragment = null;
                    callAllOrders();
                } else finish();
            }
        });
    }

    private void callAllOrders() {
        loading(true);
        pendingOrdersList = new ArrayList<>();
        viewModel.getOrders(null, null, PENDING, null);
    }

    private void setObservers() {
        viewModelWithPages._orderResponse.observe(this, new Observer<BaseResponse<OrderResponse>>() {
            @Override
            public void onChanged(BaseResponse<OrderResponse> orderResponseBaseResponse) {
                closeLoading();
                statusOrdersFragment.setServiceFree();
                if (orderResponseBaseResponse.getQpayObject() != null) {
                    if (!orderResponseBaseResponse.getQpayObject().isEmpty()) {
                        if (isPendingList) {
                            pendingOrdersList.get(0).getOrderIds().addAll(orderResponseBaseResponse.getQpayObject().get(0).getOrderIds());
                            pendingNextPage = orderResponseBaseResponse.getQpayObject().get(0).getNextPage();
                        } else {
                            deliveredOrdersList.get(0).getOrderIds().addAll(orderResponseBaseResponse.getQpayObject().get(0).getOrderIds());
                            deliveredNextPage = orderResponseBaseResponse.getQpayObject().get(0).getNextPage();
                        }
                        statusOrdersFragment.reloadList();
                    }
                }
            }
        });
        viewModel._orderFilteredResponse.observe(this, new Observer<BaseResponse<OrderResponse>>() {
            @Override
            public void onChanged(BaseResponse<OrderResponse> orderResponseBaseResponse) {
                closeLoading();
                if (orderResponseBaseResponse.getQpayObject() != null) {
                    if (!orderResponseBaseResponse.getQpayObject().isEmpty()) {
                        if (orderResponseBaseResponse.getQpayObject().get(0).getOrderIds().get(0).getStatus().equals(DELIVERED)) {
                            deliveredOrdersList = orderResponseBaseResponse.getQpayObject();
                            deliveredNextPage = orderResponseBaseResponse.getQpayObject().get(0).getNextPage();
                        } else {
                            pendingOrdersList = orderResponseBaseResponse.getQpayObject();
                            pendingNextPage = orderResponseBaseResponse.getQpayObject().get(0).getNextPage();
                        }
                    } else {
                        deliveredOrdersList = new ArrayList<>();
                        pendingOrdersList = new ArrayList<>();
                    }
                } else {
                    deliveredOrdersList = new ArrayList<>();
                    pendingOrdersList = new ArrayList<>();
                }
                fillOrdersList();
                rlHeader.setVisibility(View.VISIBLE);
            }
        });
        viewModel._orderResponse.observe(this, new Observer<BaseResponse<OrderResponse>>() {
            @Override
            public void onChanged(BaseResponse<OrderResponse> orderResponseBaseResponse) {
                closeLoading();
                if (orderResponseBaseResponse.getQpayObject() != null) {
                    if (!orderResponseBaseResponse.getQpayObject().isEmpty()) {
                        if (orderResponseBaseResponse.getQpayObject().get(0).getOrderIds().get(0).getStatus().equals(DELIVERED)) {
                            deliveredOrdersList = orderResponseBaseResponse.getQpayObject();
                            deliveredNextPage = orderResponseBaseResponse.getQpayObject().get(0).getNextPage();
                        } else {
                            pendingOrdersList = orderResponseBaseResponse.getQpayObject();
                            pendingNextPage = orderResponseBaseResponse.getQpayObject().get(0).getNextPage();
                        }
                    }
                }
                if (orderCounter++ == 0) {
                    loading(true);
                    deliveredOrdersList = new ArrayList<>();
                    viewModel.getOrders(null, null, DELIVERED, null);
                } else {
                    orderCounter = 0;
                    fillOrdersList();
                }
                rlHeader.setVisibility(View.VISIBLE);
            }
        });
        viewModel._orderDetailsResponse.observe(this, new Observer<BaseResponse<OrderDetailsResponse>>() {
            @Override
            public void onChanged(BaseResponse<OrderDetailsResponse> orderDetailsResponseBaseResponse) {
                closeLoading();
                if (orderDetailsResponseBaseResponse.getQpayObject() != null) {
                    if (!orderDetailsResponseBaseResponse.getQpayObject().isEmpty()) {
                        OrderModelResponse current = orderDetailsResponseBaseResponse.getQpayObject().get(0).getOrder();
                        String status = current.getStatus();
                        if (!status.equals(CREATED)) {
                            rlHeader.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().add(R.id.flOrder, OrderDetailFragment.newInstance(current, OrderActivity.this)).commit();
                        }
                        if (status.equals(CREATED)) {
                            getSupportFragmentManager().beginTransaction().add(R.id.flOrder, orderFragment = OrderFragment.newInstance(current.getId().toString(),
                                    getDataFromResponse(current), current.getLatitude(), current.getLongitude(), OrderActivity.this)).commit();
                        }
                        if (acceptedOrder) {
                            acceptedOrder = false;
                            rlHeader.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.flOrder, ShippingConfirmationFragment.newInstance(getContext(), current, true)).commit();
                        } else if (rejectedOrder) {
                            rejectedOrder = false;
                            rlHeader.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.flOrder, ShippingConfirmationFragment.newInstance(getContext(), current, false)).commit();
                        }
                    }
                }
            }
        });
        viewModel._acceptOrderResponse.observe(this, new Observer<BaseResponse<AcceptOrderResponse>>() {
            @Override
            public void onChanged(BaseResponse<AcceptOrderResponse> acceptOrderResponseBaseResponse) {
                closeLoading();
                if (acceptOrderResponseBaseResponse.getQpayObject() != null && !acceptOrderResponseBaseResponse.getQpayObject().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Espera la confirmación del cliente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, acceptOrderResponseBaseResponse.getQpayDescription(), Toast.LENGTH_SHORT).show();
                }
                callAllOrders();
            }
        });
        viewModel._declineOrderResponse.observe(this, new Observer<BaseResponse<DeclineOrderResponse>>() {
            @Override
            public void onChanged(BaseResponse<DeclineOrderResponse> declineOrderResponseBaseResponse) {
                closeLoading();
                if (declineOrderResponseBaseResponse.getQpayObject() != null && !declineOrderResponseBaseResponse.getQpayObject().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Rechazaste el pedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, declineOrderResponseBaseResponse.getQpayDescription(), Toast.LENGTH_SHORT).show();
                }
                callAllOrders();
            }
        });
        viewModel._getOrderAcceptanceResponse.observe(this, new Observer<BaseResponse<GetOrderAcceptanceResponse>>() {
            @Override
            public void onChanged(BaseResponse<GetOrderAcceptanceResponse> getOrderAcceptanceResponseBaseResponse) {
                closeLoading();
                if (getOrderAcceptanceResponseBaseResponse.getQpayObject() != null && !getOrderAcceptanceResponseBaseResponse.getQpayObject().isEmpty()) {
                    if (AppPreferences.getStoreLocation() == null) {
                        String strLat = getOrderAcceptanceResponseBaseResponse.getQpayObject().get(0).getB2cLat();
                        String strLon = getOrderAcceptanceResponseBaseResponse.getQpayObject().get(0).getB2cLon();
                        if (strLat != null && strLon != null && !strLat.isEmpty() && !strLon.isEmpty()) {
                            LastLocation lastLocation = new LastLocation(Double.parseDouble(strLat), Double.parseDouble(strLon), (new Date()).toString());
                            AppPreferences.setStoreLocation(lastLocation);
                        }
                    }
                    ordersFlag = getOrderAcceptanceResponseBaseResponse.getQpayObject().get(0).getAcceptB2cOrders();
                    if (ordersFlag == null || ordersFlag.isEmpty()) ordersFlag = "0";
                    if (ordersFlag.equals("1")) {
                        if (isFragmentAdded) replaceStatusOrdersFragment();
                        else addStatusOrdersFragment();
                    } else {
                        if (pendingOrdersList != null && pendingOrdersList.isEmpty() && ordersFlag.equals("0")) {
                            getSupportFragmentManager().beginTransaction().add(R.id.flOrder, ActivateMyOrdersFragment.newInstance(OrderActivity.this)).commit();
                        } else {
                            if (isFragmentAdded) replaceStatusOrdersFragment();
                            else addStatusOrdersFragment();
                        }
                    }
                }
            }
        });
        viewModel._setOrderAcceptanceResponse.observe(this, new Observer<BaseResponse<String>>() {
            @Override
            public void onChanged(BaseResponse<String> stringBaseResponse) {
                closeLoading();
                if (stringBaseResponse != null) {
                    callAllOrders();
                }
            }
        });
        viewModel._deliverOrderResponse.observe(this, new Observer<BaseResponse<AcceptOrderResponse>>() {
            @Override
            public void onChanged(BaseResponse<AcceptOrderResponse> acceptOrderResponseBaseResponse) {
                closeLoading();
                if (acceptOrderResponseBaseResponse.getQpayObject() != null && !acceptOrderResponseBaseResponse.getQpayObject().isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Entregaste el pedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, acceptOrderResponseBaseResponse.getQpayDescription(), Toast.LENGTH_SHORT).show();
                }
                callAllOrders();
            }
        });
    }

    private void fillOrdersList() {
        if (pendingOrdersList != null && pendingOrdersList.isEmpty() && ordersFlag.equals("0")) {
            finish();
        }
        if (acceptedOrder || rejectedOrder) {
            loading(true);
            viewModel.getOrderDetails(orderId);
        } else {
            viewModel.getOrderAcceptance();
        }
    }

    private ArrayList<OrderModel> getDataFromResponse(OrderModelResponse data) {
        ArrayList<OrderModel> res = new ArrayList<>();
        for (ProductsModel item : data.getProducts()) {
            OrderModel orderModel = new OrderModel(item.getName(), item.getQty(), false);
            res.add(orderModel);
        }
        return res;
    }

    private ArrayList<ProductsAcceptModel> fixPrices(ArrayList<ProductsAcceptModel> products) {
        for (ProductsAcceptModel item : products) {
            double amount = item.getAmount() / 100;
            item.setAmount(amount);
        }
        return products;
    }

    static LastLocation lastLocation;

    @Override
    public void onActivateClickListener() {
        ((HLocActivity) getContext()).obtainLocation(new IFunction() {
            @Override
            public void execute(Object[] data) {
                LastLocation lastLocation = CApplication.getLastLocation();
                AppPreferences.setStoreLocation(CApplication.getLastLocation());
                loading(true);
                ordersFlag = "1";
                viewModel.setOrderAcceptance(ordersFlag, lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
            }
        });
        callAllOrders();
    }

    @Override
    public void onSearchClickListener(String phone) {
        loading(true);
        if (isPendingList)
            viewModel.getFilteredOrders(null, null, PENDING, phone);
        else
            viewModel.getFilteredOrders(null, null, DELIVERED, phone);
    }

    @Override
    public void onStatusOrderClickListener(String enabled) {
        ((HLocActivity) getContext()).obtainLocation(new IFunction() {
            @Override
            public void execute(Object[] data) {
                LastLocation lastLocation = CApplication.getLastLocation();
                AppPreferences.setStoreLocation(CApplication.getLastLocation());
                loading(true);
                ordersFlag = enabled;
                viewModel.setOrderAcceptance(ordersFlag, lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
            }
        });
    }

    @Override
    public void onListSelected(Boolean isPendingList) {
        this.isPendingList = isPendingList;
    }

    @Override
    public void onScrollEnd(Boolean isPendingList) {
        if (isPendingList) {
            if (pendingNextPage != null) {
                int length = pendingNextPage.length();
                if (length > 0) {
                    String nextPage = pendingNextPage.substring(length - 1);
                    if (pendingLastPage.equals(nextPage)) return;
                    pendingLastPage = nextPage;
                    loading(true);
                    viewModelWithPages.getOrders(nextPage, null, PENDING, null);
                } else statusOrdersFragment.setServiceFree();

            } else statusOrdersFragment.setServiceFree();
        } else {
            if (deliveredNextPage != null) {
                int length = deliveredNextPage.length();
                if (length > 0) {
                    String nextPage = deliveredNextPage.substring(length - 1);
                    if (deliveredLasiPage.equals(nextPage)) return;
                    deliveredLasiPage = nextPage;
                    loading(true);
                    viewModelWithPages.getOrders(nextPage, null, DELIVERED, null);
                } else statusOrdersFragment.setServiceFree();
            } else statusOrdersFragment.setServiceFree();
        }
    }

    @Override
    public void onStatusOrderClickListener(OrderIds current) {
        loading(true);
        viewModel.getOrderDetails(current.getId().toString());
    }

    @Override
    public void onDeliverOrderClickListener(String orderId) {
        loading(true);
        viewModel.deliverOrder(orderId);
    }

    @Override
    public void onOrderDetailBackClickListener() {
        callAllOrders();
    }

    @Override
    public void onDeliverOrderClickListener(String orderId, ArrayList<ProductsAcceptModel> productsAcceptModels, Double totalAmount, Integer time) {
        int nullsCounter = 0;
        if (productsAcceptModels.size() == 0) Toast.makeText(this, "No hay ningún producto en la lista", Toast.LENGTH_SHORT).show();
        else {
            for (ProductsAcceptModel item : productsAcceptModels) if (item.getName() == null) nullsCounter++;
            if (nullsCounter == productsAcceptModels.size()) Toast.makeText(this, "No hay ningún producto en la lista", Toast.LENGTH_SHORT).show();
            else {
                loading(true);
                MenuActivity.stopTimerForOrders();
                viewModel.acceptOrder(orderId, fixPrices(productsAcceptModels), totalAmount, time);
            }
        }
    }

    @Override
    public void onRejectOrderClickListener(String orderId) {
        loading(true);
        MenuActivity.stopTimerForOrders();
        viewModel.declineOrder(orderId);
    }

    @Override
    public void onOrderBackClickListener() {
        callAllOrders();
    }

    @Override
    public void onGetOrderAcceptanceListener() {
        viewModel.getOrderAcceptance();
    }

    private void addStatusOrdersFragment() {
        isFragmentAdded = true;
        if (pendingOrdersList != null || deliveredOrdersList != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.flOrder, statusOrdersFragment = StatusOrdersFragment.newInstance(pendingOrdersList, deliveredOrdersList, isPendingList, ordersFlag, this)).commit();
        }
    }

    private void replaceStatusOrdersFragment() {
        if (pendingOrdersList != null || deliveredOrdersList != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flOrder, statusOrdersFragment = StatusOrdersFragment.newInstance(pendingOrdersList, deliveredOrdersList, isPendingList, ordersFlag, this)).commit();
        }
    }

    @Override
    public void onAcceptClickListener() {
        callAllOrders();
    }

}