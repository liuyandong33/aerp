package build.dream.aerp.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Base64;
import android.widget.Toast;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import build.dream.aerp.BuildConfig;
import build.dream.aerp.api.ApiRest;
import build.dream.aerp.beans.OAuthTokenError;
import build.dream.aerp.beans.WebResponse;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.constants.HttpHeaders;
import build.dream.aerp.domains.Config;
import build.dream.aerp.domains.OAuthToken;
import build.dream.aerp.eventbus.EventBusEvent;
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
    public static OAuthToken oAuthToken;
    public static final Map<String, String> APPLICATION_JSON_UTF8_HTTP_HEADERS;

    static {
        APPLICATION_JSON_UTF8_HTTP_HEADERS = new HashMap<String, String>();
        APPLICATION_JSON_UTF8_HTTP_HEADERS.put(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON_UTF8);
    }

    public static String obtainPublicKeyString() {
        if (StringUtils.isBlank(publicKeyString)) {
            Config config = ConfigUtils.obtainConfig(Constants.CONFIG_NAME_PUBLIC_KEY);
            publicKeyString = config.getValue();
        }
        return publicKeyString;
    }

    public static String obtainPrivateKeyString() {
        if (StringUtils.isBlank(privateKeyString)) {
            Config config = ConfigUtils.obtainConfig(Constants.CONFIG_NAME_PRIVATE_KEY);
            privateKeyString = config.getValue();
        }
        return privateKeyString;
    }

    public static PublicKey obtainPublicKey() {
        if (ObjectUtils.isNull(publicKey)) {
            publicKey = RSAUtils.restorePublicKey(publicKeyString);
        }
        return publicKey;
    }

    public static PrivateKey obtainPrivateKey() {
        if (ObjectUtils.isNull(privateKey)) {
            privateKey = RSAUtils.restorePrivateKey(privateKeyString);
        }
        return privateKey;
    }

    public static OAuthToken obtainOAuthToken(Context context) {
        if (ObjectUtils.isNull(oAuthToken)) {
            oAuthToken = DatabaseUtils.find(context, OAuthToken.class);
        }
        return oAuthToken;
    }

    public static String obtainAccessToken(Context context) {
        return obtainOAuthToken(context).getAccessToken();
    }

    public static void access(String accessToken, String method, String body, String type) {
        String timestamp = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN).format(new Date());
        String id = UUID.randomUUID().toString();

        Map<String, String> sortedMap = new TreeMap<String, String>();
        sortedMap.put("access_token", accessToken);
        sortedMap.put("method", method);
        sortedMap.put("timestamp", timestamp);
        sortedMap.put("id", id);

        byte[] data = (WebUtils.concat(sortedMap) + body).getBytes(Constants.CHARSET_UTF_8);
        byte[] sign = SignatureUtils.sign(data, obtainPrivateKey(), SignatureUtils.SIGNATURE_TYPE_SHA256_WITH_RSA);

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

                EventBusEvent eventBusEvent = new EventBusEvent();
                eventBusEvent.setType(Constants.EVENT_TYPE_AUTHORIZE);
                if (resultMap.containsKey("error")) {
                    OAuthTokenError oAuthTokenError = new OAuthTokenError();
                    oAuthTokenError.setError(MapUtils.getString(resultMap, "error"));
                    oAuthTokenError.setErrorDescription(MapUtils.getString(resultMap, "error_description"));

                    eventBusEvent.setSource(oAuthTokenError);
                } else {
                    Map<String, List<String>> headers = webResponse.getHeaders();
                    publicKeyString = headers.get("Public-Key").get(0);
                    privateKeyString = headers.get("Private-Key").get(0);

                    Config publicKeyConfig = Config.builder()
                            .name(Constants.CONFIG_NAME_PUBLIC_KEY)
                            .value(publicKeyString)
                            .build();
                    DatabaseUtils.insert(application, publicKeyConfig);

                    Config privateKeyConfig = Config.builder()
                            .name(Constants.CONFIG_NAME_PRIVATE_KEY)
                            .value(privateKeyString)
                            .build();
                    DatabaseUtils.insert(application, privateKeyConfig);

                    OAuthToken oAuthToken = new OAuthToken();
                    oAuthToken.setAccessToken(MapUtils.getString(resultMap, "access_token"));
                    oAuthToken.setTokenType(MapUtils.getString(resultMap, "token_type"));
                    oAuthToken.setRefreshToken(MapUtils.getString(resultMap, "refresh_token"));
                    oAuthToken.setExpiresIn(MapUtils.getIntValue(resultMap, "expires_in"));
                    oAuthToken.setScope(MapUtils.getString(resultMap, "scope"));

                    eventBusEvent.setSource(oAuthToken);
                }
                EventBusUtils.post(eventBusEvent);
            }
        }).start();
    }

    public static String obtainMacAddressDefault(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }

        WifiInfo info = wifiManager.getConnectionInfo();
        if (info == null) {
            return null;
        }

        String mac = info.getMacAddress();
        if (StringUtils.isBlank(mac)) {
            return null;
        }
        return mac.toUpperCase(Locale.ENGLISH);
    }

    public static String obtainMacAddressFromFile() throws IOException {
        return new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
    }

    public static String obtainMacAddressFromHardware() throws SocketException {
        List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface networkInterface : networkInterfaces) {
            if (!networkInterface.getName().equalsIgnoreCase("wlan0")) {
                continue;
            }

            byte[] bytes = networkInterface.getHardwareAddress();
            if (bytes == null) {
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int index = 0; index < bytes.length; index++) {
                if (index == 0) {
                    stringBuilder.append(String.format("%02X", bytes[index]));
                } else {
                    stringBuilder.append(String.format(":%02X", bytes[index]));
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public static String obtainMacAddress(Context context) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return obtainMacAddressDefault(context);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return obtainMacAddressFromFile();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return obtainMacAddressFromHardware();
        }
        return "02:00:00:00:00:00";
    }

    public static String obtainMacAddressSafe(Context context) {
        try {
            return obtainMacAddress(context);
        } catch (Exception e) {
            return "02:00:00:00:00:00";
        }
    }

    public static String obtainLocationProvider(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        }

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }

        return null;
    }

    public static LocationManager obtainLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public static void obtainLocation(Context context, LocationListener locationListener) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);

        String locationProvider = null;
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }
}
