package build.dream.aerp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import build.dream.aerp.BuildConfig;
import build.dream.aerp.R;
import build.dream.aerp.beans.OAuthToken;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.eventbus.EventBusEvent;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.CloudPushUtils;
import build.dream.aerp.utils.EventBusUtils;
import build.dream.aerp.utils.JacksonUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText loginNameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBusUtils.register(this);

        loginNameEditText = findViewById(R.id.activity_main_edit_text_login_name);
        passwordEditText = findViewById(R.id.activity_main_edit_text_password);
        loginButton = findViewById(R.id.activity_main_button_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = loginNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ApplicationHandler.authorize(loginName, password);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        String type = eventBusEvent.getType();
        if (Constants.EVENT_TYPE_AUTHORIZE.equals(type)) {
            OAuthToken oAuthToken = (OAuthToken) eventBusEvent.getSource();
            ApplicationHandler.accessToken = oAuthToken.getAccessToken();

            String cloudPushDeviceId = CloudPushUtils.getDeviceId();
            if (StringUtils.isBlank(cloudPushDeviceId)) {
                cloudPushDeviceId = UUID.randomUUID().toString();
            }

            String macAddress = ApplicationHandler.obtainMacAddressSafe(this);
            Toast.makeText(this, macAddress, Toast.LENGTH_LONG).show();

            Map<String, String> onlinePosBody = new HashMap<String, String>();
            onlinePosBody.put("deviceId", macAddress);
            onlinePosBody.put("type", "android");
            onlinePosBody.put("version", BuildConfig.VERSION_NAME);
            onlinePosBody.put("cloudPushDeviceId", cloudPushDeviceId);
            ApplicationHandler.access(ApplicationHandler.accessToken, "catering.pos.onlinePos", JacksonUtils.writeValueAsString(onlinePosBody), Constants.EVENT_TYPE_CATERING_POS_ONLINE_POS);
        }

        if (Constants.EVENT_TYPE_CATERING_POS_ONLINE_POS.equals(type)) {
            Toast.makeText(this, JacksonUtils.writeValueAsString(eventBusEvent.getSource()), Toast.LENGTH_LONG).show();
        }
    }
}
