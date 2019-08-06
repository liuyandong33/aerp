package build.dream.aerp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import build.dream.aerp.api.ApiRest;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.eventbus.EventBusEvent;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.ToastUtils;
import build.dream.aerp.utils.ValidateUtils;

public class OrderService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        long orderId = extras.getLong("orderId");

        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("dietOrderId", orderId);
        ApplicationHandler.accessAsync(ApplicationHandler.obtainAccessToken(this), Constants.METHOD_CATERING_DIET_ORDER_OBTAIN_DIET_ORDER_INFO, JacksonUtils.writeValueAsString(bodyMap), Constants.EVENT_TYPE_CATERING_DIET_ORDER_OBTAIN_DIET_ORDER_INFO);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBusUtils.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        String type = eventBusEvent.getType();
        if (Constants.EVENT_TYPE_CATERING_DIET_ORDER_OBTAIN_DIET_ORDER_INFO.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(this, apiRest)) {
                return;
            }

            ToastUtils.showLongToast(this, JacksonUtils.writeValueAsString(apiRest));
        }
    }
}
