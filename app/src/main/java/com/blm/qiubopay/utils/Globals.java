package com.blm.qiubopay.utils;

import android.content.Context;

import androidx.annotation.StringDef;

import com.blm.qiubopay.BuildConfig;
import com.blm.qiubopay.modules.chambitas.Fragment_chambitas_menu;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.nexgo.oaf.apiv3.device.printer.DotMatrixFontEnum;
import com.nexgo.oaf.apiv3.device.printer.FontEntity;

public class Globals{

    public static boolean debug  = true;
    public static boolean sit = false;
    public static ENVIRONMENT environment = ENVIRONMENT.QA;
    public static String HOST = getHostEnvironment();
    public static String HOST_RS = getHostEnvironmentRS();
    public static int SCROLLING_TIME = 4000;

    public static boolean mitec  = true;
    public static boolean kineto = false;

    public static String VAS_PROVIDER = kineto ? "k" : "g";

    public static String USER_AGENT = "bimbo/3.0";

    public static String GATEWAY_MIT  = "MITEC";
    public static String GATEWAY_VISA = "VISA";

    public static String BASE_URL         =  debug ? "https://qaag.mitec.com.mx" : "https://www.praga.io";

    public static boolean LOCAL_TRANSACTION = false;

    public static String GATEWAY = mitec ? GATEWAY_MIT : GATEWAY_VISA;

    public static  String RICOLINO_ID = "65640063";

    public static String REQUEST_LOG = "REQUEST : ";
    public static String RESPONSE_LOG = "RESPONSE : ";

    //********** NUEVA DEFINICIÓN ************
    //Alta de nuevo usuario.
    public static String URL_QIUBOPAY_REGISTER_NEW_USER     = HOST + "/api/v1/q/createUser";
    //Login de usuario.
    public static String URL_QIUBOPAY_USER_LOGIN            = HOST + "/api/v1/q/login";
    //Terminos y condiciones
    public static String URL_TERMS_AND_CONDITIONS           = HOST + "/conditions";
    //Aviso de Privacidad
    public static String URL_PRIVACY_ADVICE                 = HOST + "/privacy";
    //Contrato Financiero
    public static String URL_CONTRACT                       = HOST + "/contract";
    //Carga de categoría 1
    public static String URL_UPLOAD_CATEGORY_1              = HOST + "/api/v1/q/createAccount";
    //Carga de categoría 2
    public static String URL_UPLOAD_CATEGORY_2              = HOST + "/api/v1/q/createMerchant";
    //Información Financiera
    public static String URL_UPLOAD_FINANCIAL_INFORMATION   = HOST + "/api/v1/q/createUserAccountAndRegisterFinancial";
    //Registra toda la informacion financiera
    public static String URL_REGISTER_ALL                   = HOST + "/api/v1/s/registerFinancial";
    //Url de balance
    public static String URL_GET_BALANCE                    = HOST + "/api/v1/" + VAS_PROVIDER + "/getTaeBalance";//"/api/v1/k/getTaeBalance";
    //URL TAE Transaction
    public static String URL_TAE_SALE                       = HOST + "/api/v1/" + VAS_PROVIDER + "/taeSale";//"/api/v1/k/taeSale";
    //URL Consulta paquetes TAE MVNO
    public static String URL_TAE_PRODUCTS                   = HOST + "/api/v1/g/getTaeProducts";
    //URL VISA TRANSACTION
    public static String URL_VISA_SALE                      = HOST + "/api/v1/v/txnPaymentCardPayment4";
    //Url para subir la firma
    public static String URL_SIGNATURE_UPLOAD               = HOST + "/api/v1/v/createTxnSignature";
    //Url para el voucher
    public static String URL_VOUCHER                        = HOST + "/api/v1/v/ticket?ri=*ID*";
    //Url para devolver las últimas transacciones financieras
    public static String URL_LAST_FINANCIAL_TXN             = HOST + "/api/v1/v/getTodayTransactions";
    //Url para devolver las últimas transacciones TAE
    public static String URL_LAST_TAE_TXN                   = HOST + "/api/v1/" + VAS_PROVIDER + "/getTodayTaeSales";//"/api/v1/k/getTodayTaeSales";
    //Url para devolver las últimas transacciones de pago de servicios
    public static String URL_LAST_SERVICES_TXN              = HOST + "/api/v1/" + VAS_PROVIDER + "/getTodayServicePayments";//"/api/v1/k/getTodayServicePayments";
    //Url para pago de servicios
    public static String URL_SERVICES_PAYMENT               = HOST + "/api/v1/" + VAS_PROVIDER + "/servicePayment";//"/api/v1/k/servicePayment";
    //Url consulta de paquetes o información de cuenta
    public static String URL_QUERY_SERVICES_PAYMENT         = HOST + "/api/v1/" + VAS_PROVIDER + "/queryPayment";
    //Url para la consulta DISH
    public static String URL_QUERY_DISH                     = HOST + "/api/v1/" + VAS_PROVIDER + "/queryPaymentDish";//"/api/v1/k/queryPaymentDish";
    //Url para la consulta MEGACABLE
    public static String URL_QUERY_MEGACABLE                = HOST + "/api/v1/" + VAS_PROVIDER + "/queryPaymentMegacable";//"/api/v1/k/queryPaymentMegacable";
    //Url para CashCollection
    public static String URL_CASH_COLLECTION                = HOST + "/api/v1/" + VAS_PROVIDER + "/cashCollection";//"/api/v1/k/cashCollection";

    public static String URL_GET_TRANSACTION_EVO          = HOST + "/api/v1/g/cardCharge";

