package build.dream.aerp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import build.dream.aerp.api.ApiRest;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.database.SearchModel;
import build.dream.aerp.domains.DietOrder;
import build.dream.aerp.domains.DietOrderActivity;
import build.dream.aerp.domains.DietOrderDetail;
import build.dream.aerp.domains.DietOrderDetailGoodsAttribute;
import build.dream.aerp.domains.DietOrderGroup;
import build.dream.aerp.domains.DietOrderPayment;
import build.dream.aerp.eventbus.EventBusEvent;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.DatabaseUtils;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.ObjectUtils;
import build.dream.aerp.utils.ValidateUtils;

public class OrderService extends Service {
    private long orderId;
    private String uuid;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        orderId = extras.getLong("orderId");
        uuid = extras.getString("uuid");

        SearchModel searchModel = SearchModel.builder()
                .equal(DietOrder.ColumnName.ID, orderId)
                .build();
        DietOrder dietOrder = DatabaseUtils.find(this, DietOrder.class, searchModel);
        if (ObjectUtils.isNull(dietOrder)) {
            Map<String, Object> bodyMap = new HashMap<String, Object>();
            bodyMap.put("orderId", orderId);
            ApplicationHandler.accessAsync(ApplicationHandler.obtainAccessToken(this), Constants.METHOD_CATERING_DIET_ORDER_PULL_ORDER, JacksonUtils.writeValueAsString(bodyMap), Constants.EVENT_TYPE_CATERING_DIET_ORDER_PULL_ORDER);
        } else {
            sendOrderBroadcast();
        }
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

    private void sendOrderBroadcast() {
        Intent sendBroadcastIntent = new Intent();
        sendBroadcastIntent.putExtra("orderId", orderId);
        sendBroadcastIntent.putExtra("uuid", uuid);
        sendBroadcastIntent.setAction("ElemeOrder");
        sendBroadcast(sendBroadcastIntent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        String type = eventBusEvent.getType();
        if (Constants.EVENT_TYPE_CATERING_DIET_ORDER_PULL_ORDER.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(apiRest)) {
                return;
            }

            Map<String, Object> data = (Map<String, Object>) apiRest.getData();
            DietOrder dietOrder = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data.get("dietOrder")), DietOrder.class);
            DatabaseUtils.insert(this, dietOrder);

            List<DietOrderGroup> dietOrderGroups = JacksonUtils.readValueAsList(JacksonUtils.writeValueAsString(data.get("dietOrderGroups")), DietOrderGroup.class);
            DatabaseUtils.insertAll(this, dietOrderGroups);

            List<DietOrderDetail> dietOrderDetails = JacksonUtils.readValueAsList(JacksonUtils.writeValueAsString(data.get("dietOrderDetails")), DietOrderDetail.class);
            DatabaseUtils.insertAll(this, dietOrderDetails);

            if (data.containsKey("dietOrderDetailGoodsAttributes")) {
                List<DietOrderDetailGoodsAttribute> dietOrderDetailGoodsAttributes = JacksonUtils.readValueAsList(JacksonUtils.writeValueAsString(data.get("dietOrderDetailGoodsAttributes")), DietOrderDetailGoodsAttribute.class);
                DatabaseUtils.insertAll(this, dietOrderDetailGoodsAttributes);
            }

            if (data.containsKey("dietOrderActivities")) {
                List<DietOrderActivity> dietOrderActivities = JacksonUtils.readValueAsList(JacksonUtils.writeValueAsString(data.get("dietOrderActivities")), DietOrderActivity.class);
                DatabaseUtils.insertAll(this, dietOrderActivities);
            }

            if (data.containsKey("dietOrderPayments")) {
                List<DietOrderPayment> dietOrderPayments = JacksonUtils.readValueAsList(JacksonUtils.writeValueAsString(data.get("dietOrderPayments")), DietOrderPayment.class);
                DatabaseUtils.insertAll(this, dietOrderPayments);
            }

            sendOrderBroadcast();
        }
    }
}
