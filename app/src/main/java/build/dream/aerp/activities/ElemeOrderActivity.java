package build.dream.aerp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import build.dream.aerp.R;
import build.dream.aerp.api.ApiRest;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.eventbus.EventBusEvent;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.ObjectUtils;
import build.dream.aerp.utils.ValidateUtils;

public class ElemeOrderActivity extends AppCompatActivity {
    private Button confirmOrderButton;
    private Button waitButton;
    private OrderBroadcastReceiver orderBroadcastReceiver;
    private long orderId;
    private String uuid;
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleme_order);
        EventBusUtils.register(this);

        confirmOrderButton = findViewById(R.id.activity_eleme_order_button_confirm_order_button);
        waitButton = findViewById(R.id.activity_eleme_order_button_wait_button);
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ObjectUtils.isNotNull(ringtone) && ringtone.isPlaying()) {
                    ringtone.stop();
                    ringtone = null;
                }
                confirmOrderLite();
                confirmOrderButton.setVisibility(View.INVISIBLE);
                waitButton.setVisibility(View.INVISIBLE);
            }
        });

        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ObjectUtils.isNotNull(ringtone) && ringtone.isPlaying()) {
                    ringtone.stop();
                    ringtone = null;
                }
                confirmOrderButton.setVisibility(View.INVISIBLE);
                waitButton.setVisibility(View.INVISIBLE);
            }
        });

        orderBroadcastReceiver = new OrderBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ElemeOrder");
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

    public class OrderBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String intentAction = intent.getAction();
            if (intentAction.equals("ElemeOrder")) {
                orderId = extras.getLong("orderId");
                uuid = extras.getString("uuid");

                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                ringtone = RingtoneManager.getRingtone(ElemeOrderActivity.this, uri);
                ringtone.play();

                confirmOrderButton.setVisibility(View.VISIBLE);
                waitButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        String type = eventBusEvent.getType();
        if (Constants.EVENT_TYPE_CATERING_ELEME_CONFIRM_ORDER_LITE.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(this, apiRest)) {
                return;
            }
        }
    }
}