    //Url para Abono a cuenta
    public static String URL_ACCOUNT_DEPOSIT                = HOST + "/api/v1/" + VAS_PROVIDER + "/deposit";//"/api/v1/k/deposit";
    //Url para devolver las últimas transacciones TAE
    public static String URL_SEND_ACTIVATION_CODE           = HOST + "/api/v1/v/sendSecurityCodePhone";
    //Url para devolver las últimas transacciones TAE
    public static String URL_SEND_VALIDATE_ACTIVATION_CODE  = HOST + "/api/v1/v/validateSecurityCodePhone";
    //Url Restore Password Step 1
    public static String URL_RESTORE_PASSWORD_1             = HOST + "/api/v1/q/recoverPassword";
    //Url Restore Password Step 2
    public static String URL_RESTORE_PASSWORD_2             = HOST + "/api/v1/q/changePassword";
    //Url Registro inicial fase 2
    public static String URL_UPDATE_INITIAL_REGISTER        = HOST + "/api/v1/q/updateUser";
    //Url Financial Balance
    public static String URL_FINANCIAL_BALANCE              = HOST + "/api/v1/v/getTodayTotalAmount";
    //Url Vas Balance
    public static String URL_VAS_BALANCE                    = HOST + "/api/v1/" + VAS_PROVIDER + "/getTodayTotalAmount";//"/api/v1/k/getTodayTotalAmount";
    //Url Costo Lector
    public static String URL_DONGLE_COST                    = HOST + "/api/v1/" + VAS_PROVIDER + "/getDongleCost";//"/api/v1/k/getDongleCost";
    //Url Compra de Lector
    public static String URL_DONGLE_PURCHASE                = HOST + "/api/v1/" + VAS_PROVIDER + "/buyDongle";//"/api/v1/k/buyDongle";
    //DEPOSITO CON TARJETA
    public static String URL_DEPOSIT_CARD                   = HOST + "/conditions";

    //SOLICITAR UN CREDITO
    public static String URL_OLC_CREDIT_LINE                = HOST + "/api/v1/g/getOLCCreditLine";
    //CONSULTAR BALANCE
    public static String URL_OLC_QUERY                      = HOST + "/api/v1/g/getOLCQuery";

    //URL QUERY SUPPLIERS
    public static String URL_QUERY_SUPPLIERS                = HOST + "/api/v1/c/suppliers";
    //PAGOS ConectaSTART TXN
    public static String URL_START_TXN                      = HOST + "/api/v1/c/startTxn";
    //PAGOS ConectaSCREEN TXN
    public static String URL_SCREEN_TXN                     = HOST + "/api/v1/c/screenTxn";

    //CONSULTA PAGOSConecta
    public static String URL_TRANS_PAGOS_QIUBO              = HOST + "/api/v1/c/getTodayTransactionsCompleted";
    //Disposición de efectivo fin en común
    public static String URL_LOAN_DISPOSITION               = HOST + "/api/v1/g/getOLCCreditLine";
    //Consulta fin en común
    public static String URL_LOAN_QUERY                     = HOST + "/api/v1/g/getOLCQuery";
    //Se checa estado de la migración SP530
    public static String URL_CHECK_STATUS_SP530             = HOST + "/api/v1/q/statusMigrationSP530";
    //Se valida el folio de migración SP530
    public static String URL_VALIDATE_FOLIO_SP530           = HOST + "/api/v1/q/validateFolioSP530";
    //Se confirma el correo electrónico para la migración SP530
    public static String URL_CONFIRM_EMAIL_SP530           = HOST + "/api/v1/q/updateMail4SP530";
    //Se confirma la migración del usuario.
    public static String URL_CONFIRM_MIGRATION_SP530       = HOST + "/api/v1/q/confirmMigrationSP530";
    //Se valida el token
    public static String URL_VALIDATE_TOKEN_INFORMATION    = HOST + "/api/v1/p/convertEnc"; //"https://qtcdev2.qiubo.mx/service"
    //public static String URL_VALIDATE_TOKEN_INFORMATION    = "https://qtcdev2.qiubo.mx/service" + "/api/v1/p/convertEnc";

    //RSB_20191105. Se solicita obtener información del dispositivo y enviarla
    //URL para actualizar la información del dispositivo
    public static String URL_UPDATE_DEVICE_DATA             = HOST + "/api/v1/q/updateDeviceData";


    public static String URL_GET_ORGANIZATION          = HOST_RS + "/api/v1/e/rs/or/org/getOrganizations";
    public static String URL_GET_CATEGORIES          = HOST_RS + "/api/v1/e/rs/or/ct/getCategories";
    public static String URL_GET_INVENTORY          = HOST_RS + "/api/v1/e/rs/or/in/getInventory";
    public static String URL_GET_ORDER          = HOST_RS + "/api/v1/e/rs/or/getOrder";

    //RSB_20191113. Migracion Kineto
    //URL para migrar el usuario proviniente de al App KinetoConecta
    public static String URL_MIGRATE_KINETO                 = HOST + "/api/v1/q/migrateFromKineto";

    //URL para terminar de generar el usuario migrado de Kineto
    public static String URL_MIGRATE_KINETO_CONFIRM         = HOST + "/api/v1/q/confirmMigrateFromKineto";

    //Consulta de transacciones de cargos y abonos.
    public static String URL_LAST_DEBITS_AND_CREDITS_TXN   = HOST + "/api/v1/g/getTodayChargesAndPays";
    //Consulta de pagosConecta.
    public static String URL_QIUBO_PAYMENT_LIST            = HOST + "/api/v1/c/getProductCatalog";
    //Consulta de errores comunes
    public static String URL_QIUBO_GET_COMMONS_ERROR_LIST  = HOST + "/api/v1/q/getMerchantCommonErrors";
    //Envio de errores
    public static String URL_QIUBO_SAVE_MERCHANT_ERROR     = HOST + "/api/v1/q/saveMerchantProblem";
    //Corte de caja por transacciones
    public static String URL_TXR                           = HOST + "/api/v1/v/getTicketTxns";
    //Corte de caja HTML
    public static String URL_TXR_BY_HOUR                   = HOST + "/api/v1/v/ticketTxns";

