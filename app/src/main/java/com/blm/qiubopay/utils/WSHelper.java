package com.blm.qiubopay.utils;

import android.content.Context;
import android.util.Log;

import com.blm.qiubopay.connection.ConnectionManager;
import com.blm.qiubopay.connection.GenericConnection;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.listeners.*;
import com.blm.qiubopay.listeners.connectionreport.IConnectionReport;
import com.blm.qiubopay.listeners.fiado.IClient;
import com.blm.qiubopay.listeners.fiado.IFiado;
import com.blm.qiubopay.listeners.fiado.IFiar;
import com.blm.qiubopay.listeners.fiado.IListClients;
import com.blm.qiubopay.listeners.fiado.IListDebts;
import com.blm.qiubopay.listeners.fiado.INewClient;
import com.blm.qiubopay.listeners.fiado.IPagoParcial;
import com.blm.qiubopay.listeners.fiado.IRecibo;
import com.blm.qiubopay.listeners.fiado.IRecordatorio;
import com.blm.qiubopay.listeners.fiado.IUpdateClient;
import com.blm.qiubopay.models.QPAY_AccountDeposit;
import com.blm.qiubopay.models.QPAY_ActivationMerchant;
import com.blm.qiubopay.models.QPAY_Balance;
import com.blm.qiubopay.models.QPAY_CashCollection;
import com.blm.qiubopay.models.QPAY_Category1;
import com.blm.qiubopay.models.QPAY_Category2;
import com.blm.qiubopay.models.QPAY_CommissionReport;
import com.blm.qiubopay.models.QPAY_DeviceUpdate;
import com.blm.qiubopay.models.QPAY_FinancialInformation;
import com.blm.qiubopay.models.QPAY_LinkCode;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUserStatus;
import com.blm.qiubopay.models.QPAY_Loans;
import com.blm.qiubopay.models.QPAY_Login;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_NewUserResponse;
import com.blm.qiubopay.models.QPAY_OLCCreditLine;
import com.blm.qiubopay.models.QPAY_OLCQuery;
import com.blm.qiubopay.models.QPAY_Privileges;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.QPAY_RegisterAll;
import com.blm.qiubopay.models.QPAY_Request_reporte_promos;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_SeedDeviceRequest;
import com.blm.qiubopay.models.QPAY_Sigature;
import com.blm.qiubopay.models.QPAY_StartTxn;
import com.blm.qiubopay.models.QPAY_TodayTotalAmount;
import com.blm.qiubopay.models.QPAY_TokenValidateInformation;
import com.blm.qiubopay.models.QPAY_TransactionCard;
import com.blm.qiubopay.models.ahorros.QPAY_CreateUserSaving;
import com.blm.qiubopay.models.apuestas.QPAY_GetBetDetailsPetition;
import com.blm.qiubopay.models.apuestas.QPAY_GetUrlPetition;
import com.blm.qiubopay.models.balance.QPAY_BalanceInquiryPetition;
import com.blm.qiubopay.models.bank.QPAY_BankPetition;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.CatalogoInfoDTO;
import com.blm.qiubopay.models.bimbo.GetOrdersRequest;
import com.blm.qiubopay.models.bimbo.GetPointRequest;
import com.blm.qiubopay.models.bimbo.GetPromotionsRequest;
import com.blm.qiubopay.models.bimbo.GetReedemptionPointsRequest;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.bimbo.PesitoCatalogRequest;
import com.blm.qiubopay.models.bimbo.PesitoCreditRequest;
import com.blm.qiubopay.models.bimbo.PromotionRequest;
import com.blm.qiubopay.models.bimbo.QualifySellerRequest;
import com.blm.qiubopay.models.bimbo.ReedemPointsRequest;
import com.blm.qiubopay.models.bimbo.SavePromotionRequest;
import com.blm.qiubopay.models.bimbo.SellerUserRequest;
import com.blm.qiubopay.models.bimbo.SoporteDTO;
import com.blm.qiubopay.models.bimbo.TokenDTO;
import com.blm.qiubopay.models.bimbo.UpdateOrderRequest;
import com.blm.qiubopay.models.bimbo.UpdatePromotionRequest;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaign;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignNew;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_ActiveCampaignProgress;
import com.blm.qiubopay.models.chambitas.campañas.QPAY_CampaignDone;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeOrder;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengePhoto;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQR;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeQuestion;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeTypes;
import com.blm.qiubopay.models.chambitas.retos.QPAY_ChallengeVideo;
import com.blm.qiubopay.models.corte_caja.QPAY_BoxCutPetition;
import com.blm.qiubopay.models.fiado.QPAY_Cliente_Request;
import com.blm.qiubopay.models.fiado.QPAY_Detail_Cliente_Request;
import com.blm.qiubopay.models.fiado.QPAY_Fiado_Request;
import com.blm.qiubopay.models.fiado.QPAY_Fiar_Request;
import com.blm.qiubopay.models.fiado.QPAY_List_Clientes_Request;
import com.blm.qiubopay.models.fiado.QPAY_List_Debts_Request;
import com.blm.qiubopay.models.fiado.QPAY_Pago_Parcial_Request;
import com.blm.qiubopay.models.fiado.QPAY_Recibo_Request;
import com.blm.qiubopay.models.fiado.QPAY_Recordatorio_Request;
import com.blm.qiubopay.models.fiado.QPAY_Update_Cliente_Request;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsPetition;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasPetition;
import com.blm.qiubopay.models.migration_kineto.QPAY_ConfirmUser;
import com.blm.qiubopay.models.migration_kineto.QPAY_UserKineto;
import com.blm.qiubopay.models.migration_sp530.QPAY_ConfirmEmail;
import com.blm.qiubopay.models.migration_sp530.QPAY_StatusSp530;
import com.blm.qiubopay.models.mitec.QPAY_MitErrorFinancialTxrRequest;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketDetail;
import com.blm.qiubopay.models.nubity.QPAY_CheckTicketsClient;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailer;
import com.blm.qiubopay.models.operative.restaurant.QPAY_TipOrder;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategories;
import com.blm.qiubopay.models.pedidos.QPAY_GetInventory;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrder;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecord;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnline;
import com.blm.qiubopay.models.proceedings.QPAY_UserDataRecord;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcIdentification;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcPersonalData;
import com.blm.qiubopay.models.product.QPAY_BuyEquipmentRequest;
import com.blm.qiubopay.models.questions.QPAY_CampaignAnswers;
import com.blm.qiubopay.models.questions.QPAY_CampaignRequest;
import com.blm.qiubopay.models.questions.QPAY_Channels;
import com.blm.qiubopay.models.recarga.QPAY_TaeProductRequest;
import com.blm.qiubopay.models.reportes.FinancialReportRequest;
import com.blm.qiubopay.models.remesas.TC_PayRemittancePetition;
import com.blm.qiubopay.models.remesas.TC_QueryRemittancePetition;
import com.blm.qiubopay.models.restore_password.QPAY_RestorePassword;
import com.blm.qiubopay.models.rolls.QPAY_BuyRollPetition;
import com.blm.qiubopay.models.services.QPAY_ServicePayment;
import com.blm.qiubopay.models.soporte.QPAY_ErrorReport;
import com.blm.qiubopay.models.swap.QPAY_LinkT1000Request;
import com.blm.qiubopay.models.swap.QPAY_SwapN3LoginRequest;
import com.blm.qiubopay.models.tae.QPAY_TaeSale;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;

