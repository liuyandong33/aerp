package build.dream.aerp.utils;

import android.app.Application;
import android.util.Base64;

import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import build.dream.aerp.BuildConfig;
import build.dream.aerp.EventBusEvent;
import build.dream.aerp.api.ApiRest;
import build.dream.aerp.beans.OAuthToken;
import build.dream.aerp.beans.WebResponse;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.constants.HttpHeaders;
import build.dream.aerp.models.web.DoGetWithRequestParametersModel;
import build.dream.aerp.models.web.DoPostWithRequestBodyModel;

/**
 * Created by liuyandong on 2019/1/1.
 */

public class ApplicationHandler {
    public static Application application;
    public static String publicKeyString;
    public static String privateKeyString;
    public static PublicKey publicKey;
    public static PrivateKey privateKey;
    public static String accessToken;
    public static final Map<String, String> APPLICATION_JSON_UTF8_HTTP_HEADERS;

    static {
        APPLICATION_JSON_UTF8_HTTP_HEADERS = new HashMap<String, String>();
        APPLICATION_JSON_UTF8_HTTP_HEADERS.put(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON_UTF8);
    }

    public static void access(String accessToken, String method, String body, String type) {
        String timestamp = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN).format(new Date());
        String id = UUID.randomUUID().toString();

        Map<String, String> sortedMap = new TreeMap<String, String>();
        sortedMap.put("access_token", accessToken);
        sortedMap.put("method", method);
        sortedMap.put("timestamp", timestamp);
        sortedMap.put("id", id);
//        sortedMap.put("body", body);

        byte[] data = (WebUtils.concat(sortedMap) + body).getBytes(Constants.CHARSET_UTF_8);
        byte[] sign = SignatureUtils.sign(data, privateKey, SignatureUtils.SIGNATURE_TYPE_SHA256_WITH_RSA);

        String signature = Base64.encodeToString(sign, Base64.DEFAULT);
        Map<String, String> queryStringMap = new HashMap<String, String>();
        queryStringMap.put("access_token", accessToken);
        queryStringMap.put("method", method);
        queryStringMap.put("timestamp", timestamp);
        queryStringMap.put("id", id);
        queryStringMap.put("signature", signature);

        String url = BuildConfig.APP_API_SERVICE_URL + "?" + WebUtils.buildQueryString(queryStringMap, Constants.CHARSET_NAME_UTF_8);

        DoPostWithRequestBodyModel doPostWithRequestBodyModel = DoPostWithRequestBodyModel.builder()
                .requestUrl(url)
                .readTimeout(0)
                .connectTimeout(0)
                .headers(APPLICATION_JSON_UTF8_HTTP_HEADERS)
                .requestBody(body)
                .build();
        access(doPostWithRequestBodyModel, type);
    }

    public static void access(final DoPostWithRequestBodyModel doPostWithRequestBodyModel, final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebResponse webResponse = null;
                try {
                    webResponse = WebUtils.doPostWithRequestBody(doPostWithRequestBodyModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result = webResponse.getResult();
                ApiRest apiRest = ApiRest.fromJson(result);
                EventBusEvent eventBusEvent = new EventBusEvent();
                eventBusEvent.setSource(apiRest);
                eventBusEvent.setType(type);
                EventBusUtils.post(eventBusEvent);
            }
        }).start();
    }

    public static void authorize(String loginName, String password) {
        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("grant_type", "password");
        requestParameters.put("username", loginName);
        requestParameters.put("password", password);
        requestParameters.put("client_id", BuildConfig.CLIENT_ID);
        requestParameters.put("client_secret", BuildConfig.CLIENT_SECRET);

        DoGetWithRequestParametersModel doGetWithRequestParametersModel = DoGetWithRequestParametersModel.builder()
                .requestUrl(BuildConfig.AUTHORIZE_URL)
                .readTimeout(0)
                .connectTimeout(0)
                .requestParameters(requestParameters)
                .build();
        access(doGetWithRequestParametersModel);
    }

    public static void access(final DoGetWithRequestParametersModel doGetWithRequestParametersModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebResponse webResponse = null;
                try {
                    webResponse = WebUtils.doGetWithRequestParameters(doGetWithRequestParametersModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result = webResponse.getResult();
                Map<String, Object> resultMap = JacksonUtils.readValueAsMap(result, String.class, Object.class);


                Map<String, List<String>> headers = webResponse.getHeaders();
                publicKeyString = headers.get("Public-Key").get(0);
                privateKeyString = headers.get("Private-Key").get(0);

                publicKey = RSAUtils.restorePublicKey(publicKeyString);
                privateKey = RSAUtils.restorePrivateKey(privateKeyString);

                OAuthToken oAuthToken = new OAuthToken();
                oAuthToken.setAccessToken(MapUtils.getString(resultMap, "access_token"));
                oAuthToken.setTokenType(MapUtils.getString(resultMap, "token_type"));
                oAuthToken.setRefreshToken(MapUtils.getString(resultMap, "refresh_token"));
                oAuthToken.setExpiresIn(MapUtils.getIntValue(resultMap, "expires_in"));
                oAuthToken.setScope(MapUtils.getString(resultMap, "scope"));

                EventBusEvent eventBusEvent = new EventBusEvent();
                eventBusEvent.setSource(oAuthToken);
                eventBusEvent.setType(Constants.EVENT_TYPE_AUTHORIZE);
                EventBusUtils.post(eventBusEvent);
            }
        }).start();
    }
}
