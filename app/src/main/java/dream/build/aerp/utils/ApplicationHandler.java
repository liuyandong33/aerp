package dream.build.aerp.utils;

import java.io.IOException;
import java.util.Map;

import dream.build.aerp.api.ApiRest;
import dream.build.aerp.beans.WebResponse;

/**
 * Created by liuyandong on 2019/1/1.
 */

public class ApplicationHandler {
    public static ApiRest doGetWithRequestParameters(String requestUrl, Map<String, String> requestParameters) throws IOException {
        WebResponse webResponse = WebUtils.doGetWithRequestParameters(requestUrl, requestParameters);
        String result = webResponse.getResult();
        return ApiRest.fromJson(result);
    }
}
