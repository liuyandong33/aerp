package build.dream.aerp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import build.dream.aerp.BuildConfig;
import build.dream.aerp.R;
import build.dream.aerp.api.ApiRest;
import build.dream.aerp.beans.OAuthTokenError;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.domains.Branch;
import build.dream.aerp.domains.OAuthToken;
import build.dream.aerp.domains.Pos;
import build.dream.aerp.domains.SystemUser;
import build.dream.aerp.domains.Tenant;
import build.dream.aerp.eventbus.EventBusEvent;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.CloudPushUtils;
import build.dream.aerp.utils.DatabaseUtils;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.ObjectUtils;
import build.dream.aerp.utils.ToastUtils;
import build.dream.aerp.utils.ValidateUtils;

public class MainActivity extends AppCompatActivity {
    private EditText loginNameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OAuthToken oAuthToken = ApplicationHandler.obtainOAuthToken(this);
        if (ObjectUtils.isNotNull(oAuthToken) && oAuthToken.isEffective()) {
            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
        } else {
            DatabaseUtils.delete(this, OAuthToken.TABLE_NAME);
            DatabaseUtils.delete(this, Tenant.TABLE_NAME);
            DatabaseUtils.delete(this, SystemUser.TABLE_NAME);
            DatabaseUtils.delete(this, Branch.TABLE_NAME);
            DatabaseUtils.delete(this, Pos.TABLE_NAME);

            setContentView(R.layout.activity_main);
            EventBusUtils.register(this);

            loginNameEditText = findViewById(R.id.activity_main_edit_text_login_name);
            passwordEditText = findViewById(R.id.activity_main_edit_text_password);
            loginButton = findViewById(R.id.activity_main_button_login_button);
            loginButton.setOnClickListener(buildOnClickListener());
        }
    }

    private View.OnClickListener buildOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = loginNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ApplicationHandler.authorize(loginName, password);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    /**
     * 获取用户信息
     */
    private void obtainUserInfo() {
        ApplicationHandler.access(ApplicationHandler.obtainAccessToken(this), Constants.METHOD_CATERING_USER_OBTAIN_USER_INFO, Constants.EMPTY_JSON_OBJECT, Constants.EVENT_TYPE_CATERING_USER_OBTAIN_USER_INFO);
    }

    /**
     * 上线POS
     */
    private void onlinePos() {
        String cloudPushDeviceId = CloudPushUtils.getDeviceId();
        if (StringUtils.isBlank(cloudPushDeviceId)) {
            cloudPushDeviceId = UUID.randomUUID().toString();
        }

        String macAddress = ApplicationHandler.obtainMacAddressSafe(this);

        Map<String, String> onlinePosBody = new HashMap<String, String>();
        onlinePosBody.put("deviceId", macAddress);
        onlinePosBody.put("type", "android");
        onlinePosBody.put("version", BuildConfig.VERSION_NAME);
        onlinePosBody.put("cloudPushDeviceId", cloudPushDeviceId);
        ApplicationHandler.access(ApplicationHandler.obtainAccessToken(this), Constants.METHOD_CATERING_POS_ONLINE_POS, JacksonUtils.writeValueAsString(onlinePosBody), Constants.EVENT_TYPE_CATERING_POS_ONLINE_POS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        String type = eventBusEvent.getType();
        if (Constants.EVENT_TYPE_AUTHORIZE.equals(type)) {
            Object source = eventBusEvent.getSource();
            if (source instanceof OAuthTokenError) {
                OAuthTokenError oAuthTokenError = (OAuthTokenError) source;
                ToastUtils.showLongToast(this, oAuthTokenError.getErrorDescription());
                return;
            }

            OAuthToken oAuthToken = (OAuthToken) source;

            Date now = new Date();
            oAuthToken.setCreatedTime(now);
            oAuthToken.setCreatedUserId(Constants.BIGINT_DEFAULT_VALUE);
            oAuthToken.setUpdatedTime(now);
            oAuthToken.setUpdatedUserId(Constants.BIGINT_DEFAULT_VALUE);
            oAuthToken.setUpdatedRemark("登录成功，保存token！");
            oAuthToken.setDeletedTime(Constants.DATETIME_DEFAULT_VALUE);
            oAuthToken.setDeleted(false);
            DatabaseUtils.insert(this, oAuthToken);

            ApplicationHandler.oAuthToken = oAuthToken;

            obtainUserInfo();
        }

        if (Constants.EVENT_TYPE_CATERING_USER_OBTAIN_USER_INFO.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(this, apiRest)) {
                return;
            }

            Map<String, Object> data = (Map<String, Object>) apiRest.getData();
            Tenant tenant = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data.get("tenant")), Tenant.class);
            SystemUser systemUser = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data.get("user")), SystemUser.class);
            Branch branch = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data.get("branch")), Branch.class);

            DatabaseUtils.insert(this, tenant);
            DatabaseUtils.insert(this, systemUser);
            DatabaseUtils.insert(this, branch);
            onlinePos();
        }

        if (Constants.EVENT_TYPE_CATERING_POS_ONLINE_POS.equals(type)) {
            ApiRest apiRest = (ApiRest) eventBusEvent.getSource();
            if (!ValidateUtils.validateApiRest(this, apiRest)) {
                return;
            }

            Map<String, Object> data = (Map<String, Object>) apiRest.getData();
            Pos pos = JacksonUtils.readValue(JacksonUtils.writeValueAsString(data.get("pos")), Pos.class);
            DatabaseUtils.insert(this, pos);

            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
        }
    }
}