    //URL Generar codigo para cajero
    public static String URL_GENERATE_LINK_CODE            = HOST + "/api/v1/q/generateLinkCode";
    //URL Recuperar cajeros
    public static String URL_GET_LINKED_USERS              = HOST + "/api/v1/q/getLinkedUsers";
    //URL Validar codigo de cajero
    public static String URL_VALIDATE_LINK_CODE            = HOST + "/api/v1/q/validateLinkCode";
    //URL Confirma cajero
    public static String URL_CONFIRM_LINK_USER             = HOST + "/api/v1/q/confirmLinkUser";
    //URL Vincular usuario
    public static String URL_LINK_USER                     = HOST + "/api/v1/q/approveLinkUser";
    //URL Desvincular usuario siendo cajero
    public static String URL_UNLINK_USER                   = HOST + "/api/v1/q/unlinkUser";
    //URL Desvincular cajero
    public static String URL_UNLINK_USER_BY_ADMIN          = HOST + "/api/v1/q/unlinkUserByAdmin";
    //URL Obtener privilegios de cajero
    public static String URL_GET_USER_PRIVILEGES           = HOST + "/api/v1/q/getUserPrivileges";
    //URL Actualizar privilegios de cajero
    public static String URL_CREATE_USER_PRIVILEGES        = HOST + "/api/v1/q/createUserPrivileges";
    //URL Crear dispositivo nuevo
    public static String URL_CREATE_USER_DEVICE            = HOST + "/api/v1/q/createUserDevice";

    //Url para devolver las últimas transacciones TAE
    public static String URL_LAST_TAE_TXN_MULTI            = HOST + "/api/v1/" + VAS_PROVIDER + "/getTodayTaeSalesByUser";
    //Url para devolver las últimas transacciones de pago de servicios
    public static String URL_LAST_SERVICES_TXN_MULTI       = HOST + "/api/v1/" + VAS_PROVIDER + "/getTodayServicePaymentsByUser";
    //CONSULTA PAGOSConecta
    public static String URL_TRANS_PAGOS_QIUBO_MULTI       = HOST + "/api/v1/c/getTodayTransactionsCompletedByUser";
    //Url para devolver las últimas transacciones financieras
    public static String URL_LAST_FINANCIAL_TXN_MULTI      = HOST + "/api/v1/v/getTodayTransactionsByUser";
    //Consulta de transacciones de cargos y abonos.
    public static String URL_LAST_CHARGES_TXN_MULTI        = HOST + "/api/v1/g/getTodayChargesAndPaysByUser";
    //Consulta de banco a partir de la cuetna CLABE
    public static String URL_GET_BANK_FROM_CLABE           = HOST + "/api/v1/q/getBankFromCLABE";
    /*VAS CON SALDO FINANCIERO*/
    //Validar la aprobación de VAS con saldo financiero
    public static String URL_FINANCIAL_VAS_BALANCE_APPROVAL    = HOST + "/api/v1/g/transferVasFinancialBalanceApproval";
    //Transferencia de saldo
    public static String URL_FINANCIAL_VAS_BALANCE_REQUEST      = HOST + "/api/v1/g/transferVasFinancialBalanceRequest";
    //Recuperar montos minimos de saldo financiero
    public static String URL_BUY_FINANCIAL_VAS_MINIMUM_AMOUNTS  = HOST + "/api/v1/g/getMinimumAmounts";
    /*VAS CON SALDO FINANCIERO*/

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Servicio de Catálogos FC
    public static String URL_CATALOG_INFO                  = HOST_RS + "/api/v1/fc/getCatalogInfo";
    public static String URL_UPDATE_CATALOG_INFO           = HOST_RS + "/api/v1/fc/updateCatalogInfo";
    //Servicio Obtención de Token FC
    public static String URL_TOKEN                         = HOST_RS + "/api/v1/fc/getFCToken";
    //Servicio Levantamiento de Incidente FC
    public static String URL_SEND_REPORT                   = HOST_RS + "/api/v1/fc/sendReport";

    public static String URL_SAVE_ORDER                   = HOST_RS + "/api/v1/e/rs/or/createOrder";

    public static String URL_GET_SELLER_USER                = HOST_RS + "/api/v1/e/rs/getSellerUser";
    public static String URL_QUALIFY_SELLER                 = HOST_RS + "/api/v1/e/rs/qualifySeller";
    public static String URL_PESITO_CATALOG                 = HOST_RS + "/api/v1/e/rs/getPesitoCatalog";
    public static String URL_REQUEST_PESITO_CREDITO         = HOST_RS + "/api/v1/e/rs/updatePesitoCatalog";//requestPesitoCredit";

    public static String URL_GET_PROMOTIONS                 = HOST_RS + "/api/v1/e/rs/getPromotions";
    public static String URL_SAVE_PROMOTIONS                 = HOST_RS + "/api/v1/e/rs/savePromotionRequest";

    public static String URL_PUNTOS_BIMBO           =  "http://puntos.pagosapp.com.mx";

    public static String URL_IMAGES_SELLER           = "https://bimbo-s3.s3.amazonaws.com/Vendedores/";
    public static String URL_IMAGES_PROMOTION        =  "https://bimbo-s3.s3.amazonaws.com/promociones/";
    public static String URL_IMAGES_PRODUCT          =  "https://bimbo-s3.s3.amazonaws.com/marketplace/";
    public static String URL_IMAGES_PREMIOS          =  "https://bimbo-s3.s3.amazonaws.com/premios/";

    public static String URL_GET_PRODUCTS         = HOST_RS + "/api/v1/e/rs/getProducts";
    public static String URL_GET_POINTS         = HOST_RS + "/api/v1/e/rs/getPointsByBimboId";

    public static String URL_GET_AWARDS        = HOST_RS + "/api/v1/e/rs/getAwards";
    public static String URL_REEDEM_POINTS        = HOST_RS + "/api/v1/e/rs/redeemPoints";
    public static String URL_GET_REEDEMPTION_POINTS        = HOST_RS + "/api/v1/e/rs/getRedemptionPointsbyBimboId";


    public static String URL_GET_TOKEN_RS                 =  HOST_RS +  "/oauth/token";
    public static String URL_TOKEN_AUTORIZATION_RS        =  "cWl1Ym8tc2VjdXJlLWFnZ3JlZy1zMTpxaXViby0xOTA5LXB3ZC4zbjFnbWEz";
    public static String URL_TOKEN_DATA_RS                 =  "grant_type=password&username=aggregator&password=paS5worD.AGg4egat0r&client_id=qiubo-secure-aggreg-s1";

