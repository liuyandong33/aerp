package build.dream.aerp.receivers;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import org.apache.commons.collections4.MapUtils;

import java.math.BigInteger;
import java.util.Map;

import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.OrderUtils;
import build.dream.aerp.utils.ToastUtils;

public class CloudPushMessageReceiver extends MessageReceiver {
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        String content = cPushMessage.getContent();
        Map<String, Object> contentMap = JacksonUtils.readValueAsMap(content, String.class, Object.class);
        int type = MapUtils.getIntValue(contentMap, "type");
        switch (type) {
            case 1:
                OrderUtils.saveOrder(context, MapUtils.getLongValue(contentMap, "orderId"), MapUtils.getString(contentMap, "uuid"));
                break;
        }
        ToastUtils.showLongToast(context, content);
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }
}
