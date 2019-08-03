package build.dream.aerp.constants;

import java.nio.charset.Charset;

import build.dream.aerp.utils.WebUtils;

/**
 * Created by liuyandong on 2019/1/1.
 */

public class Constants {
    /**
     * 字符集相关常量
     */
    public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
    public static final Charset CHARSET_GBK = Charset.forName("GBK");
    public static final Charset CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final String CHARSET_NAME_UTF_8 = "UTF-8";
    public static final String CHARSET_NAME_GBK = "GBK";
    public static final String CHARSET_NAME_ISO_8859_1 = "ISO-8859-1";

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_HEAD = "HEAD";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_PUT = "PUT";
    public static final String REQUEST_METHOD_PATCH = "PATCH";
    public static final String REQUEST_METHOD_DELETE = "DELETE";
    public static final String REQUEST_METHOD_OPTIONS = "OPTIONS";
    public static final String REQUEST_METHOD_TRACE = "TRACE";

    public static final String TLS = "TLS";

    /**
     * 日期格式化相关常量
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String GMT = "GMT";

    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    public static final String EMPTY_JSON_OBJECT = "{}";

    public static final String CONTENT_TYPE_APPLICATION_FORM_URLENCODED_UTF8 = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String CONTENT_TYPE_APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    public static final String CONTENT_TYPE_MULTIPART_FORM_DATA_UTF8 = "multipart/form-data;boundary=" + WebUtils.BOUNDARY + ";charset=UTF-8";

    public static final String EVENT_TYPE_AUTHORIZE = "AUTHORIZE";
    public static final String EVENT_TYPE_CATERING_POS_ONLINE_POS = "catering.pos.onlinePos";
    public static final String EVENT_TYPE_CATERING_USER_OBTAIN_USER_INFO = "catering.user.obtainUserInfo";

    public static final String METHOD_CATERING_POS_ONLINE_POS = "catering.pos.onlinePos";
    public static final String METHOD_CATERING_USER_OBTAIN_USER_INFO = "catering.user.obtainUserInfo";
}
