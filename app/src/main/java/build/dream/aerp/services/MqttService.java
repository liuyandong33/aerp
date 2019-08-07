package build.dream.aerp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

import build.dream.aerp.constants.Constants;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.OrderUtils;
import build.dream.aerp.utils.ThreadUtils;
import build.dream.aerp.utils.ToastUtils;

public class MqttService extends Service {
    private String endPoint;
    private String clientId;
    private String userName;
    private String password;
    private String topic;
    private MqttAndroidClient mqttAndroidClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        endPoint = extras.getString("endPoint");
        clientId = extras.getString("clientId");
        userName = extras.getString("userName");
        password = extras.getString("password");
        topic = extras.getString("topic");
        mqttConnect();
        return super.onStartCommand(intent, flags, startId);
    }

    private MqttCallback buildMqttCallback() {
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
                        OrderUtils.handleElemeOrder(MqttService.this, payloadMap);
                        break;
                }
                ToastUtils.showLongToast(MqttService.this, payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };
    }

    private IMqttActionListener buildIMqttActionListener() {
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

    public void mqttConnect() {
        try {
            mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), endPoint, clientId);
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

    public void subscribe() {
        try {
            mqttAndroidClient.subscribe(topic, 1);
        } catch (MqttException ex) {
            Log.e("subscribe", "exception", ex);
        }
    }
}
