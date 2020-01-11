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
    public static final String EVENT_TYPE_CATERING_DIET_ORDER_DO_PAY_COMBINED = "catering.dietOrder.doPayCombined";

    public static final String METHOD_CATERING_USER_OBTAIN_USER_INFO = "catering.user.obtainUserInfo";
    public static final String METHOD_CATERING_POS_ONLINE_POS = "catering.pos.onlinePos";
    public static final String METHOD_CATERING_POS_OFFLINE_POS = "catering.pos.offlinePos";
    public static final String METHOD_CATERING_DIET_ORDER_OBTAIN_DIET_ORDER_INFO = "catering.dietOrder.obtainDietOrderInfo";
    public static final String METHOD_CATERING_DIET_ORDER_PULL_ORDER = "catering.dietOrder.pullOrder";
    public static final String METHOD_CATERING_ELEME_CONFIRM_ORDER_LITE = "catering.eleme.confirmOrderLite";
    public static final String METHOD_TYPE_CATERING_DIET_ORDER_DO_PAY_COMBINED = "catering.dietOrder.doPayCombined";

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
    public static final String TEXT_DEFAULT_VALUE = "";

    public static final String CONFIG_NAME_PUBLIC_KEY = "PUBLIC_KEY";
    public static final String CONFIG_NAME_PRIVATE_KEY = "PRIVATE_KEY";

    public static final String INTENT_ACTION_ELEME_ORDER = "ELEME_ORDER";

    public static final String POS_TYPE_ANDROID = "android";
    public static final String POS_TYPE_IOS = "ios";
    public static final String POS_TYPE_WINDOWS = "windows";

    // 支付方式编码，会员积分
    public static final String PAYMENT_CODE_HYJF = "HYJF";
    // 支付方式编码，会员钱包
    public static final String PAYMENT_CODE_HYQB = "HYQB";
    // 支付方式编码，微信支付
    public static final String PAYMENT_CODE_WX = "WX";
    // 支付方式编码，支付宝支付
    public static final String PAYMENT_CODE_ALIPAY = "ALIPAY";
    // 支付方式编码，现金
    public static final String PAYMENT_CODE_CASH = "CASH";

    /**
     * 支付场景常量
     *
     * @see #PAID_SCENE_WEI_XIN_MICROPAY: 微信付款码支付
     * @see #PAID_SCENE_WEI_XIN_JSAPI_PUBLIC_ACCOUNT: 微信JSAPI支付
     * @see #PAID_SCENE_WEI_XIN_NATIVE: 微信Native支付
     * @see #PAID_SCENE_WEI_XIN_APP: 微信APP支付
     * @see #PAID_SCENE_WEI_XIN_MWEB: 微信H5支付
     * @see #PAID_SCENE_WEI_XIN_JSAPI_MINI_PROGRAM: 微信小程序支付
     * @see #PAID_SCENE_ALIPAY_MOBILE_WEBSITE: 支付宝手机网站支付
     * @see #PAID_SCENE_ALIPAY_PC_WEBSITE: 支付宝电脑网站支付支付
     * @see #PAID_SCENE_ALIPAY_APP: 支付宝APP支付
     * @see #PAID_SCENE_ALIPAY_FAC_TO_FACE: 支付宝当面付
     */
    public static final Integer PAID_SCENE_WEI_XIN_MICROPAY = 1;
    public static final Integer PAID_SCENE_WEI_XIN_JSAPI_PUBLIC_ACCOUNT = 2;
    public static final Integer PAID_SCENE_WEI_XIN_NATIVE = 3;
    public static final Integer PAID_SCENE_WEI_XIN_APP = 4;
    public static final Integer PAID_SCENE_WEI_XIN_MWEB = 5;
    public static final Integer PAID_SCENE_WEI_XIN_JSAPI_MINI_PROGRAM = 6;
    public static final Integer PAID_SCENE_ALIPAY_MOBILE_WEBSITE = 7;
    public static final Integer PAID_SCENE_ALIPAY_PC_WEBSITE = 8;
    public static final Integer PAID_SCENE_ALIPAY_APP = 9;
    public static final Integer PAID_SCENE_ALIPAY_FAC_TO_FACE = 10;
}
