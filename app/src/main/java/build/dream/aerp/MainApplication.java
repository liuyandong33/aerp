package build.dream.aerp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import build.dream.aerp.utils.ApplicationHandler;

public class MainApplication extends Application {
    private static final String TAG = "Init";

    @Override
    public void onCreate() {
        super.onCreate();
        initCloudChannel(this);
        ApplicationHandler.application = this;
    }

    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
        cloudPushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }
}
