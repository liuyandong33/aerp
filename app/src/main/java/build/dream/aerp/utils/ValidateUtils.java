package build.dream.aerp.utils;

import android.content.Context;

import build.dream.aerp.BuildConfig;
import build.dream.aerp.api.ApiRest;
import build.dream.aerp.constants.Constants;

public class ValidateUtils {
    public static boolean validateApiRest(Context context, ApiRest apiRest) {
        if (!apiRest.isSuccessful()) {
            ToastUtils.showApiRestErrorToast(context, apiRest);
            return false;
        }

        if (!apiRest.verifySign(BuildConfig.PLATFORM_PUBLIC_KEY, Constants.DEFAULT_DATE_PATTERN)) {
            ToastUtils.showLongToast(context, "签名错误！");
            return false;
        }

        return true;
    }

    public static boolean validateApiRest(ApiRest apiRest) {
        if (!apiRest.isSuccessful()) {
            return false;
        }

        return apiRest.verifySign(BuildConfig.PLATFORM_PUBLIC_KEY, Constants.DEFAULT_DATE_PATTERN);
    }
}
