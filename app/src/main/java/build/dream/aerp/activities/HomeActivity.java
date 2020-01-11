package build.dream.aerp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import build.dream.aerp.R;
import build.dream.aerp.api.ApiRest;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.domains.Branch;
import build.dream.aerp.domains.OAuthToken;
import build.dream.aerp.domains.Pos;
import build.dream.aerp.domains.SystemUser;
import build.dream.aerp.domains.Tenant;
import build.dream.aerp.eventbus.EventBusEvent;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.DatabaseUtils;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.StatusBarUtils;
import build.dream.aerp.utils.ValidateUtils;

public class HomeActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button orderButton;
    private Button weiXinPayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        StatusBarUtils.setStatusBarColor(this, R.color.color41d09b);
        setContentView(R.layout.activity_home);

        logoutButton = findViewById(R.id.activity_home_button_logout_button);
        orderButton = findViewById(R.id.activity_home_button_order_button);
        weiXinPayButton = findViewById(R.id.activity_home_button_wei_xin_pay_button);

        final Tenant tenant = DatabaseUtils.find(this, Tenant.class);
        final Branch branch = DatabaseUtils.find(this, Branch.class);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationHandler.accessAsync(ApplicationHandler.obtainAccessToken(HomeActivity.this), Constants.METHOD_CATERING_POS_OFFLINE_POS, Constants.EMPTY_JSON_OBJECT, Constants.EVENT_TYPE_CATERING_POS_OFFLINE_POS);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ElemeOrderActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });

        weiXinPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(UUID.randomUUID().toString());

                Map<String, Object> bodyMap = new HashMap<String, Object>();
                bodyMap.put("tenantId", tenant.getId());
                bodyMap.put("branchId", branch.getId());
                bodyMap.put("vipId", 1);
                bodyMap.put("dietOrderId", 1);

                Map<String, Object> paymentInfo = new HashMap<String, Object>();
                paymentInfo.put("paymentCode", Constants.PAYMENT_CODE_WX);
                paymentInfo.put("paidAmount", 0.01);
                paymentInfo.put("paidScene", Constants.PAID_SCENE_WEI_XIN_APP);

                List<Map<String, Object>> paymentInfos = new ArrayList<Map<String, Object>>();
                paymentInfos.add(paymentInfo);

                bodyMap.put("paymentInfos", paymentInfos);

                ApplicationHandler.accessAsync(ApplicationHandler.obtainAccessToken(HomeActivity.this), Constants.EVENT_TYPE_CATERING_DIET_ORDER_DO_PAY_COMBINED, JacksonUtils.writeValueAsString(bodyMap), Constants.METHOD_TYPE_CATERING_DIET_ORDER_DO_PAY_COMBINED);
            }
        });

        TextView branchNameTextView = findViewById(R.id.activity_home_text_view_branch_name);
        branchNameTextView.setText(branch.getName());

        LocationManager locationManager = ApplicationHandler.obtainLocationManager(this);
        String locationProvider = ApplicationHandler.obtainLocationProvider(locationManager);

        if (StringUtils.isNoneBlank(locationProvider)) {
            requestLocationUpdates(locationManager, locationProvider);
        }
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(LocationManager locationManager, String locationProvider) {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(HomeActivity.this, "精度：" + location.getLongitude() + ",纬度：" + location.getLatitude(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        String type = eventBusEvent.getType();
        if (Constants.EVENT_TYPE_CATERING_POS_OFFLINE_POS.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(this, apiRest)) {
                return;
            }

            DatabaseUtils.delete(this, OAuthToken.TABLE_NAME);
            DatabaseUtils.delete(this, Tenant.TABLE_NAME);
            DatabaseUtils.delete(this, SystemUser.TABLE_NAME);
            DatabaseUtils.delete(this, Branch.TABLE_NAME);
            DatabaseUtils.delete(this, Pos.TABLE_NAME);

            ApplicationHandler.publicKeyString = null;
            ApplicationHandler.privateKeyString = null;
            ApplicationHandler.publicKey = null;
            ApplicationHandler.privateKey = null;
            ApplicationHandler.oAuthToken = null;

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (Constants.METHOD_TYPE_CATERING_DIET_ORDER_DO_PAY_COMBINED.equals(type)) {
            int a = 100;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
