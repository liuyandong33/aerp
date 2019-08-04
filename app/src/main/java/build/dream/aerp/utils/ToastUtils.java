package build.dream.aerp.utils;

import android.content.Context;
import android.widget.Toast;

import build.dream.aerp.api.ApiRest;

public class ToastUtils {
    public static void showApiRestErrorToast(Context context, ApiRest apiRest) {
        Toast.makeText(context, apiRest.getError().getMessage(), Toast.LENGTH_LONG).show();
    }
}
