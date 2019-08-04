package build.dream.aerp.domains;

import java.math.BigInteger;

public class Pos extends BasicDomain {
    public static final String TABLE_NAME = "pos";
    /**
     * 商户ID
     */
    private BigInteger tenantId;
    /**
     * 商户编号
     */
    private String tenantCode;
    /**
     * 门店ID
     */
    private BigInteger branchId;
    /**
     * 门店编号
     */
    private String branchCode;
    /**
     * 用户ID
     */
    private BigInteger userId;
    /**
     * 设备ID，mac地址
     */
    private String deviceId;
    /**
     * pos 类型，安卓-android，苹果-ios
     */
    private String type;
    /**
     * pos 版本号
     */
    private String version;
    /**
     * 是否在线
     */
    private boolean online;

    /**
     * 阿里云推送服务设备ID
     */
    private String cloudPushDeviceId;

    /**
     * MQTT Client Id
     */
    private String mqttClientId;

    /**
     * MQTT Token
     */
    private String mqttToken;

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public BigInteger getBranchId() {
        return branchId;
    }

    public void setBranchId(BigInteger branchId) {
        this.branchId = branchId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getCloudPushDeviceId() {
        return cloudPushDeviceId;
    }

    public void setCloudPushDeviceId(String cloudPushDeviceId) {
        this.cloudPushDeviceId = cloudPushDeviceId;
    }

    public String getMqttClientId() {
        return mqttClientId;
    }

    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    public String getMqttToken() {
        return mqttToken;
    }

    public void setMqttToken(String mqttToken) {
        this.mqttToken = mqttToken;
    }
}
