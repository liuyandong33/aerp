package build.dream.aerp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import build.dream.aerp.R;
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
import build.dream.aerp.utils.OrderUtils;
import build.dream.aerp.utils.ToastUtils;
import build.dream.aerp.utils.ValidateUtils;

public class ElemeOrderActivity extends AppCompatActivity {
    private Button confirmOrderButton;
    private Button waitButton;
    private Button clearDataButton;
    private OrderBroadcastReceiver orderBroadcastReceiver;
    Map<Long, Ringtone> ringtoneMap = new HashMap<Long, Ringtone>();
    private long orderId;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleme_order);
        EventBusUtils.register(this);

        confirmOrderButton = findViewById(R.id.activity_eleme_order_button_confirm_order_button);
        waitButton = findViewById(R.id.activity_eleme_order_button_wait_button);
        clearDataButton = findViewById(R.id.activity_eleme_order_button_clear_data_button);
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ringtone ringtone = ringtoneMap.remove(orderId);
                if (ObjectUtils.isNotNull(ringtone) && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                confirmOrderLite();
                confirmOrderButton.setVisibility(View.GONE);
                waitButton.setVisibility(View.GONE);
            }
        });

        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ringtone ringtone = ringtoneMap.remove(orderId);
                if (ObjectUtils.isNotNull(ringtone) && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                confirmOrderButton.setVisibility(View.GONE);
                waitButton.setVisibility(View.GONE);
            }
        });

        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseUtils.delete(ElemeOrderActivity.this, DietOrder.TABLE_NAME);
                DatabaseUtils.delete(ElemeOrderActivity.this, DietOrderGroup.TABLE_NAME);
                DatabaseUtils.delete(ElemeOrderActivity.this, DietOrderDetail.TABLE_NAME);
                DatabaseUtils.delete(ElemeOrderActivity.this, DietOrderDetailGoodsAttribute.TABLE_NAME);
                DatabaseUtils.delete(ElemeOrderActivity.this, DietOrderActivity.TABLE_NAME);
                DatabaseUtils.delete(ElemeOrderActivity.this, DietOrderPayment.TABLE_NAME);
                ToastUtils.showLongToast(ElemeOrderActivity.this, "清除数据成功！");
            }
        });

        orderBroadcastReceiver = new OrderBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_ACTION_ELEME_ORDER);
        registerReceiver(orderBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        unregisterReceiver(orderBroadcastReceiver);
        super.onDestroy();
    }

    private void confirmOrderLite() {
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        bodyMap.put("orderId", orderId);
        bodyMap.put("uuid", uuid);
        String body = JacksonUtils.writeValueAsString(bodyMap);
        ApplicationHandler.accessAsync(ApplicationHandler.obtainAccessToken(ElemeOrderActivity.this), Constants.METHOD_CATERING_ELEME_CONFIRM_ORDER_LITE, body, Constants.EVENT_TYPE_CATERING_ELEME_CONFIRM_ORDER_LITE);
    }

    private void pullOrder(long orderId) {
        SearchModel searchModel = SearchModel.builder()
                .equal(DietOrder.ColumnName.ID, orderId)
                .build();
        DietOrder dietOrder = DatabaseUtils.find(this, DietOrder.class, searchModel);
        if (ObjectUtils.isNull(dietOrder)) {
            Map<String, Object> bodyMap = new HashMap<String, Object>();
            bodyMap.put("orderId", orderId);
            ApplicationHandler.accessAsync(ApplicationHandler.obtainAccessToken(this), Constants.METHOD_CATERING_DIET_ORDER_PULL_ORDER, JacksonUtils.writeValueAsString(bodyMap), Constants.EVENT_TYPE_CATERING_DIET_ORDER_PULL_ORDER);
        } else {
            Ringtone ringtone = OrderUtils.play(this, RingtoneManager.TYPE_NOTIFICATION);
            ringtoneMap.put(orderId, ringtone);
            confirmOrderButton.setVisibility(View.VISIBLE);
            waitButton.setVisibility(View.VISIBLE);
        }
    }

    public class OrderBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String intentAction = intent.getAction();
            if (intentAction.equals(Constants.INTENT_ACTION_ELEME_ORDER)) {
                orderId = Long.parseLong(extras.getString("orderId"));
                uuid = extras.getString("uuid");
                pullOrder(orderId);
            }
        }
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

            Ringtone ringtone = OrderUtils.play(this, RingtoneManager.TYPE_NOTIFICATION);
            ringtoneMap.put(dietOrder.getId(), ringtone);

            confirmOrderButton.setVisibility(View.VISIBLE);
            waitButton.setVisibility(View.VISIBLE);
        }

        if (Constants.EVENT_TYPE_CATERING_ELEME_CONFIRM_ORDER_LITE.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(this, apiRest)) {
                return;
            }
        }
    }
}