public class WSHelper extends GenericConnection
        implements IRegisterUser,
        IBalance,
        ILogin,
        IRegisterUserInformation,
        IRegisterCommerceInformation,
        IVisa,
        IRegisterFinancialInformation,
        IRegisterAll,
        ITaeSale,
        ISignatureUpload,
        IGetLastFinancialTransactions,
        iServicePayment,
        ICashCollection,
        IAccountDeposit,
        IGetLastTaeTransactions,
        IGetLastServicesTransactions,
        ISendActivationCodeToPhone,
        IValidateMerchantActivationCode,
        ISendRestorePassword,
        IDishQuery,
        IGetFinancialBalance,
        IGetVasBalance,
        IMegacableQuery,
        IDongleCost,
        IDonglePurchase,
        IUpdateUserInformation,
        ILoansQuery,
        ILoans,
        ISp530Status,
        ISp530ValidateFolio,
        ISp530ConfirmEmail,
        IGetOLCQuery,
        IGetOLCCreditLine,
        IGetQuerySuppliers,
        IScreenTxn,
        IStartTxn,
        IGetTodayTransactionsComplete,
        ISP530ConfirmMigration,
        ITokenValidateInformation,
        IUpdateDeviceData,
        IMigrateKineto,
        IMigrateKinetoConfirm,
        IDebitAndCreditTxrs,
        IGetQiuboPaymentList,
        IGetCommonsError,
        ISaveErrorReport,
        IGetBoxCutTxr,
        IMultiUserListener,
        IGetBankFromClabe,
        IFinancialVas,
        IFCReport,
        ICatalogInfo,
        IFCToken,
        IOrder,
        IGetSellerUser,
        IQualifySeller,
        IPesitoCatalog,
        IPesitoCredito,
        IPromotions,
        ISavePromotions,
        IGetProducts,
        IGetPoint,
        IGetAwards,
        IReedemPoints,
        IGetReedemptionPoints,
        IPromotionReport,
        IGetOrders,
        IGetPromotios,
        IUpdateOrder,
        IUpdatePromotion,
        IGetDynamicQuestions,
        ICreateCampaignAnswers,
        IGetTipsAdvertising,
        IViewTipsAdvertising,
        IGetChannels,
        IGetUserDataRecord,
        IGetAllDataRecord,
        ICreateUserFcIdentification,
        ICreateUserPersonalData,
        IGetDataRecordOnline,
        ICreateRegister,
        IGetSalesByRetailerId,
        IGetCheckTicketClient,
        IGetCheckTicketDetail,
        IGetCommissionReport,
        IGetTodayTotalAmount,
        ICreateUserSaving,
        IUpdateUserSaving,
        IGetUserSaving,
        IGetCampaignActive,
        IMakeChallengePhoto,
        IMakeChallengeQR,
        IMakeChallengeVideo,
        IMakeChallengeQuestion,
        IChallengeDoneOrder,
        IGetChallengeTypes,
        IGetCampaignDone,
        IGetActiveCampaignNew,
        IGetCampaignProgress,
        IDeleteUserSaving,
        IGetRollProducts,
        ISwapT1000,
        ISwapN3,
        IMultiDevice,
        IRollPuchase,
        IBuyProduct,
        IReports,
        IClient,
        IFiar,
        IListClients,
        INewClient,
        IPagoParcial,
        IRecordatorio,
        IRecibo,
        IUpdateClient,
        IFiado,
        IListDebts,
        IBalanceInquiry,
        IApuestasDeportivas,
        IRestaurantOperative,
        IGetOrganization,
        IGetCategories,
        IGetInventory,
        IGetOrder,
        IGetTransactionId,
        IRemesas,
        IMitDebugTxrListener,
        IConnectionReport
{
        	
        //ISale,
    private Context context;

    public WSHelper(IGenericConnectionDelegate genericConnectionDelegate,
                    Context context) throws Exception {
        super(genericConnectionDelegate);
        this.context = context;

    }

    public void buildConnection(String url, boolean userRegistration, boolean timeOut, ConnectionManager.RequestTypeEnum requestType, Object object){

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            connectionManager.setUserRegistration(userRegistration);
            connectionManager.setReqTimeOut(timeOut);
            connectionManager.setRequestType(requestType);
            connectionManager.setBodyContent(object);
            connectionManager.execute(url, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    public void buildConnectionOut(String url, boolean userRegistration, boolean timeOut, ConnectionManager.RequestTypeEnum requestType, Object object){

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            connectionManager.setUserRegistration(userRegistration);
            connectionManager.setReqTimeOut(timeOut);
            connectionManager.setRequestType(requestType);
            connectionManager.setBodyContent(object);
            connectionManager.execute(url, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void registerUser(QPAY_NewUser newUser) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QIUBOPAY_REGISTER_NEW_USER;
            //connectionManager.setReqTimeOut(true);
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(newUser);
            connectionManager.execute(strUrl, QPAY_NewUserResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void getBalance(QPAY_Balance object){

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_BALANCE;
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());//QPAY_BalanceResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    /*@Override
    public void doSale(TaeSale information) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.getUrlToSaleTAE();
            connectionManager.setUserRegistration(false);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(information);
            connectionManager.execute(strUrl, SaleResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }*/

    @Override
    public void login(QPAY_Login login) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {

            /*Gson gson = new GsonBuilder().create();
            String json = gson.toJson(login);
            Log.d("ConectarDispositivo","Sending " + json + " to " + Globals.URL_QIUBOPAY_USER_LOGIN);*/

            //if(PingStatusHelper.getStatusB(Globals.URL_TERMS_AND_CONDITIONS)) {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QIUBOPAY_USER_LOGIN;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(login);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());//QPAY_UserProfile.class.getCanonicalName());
            /*}else
            {
                this.onConnectionFailed(null);
            }*/
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void registerUserInformation(QPAY_Category1 category1) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPLOAD_CATEGORY_1;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(category1);
            connectionManager.execute(strUrl, QPAY_BaseResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void registerCommerceInformation(QPAY_Category2 category2) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPLOAD_CATEGORY_2;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(category2);
            connectionManager.execute(strUrl, QPAY_BaseResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTransact(QPAY_VisaEmvRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_VISA_SALE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());//QPAY_VisaResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void registerFinancialInformation(QPAY_FinancialInformation object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPLOAD_FINANCIAL_INFORMATION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void registerAllInformation(QPAY_RegisterAll object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_REGISTER_ALL;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doTaeSale(QPAY_TaeSale object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_TAE_SALE;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTaeProducts(QPAY_TaeProductRequest object) {
        buildConnection(Globals.URL_TAE_PRODUCTS,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void uploadSignature(QPAY_Sigature object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SIGNATURE_UPLOAD;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, QPAY_BaseResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getLastFinancialTransactions(QPAY_VisaEmvRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_FINANCIAL_TXN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());//QPAY_VisaResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doServicePayment(QPAY_ServicePayment object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SERVICES_PAYMENT;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());//QPAY_ServicePaymentResponse.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doQueryPayment(QPAY_ServicePayment object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QUERY_SERVICES_PAYMENT;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doCashCollection(QPAY_CashCollection object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CASH_COLLECTION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doAccountDeposit(QPAY_AccountDeposit object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_ACCOUNT_DEPOSIT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getLastServicesTransactions(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_SERVICES_TXN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doPurchase(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_DONGLE_PURCHASE;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getLastTaeTransactions(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_TAE_TXN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doSendCode(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SEND_ACTIVATION_CODE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doValidateCode(QPAY_ActivationMerchant object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SEND_VALIDATE_ACTIVATION_CODE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void restorePassword(QPAY_RestorePassword object, Boolean isStep2) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = "";
            if(!isStep2)
                strUrl = Globals.URL_RESTORE_PASSWORD_1;
            else
                strUrl = Globals.URL_RESTORE_PASSWORD_2;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    /*@Override
    public void restorePasswordStep2(QPAY_RestorePassword object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_RESTORE_PASSWORD_2;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }*/


    @Override
    public void doDishQuery(QPAY_ServicePayment object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QUERY_DISH;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doMegacableQuery(QPAY_ServicePayment object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QUERY_MEGACABLE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doGetDongleCost() {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_DONGLE_COST;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(null);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getBalanceF(QPAY_VisaEmvRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FINANCIAL_BALANCE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getBalanceV(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_VAS_BALANCE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void updateUserInformation(QPAY_NewUser newUser) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPDATE_INITIAL_REGISTER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(newUser);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void doLoansQuery(QPAY_Loans object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LOAN_QUERY;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void doLoansService(QPAY_Loans object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LOAN_DISPOSITION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void doCheckStatusSp530(QPAY_StatusSp530 object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CHECK_STATUS_SP530;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void doValidateFolioSp530(QPAY_StatusSp530 object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_VALIDATE_FOLIO_SP530;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void doConfirmEmail(QPAY_ConfirmEmail object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CONFIRM_EMAIL_SP530;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(),e.getCause().getMessage());
        }
    }

    @Override
    public void olcQuery(QPAY_OLCQuery object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_OLC_QUERY;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void olcCreditLine(QPAY_OLCCreditLine object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_OLC_CREDIT_LINE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void querySuppliers(QPAY_TaeSale object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = false;

    }

    @Override
    public void startTxn(QPAY_StartTxn object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_START_TXN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void screenTxn(String object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SCREEN_TXN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBody(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayTransactionsComplete(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_TRANS_PAGOS_QIUBO;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void doConfirmMigration(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CONFIRM_MIGRATION_SP530;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doValidateTokenInformation(QPAY_TokenValidateInformation object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_VALIDATE_TOKEN_INFORMATION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }


    @Override
    public void doUpdateDeviceData(QPAY_DeviceUpdate object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {

            /*Gson gson = new GsonBuilder().create();
            String json = gson.toJson(object);
            Log.d("ConectarDispositivo","Sending " + json + " to " + Globals.URL_UPDATE_DEVICE_DATA);*/

            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPDATE_DEVICE_DATA;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }


    @Override
    public void doMigrateKinetoUser(QPAY_UserKineto object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {

            /*Gson gson = new GsonBuilder().create();
            String json = gson.toJson(object);
            Log.d("KinetoMigration","Sending " + json + " to " + Globals.URL_MIGRATE_KINETO);*/

            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MIGRATE_KINETO;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doMigrateKinetoUserConfirm(QPAY_ConfirmUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {

            /*Gson gson = new GsonBuilder().create();
            String json = gson.toJson(object);
            Log.d("KinetoMigration","Sending " + json + " to " + Globals.URL_MIGRATE_KINETO_CONFIRM);*/

            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MIGRATE_KINETO_CONFIRM;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doGetTransactions(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_DEBITS_AND_CREDITS_TXN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doGetList(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QIUBO_PAYMENT_LIST;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doGetErrorList(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QIUBO_GET_COMMONS_ERROR_LIST;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void saveErrorReport(QPAY_ErrorReport object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QIUBO_SAVE_MERCHANT_ERROR;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTrx(QPAY_BoxCutPetition object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_TXR;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getLinkCode(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GENERATE_LINK_CODE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getLinkedUsers(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_LINKED_USERS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getLinkedUsers(QPAY_LinkedUserStatus object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_LINKED_USERS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void validateLinkCode(QPAY_LinkCode object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_VALIDATE_LINK_CODE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void confirmLinkUser(QPAY_LinkCode object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CONFIRM_LINK_USER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void linkUser(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LINK_USER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void unlinkUser(QPAY_Seed object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UNLINK_USER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void unlinkUserByAdmin(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UNLINK_USER_BY_ADMIN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getUserPrivileges(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_USER_PRIVILEGES;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void createUserPrivileges(QPAY_Privileges object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_USER_PRIVILEGES;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void createUserDevice(QPAY_Login object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_USER_DEVICE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayTaeSalesByUser(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_TAE_TXN_MULTI;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayServicePaymentsByUser(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_SERVICES_TXN_MULTI;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayTransactionsCompletedByUser(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_TRANS_PAGOS_QIUBO_MULTI;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayTransactions(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_FINANCIAL_TXN_MULTI;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayChargesAndPaysByUser(QPAY_LinkedUser object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_LAST_CHARGES_TXN_MULTI;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }


    @Override
    public void getBank(QPAY_BankPetition object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_BANK_FROM_CLABE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getMinimumAmounts(QPAY_FinancialVasGetAmountsPetition object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_BUY_FINANCIAL_VAS_MINIMUM_AMOUNTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void checkIfFinancialVasIsAvailable(QPAY_FinancialVasPetition object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FINANCIAL_VAS_BALANCE_APPROVAL;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void processFinancialVas(QPAY_FinancialVasPetition object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FINANCIAL_VAS_BALANCE_REQUEST;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void sendReport(SoporteDTO object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SEND_REPORT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getCatalogoInfo(CatalogoInfoDTO object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CATALOG_INFO;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getToken(TokenDTO object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_TOKEN;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void sendOrder(PedidoRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SAVE_ORDER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getSellerUser(SellerUserRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_SELLER_USER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void qualifySeller(QualifySellerRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_QUALIFY_SELLER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void pesitoCatalog(PesitoCatalogRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_PESITO_CATALOG;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void pesitoCredito(PesitoCreditRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_REQUEST_PESITO_CREDITO;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getPromotions(PromotionRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_PROMOTIONS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void savePromotions(SavePromotionRequest object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SAVE_PROMOTIONS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getProducts() {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_PRODUCTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(null);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getPoint(GetPointRequest getPointRequest) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_POINTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(getPointRequest);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getAwards() {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_AWARDS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(null);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void reddemPoints(ReedemPointsRequest reedemPointsRequest) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_REEDEM_POINTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(reedemPointsRequest);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }
    @Override
    public void IgetReedemptionPoints(GetReedemptionPointsRequest getReedemptionPointsRequest) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_REEDEMPTION_POINTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(getReedemptionPointsRequest);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getPromotionPhoneOperationReport(QPAY_Request_reporte_promos object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_PROMOTIONS_REPORT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getOrderByRetailerId(GetOrdersRequest request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_ORDERS_RETAILER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getPromotionReportByRetailerId(GetPromotionsRequest request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_PROMOTIONS_RETAILER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void changeStatusSaleOrder(UpdateOrderRequest request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPDATE_STATUS_ORDER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void changeStatusPromotionReport(UpdatePromotionRequest request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPDATE_STATUS_PROMOTION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void createCampaignAnswer(QPAY_CampaignAnswers request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_CAMPAING_ANSWERS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getDynamicQuestions(QPAY_CampaignRequest qpay_seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_DYNAMIC_QUESTIONS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(qpay_seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getTipsAdvertising(QPAY_Seed request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_TIPS_ADVERTISING;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getAllTipsAdvertising(QPAY_Seed seed) {
        buildConnection(Globals.URL_GET_TIPS_ADVERTISINGS,true,true,
                ConnectionManager.RequestTypeEnum.POST,seed);
    }

    @Override
    public void getActiveTipsCampaignCount(QPAY_Seed seed) {
        buildConnectionOut(Globals.URL_COUNT_ADVERTISING,true,true,
                ConnectionManager.RequestTypeEnum.POST,seed);
    }

    @Override
    public void viewTipsAdvertising(QPAY_CampaignAnswers request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_VIEW_TIPS_ADVERTISING;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getChannels(QPAY_Channels request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_CHANNELS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getUserDataRecord(QPAY_UserDataRecord seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_USER_DATA_RECORD;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getAllDataRecord(QPAY_AllDataRecord seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_ALL_DATA_RECORD;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void createUserFcIdentification(QPAY_UserFcIdentification seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_USER_FC_IDENTIFICATION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void createUserPersonalData(QPAY_UserFcPersonalData seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_USER_PERSONAL_DATA;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getDataRecordOnline(QPAY_DataRecordOnline seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_DATA_RECORD_ONLINE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void createRegister(QPAY_Register seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_REGISTER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getSalesByRetailerId(QPAY_SalesRetailer seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SALES_RETAILER_ID;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getCheckTicketClient(QPAY_CheckTicketsClient seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CHECK_TICKET_CLIENT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getCheckTicketDetail(QPAY_CheckTicketDetail seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CHECK_TICKET_DETAIL;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getCommissionReport(QPAY_CommissionReport seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_COMMISSION_REPORT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTodayTotalAmount(QPAY_TodayTotalAmount seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_TOTAL_AMOUNT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void createUserSaving(QPAY_CreateUserSaving seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CREATE_USER_SAVING;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void updateUserSaving(QPAY_CreateUserSaving seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_UPDATE_USER_SAVING;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getUserSaving(QPAY_CreateUserSaving seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_USER_SAVING;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void deleteUserSaving(QPAY_CreateUserSaving seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_DELETE_USER_SAVING;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void beginSwap(QPAY_NewUser object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_START_MIGRATION_T1000;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void finishSwap(QPAY_NewUser object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FINISH_MIGRATION_T1000;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void loginSwap(QPAY_SwapN3LoginRequest object) {
        buildConnection(Globals.URL_LOGIN_SWAP_N3,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void swapOutN3(QPAY_NewUser object) {
        buildConnection(Globals.URL_SWAP_OUT_N3,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void swapInN3(QPAY_NewUser object) {
        buildConnection(Globals.URL_SWAP_IN_N3,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void startLinkT1000(QPAY_LinkT1000Request object) {
        buildConnection(Globals.URL_START_LINK_T1000,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void validateLinkT1000(QPAY_LinkT1000Request object) {
        buildConnection(Globals.URL_VALIDATE_LINK_T1000,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void finishLinkT1000(QPAY_NewUser object) {
        buildConnection(Globals.URL_FINISH_LINK_T1000,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void getRollProduts() {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_ROLLS_COST;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(null);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void doPurchaseRoll(QPAY_BuyRollPetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_BUY_ROLLS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }


    @Override
    public void getCampaignActive(QPAY_ActiveCampaign seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_CAMPAIGN_ACTIVE;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void makeChallengePhoto(QPAY_ChallengePhoto seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MAKE_CHALLENGE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void makeChallengeQR(QPAY_ChallengeQR seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MAKE_CHALLENGE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void makeChallengeVideo(QPAY_ChallengeVideo seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MAKE_CHALLENGE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void makeChallengeQuestion(QPAY_ChallengeQuestion seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MAKE_CHALLENGE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void challengeDoneOrder(QPAY_ChallengeOrder request) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MAKE_CHALLENGE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(request);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }


    @Override
    public void getChallengeTypes(QPAY_ChallengeTypes seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_CHALLENGE_TYPES_CATALOG;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getCampaignDone(QPAY_CampaignDone seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_CAMPAIGN_DONE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getActiveCampaignNew(QPAY_ActiveCampaignNew seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_ACTIVE_CAMPAIGN_NEW;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }



    @Override
    public void getCampaignProgress(QPAY_ActiveCampaignProgress seed) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_CAMPAIGN_PROGRESS;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getEquipmentCost(QPAY_SeedDeviceRequest object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_EQUIPMENT_COST;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void buyEquipment(QPAY_BuyEquipmentRequest object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_BUY_EQUIPMENT;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void client(QPAY_Cliente_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_CREATE_CLIENT;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }


    }


    @Override
    public void fiar(QPAY_Fiar_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_NEW_DEBT;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }


    @Override
    public void newCliente(QPAY_Detail_Cliente_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_CLIENT_DETAIL;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void pagoParcial(QPAY_Pago_Parcial_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_NEW_PAYMENTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void recordatorio(QPAY_Recordatorio_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_SEND_REMINDER;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void recibo(QPAY_Recibo_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_SEND_TICKET;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void updateClient(QPAY_Update_Cliente_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_UPDATE_CLIENT;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }


    }

    @Override
    public void fiadoDetail(QPAY_Fiado_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_DEBTS_DETAIL;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void listClients(QPAY_List_Clientes_Request object, Boolean isDebt) {


        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = isDebt ? Globals.URL_FIADO_CLIENTS_DEBTS : Globals.URL_FIADO_CLIENTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void listDebts(QPAY_List_Debts_Request object) {

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_FIADO_DEBTS;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getBalance(QPAY_BalanceInquiryPetition object) {

        if(ConnectionManager.trasaction)
            return;

        ConnectionManager.trasaction = true;

        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_NEW_BALANCE_INQUIRY;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }

    }

    @Override
    public void getUrl(QPAY_GetUrlPetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_AP_GET_URL;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void requestFolio(QPAY_GetBetDetailsPetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_AP_REQUEST_FOLIO;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getBetDetails(QPAY_GetBetDetailsPetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_AP_GET_BET_DETAILS;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void payBet(QPAY_GetBetDetailsPetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_AP_PAY_BET;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }


    @Override
    public void saveTipDetail(QPAY_TipOrder order) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_SAVE_TIP_DETAIL;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(order);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getTransactionId(QPAY_TransactionCard object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_TRANSACTION_EVO;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getFinancialReport(FinancialReportRequest object) {
        buildConnection(Globals.URL_FINANCIAL_REPORT,true,true,
                ConnectionManager.RequestTypeEnum.POST,object);
    }

    @Override
    public void getOrganization(QPAY_GetOrganization seed) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_ORGANIZATION;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getCategories(QPAY_GetCategories seed) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_CATEGORIES;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getInventory(QPAY_GetInventory seed) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_INVENTORY;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getOrder(QPAY_GetOrder seed) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_GET_ORDER;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(seed);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void queryRemittance(TC_QueryRemittancePetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_REMESAS_QUERY_REMITTANCE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void payRemittance(TC_PayRemittancePetition object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_REMESAS_PAY_REMITTANCE;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void getRemittances(QPAY_Seed object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_REMESAS_QUERY_ALL_REMITTANCES;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void sendDebugInfoMitTxr(QPAY_MitErrorFinancialTxrRequest object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_MIT_ERROR_TXR;
            connectionManager.setUserRegistration(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }

    @Override
    public void report(TransactionsModel object) {
        ConnectionManager connectionManager = null;
        try {
            connectionManager = new ConnectionManager(this, this.context);
            String strUrl = Globals.URL_CONNECTIONS_REPORT;
            connectionManager.setUserRegistration(true);
            connectionManager.setReqTimeOut(true);
            connectionManager.setRequestType(ConnectionManager.RequestTypeEnum.POST);
            connectionManager.setBodyContent(object);
            connectionManager.execute(strUrl, Object.class.getCanonicalName());
        } catch (Exception e) {
            Log.e(e.getMessage(), e.getCause().getMessage());
        }
    }
}
