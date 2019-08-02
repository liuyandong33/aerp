package build.dream.aerp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import build.dream.aerp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText loginNameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginNameEditText = findViewById(R.id.activity_main_edit_text_login_name);
        passwordEditText = findViewById(R.id.activity_main_edit_text_password);
        loginButton = findViewById(R.id.activity_main_button_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
                cloudPushService.bindAccount("61011888", new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.i(TAG, s);
                    }

                    @Override
                    public void onFailed(String s, String s1) {
                        Log.i(TAG, s + "-" + s1);
                    }
                });

                String deviceId = cloudPushService.getDeviceId();

                Toast.makeText(MainActivity.this, deviceId, Toast.LENGTH_LONG).show();
            }
        });
    }
}
