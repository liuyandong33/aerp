package build.dream.aerp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import build.dream.aerp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
