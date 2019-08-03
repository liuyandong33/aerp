package build.dream.aerp.utils;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

public class CloudPushUtils {
    public static CloudPushService getCloudPushService() {
        return PushServiceFactory.getCloudPushService();
    }

    public static String getDeviceId() {
        return getCloudPushService().getDeviceId();
    }
}
