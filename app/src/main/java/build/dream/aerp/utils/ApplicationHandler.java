package build.dream.aerp.utils;

import android.app.Application;

import java.io.IOException;
import java.util.Map;

import build.dream.aerp.api.ApiRest;
import build.dream.aerp.beans.WebResponse;

/**
 * Created by liuyandong on 2019/1/1.
 */

public class ApplicationHandler {
    public static Application application;

    public static ApiRest doGetWithRequestParameters(String requestUrl, Map<String, String> requestParameters) throws IOException {
        WebResponse webResponse = WebUtils.doGetWithRequestParameters(requestUrl, requestParameters);
        String result = webResponse.getResult();
        return ApiRest.fromJson(result);
    }
}