    public static String URL_GET_TOKEN                  =  HOST +  "/oauth/token";
    public static String URL_TOKEN_AUTORIZATION         =  "cWl1Ym8tc2VjdXJlLWFnZ3JlZy1zMTpxaXViby0xOTA5LXB3ZC4zbjFnbWEz";
    public static String URL_TOKEN_DATA                 =  "grant_type=password&username=aggregator&password=paS5worD.AGg4egat0r&client_id=qiubo-secure-aggreg-s1";


    public static String URL_GET_PROMOTIONS_REPORT        = HOST_RS + "/api/v1/bo/getPromotionPhoneOperationReport";

    public static String URL_GET_ORDERS_RETAILER        = HOST_RS + "/api/v1/e/rs/getOrderByRetailerId";
    public static String URL_UPDATE_STATUS_ORDER        = HOST_RS + "/api/v1/e/rs/changeStatusSaleOrder";
    public static String URL_GET_PROMOTIONS_RETAILER    = HOST_RS + "/api/v1/e/rs/getPromotionReportByRetailerId";
    public static String URL_UPDATE_STATUS_PROMOTION    = HOST_RS + "/api/v1/e/rs/changeStatusPromotionReport";

    public static String URL_GET_DYNAMIC_QUESTIONS    = HOST_RS + "/api/v1/dq/getCampaignActive";
    public static String URL_CREATE_CAMPAING_ANSWERS    = HOST_RS + "/api/v1/dq/createCampaignAnswers";
    public static String URL_GET_TIPS_ADVERTISING    = HOST_RS + "/api/v1/tp/getCampaignActive";
    public static String URL_VIEW_TIPS_ADVERTISING    = HOST_RS + "/api/v1/tp/createCampaignViewed";
    public static String URL_GET_CHANNELS    = HOST_RS + "/api/v1/cat/getChannels";

    public static String URL_GET_ALL_DATA_RECORD    = HOST_RS + "/api/v1/r/getAllDataRecord";
    public static String URL_GET_USER_DATA_RECORD    = HOST_RS + "/api/v1/r/getUserDataRecord";
    public static String URL_CREATE_USER_FC_IDENTIFICATION = HOST_RS + "/api/v1/r/createUserFcIdentification";
    public static String URL_CREATE_USER_PERSONAL_DATA  = HOST_RS + "/api/v1/r/createUserFcPersonalData";
    public static String URL_GET_DATA_RECORD_ONLINE    = HOST_RS + "/api/v1/r/getDataRecordOnline";

    public static String URL_CREATE_REGISTER    = HOST_RS + "/api/v1/fc/createRegister";

    public static String URL_SALES_RETAILER_ID   = HOST_RS + "/api/v1/e/rs/getSalesByRetailerId";
    public static String URL_CHECK_TICKET_CLIENT   = HOST_RS + "/api/v1/e/rs/getTicketByRetailerIdAndOrganizationId";
    public static String URL_CHECK_TICKET_DETAIL   = HOST_RS + "/api/v1/e/rs/getDetailTicket";

    public static String URL_COMMISSION_REPORT   = HOST + "/api/v1/g/getCommissionsReport";

    public static String URL_TOTAL_AMOUNT  = HOST + "/api/v1/v/getTodayTotalAmount";

    public static String URL_CREATE_USER_SAVING  = HOST + "/api/v1/saving/createUserSaving";
    public static String URL_UPDATE_USER_SAVING  = HOST + "/api/v1/saving/updateUserSaving";
    public static String URL_GET_USER_SAVING  = HOST + "/api/v1/saving/getUserSaving";
    public static String URL_DELETE_USER_SAVING  = HOST + "/api/v1/saving/deleteUserSaving";

    //APUESTAS DEPORTIVAS
    public static final String URL_AP_GET_URL                = HOST + "/api/v1/jd/requestUrl";
    public static final String URL_AP_GET_BET_DETAILS        = HOST + "/api/v1/jd/betDetails";
    public static final String URL_AP_REQUEST_FOLIO          = HOST + "/api/v1/jd/generateFolio";
    public static final String URL_AP_PAY_BET                = HOST + "/api/v1/jd/payBet";

    public static String URL_GET_CAMPAIGN_ACTIVE = HOST_RS + "/api/v1/ch/getCampaignActive";

    public static String URL_MAKE_CHALLENGE = HOST_RS + "/api/v1/ch/createChallengeDone";
    public static String URL_GET_CHALLENGE_TYPES_CATALOG= HOST_RS + "/api/v1/ch/getChallengeTypesCatalog";

    public static String URL_GET_CAMPAIGN_DONE= HOST_RS + "/api/v1/ch/getCampaignsDone";
    public static String URL_GET_ACTIVE_CAMPAIGN_NEW= HOST_RS + "/api/v1/ch/getCampaignsActiveNew";
    public static String URL_GET_CAMPAIGN_PROGRESS= HOST_RS + "/api/v1/ch/getCampaignsActiveInProgress";

    //Extraer costos de equipo promocional
    public static final String URL_GET_EQUIPMENT_COST       = HOST + "/api/v1/g/getTCEquipmentCost";//"/api/v1/g/getEquipmentCost";
    //Realizar compra de material promocional
    public static final String URL_BUY_EQUIPMENT            = HOST + "/api/v1/g/buyEquipment";
    //Crear Cliente Fiado (post)
    public static String URL_FIADO_CREATE_CLIENT  = HOST + "/api/v1/fiado/createClient";
    //Actualizar Cliente Fiado (post)
    public static String  URL_FIADO_UPDATE_CLIENT          = HOST + "/api/v1/fiado/updateClient";
    //Consulta de Clientes con Adeudo (Get)
    public static String URL_FIADO_CLIENTS_DEBTS          = HOST + "/api/v1/fiado/clients/debts";
    //Consulta de Todos los clientes Fiado por cliente Qiubo (Get)
    public static String URL_FIADO_CLIENTS                = HOST + "/api/v1/fiado/clients";
    //Fiar (Post)
    public static String URL_FIADO_NEW_DEBT               = HOST + "/api/v1/fiado/newDebt";
    //Pago Parcial o Total (Post)
    public static String URL_FIADO_NEW_PAYMENTS           = HOST + "/api/v1/fiado/newPayment";
    //Consultar Detalle del cliente (Get)
    public static String URL_FIADO_CLIENT_DETAIL          = HOST + "/api/v1/fiado/clients/detail";
    //Consultar Detalle de Préstamo (Get)
    public static String URL_FIADO_DEBTS_DETAIL           = HOST + "/api/v1/fiado/checkDebt";
    //Envío de Recordatorio (Post)
    public static String URL_FIADO_SEND_REMINDER          = HOST + "/api/v1/fiado/sendReminder";
    //Envío de Recibo (Post)
    public static String URL_FIADO_SEND_TICKET            = HOST + "/api/v1/fiado/sendReceipt";
    //Consulta de adeudos
    public static String URL_FIADO_DEBTS                      = HOST + "/api/v1/fiado/debts";



