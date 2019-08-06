package build.dream.aerp.utils;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Map;

import build.dream.aerp.constants.Constants;

public class OrderUtils {
    public static void handleElemeOrder(Context context, Map<String, Object> messageMap) {
        Intent intent = new Intent();
        for (Map.Entry<String, Object> entry : messageMap.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue().toString());
        }
        intent.setAction(Constants.INTENT_ACTION_ELEME_ORDER);
        context.sendBroadcast(intent);
    }

    public static Ringtone play(Context context, int type) {
        Uri uri = RingtoneManager.getDefaultUri(type);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();
        return ringtone;
    }
}
