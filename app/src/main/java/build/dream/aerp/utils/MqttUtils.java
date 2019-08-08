package build.dream.aerp.utils;

import android.util.Log;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

import build.dream.aerp.domains.MqttInfo;
import build.dream.aerp.constants.Constants;

public class MqttUtils {
    private static MqttAndroidClient mqttAndroidClient;
    private static String endPoint;
    private static String clientId;
    private static String userName;
    private static String password;
    private static String topic;

    private static MqttCallback buildMqttCallback() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                ThreadUtils.sleepSafe(2000);
                mqttConnect();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload(), Constants.CHARSET_UTF_8);
                Map<String, Object> payloadMap = JacksonUtils.readValueAsMap(payload, String.class, Object.class);
                int type = MapUtils.getIntValue(payloadMap, "type");
                switch (type) {
                    case 1:
                        OrderUtils.handleElemeOrder(ApplicationHandler.application, payloadMap);
                        break;
                }
                ToastUtils.showLongToast(ApplicationHandler.application, payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };
    }

    private static IMqttActionListener buildIMqttActionListener() {
        return new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.w("connect", "onSuccess");
                if (StringUtils.isNotBlank(topic)) {
                    subscribe();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                ThreadUtils.sleepSafe(2000);
                mqttConnect();
            }
        };
    }

    public static void mqttConnect(MqttInfo mqttInfo) {
        endPoint = mqttInfo.getEndPoint();
        clientId = mqttInfo.getClientId();
        userName = mqttInfo.getUserName();
        password = mqttInfo.getPassword();
        topic = mqttInfo.getTopic();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mqttConnect();
            }
        }).start();
    }

    public static void mqttConnect() {
        try {
            mqttAndroidClient = new MqttAndroidClient(ApplicationHandler.application.getApplicationContext(), "tcp://" + endPoint + ":1883", clientId);
            mqttAndroidClient.setCallback(buildMqttCallback());

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setConnectionTimeout(3000);
            mqttConnectOptions.setKeepAliveInterval(90);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(true);

            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());

            mqttAndroidClient.connect(mqttConnectOptions, null, buildIMqttActionListener());
        } catch (Exception e) {
            ThreadUtils.sleepSafe(2000);
            mqttConnect();
        }
    }

    public static void subscribe() {
        try {
            mqttAndroidClient.subscribe(topic, 1);
        } catch (MqttException ex) {
            Log.e("subscribe", "exception", ex);
        }
    }
}