    public static String URL_GET_NEW_BALANCE_INQUIRY= HOST + "/api/v1/g/balanceReport";

    /*OPERATIVA RESTAURANTE*/
    public static String URL_SAVE_TIP_DETAIL            = HOST + "/api/v1/v/saveTipDetailRestaurant";
    /*OPERATIVA RESTAURANTE*/

    public static String URL_FAQS = "https://documentos.t-conecta.app/faqs.html";
    public static String URL_MANUAL = "https://drive.google.com/viewerng/viewer?embedded=true&url=https://documentos.t-conecta.app/manual.pdf";
    public static String PHONE_WHATSAPP = "5542121414";
    public static String PHONE_SUPPORT = "5542121427";

    public static String URL_BUY_ROLLS                     = HOST + "/api/v1/g/buyRolls";
    public static String URL_GET_ROLLS_COST                = HOST + "/api/v1/g/getRollsCost";

    //Iniciar migracion T1000
    public static final String URL_START_MIGRATION_T1000          = HOST + "/api/v1/q/startMigrationT1000";
    //Finalizar migracion T1000
    public static final String URL_FINISH_MIGRATION_T1000         = HOST + "/api/v1/q/finishMigrationT1000";
    //Login swap n3 a n3
    public static final String URL_LOGIN_SWAP_N3             = HOST + "/api/v1/q/loginPromotor";
    //Swap out n3 a n3
    public static final String URL_SWAP_OUT_N3               = HOST + "/api/v1/q/swapOut";
    //Swap in n3 a n3
    public static final String URL_SWAP_IN_N3                = HOST + "/api/v1/q/swapIn";
    //Iniciar asociacion multidevice T1000
    public static final String URL_START_LINK_T1000          = HOST + "/api/v1/q/startLinkDevice";
    //Validar folio asociacion multidevice T1000
    public static final String URL_VALIDATE_LINK_T1000       = HOST + "/api/v1/q/validateLinkFolio";
    //Finalizar asocicion multidevice T1000
    public static final String URL_FINISH_LINK_T1000         = HOST + "/api/v1/q/finishLinkDevice";

    //Consulta reporte financiero
    public static final String URL_FINANCIAL_REPORT          = HOST + "/api/v1/g/financialReport";

    public static final String URL_CONNECTIONS_REPORT          = HOST + "/api/v1/cr/add";

    public static boolean EMV_FULL = true;

    //PRINTER
    public static final int FONT_SIZE_SMALL   = 20;
    public static final int FONT_SIZE_NORMAL  = 24;
    public static final int FONT_SIZE_BIG     = 24;

    public static final int FONT_SIZE_SMALL_V2   = 14;
    public static final int FONT_SIZE_NORMAL_V2  = 18;
    public static final int FONT_SIZE_BIG_V2     = 20;

    //N3_FLAG_COMMENT

