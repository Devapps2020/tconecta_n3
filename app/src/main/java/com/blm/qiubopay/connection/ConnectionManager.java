package com.blm.qiubopay.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.blm.qiubopay.models.QPAY_StartTxn_response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.models.Balance;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.RegisterUserResponse;
import com.blm.qiubopay.models.bimbo.TokenDTO;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WebService;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectionManager extends AsyncTask<String, Void, Object> {

    private Object bodyContent;
    private Map<String, String> parameters;
    private RequestTypeEnum requestType;
    private IConnectionManagerDelegate delegate;
    private ConnectionManagerErrorEnum connectionManagerError;
    private Context context;
    private Boolean isUserRegistration;
    private RegisterUserResponse user;
    private String body;
    private Boolean isReqTimeOut;

    public static int CONNECT_TIMEOUT = 40000;
    public static String CONNECT_TIMEOUT_MSG = "Tiempo de respuesta agotado";

    public static Boolean trasaction = false;

    public RegisterUserResponse getUser() {
        return user;
    }

    public void setUser(RegisterUserResponse user) {
        this.user = user;
    }

    public Boolean getUserRegistration() {
        return isUserRegistration;
    }

    public void setUserRegistration(Boolean userRegistration) {
        isUserRegistration = userRegistration;
    }

    public ConnectionManager(IConnectionManagerDelegate delegate, Context context) throws Exception {

        if (delegate == null || context == null) {
            throw new Exception("Parameters (delegate) and (context) MUST not be null");
        }
        this.isUserRegistration = false;
        this.delegate = delegate;
        this.context = context;
    }

    protected Object doInBackground(String... params) {

        Object response = null;

        try {

            String url = params[0];
            Class<?> returnType = Class.forName(params[1]);

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                this.connectionManagerError = ConnectionManagerErrorEnum.ConnectionManagerErrorNoConnection;
                ConnectionManager.trasaction = false;
                return null;
            }

            response = consumeWebService(url, returnType);

            ConnectionManager.trasaction = false;

            return response;

        } catch (Exception e) {

            ConnectionManager.trasaction = false;

            e.printStackTrace();
            this.connectionManagerError = ConnectionManagerErrorEnum.ConnectionManagerErrorRuntimeError;
            return null;
        }

    }

    private Object consumeWebService(String url, Class<?> returnType) throws Exception {

        //Log.i(this.toString(), "=================================");
        //Log.i(this.toString(), "REQUEST TO: " + url);
        //Log.i(this.toString(), "=================================");

        //Logger.d("URL :" + url);

        String token = getAuth(url);

        //Logger.d("Authorization : " + token);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Object response = null;
        try {
            if (this.requestType == RequestTypeEnum.POST) {
                List<Charset> charsetList = new ArrayList<>();
                charsetList.add(Charset.forName("UTF-8"));

                HttpHeaders headers = new HttpHeaders();
                //headers.setUserAgent(Globals.USER_AGENT);
                headers.setContentType(MediaType.APPLICATION_JSON);

                headers.setAcceptCharset(charsetList);

                if(token != null)
                    headers.set("Authorization", "Bearer " + token);

                Gson gson = new Gson();
                String parameters = "";

                if(this.bodyContent != null) {
                    parameters = (String) gson.toJson(this.bodyContent);

                    Logger.d("URL : "+ url +"\nREQUEST :" + parameters);

                } else {
                    parameters = body;
                }

                if(parameters == null)
                    parameters = "";

                //
                HttpEntity<String> entity = new HttpEntity<>(com.blm.qiubopay.tools.StringUtils.convertStringToUTF8(parameters), headers);

                if (this.isReqTimeOut != null && this.isReqTimeOut) {
                    ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(CONNECT_TIMEOUT);
                }

                response = restTemplate.postForObject(url, entity, returnType);

            } else if (this.requestType == RequestTypeEnum.GET) {

                URI uri = URI.create(url);

                MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
                headers.add("Content-Type", "application/json");
                //headers.add("User-Agent", Globals.USER_AGENT);

                if(this.user == null)
                    headers.add("authenticationToken", "ZWE2NDU5M2ItMGJiZS00MjNhLWI5Y2ItYjA0MzY0MGQ1YzMx");
                else
                    headers.add("authenticationToken", user.getDynamicData().getEntry().get(1).getValue());

                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

                ResponseEntity<Map> responseKinetu  = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
                JSONObject respuestaJSON = new JSONObject(responseKinetu.getBody());
                String r = respuestaJSON.toString();//responseKinetu.getBody().toString();
                //Log.i("",r);
                Gson gson = new Gson();

                response = gson.fromJson(r, Balance.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (e.getCause() instanceof SocketTimeoutException) {

                    QPAY_StartTxn_response errorResponse = new QPAY_StartTxn_response();
                    errorResponse.setQpay_response("false");
                    errorResponse.setQpay_code("01");
                    errorResponse.setQpay_description(CONNECT_TIMEOUT_MSG);
                    response = errorResponse;
                    return response;
                }

                HttpClientErrorException httpException = (HttpClientErrorException) e;
                String responseBody = httpException.getResponseBodyAsString();
                ObjectMapper mapper = new ObjectMapper();
                ErrorResponse errorResponse = mapper.readValue(responseBody, ErrorResponse.class);
                response = errorResponse;

            } catch (Exception ex) {
                ex.printStackTrace();
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setInternalCode("UNKNOWN");
                errorResponse.setMessage(ex.getMessage());
                response = errorResponse;

            }

        }

        Logger.d("URL : "+ url +"\nRESPONSE :" + new Gson().toJson(response).replaceAll("\\n",""));

        return response;

    }

    @Override
    protected void onPostExecute(Object result) {

        if (result != null) {
            this.delegate.onConnectionSucceeded(result);
        } else {
            this.delegate.onConnectionFailed(this.connectionManagerError);
        }

    }

    public void setBodyContent(Object bodyContent) {
        this.bodyContent = bodyContent;
    }

    public void setRequestType(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }


    public enum RequestTypeEnum {
        GET,
        POST
    }

    public enum ConnectionManagerErrorEnum {
        ConnectionManagerErrorNoError,
        ConnectionManagerErrorRuntimeError,
        ConnectionManagerErrorNoConnection
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getReqTimeOut() {
        return isReqTimeOut;
    }

    public void setReqTimeOut(Boolean reqTimeOut) {
        isReqTimeOut = reqTimeOut;
    }

    public String getAuth(String url) throws Exception {

        return null;

        /*String json = null;

        WebService webService = new WebService();
        webService.getClient().setContent_type("application/x-www-form-urlencoded");
        webService.getClient().setAccept("application/x-www-form-urlencoded");



        if(url.contains(Globals.HOST_RS)) {
            webService.setURL(Globals.URL_GET_TOKEN_RS);
            webService.getClient().setAutorization("Basic " + Globals.URL_TOKEN_AUTORIZATION_RS);
            json = webService.getClient()._POST(Globals.URL_TOKEN_DATA_RS);
        } else {
            return null;
        }

        if(webService.getClient().getCode() != 200)
            throw new Exception("Error de conexión");

        TokenDTO token = new Gson().fromJson(json, TokenDTO.class);

        if(token.getAccess_token() == null)
            return "";
        else
            return token.getAccess_token();*/

    }
}