package build.dream.aerp.constants;

import java.nio.charset.Charset;
import java.util.Date;

import build.dream.aerp.utils.CustomDateUtils;
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
    public static final String EVENT_TYPE_CATERING_USER_OBTAIN_USER_INFO = "catering.user.obtainUserInfo";
    public static final String EVENT_TYPE_CATERING_POS_ONLINE_POS = "catering.pos.onlinePos";
    public static final String EVENT_TYPE_CATERING_POS_OFFLINE_POS = "catering.pos.offlinePos";
    public static final String EVENT_TYPE_CATERING_DIET_ORDER_OBTAIN_DIET_ORDER_INFO = "catering.dietOrder.obtainDietOrderInfo";
    public static final String EVENT_TYPE_CATERING_DIET_ORDER_PULL_ORDER = "catering.dietOrder.pullOrder";
    public static final String EVENT_TYPE_CATERING_ELEME_CONFIRM_ORDER_LITE = "catering.eleme.confirmOrderLite";

    public static final String METHOD_CATERING_USER_OBTAIN_USER_INFO = "catering.user.obtainUserInfo";
    public static final String METHOD_CATERING_POS_ONLINE_POS = "catering.pos.onlinePos";
    public static final String METHOD_CATERING_POS_OFFLINE_POS = "catering.pos.offlinePos";
    public static final String METHOD_CATERING_DIET_ORDER_OBTAIN_DIET_ORDER_INFO = "catering.dietOrder.obtainDietOrderInfo";
    public static final String METHOD_CATERING_DIET_ORDER_PULL_ORDER = "catering.dietOrder.pullOrder";
    public static final String METHOD_CATERING_ELEME_CONFIRM_ORDER_LITE = "catering.eleme.confirmOrderLite";

    public static final String SQL_OPERATION_SYMBOL_IN = "IN";
    public static final String SQL_OPERATION_SYMBOL_NOT_IN = "NOT IN";
    public static final String SQL_OPERATION_SYMBOL_LIKE = "LIKE";
    public static final String SQL_OPERATION_SYMBOL_NOT_LIKE = "NOT LIKE";
    public static final String SQL_OPERATION_SYMBOL_EQUAL = "=";
    public static final String SQL_OPERATION_SYMBOL_NOT_EQUAL = "!=";
    public static final String SQL_OPERATION_SYMBOL_LESS_THAN = "<";
    public static final String SQL_OPERATION_SYMBOL_LESS_THAN_EQUAL = "<=";
    public static final String SQL_OPERATION_SYMBOL_GREATER_THAN = ">";
    public static final String SQL_OPERATION_SYMBOL_GREATER_THAN_EQUAL = ">=";
    public static final String SQL_OPERATION_SYMBOL_IS_NULL = "IS NULL";
    public static final String SQL_OPERATION_SYMBOL_IS_NOT_NULL = "IS NOT NULL";
    public static final String DESC = "DESC";
    public static final String ASC = "ASC";

    /**
     * 数据库默认值
     */
    public static final int TINYINT_DEFAULT_VALUE = 0;
    public static final int INT_DEFAULT_VALUE = 0;
    public static final long BIGINT_DEFAULT_VALUE = 0;
    public static final long DECIMAL_DEFAULT_VALUE = 0;
    public static final Date DATETIME_DEFAULT_VALUE = CustomDateUtils.parse("1970-01-01 00:00:00", DEFAULT_DATE_PATTERN);
    public static final String VARCHAR_DEFAULT_VALUE = "";

    public static final String CONFIG_NAME_PUBLIC_KEY = "PUBLIC_KEY";
    public static final String CONFIG_NAME_PRIVATE_KEY = "PRIVATE_KEY";

    public static final String INTENT_ACTION_ELEME_ORDER = "ELEME_ORDER";
}