    public static FontEntity fontSmall    = new FontEntity(DotMatrixFontEnum.CH_SONG_20X20, DotMatrixFontEnum.ASC_SONG_8X16);
    public static FontEntity fontNormal   = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_12X24);
    public static FontEntity fontBold     = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_BOLD_16X24);
    public static FontEntity fontBig      = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_12X24, false, true);


    public static String REGISTER_QUESTIONS     = "REGISTER";
    public static String PROFILE_QUESTIONS       = "PROFILE";
    public static String LOGIN_QUESTIONS        = "LOGIN";


    public static String REGISTER_TYPE_FC_CODI     = "Codi";
    public static String REGISTER_TYPE_FC_CREDITO      = "Credito";
    public static String REGISTER_TYPE_FC_CUENTA        = "Cuenta";

    /* Remesas */
    public static String URL_REMESAS_QUERY_REMITTANCE           = HOST + "/api/v1/g/queryRemittance";//Consulta de la información de una remesa
    public static String URL_REMESAS_PAY_REMITTANCE             = HOST + "/api/v1/g/payRemittance";//Pago de una remesa
    public static String URL_REMESAS_QUERY_ALL_REMITTANCES      = HOST + "/api/v1/g/getRemittances";//Consulta todas las remesas
    public static String URL_REMESAS_GET_REMITTANCES_BY_USER    = HOST + "/api/v1/g/getRemittancesByUser";//Consulta todas las remesas en modo cajero - patrón
    public static String URL_REMESAS_GET_REMITTANCE_TICKET      = HOST + "/api/v1/g/remittanceTicket";//Consulta el ticket de un remesa pagada
    public static String URL_REMESAS_TERMINOS_Y_CONDICIONES     = HOST + "/cloudTransferConditions";
    /* Remesas */



    public static String URL_COUNT_ADVERTISING      =   HOST_RS + "/api/v1/tp/getCampaignsActiveCount";

    public static String URL_GET_TIPS_ADVERTISINGS  =   HOST_RS + "/api/v1/tp/getCampaignsActive";

    public static String URL_MIT_ERROR_TXR  =   HOST + "/api/v1/v/notifyFailedTransaction";

    /** @hide */
    @StringDef({ID_ALO,ID_IUSACELL,ID_MAS_RECARGA,ID_MOVISTAR,ID_TELCEL,ID_TUENTI,ID_UNEFON,
            ID_VIRGIN_MOBILE,ID_FREEDOM_POP,ID_TELCEL_DATOS,ID_BUENOCELL,ID_TELCEL_INTERNET,
            ID_MI_MOVIL,ID_TURBORED_TAE,ID_TURBORED_TAE,ID_TURBORED_DATOS,ID_SPACE,ID_DIRI,
            ID_PILLOFON,ID_FOBO,ID_BAIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CarrierID {}

    //CARRIERS TAE
    public static final String ID_ALO           = "01";
    public static final String ID_IUSACELL      = "02";
    public static final String ID_MAS_RECARGA   = "03";
    public static final String ID_MOVISTAR      = "04";
    public static final String ID_TELCEL        = "05";
    public static final String ID_TUENTI        = "06";
    public static final String ID_UNEFON        = "07";
    public static final String ID_VIRGIN_MOBILE = "08";
    public static final String ID_FREEDOM_POP   = "09";
    public static final String ID_TELCEL_DATOS  = "10";
    public static final String ID_BUENOCELL     = "11";
    public static final String ID_TELCEL_INTERNET = "12";
    public static final String ID_MI_MOVIL      = "13";
    public static final String ID_TURBORED_TAE  = "14";
    public static final String ID_TURBORED_DATOS= "15";
    public static final String ID_SPACE         = "16";
    public static final String ID_DIRI          = "17";
    public static final String ID_PILLOFON      = "18";
    public static final String ID_FOBO          = "19";
    public static final String ID_BAIT          = "20";

    /** @hide */
    @StringDef({TELMEX_ID,SKY_ID,VTV_ID,CFE_ID,DISH_ID,TELEVIA_ID,
            NATURGY_ID,IZZI_ID,OMNIBUS_ID,MEGACABLE_ID,PASE_URBANO_ID,
            TOTALPLAY_ID,STARTV_ID,CEA_QRO_ID,NETWAY_ID,
            VEOLIA_ID,AMAZON_CASH_ID,OPDM_ID,POST_ATT_ID,
            POST_MOVISTAR_ID,SACMEX_ID,GOB_MX_ID,SIAPA_GDL_ID,
            AYDM_MTY_ID,GOB_EDOMEX_ID,CESPT_TIJ_ID,BLUE_TEL_ID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ServicePayId {}

    public static final String TELMEX_ID         = "01";
    public static final String SKY_ID            = "02";
    public static final String VTV_ID            = "02";
    public static final String CFE_ID            = "03";
    public static final String DISH_ID           = "06";
    public static final String TELEVIA_ID        = "05";
    public static final String NATURGY_ID        = "08";
    public static final String IZZI_ID           = "04";
    public static final String OMNIBUS_ID        = "07";
    public static final String MEGACABLE_ID      = "09";
    public static final String PASE_URBANO_ID    = "10";
    public static final String TOTALPLAY_ID      = "11";
    public static final String STARTV_ID         = "12";
    public static final String CEA_QRO_ID        = "13";
    public static final String NETWAY_ID         = "14";
    public static final String VEOLIA_ID         = "15";
    public static final String AMAZON_CASH_ID    = "16";
    public static final String OPDM_ID           = "17";
    public static final String POST_ATT_ID       = "18";
    public static final String POST_MOVISTAR_ID  = "19";
    public static final String SACMEX_ID         = "20";
    public static final String GOB_MX_ID         = "21";
    public static final String SIAPA_GDL_ID      = "22";
    public static final String AYDM_MTY_ID       = "23";
    public static final String GOB_EDOMEX_ID     = "24";
    public static final String CESPT_TIJ_ID      = "25";
    public static final String BLUE_TEL_ID       = "26";


    /** @hide */
    @StringDef({APPLINK_URL,F_CHAMBITAS,F_FINCOMUN,F_COMPRA_ROLLOS,F_COMPRA_LECTOR,F_COMPRA_MATERIAL,F_NOTIFICACIONES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppLink {}

    public static final String APPLINK_URL        = "app.tconecta.com/";
    public static final String F_CHAMBITAS        = "chambitas";
    public static final String F_FINCOMUN         = "fincomun";
    public static final String F_COMPRA_ROLLOS    = "comprarollos";
    public static final String F_COMPRA_LECTOR    = "compralector";
    public static final String F_COMPRA_MATERIAL  = "compramaterial";
    public static final String F_HAZ_TU_PEDIDO    = "haztupedido";
    public static final String F_NOTIFICACIONES   = "notificaciones";


    //LECTORES
    public static String NEXGO_N3       = "N5";

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        Globals.HOST = HOST;
    }

    /*public static String getUrlToUploadCategory1() {
        return URL_UPLOAD_CATEGORY_1;
    }
    public static String getUrlToUploadCategory2() {
        return URL_UPLOAD_CATEGORY_2;
    }
    public static String getUrlToRegisterNewUser() {
        return URL_QIUBOPAY_REGISTER_NEW_USER;
    }
    public static String getLoginUrl() {
        return URL_QIUBOPAY_USER_LOGIN;
    }*/

    //Textos TAE

    public static String TAE_CARRIER_05 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA *264\n " +
            "DE TU TELCEL O AL 01 800 710 5687";//TELCEL

    public static String TAE_CARRIER_10 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA *264\n " +
            "DE TU TELCEL O AL 01 800 710 5687";//TELCEL_DATOS

    public static String TAE_CARRIER_04 =   "ESTIMADO CLIENTE EN CASO DE\n " +
            "ACLARACIÓN MARCAR *611 DESDE\n " +
            "SU MOVISTAR O AL 01 800 888 8366";//MOVISTAR

    public static String TAE_CARRIER_02 =   "PARA DUDAS O ACLARACIONES\n " +
            "COMUNÍCATE AL NÚMERO *8482(VIVA)\n " +
            "DESDE TU AT&T Y AL 050 DESDE TU UNEFON";//IUSACELL Y AT&T

    public static String TAE_CARRIER_07 =   "PARA DUDAS O ACLARACIONES\n " +
            "COMUNÍCATE AL NÚMERO *8482(VIVA)\n " +
            "DESDE TU AT&T Y AL 050 DESDE TU UNEFON";//UNEFON

    public static String TAE_CARRIER_08 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA DESDE\n " +
            "TU VIRGIN MOBILE AL *7625";//VIRGIN_MOBILE

    public static String TAE_CARRIER_03 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA AL 01 800 888 8366";//MAS_RECARGA

    public static String TAE_CARRIER_06 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA AL 01 800 888 8366";//TUENTI

    public static String TAE_CARRIER_09 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA *3733\n " +
            "DESDE TU NÚMERO FREEDOMPOP O AL 01 800 953 0743";//FREEDOM_POP

    public static String TAE_CARRIER_01 =   "PARA DUDAS O ACLARACIONES\n " +
            "DE TU RECARGA MARCA *611 DE TU ALO O AL 01 552 581 3388";//ALO

    public static String TAE_CARRIER_11 =   "PARA ACLARACIONES O PREGUNTAS\n " +
            "DE TU RECARGA MARCA SIN COSTO\n " +
            "AL *711 O AL (55) 7316 6666";//BUENO CELL

    public static String TELCEL_INTERNET_CLARIFICATION = "AL REALIZAR UNA RECARGA DE SALDO\n " +
            "O COMPRA DE PAQUETES ACEPTAS\n " +
            "LOS TÉRMINOS Y CONDICIONES EN\n " +
            "WWW.TELCEL.COM/AMIGO O EN *264";

    //Textos Servicios
    public static String SERVICE_01 =       "SERVICIO OPERADO POR AKINET.\n " +
            "EL PERIODO PARA LA APLICACIÓN\n " +
            "DEL PAGO ES DE 25 HRS. PARA\n " +
            "CUALQUIER DUDA LLAME AL 01 800 300 0808 O 91770277";//TELMEX

    public static String SERVICE_02 =       "SERVICIO OPERADO POR REGALII.\n " +
            "EL PERIODO PARA LA APLICACIÓN\n " +
            "DE PAGO ES DE 24 HORAS. PARA\n " +
            "CUALQUIER DUDA LLAME A SKY 51690000 Y 01 800 475 9759";//SKY

    public static String SERVICE_03 =       "GRACIAS POR SU PAGO, EL PERIODO\n " +
            "PARA LA APLICACIÓN DEL PAGO ES\n " +
            "DE 24 HORAS. PARA CUALQUIER DUDA\n " +
            "LLAME A CFE AL 071 O ACUDA A SU\n " +
            "CENTRO DE ATENCIÓN MAS CERCANO.";//CFE

    public static String SERVICE_06 =       "PARA DUDAS Y ACLARACIONES COMUNÍCATE\n " +
            "AL 9628-3450 DESDE LA CIUDAD\n " +
            "DE MÉXICO, GUADALAJARA Y MONTERREY,\n " +
            "Y DEL INTERIOR AL 01 800 900 11 11";//DISH

    public static String SERVICE_05 =       "PARA DUDAS O ACLARACIONES SOBRE\n " +
            "SU RECARGA LLAME AL 01 800 347 8238 O INGRESE A WWW.TELEVIA.COM.MX";//TELEVIA

    public static String SERVICE_08 =       "SERVICIO OPERADO POR DATALOGIC.\n " +
            "EL PERIODO PARA LA APLICACIÓN DEL\n " +
            "PAGO ES DE 24 HRS. PARA CUALQUIER\n " +
            "DUDA LLAME A GAS NATURAL AL 01 800 171 3000";//GAS_NATURAL

    public static String SERVICE_04 =       "SERVICIO OPERADO POR DATALOGIC.\n " +
            "CALL CENTER IZZI 01 800 120 5000\n " +
            "HORARIO 24 HORAS 7 DÍAS DE LA SEMANA,\n " +
            "PÁGINA WEB WWW.IZZI.MX";//IZZI

    public static String SERVICE_09 =       "GRACIAS POR SU PAGO EL PERIODO\n " +
            "PARA LA APLICACIÓN DEL PAGO ES\n " +
            "DE 24 HRS. PARA CUALQUIER DUDA\n " +
            "LLAME A MEGACABLE AL 9690 0000\n " +
            "O ACUDA A SU CENTRO MAS CERCANO";//MEGACABLE

    public static String SERVICE_10 =       "PARA ACLARACIONES Y SERVICIOS AL\n " +
            "CLIENTE COMUNICARSE AL CENTRO DE\n " +
            "ATENCIÓN A CLIENTES DE PASE URBANO\n " +
            "DESDE LA CIUDAD DE MÉXICO AL\n " +
            "5559501440 O DESDE EL INTERIOR DE\n " +
            "LA REPÚBLICA AL 01 80090007273";//PASE URBANO

    public static String SERVICE_11 =       "PARA DUDAS Y ACLARACIONES COMUNICARSE\n " +
            "DESDE LA CIUDAD DE MÉXICO Y ÁREA\n " +
            "METROPOLITANA AL 55 1579 8000 O DESDE\n " +
            "EL INTERIOR DE LA RÉPUBLICA AL 01 800 5100 510";//TOTALPLAY

    public static final String STARTV_CLARIFICATION =   "PARA ACLARACIONES Y SERVICIO AL CLIENTE\n " +
            "COMUNICARSE AL 01800 700 7827, CON STARTV";

    public static final String NETWEY_CLARIFICATION =   "PARA ACLARACIONES Y SERVICIO AL CLIENTE\n " +
            "COMUNICARSE AL 55 4742 1245, CON NETWEY";

    public static final String CEA_QUERETARO_CLARIFICATION =    "PARA DUDAS Y ACLARACIONES MARQUE AL (01 800)\n " +
            "9090 232 o al (442) 211 066 SERVICIO AL CLIENTE\n " +
            "DE CEA QUERETARO.\n " +
            "5 DÍAS DE VIGENCIA DESPUÉS DE LA FECHA DE PAGO\n " +
            "PARA CUALQUIER ACLARACIÓN";

    public static final String VEOLIA_CLARIFICATION =   "PARA DUDAS Y ACLARACIONES MARQUE AL 073\n " +
            "SERVICIO A CLIENTES PROACTIVA MEDIO AMBIENTE\n " +
            "CAASA.\n " +
            "5 DÍAS DE VIGENCIA DESPUÉS DE LA FECHA DE PAGO\n " +
            "PARA CUALQUIER ACLARACIÓN";

    public static final String OPDM_CLARIFICATION = "Para dudas y aclaraciones marque al 01800-006-6736 servicio a clientes OPDM Tlalnepantla. 5 días de vigencia después de la fecha de pago para cualquier aclaración";

    // JLH: Este número de versión se debe incrementar cada vez que se agregen nuevos retos a las chambitas
    public static final Integer CHAMBITAS_VERSION = 2;



    //ROLES
    public static final String ROL_NORMAL     = "0";
    public static final String ROL_PATRON     = "1";
    public static final String ROL_CAJERO     = "2";

    public class NotificationType {
        public static final String NOTIFICATION_LINK_REQUEST = "linkRequest"; //Usuario admin recibe notificación de operador intentando asociarse
        public static final String NOTIFICATION_UNLINK_MESSAGE = "message"; //Usuario admin recibe notificación de operador se ha desvinculado por si mismo
        public static final String NOTIFICATION_BASIC_USER = "basicUser"; //Usuario admin recibe notificación se ha quedado sin operadores
        public static final String DATA_LINK = "link";  //Usuario operador recibe data y hace logout porque el admin ha aceptado la asociación a su cuenta
        public static final String DATA_PRIVILEGES = "privileges";  //Usuario operador recibe data y hace logout porque el admin cambio sus privilegios
        public static final String DATA_UNLINK = "unlink";  //Usuario operador recibe data y hace logout por que el patrón lo elimino de su cuenta
        public static final String DATA_RATE_APP = "rateApp"; //20200728 RSB. RateApp. Se recibe para activar la calificación del app
        public static final String DATA_ON_DEMAND = "onDemand";
        public static final String NOTIFICATION_NEW_ORDER = "newOrder";
        public static final String NOTIFICATION_ACCEPT_ORDER = "acceptOrder";
        public static final String NOTIFICATION_DECLINE_ORDER = "declineOrder";
        public static final String CHAMBITA_ON_DEMAND = "chambitaOnDemand";
    }

    public static final String OPERATIVE_TYPE_RESTAURANT = "RESTAURANT";
    public static final String OPERATIVE_TYPE_RETAIL = "RETAIL";

    //ENKO URL
    public static final String URL_ENKO = "www.enko.solutions";
    //Antigua
    //public static final String URL_ENKO = "https://m.enko.org/home/tabs/register?token=1f7138639d9dbd90ed57f78ebe07e12c15fab4f2";
    public static final String URL_ENKO_ONBOARDING = "?token=7d097301ba9d082402a74016ab42e8c2&wsfunction=core_user_create_users&moodlewsrestformat=json";

    //210429 RSB. Homologación Enko 2.0
    public static final String URL_ENKO_2 = debug ?  "https://api.tuuk.mx/" : "https://api.enko.org";
    public static final String URL_ENKO_2_TABS = debug ? "https://m.tuuk.mx/home/tabs/" : "https://m.enko.org/home/tabs/";

    //20200728 RSB. RateApp. Transacciones a completar para presentar dialogo de calificación
    public static final int TRANSACTIONS_TO_RATE_APP = 3;
    public enum CATFC {
        tipoCredito,
        productoCredito,
        modCredito,
        matriz,
        genero,
        estado_civil,
        ocupacion,
        nacionalidad,
        parentesco,
        solicitud,
        lugarNacimiento,
        respuesta,
        gradoEstudios,
        estatusSolicitud,
        vivienda,
        giroNegocio,
        sector,
        municipio,
        localidad,
        actividadGeneral,
        actividadEconomica,
        horarios,
        destino,
        puesto,
        cliente_premium,
        afiliados,
        banco,
        none
    }

    public enum NUMBER {
        NONE,
        UNO,
        DOS,
        TRES,
        CUATRO,
        CINCO,
        SEIS,
        SIETE,
        OCHO,
        NUEVE,
        DIEZ,
        ONCE,
        DOCE,
        TRECE,
        CATORCE_15,
        CATORCE,
        QUINCE,
        DIECISEIS;

        public static NUMBER from(int id) {
            for (NUMBER type : values()) {
                if (type.ordinal() == id) {
                    return type;
                }
            }
            return null;
        }
    }

    public static String FC_PROMOTOR      = "5";
    public static String FC_SUCURSAL      = environment == ENVIRONMENT.PRD ? "701" : "001";
    public static String FC_USUARIO       = environment == ENVIRONMENT.PRD ? "999989" : "000662";

    public enum ORGANIZACION {
        None,
        Bimbo;

        @Override
        public String toString() {
            return super.name().replace("_", " ");
        }
    }

    public enum CATEGORIAS {

        TODAS;

        @Override
        public String toString() {
            return super.name().replace("_", " ");
        }
    }

    public enum MARCAS {
        None,
        Bimbo,
        Del_Hogar,
        Lara,
        Marinela,
        Milpa_Real,
        Sanissimo,
        Suandy,
        Tia_Rosa,
        Wonder;

        @Override
        public String toString() {
            return super.name().replace("_", " ");
        }
    }

    public static String loadJSONFromAsset(Context context, String file) {

        String json = null;

        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public enum ENVIRONMENT {
        DEV,
        QA,
        PRD
    }

    public static String getHostEnvironment() {

        switch (environment) {
            case DEV:
                debug = true;
                return "https://qtcdev.qiubo.mx/service";
            case QA:
                debug = true;
                return "https://qtcdev2.qiubo.mx/service";
            case PRD:
                debug = false;
                return "https://rs.t-conecta.app/service";
        }

        return "";
    }

    public static String getHostEnvironmentRS() {

        switch (environment) {
            case DEV:
                debug = true;
                return "https://rsdev.pagosapp.com.mx/service";
            case QA:
                debug = true;
                return "https://rsqa.pagosapp.com.mx/service";
            case PRD:
                debug = false;
                return "https://rs.pagosapp.com.mx/service";
        }

        return "";
    }

    public static String VERSION = debug ? BuildConfig.VERSION_NAME + " QA" : BuildConfig.VERSION_NAME.substring(0,BuildConfig.VERSION_NAME.lastIndexOf("."));

    public static String TAG_PREFERENCES = "com.blm.qiubopay.helpers";

}
